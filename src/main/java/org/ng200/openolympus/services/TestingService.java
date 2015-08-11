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

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
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
import java.util.stream.Stream;

import org.jooq.DSLContext;
import org.jppf.client.JPPFClient;
import org.jppf.client.JPPFJob;
import org.jppf.scheduling.JPPFSchedule;
import org.jppf.task.storage.DataProvider;
import org.jppf.task.storage.MemoryMapDataProvider;
import org.ng200.openolympus.Authorities;
import org.ng200.openolympus.cerberus.Janitor;
import org.ng200.openolympus.cerberus.SolutionJudge;
import org.ng200.openolympus.cerberus.SolutionResult;
import org.ng200.openolympus.cerberus.SolutionResult.Result;
import org.ng200.openolympus.cerberus.util.Lists;
import org.ng200.openolympus.factory.JacksonSerializationFactory;
import org.ng200.openolympus.jppfsupport.JacksonSerializationDelegatingTask;
import org.ng200.openolympus.jppfsupport.JsonTaskExecutionResult;
import org.ng200.openolympus.jppfsupport.SolutionCompilationTask;
import org.ng200.openolympus.jppfsupport.VerdictCheckingTask;
import org.ng200.openolympus.jooq.Tables;
import org.ng200.openolympus.jooq.enums.VerdictStatusType;
import org.ng200.openolympus.jooq.tables.daos.SolutionDao;
import org.ng200.openolympus.jooq.tables.daos.TaskDao;
import org.ng200.openolympus.jooq.tables.daos.VerdictDao;
import org.ng200.openolympus.jooq.tables.pojos.Solution;
import org.ng200.openolympus.jooq.tables.pojos.Task;
import org.ng200.openolympus.jooq.tables.pojos.Verdict;
import org.ng200.openolympus.tasks.TaskContainer;
import org.ng200.openolympus.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

@Service
public class TestingService {

	private final class SystemAuthenticationToken extends
			AbstractAuthenticationToken {
		/**
		 *
		 */
		private static final long serialVersionUID = -5336945477077794036L;

		public SystemAuthenticationToken() {
			super(Lists.from(Authorities.SUPERUSER, Authorities.USER));
			this.setAuthenticated(true);
		}

		private SystemAuthenticationToken(
				Collection<? extends GrantedAuthority> authorities) {
			super(authorities);
		}

		@Override
		public Object getCredentials() {
			return null;
		}

		@Override
		public Object getPrincipal() {
			return TestingService.this.userService.getUserByUsername("system");
		}
	}

	private final JPPFClient jppfClient = new JPPFClient();
	private final DataProvider dataProvider = new MemoryMapDataProvider();

	private static final Logger logger = LoggerFactory
			.getLogger(TestingService.class);

	private final Cache<Integer, Pair<String, String>> internalErrors = CacheBuilder
			.newBuilder().maximumSize(30).build();

	private int internalErrorCounter = 0;

	private final ScheduledExecutorService verdictCheckSchedulingExecutorService = Executors
			.newSingleThreadScheduledExecutor(new ThreadFactoryBuilder()
					.setNameFormat("Judgement scheduler %1$d").build());
	private final ExecutorService compilationAndCheckingExecutor = Executors
			.newFixedThreadPool(
					16,
					new ThreadFactoryBuilder().setNameFormat(
							"Judgement awaiter %1$d").build());
	@Autowired
	private SolutionService solutionService;

	private UserService userService;

	@Autowired
	private TaskContainerCache taskContainerCache;

	@Autowired
	private SolutionDao solutionDao;

	@Autowired
	private TaskDao taskDao;

	@Autowired
	private VerdictDao verdictDao;

	@Autowired
	private DSLContext dslContext;

	@Autowired
	private TaskService taskService;

	private StorageService storageService;

	@Autowired
	public TestingService(TaskContainerCache taskContainerCache,
			StorageService storageService) {
		super();
		this.taskContainerCache = taskContainerCache;
		this.storageService = storageService;

		this.dataProvider.setParameter("storageService", storageService);

		final HashSet<Verdict> alreadyScheduledJobs = new HashSet<>();
		this.verdictCheckSchedulingExecutorService.scheduleAtFixedRate(
				() -> {
					this.logInAsSystem();
					solutionService
							.getPendingVerdicts()
							.filter((verdict) -> !alreadyScheduledJobs
									.contains(verdict))
							.sorted((l, r) -> {

						if (l.getViewableDuringContest() != r
								.getViewableDuringContest()) {
							// Schedule base tests first
							return Boolean.compare(
									r.getViewableDuringContest(),
									l.getViewableDuringContest());
						}
						return Long.compare(l.getId(), r.getId());
					}).forEach((verdict) -> {
						alreadyScheduledJobs.add(verdict);
						this.processVerdict(verdict);
					});
				} , 0, 100, TimeUnit.MILLISECONDS);
	}

	private void checkVerdict(final Verdict verdict, final SolutionJudge judge,
			final List<Path> testFiles, final BigDecimal maximumScore,
			final Properties properties) throws ExecutionException {
		if (this.dataProvider == null) {
			throw new IllegalStateException("Shared data provider is null!");
		}
		Solution solution = solutionDao.fetchOneById(verdict.getSolutionId());
		Task task = taskDao.fetchOneById(solution.getTaskId());

		final Lock lock = task.readLock();
		lock.lock();

		try {
			TestingService.logger.info("Scheduling verdict {} for testing.",
					verdict.getId());

			final JPPFJob job = new JPPFJob();
			job.setDataProvider(this.dataProvider);

			job.setName("Check verdict " + verdict.getId());

			final int priority = (int) ((verdict.getViewableDuringContest()
					? (Integer.MAX_VALUE / 2)
					: 0) - verdict.getId());

			job.getSLA().setMaxNodes(1);
			job.getSLA().setPriority(priority);
			job.getSLA()
					.setDispatchExpirationSchedule(new JPPFSchedule(60000L));
			job.getSLA().setMaxDispatchExpirations(3);

			TaskContainer taskContainer = taskContainerCache
					.getTaskContainerForTask(task);

			Thread.currentThread().setContextClassLoader(
					new URLClassLoader(taskContainer.getClassLoaderURLs()
							.toArray(new URL[0]), Thread.currentThread()
									.getContextClassLoader()));

			job.add(new JacksonSerializationDelegatingTask<>(
					new VerdictCheckingTask(judge, testFiles, maximumScore,
							properties),
					taskContainer.getClassLoaderURLs()));

			job.setBlocking(true);

			jppfClient.registerClassLoader(taskContainer.getClassLoader(),
					job.getUuid());
			this.jppfClient.submitJob(job);
			@SuppressWarnings("unchecked")
			final org.jppf.node.protocol.Task<String> jppfTask = (org.jppf.node.protocol.Task<String>) job
					.awaitResults().get(0);

			if (jppfTask.getThrowable() != null) {
				throw jppfTask.getThrowable();
			}

			ObjectMapper objectMapper = JacksonSerializationFactory
					.createObjectMapper();

			final JsonTaskExecutionResult<Pair<SolutionJudge, SolutionResult>> checkingResult = ((JacksonSerializationDelegatingTask<Pair<SolutionJudge, SolutionResult>, VerdictCheckingTask>) job
					.awaitResults().get(0)).getResultOrThrowable();

			if (checkingResult.getError() != null) {
				throw checkingResult.getError();
			}

			final SolutionResult result = checkingResult.getResult()
					.getSecond();

			verdict.setScore(result.getScore());

			verdict.setMemoryPeak(result.getMemoryPeak());
			verdict.setCpuTime(Duration.ofMillis(result.getCpuTime()));
			verdict.setRealTime(Duration.ofMillis(result.getRealTime()));

			verdict.setStatus(convertCerberusResultToVerdictStatus(result
					.getResult()));
			switch (result.getResult()) {
			case OK:
			case TIME_LIMIT:
			case MEMORY_LIMIT:
			case OUTPUT_LIMIT:
			case PRESENTATION_ERROR:
			case WRONG_ANSWER:
			case RUNTIME_ERROR:
			case SECURITY_VIOLATION:
				break;
			case INTERNAL_ERROR:
				result.getErrorMessages().forEach(
						(stage, message) -> this.internalErrors.put(
								this.internalErrorCounter++,
								new Pair<String, String>(task.getName(),
										message)));
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
			verdict.setStatus(VerdictStatusType.internal_error);
			throw new RuntimeException("Couldn't run solution: ", throwable);
		} finally {
			lock.unlock();

			if (verdict.getStatus() == VerdictStatusType.waiting) {
				verdict.setStatus(VerdictStatusType.internal_error);
				TestingService.logger
						.error("Judge for task {} did not set the result status to an acceptable value: got WAITING instead.",
								task.getId());
			}
			this.solutionService.updateVerdict(verdict);
		}
	}

	private VerdictStatusType convertCerberusResultToVerdictStatus(
			Result result) {
		switch (result) {
		case COMPILE_ERROR:
			return VerdictStatusType.compile_error;
		case INTERNAL_ERROR:
			return VerdictStatusType.internal_error;
		case MEMORY_LIMIT:
			return VerdictStatusType.memory_limit_exceeded;
		case OK:
			return VerdictStatusType.ok;
		case OUTPUT_LIMIT:
			return VerdictStatusType.output_limit_exceeded;
		case PRESENTATION_ERROR:
			return VerdictStatusType.presentation_error;
		case RUNTIME_ERROR:
			return VerdictStatusType.runtime_error;
		case SECURITY_VIOLATION:
			return VerdictStatusType.security_violated;
		case TIME_LIMIT:
			return VerdictStatusType.real_time_limit_exceeded;
		case WAITING:
			return VerdictStatusType.waiting;
		case WRONG_ANSWER:
			return VerdictStatusType.wrong_answer;
		default:
			throw new IllegalStateException(
					"Possible version mismatch: unknown Cerberus result!");
		}
	}

	private SolutionJudge compileSolution(final Solution solution,
			final SolutionJudge judge, final Properties properties)
					throws ExecutionException {
		if (this.dataProvider == null) {
			throw new IllegalStateException("Shared data provider is null!");
		}
		Task task = taskDao.fetchOneById(solution.getTaskId());

		final Lock lock = task.readLock();
		lock.lock();

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

			TaskContainer taskContainer = taskContainerCache
					.getTaskContainerForTask(task);

			Thread.currentThread().setContextClassLoader(
					new URLClassLoader(taskContainer.getClassLoaderURLs()
							.toArray(new URL[0]), Thread.currentThread()
									.getContextClassLoader()));
			job.add(new JacksonSerializationDelegatingTask<>(
					new SolutionCompilationTask(judge, Lists
							.from(storageService.getSolutionFile(solution)),
							properties),
					taskContainer.getClassLoaderURLs()));

			job.setBlocking(false);

			jppfClient.registerClassLoader(taskContainer.getClassLoader(),
					job.getUuid());
			this.jppfClient.submitJob(job);
			final JsonTaskExecutionResult<SolutionJudge> result = ((JacksonSerializationDelegatingTask<SolutionJudge, SolutionCompilationTask>) job
					.awaitResults().get(0)).getResultOrThrowable();

			if (result.getError() != null) {
				throw result.getError();
			}

			return result.getResult();

		} catch (final Throwable throwable) {
			throw new RuntimeException("Couldn't compile solution: ",
					throwable);
		} finally {
			lock.unlock();
		}
	}

	public List<Pair<String, String>> getJudgeInternalErrors() {
		return new ArrayList<>(this.internalErrors.asMap().values());
	}

	private void logInAsSystem() {
		SecurityContextHolder.getContext().setAuthentication(
				new SystemAuthenticationToken());
	}

	private void processVerdict(final Verdict verdict) {
		try {
			Solution solution = solutionDao.fetchOneById(verdict
					.getSolutionId());
			Task task = taskDao.fetchOneById(solution.getTaskId());
			final TaskContainer taskContainer = this.taskContainerCache
					.getTaskContainerForTask(task);
			final Function<CompletableFuture<SolutionJudge>, CompletableFuture<SolutionJudge>> functionToApplyToJudge = (
					final CompletableFuture<SolutionJudge> futureJudge) -> {
				this.logInAsSystem();
				return futureJudge
						.thenApplyAsync(
								(final SolutionJudge judge) -> {
					this.logInAsSystem();
					try {
						if (!judge.isCompiled()) {
							return this.compileSolution(
									solution, judge,
									taskContainer
											.getProperties());
						}
					} catch (final Exception e) {
						TestingService.logger.error(
								"Solution compilation failed "
										+ "because judge for task "
										+ "\"{}\"({}) thew an "
										+ "exception: {}",
								task.getName(), task.getId(), e);
					} finally {
						Janitor.cleanUp(judge);
					}
					return judge;
				} , this.compilationAndCheckingExecutor)
						.thenApplyAsync(
								(final SolutionJudge judge) -> {
					this.logInAsSystem();
					try {
						this.checkVerdict(verdict, judge,
								taskContainer
										.getTestFiles(verdict
												.getPath()),
								verdict.getMaximumScore(),
								taskContainer.getProperties());
					} catch (final Throwable e) {
						Task t = taskService.getTaskFromVerdict(verdict);
						TestingService.logger
								.error("Solution judgement failed "
										+ "because judge for task "
										+ "\"{}\"({}) thew an "
										+ "exception: {}",
										t.getName(), t.getId(),
										e);
					} finally {
						Janitor.cleanUp(judge);
					}

					return judge;
				} , this.compilationAndCheckingExecutor)
						.handle((final SolutionJudge judge,
								final Throwable throwable) -> {
					this.logInAsSystem();

					if (throwable != null) {
						throw new RuntimeException(
								"Couldn't judge verdict: ", throwable);
					}
					return judge;
				});
			};
			this.logInAsSystem();

			taskContainer.applyToJudge(solution, functionToApplyToJudge);
		} catch (final Exception e) {
			TestingService.logger.error(
					"Couldn't schedule judgement for verdict {}: ", verdict, e);
		}
	}

	public void reloadTasks() {
		this.taskContainerCache.clear();
	}

	public void shutdownNow() {
		this.verdictCheckSchedulingExecutorService.shutdownNow();
	}

	@Transactional
	@CacheEvict(value = "solutions", key = "#solution.id")
	public void testSolutionOnAllTests(Solution solution) throws IOException {
		Task task = taskDao.fetchOneById(solution.getTaskId());
		final List<Verdict> verdicts = this.taskContainerCache
				.getTaskContainerForTask(task).generateTestVerdicts(solution);

		verdictDao.insert(verdicts);
	}
}
