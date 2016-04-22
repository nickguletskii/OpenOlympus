/**
 * The MIT License
 * Copyright (c) 2014-2016 Nick Guletskii
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.jooq.DSLContext;
import org.jooq.Record1;
import org.jooq.SelectForUpdateOfStep;
import org.jooq.impl.DSL;
import org.jppf.client.JPPFClient;
import org.jppf.client.JPPFJob;
import org.jppf.client.event.JobEvent;
import org.jppf.client.event.JobListenerAdapter;
import org.neo4j.cypher.internal.compiler.v2_2.planner.logical.steps.solveOptionalMatches;
import org.neo4j.cypher.internal.helpers.Converge.iterateUntilConverged;
import org.ng200.openolympus.cerberus.Janitor;
import org.ng200.openolympus.cerberus.SolutionJudge;
import org.ng200.openolympus.cerberus.SolutionResult;
import org.ng200.openolympus.cerberus.SolutionResult.Result;
import org.ng200.openolympus.cerberus.util.Lists;
import org.ng200.openolympus.exceptions.GeneralNestedRuntimeException;
import org.ng200.openolympus.jooq.Tables;
import org.ng200.openolympus.jooq.enums.VerdictStatusType;
import org.ng200.openolympus.jooq.tables.daos.SolutionDao;
import org.ng200.openolympus.jooq.tables.daos.TaskDao;
import org.ng200.openolympus.jooq.tables.daos.VerdictDao;
import org.ng200.openolympus.jooq.tables.pojos.Solution;
import org.ng200.openolympus.jooq.tables.pojos.Task;
import org.ng200.openolympus.jooq.tables.pojos.Verdict;
import org.ng200.openolympus.jppfsupport.JacksonSerializationDelegatingTask;
import org.ng200.openolympus.jppfsupport.JsonTaskExecutionResult;
import org.ng200.openolympus.jppfsupport.SolutionCompilationTask;
import org.ng200.openolympus.jppfsupport.VerdictCheckingTask;
import org.ng200.openolympus.tasks.TaskContainer;
import org.ng200.openolympus.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

@Service
public class TestingService {

	private static final Logger logger = LoggerFactory
			.getLogger(TestingService.class);
	private static final Object schedulerLock = new Object();
	private final JPPFClient jppfClient = new JPPFClient();

	private final Cache<Integer, Pair<String, String>> internalErrors = CacheBuilder
			.newBuilder().maximumSize(30).build();

	private int internalErrorCounter = 0;

	private final ScheduledExecutorService verdictCheckSchedulingExecutorService = Executors
			.newSingleThreadScheduledExecutor(new ThreadFactoryBuilder()
					.setNameFormat("Judgement scheduler %1$d").build());

	@Autowired
	private SolutionService solutionService;

	@Autowired
	private final TaskContainerCache taskContainerCache;

	@Autowired
	private SolutionDao solutionDao;

	@Autowired
	private TaskDao taskDao;

	@Autowired
	private VerdictDao verdictDao;

	private final StorageService storageService;

	private HashMap<Long, SolutionJudgeState> solutionJudgeStates = new HashMap<>();

	private Set<Long> solutionsToSweep = new HashSet<Long>();

	private DSLContext dslContext;

	private void markSolution(Long id) {
		synchronized (schedulerLock) {
			solutionsToSweep.add(id);
		}
	}

	private class SolutionJudgeState {
		private CompletableFuture<SolutionJudge> futureJudge = null;
		private Solution solution;
		private boolean closed = false;

		public SolutionJudgeState(
				Solution solution, Task task, TaskContainer taskContainer) {
			this.solution = solution;
			futureJudge = CompletableFuture
					.supplyAsync(() -> taskContainer.createSolutionJudge())
					.thenCompose((judge) -> {
						try {
							return (CompletableFuture<SolutionJudge>) compileSolution(
									solution, judge,
									taskContainer
											.getProperties());

						} catch (final Throwable e) {
							TestingService.logger.error(
									"Solution compilation failed "
											+ "because judge for task "
											+ "\"{}\"({}) thew an "
											+ "exception: {}",
									task.getName(), task.getId(), e);
							final CompletableFuture<SolutionJudge> ex = new CompletableFuture<>();
							ex.completeExceptionally(e);
							try {
								judge.closeShared();
							} catch (Exception e1) {
								tryCloseShared(judge);
							}
							return (CompletableFuture<SolutionJudge>) ex;
						} finally {
							markSolution(solution.getId());
							Janitor.cleanUp(judge);
							logger.info(
									"Calling janitor on solution judge for solution {}",
									solution.getId());
						}
					});
			futureJudge.handle((j, t) -> {
				if (t != null)
					tryCloseShared(j);
				return null;
			});
		}

		public void checkVerdict(Verdict verdict) {
			if (closed) {
				throw new IllegalStateException(
						"Attempt to invoke checkVerdict on a closed solution judge!");
			}
			try {
				final Solution solution = solutionDao.fetchOneById(verdict
						.getSolutionId());
				final Task task = taskDao
						.fetchOneById(solution.getTaskId());
				final TaskContainer taskContainer = taskContainerCache
						.getTaskContainerForTask(task);

				this.futureJudge
						.thenApply((judge) -> {
							try {
								TestingService.this.checkVerdict(verdict, judge,
										taskContainer
												.getTestFiles(verdict
														.getPath()),
										verdict.getMaximumScore(),
										taskContainer.getProperties());
								return judge;
							} catch (final Throwable e) {
								logger.error("Couldn't check verdict: ", e);
								throw new GeneralNestedRuntimeException(
										"Couldn't check verdict: ", e);
							} finally {
								markSolution(solution.getId());
								Janitor.cleanUp(judge);
								logger.info(
										"Called janitor on solution judge for solution {}",
										solution.getId());
							}
						});
			} catch (final Exception e) {
				TestingService.logger.error(
						"Couldn't schedule judgement for verdict {}: ", verdict,
						e);
			}
		}

		public void close() {
			this.closed = true;
			solutionJudgeStates.remove(this);
			this.futureJudge.thenAccept(j -> {
				tryCloseShared(j);
			});
		}

		private void tryCloseShared(SolutionJudge j) {
			try {
				j.closeShared();
				logger.info(
						"Closed solution judge's shared resources for solution {}",
						solution.getId());
			} catch (Exception e) {
				logger.error(
						"Couldn't close shared resources of the solution judge for solution {}",
						solution.getId());
			}
		}
	}

	@Autowired
	public TestingService(TaskContainerCache taskContainerCache,
			StorageService storageService, DSLContext dslContext) {
		this.taskContainerCache = taskContainerCache;
		this.storageService = storageService;
		this.dslContext = dslContext;

		this.verdictCheckSchedulingExecutorService.scheduleAtFixedRate(
				() -> {
					Verdict toTest;
					try {
						while ((toTest = this.getAndLockVerdictForTesting(
								dslContext)) != null) {
							this.processVerdict(toTest);
						}
					} catch (final Throwable t) {
						TestingService.logger.error(
								"Couldn't poll database for pending verdicts: {}",
								t);
					}
				}, 0, 100, TimeUnit.MILLISECONDS);

		this.verdictCheckSchedulingExecutorService.scheduleAtFixedRate(
				() -> {
					synchronized (schedulerLock) {
						solutionsToSweep.stream()
								.filter(id -> solutionTestingComplete(id))
								.forEach(id -> {
									TestingService.logger.info(
											"The solution judge for solution {} is garbage, removing!",
											id);
									solutionJudgeStates.remove(id).close();
								});
					}
				}, 0, 10, TimeUnit.SECONDS);
	}

	private boolean solutionTestingComplete(Long id) {
		return dslContext.select(DSL.field(DSL.not(DSL.exists(
				dslContext.selectOne().from(Tables.VERDICT)
						.where(Tables.VERDICT.SOLUTION_ID
								.eq(id)
								.and(Tables.VERDICT.STATUS
										.eq(VerdictStatusType.being_tested)
										.or(Tables.VERDICT.STATUS
												.eq(VerdictStatusType.waiting))))))))
				.fetchOne().value1();
	}

	private void checkVerdict(final Verdict verdict, final SolutionJudge judge,
			final List<Path> testFiles, final BigDecimal maximumScore,
			final Properties properties) throws ExecutionException {
		final Solution solution = this.solutionDao
				.fetchOneById(verdict.getSolutionId());
		final Task task = this.taskDao.fetchOneById(solution.getTaskId());

		try {
			TestingService.logger.info("Scheduling verdict {} for testing.",
					verdict.getId());

			final JPPFJob job = new JPPFJob();

			job.setName("Check verdict " + verdict.getId());

			final int priority = (int) ((verdict.getViewableDuringContest()
					? (Integer.MAX_VALUE / 2)
					: 0) - verdict.getId());

			job.getSLA().setMaxNodes(1);
			job.getSLA().setPriority(priority);
			job.getSLA().setMaxDispatchExpirations(3);

			final TaskContainer taskContainer = this.taskContainerCache
					.getTaskContainerForTask(task);

			Thread.currentThread().setContextClassLoader(
					new URLClassLoader(taskContainer.getClassLoaderURLs()
							.toArray(new URL[0]), Thread.currentThread()
									.getContextClassLoader()));

			job.add(new JacksonSerializationDelegatingTask<>(
					new VerdictCheckingTask(judge, testFiles, maximumScore,
							properties),
					taskContainer.getClassLoaderURLs()));

			job.setBlocking(false);

			this.jppfClient.registerClassLoader(taskContainer.getClassLoader(),
					job.getUuid());

			job.addJobListener(new JobListenerAdapter() {

				@Override
				public void jobEnded(JobEvent event) {

				}

				@Override
				public void jobReturned(JobEvent event) {
					try {
						final JsonTaskExecutionResult<Pair<SolutionJudge, SolutionResult>> checkingResult = ((JacksonSerializationDelegatingTask<Pair<SolutionJudge, SolutionResult>, VerdictCheckingTask>) event
								.getJob().getResults().getResultTask(0))
										.getResultOrThrowable();
						if (checkingResult.getError() != null) {
							throw checkingResult.getError();
						}

						final SolutionResult result = checkingResult.getResult()
								.getSecond();

						verdict.setScore(result.getScore());

						verdict.setMemoryPeak(result.getMemoryPeak());
						verdict.setCpuTime(
								Duration.ofMillis(result.getCpuTime()));
						verdict.setRealTime(
								Duration.ofMillis(result.getRealTime()));

						verdict.setStatus(
								TestingService.this
										.convertCerberusResultToVerdictStatus(
												result
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
									(stage, message) -> TestingService.this.internalErrors
											.put(
													TestingService.this.internalErrorCounter++,
													new Pair<String, String>(
															task.getName(),
															message)));
							logger.info(
									"The judge returned that an internal error has occurred, but no exception is available. More information: {}",
									result.getErrorMessages());
							break;
						case COMPILE_ERROR:
							final String message = result.getErrorMessages()
									.values()
									.stream().collect(Collectors.joining("\n"));

							verdict.setAdditionalInformation(message
									.substring(0, Math.min(128,
											message.length())));
							break;
						case WAITING:
							throw new IllegalStateException(
									"Judge returned result \"waiting\".");
						}
					} catch (final Throwable t) {
						verdict.setStatus(VerdictStatusType.internal_error);
						TestingService.logger.error(
								"Testing system internal error: couldn't commit a verdict: {}",
								t);
					} finally {
						TestingService.this.solutionService
								.updateVerdict(verdict);
					}
				}

			});
			this.jppfClient.submitJob(job);

		} catch (final Throwable throwable) {
			verdict.setStatus(VerdictStatusType.internal_error);
			this.solutionService.updateVerdict(verdict);
			throw new RuntimeException("Couldn't run solution: ", throwable);
		}
	}

	private CompletableFuture<SolutionJudge> compileSolution(
			final Solution solution,
			final SolutionJudge judge, final Properties properties)
			throws ExecutionException {

		final CompletableFuture<SolutionJudge> completableFuture = new CompletableFuture<>();

		final Task task = this.taskDao.fetchOneById(solution.getTaskId());

		try {
			TestingService.logger
					.info("Scheduling solution {} for compilation.",
							solution.getId());

			final JPPFJob job = new JPPFJob();

			job.setName("Compile solution " + solution.getId());

			job.getSLA().setMaxNodes(1);
			job.getSLA().setPriority(
					(int) (Integer.MAX_VALUE - solution.getId()));
			job.getSLA().setMaxDispatchExpirations(5);

			final TaskContainer taskContainer = this.taskContainerCache
					.getTaskContainerForTask(task);

			Thread.currentThread().setContextClassLoader(
					new URLClassLoader(taskContainer.getClassLoaderURLs()
							.toArray(new URL[0]), Thread.currentThread()
									.getContextClassLoader()));
			job.add(new JacksonSerializationDelegatingTask<>(
					new SolutionCompilationTask(judge, Lists
							.from(this.storageService
									.getSolutionFile(solution)),
							properties),
					taskContainer.getClassLoaderURLs()));

			job.setBlocking(false);

			this.jppfClient.registerClassLoader(taskContainer.getClassLoader(),
					job.getUuid());

			job.addJobListener(new JobListenerAdapter() {

				@Override
				public void jobEnded(JobEvent event) {

				}

				@Override
				public void jobReturned(JobEvent event) {
					try {
						final JsonTaskExecutionResult<SolutionJudge> result = ((JacksonSerializationDelegatingTask<SolutionJudge, SolutionCompilationTask>) event
								.getJob().getResults().getResultTask(0))
										.getResultOrThrowable();
						if (result.getError() != null) {
							completableFuture
									.completeExceptionally(result.getError());
						}
						completableFuture.complete(result.getResult());
					} catch (final Throwable t) {
						completableFuture.completeExceptionally(t);
					}
				}

			});
			this.jppfClient.submitJob(job);

			return completableFuture;

		} catch (final Throwable throwable) {
			throw new RuntimeException("Couldn't compile solution: ",
					throwable);
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

	private Verdict getAndLockVerdictForTesting(DSLContext dslContext) {
		final SelectForUpdateOfStep<Record1<Long>> view = dslContext
				.select(Tables.VERDICT.ID)
				.from(Tables.VERDICT)
				.where(Tables.VERDICT.STATUS
						.eq(VerdictStatusType.waiting))
				.orderBy(
						Tables.VERDICT.VIEWABLE_DURING_CONTEST
								.desc(),
						Tables.VERDICT.SOLUTION_ID.asc(),
						Tables.VERDICT.ID.asc())
				.limit(1)
				.forUpdate();
		return dslContext
				.update(Tables.VERDICT)
				.set(Tables.VERDICT.STATUS,
						VerdictStatusType.being_tested)
				.from(view)
				.where(Tables.VERDICT.ID
						.eq(view.field(Tables.VERDICT.ID)))
				.returning(Tables.VERDICT.fields())
				.fetchOptional()
				.map(verdictRecord -> verdictRecord
						.into(Verdict.class))
				.orElse(null);
	}

	public List<Pair<String, String>> getJudgeInternalErrors() {
		return new ArrayList<>(this.internalErrors.asMap().values());
	}

	private void processVerdict(final Verdict verdict) {
		try {
			final Solution solution = this.solutionDao.fetchOneById(verdict
					.getSolutionId());
			final Task task = this.taskDao.fetchOneById(solution.getTaskId());
			final TaskContainer taskContainer = this.taskContainerCache
					.getTaskContainerForTask(task);

			synchronized (schedulerLock) {
				this.solutionJudgeStates.computeIfAbsent(
						verdict.getSolutionId(),
						(id) -> new SolutionJudgeState(solution, task,
								taskContainer));
				this.solutionJudgeStates.get(verdict.getSolutionId())
						.checkVerdict(verdict);
			}

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
		final Task task = this.taskDao.fetchOneById(solution.getTaskId());
		final List<Verdict> verdicts = this.taskContainerCache
				.getTaskContainerForTask(task).generateTestVerdicts(solution);

		this.verdictDao.insert(verdicts);
	}
}
