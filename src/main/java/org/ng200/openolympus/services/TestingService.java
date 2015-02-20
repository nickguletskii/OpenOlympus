/**
 * The MIT License
 * Copyright (c) 2014-2015 Nick Guletskii
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.ng200.openolympus.services;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.jppf.client.JPPFClient;
import org.jppf.client.JPPFJob;
import org.jppf.scheduling.JPPFSchedule;
import org.jppf.task.storage.DataProvider;
import org.jppf.task.storage.MemoryMapDataProvider;
import org.ng200.openolympus.cerberus.Janitor;
import org.ng200.openolympus.cerberus.SolutionJudge;
import org.ng200.openolympus.cerberus.SolutionResult;
import org.ng200.openolympus.model.Solution;
import org.ng200.openolympus.model.Verdict;
import org.ng200.openolympus.repositories.SolutionRepository;
import org.ng200.openolympus.repositories.UserRepository;
import org.ng200.openolympus.repositories.VerdictRepository;
import org.ng200.openolympus.tasks.TaskContainer;
import org.ng200.openolympus.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

@Service
public class TestingService {

	private final JPPFClient jppfClient = new JPPFClient();
	private final DataProvider dataProvider = new MemoryMapDataProvider();

	private static final Logger logger = LoggerFactory
			.getLogger(TestingService.class);

	private VerdictRepository verdictRepository;

	private TaskContainerCache taskContainerCache;
	private final Map<Long, TaskContainer> taskContainers = new HashMap<>();

	private final Cache<Integer, Pair<String, String>> internalErrors = CacheBuilder
			.newBuilder().maximumSize(30).build();

	private int internalErrorCounter = 0;

	private final ScheduledExecutorService verdictCheckSchedulingExecutorService = Executors
			.newSingleThreadScheduledExecutor(new ThreadFactoryBuilder()
					.setNameFormat("Judgement scheduler {0}").build());
	private final ExecutorService compilationAndCheckingExecutor = Executors
			.newFixedThreadPool(
					16,
					new ThreadFactoryBuilder().setNameFormat(
							"Judgement waiter {0}").build());
	@Autowired
	private SolutionService solutionService;

	public void shutdownNow() {
		verdictCheckSchedulingExecutorService.shutdownNow();
	}

	@Autowired
	public TestingService(final SolutionService solutionService,
			final SolutionRepository solutionRepository,
			final UserRepository userRepository,
			final VerdictRepository verdictRepository,
			final StorageService storageService,
			final TaskContainerCache taskContainerCache) {
		super();
		this.verdictRepository = verdictRepository;
		this.taskContainerCache = taskContainerCache;

		this.dataProvider.setParameter("storageService", storageService);

		final HashSet<Verdict> alreadyScheduledJobs = new HashSet<>();
		this.verdictCheckSchedulingExecutorService.scheduleAtFixedRate(
				() -> {
					solutionService
							.getPendingVerdicts()
							.stream()
							.filter((verdict) -> !alreadyScheduledJobs
									.contains(verdict))
							.sorted((l, r) -> {
								if (l.isViewableWhenContestRunning() != r
										.isViewableWhenContestRunning()) {
									// Schedule base tests first
									return Boolean.compare(
											r.isViewableWhenContestRunning(),
											l.isViewableWhenContestRunning());
								}
								return Long.compare(l.getId(), r.getId());
							}).forEach((verdict) -> {
								alreadyScheduledJobs.add(verdict);
								this.processVerdict(verdict);
							});
				}, 0, 100, TimeUnit.MILLISECONDS);
	}

	private void checkVerdict(final Verdict verdict, final SolutionJudge judge,
			final List<File> testFiles, final BigDecimal maximumScore,
			final Properties properties) throws ExecutionException {
		if (this.dataProvider == null) {
			throw new IllegalStateException("Shared data provider is null!");
		}

		final Lock lock = verdict.getSolution().getTask().readLock();
		lock.lock();

		try {
			TestingService.logger.info("Scheduling verdict {} for testing.",
					verdict.getId());

			final JPPFJob job = new JPPFJob();
			job.setDataProvider(this.dataProvider);

			job.setName("Check verdict " + verdict.getId());

			final int priority = (int) ((verdict.isViewableWhenContestRunning()
					? (Integer.MAX_VALUE / 2)
					: 0) - verdict.getId());
			job.getSLA().setMaxNodes(1);
			job.getSLA().setPriority(priority);
			job.getSLA()
					.setDispatchExpirationSchedule(new JPPFSchedule(60000L));
			job.getSLA().setMaxDispatchExpirations(3);

			job.add(new VerdictCheckingTask(verdict, judge, testFiles,
					maximumScore, properties));

			job.setBlocking(true);

			this.jppfClient.submitJob(job);
			@SuppressWarnings("unchecked")
			final org.jppf.node.protocol.Task<Pair<SolutionJudge, SolutionResult>> task = (org.jppf.node.protocol.Task<Pair<SolutionJudge, SolutionResult>>) job
					.awaitResults().get(0);

			if (task.getThrowable() != null) {
				throw task.getThrowable();
			}

			final SolutionResult result = task.getResult().getSecond();

			verdict.setScore(result.getScore());
			verdict.setMemoryPeak(result.getMemoryPeak());
			verdict.setCpuTime(result.getCpuTime());
			verdict.setRealTime(result.getRealTime());

			verdict.setStatus(result.getResult());
			switch (result.getResult()) {
			case OK:
			case TIME_LIMIT:
			case MEMORY_LIMIT:
			case OUTPUT_LIMIT:
			case PRESENTATION_ERROR:
			case WRONG_ANSWER:
			case RUNTIME_ERROR:
				break;
			case INTERNAL_ERROR:
				result.getErrorMessages().forEach(
						(stage, message) -> this.internalErrors.put(
								this.internalErrorCounter++,
								new Pair<String, String>(verdict.getSolution()
										.getTask().getName(), message)));
				break;
			case SECURITY_VIOLATION:
				verdict.setUnauthorisedSyscall(result.getUnauthorisedSyscall());
				break;
			case COMPILE_ERROR:
				final String message = result.getErrorMessages().values()
						.stream().collect(Collectors.joining("\n"));
				verdict.setAdditionalInformation(HtmlUtils.htmlEscape(message
						.substring(0, Math.min(128, message.length()))));
				break;
			case WAITING:
				throw new IllegalStateException(
						"Judge returned result \"waiting\".");
			}

		} catch (final Throwable throwable) {
			throw new RuntimeException("Couldn't run solution: ", throwable);
		} finally {
			lock.unlock();

			verdict.setTested(true);
			if (verdict.getStatus() == SolutionResult.Result.WAITING) {
				verdict.setStatus(SolutionResult.Result.INTERNAL_ERROR);
				TestingService.logger
						.error("Judge for task {} did not set the result status to an acceptable value: got WAITING instead.",
								verdict.getSolution().getTask().getId());
			}
			this.solutionService.saveVerdict(verdict);
		}
	}

	private SolutionJudge compileSolution(final Solution solution,
			final SolutionJudge judge) {
		if (this.dataProvider == null) {
			throw new IllegalStateException("Shared data provider is null!");
		}

		// solution.getTask().lockRead();

		try {
			TestingService.logger
					.info("Scheduling solution {} for compilation.",
							solution.getId());

			final JPPFJob job = new JPPFJob();
			job.setDataProvider(this.dataProvider);

			job.setName("Compile solution " + solution.getId());

			job.getSLA().setMaxNodes(1);
			job.getSLA().setPriority(
					(int) (Integer.MAX_VALUE - solution.getId()));
			job.getSLA()
					.setDispatchExpirationSchedule(new JPPFSchedule(20000L));
			job.getSLA().setMaxDispatchExpirations(5);

			job.add(new SolutionCompilationTask(judge, solution));

			job.setBlocking(false);

			this.jppfClient.submitJob(job);
			final org.jppf.node.protocol.Task<SolutionJudge> task = (org.jppf.node.protocol.Task<SolutionJudge>) job
					.awaitResults().get(0);

			if (task.getThrowable() != null) {
				throw task.getThrowable();
			}

			return task.getResult();

		} catch (final Throwable throwable) {
			throw new RuntimeException("Couldn't compile solution: ", throwable);
		} finally {
			// solution.getTask().unlockRead();
		}
	}

	public List<Pair<String, String>> getJudgeInternalErrors() {
		return new ArrayList<>(this.internalErrors.asMap().values());
	}

	private void processVerdict(final Verdict verdict) {
		try {
			final TaskContainer taskContainer = this.taskContainerCache
					.getTaskContainerForTask(verdict.getSolution().getTask());
			final Function<CompletableFuture<SolutionJudge>, CompletableFuture<SolutionJudge>> functionToApplyToJudge = (
					final CompletableFuture<SolutionJudge> futureJudge) -> {
				return futureJudge
						.thenApplyAsync(
								(final SolutionJudge judge) -> {
									try {
										if (!judge.isCompiled()) {
											return this.compileSolution(
													verdict.getSolution(),
													judge);
										}
									} catch (final Exception e) {
										TestingService.logger.error(
												"Solution compilation failed "
														+ "because judge for task "
														+ "\"{}\"({}) thew an "
														+ "exception: {}",
												verdict.getSolution().getTask()
														.getName(), verdict
														.getSolution()
														.getTask().getId(), e);
									} finally {
										Janitor.cleanUp(judge);
									}
									return judge;
								}, this.compilationAndCheckingExecutor)
						.thenApplyAsync(
								(final SolutionJudge judge) -> {
									try {
										this.checkVerdict(
												verdict,
												judge,
												taskContainer.getTestFiles(verdict
														.getPathToTest()),
												verdict.getMaximumScore(),
												taskContainer.getProperties());
									} catch (final Throwable e) {
										TestingService.logger.error(
												"Solution judgement failed "
														+ "because judge for task "
														+ "\"{}\"({}) thew an "
														+ "exception: {}",
												verdict.getSolution().getTask()
														.getName(), verdict
														.getSolution()
														.getTask().getId(), e);
									} finally {
										Janitor.cleanUp(judge);
									}
									return judge;
								}, this.compilationAndCheckingExecutor)
						.handle((final SolutionJudge judge,
								final Throwable throwable) -> {
							if (throwable != null) {
								throw new RuntimeException(
										"Couldn't judge verdict: ", throwable);
							}
							return judge;
						});
			};
			taskContainer.applyToJudge(verdict.getSolution(),
					functionToApplyToJudge);
		} catch (final Exception e) {
			TestingService.logger.error(
					"Couldn't schedule judgement for verdict {}: ", verdict, e);
		}
	}

	public void reloadTasks() {
		this.taskContainers.clear();
	}

	@Transactional
	public void testSolutionOnAllTests(Solution solution) {
		solution = this.solutionService.saveSolution(solution);
		final List<Verdict> verdicts = this.taskContainerCache
				.getTaskContainerForTask(solution.getTask())
				.generateTestVerdicts(solution);

		this.verdictRepository.save(verdicts);
		this.verdictRepository.flush();
	}
}
