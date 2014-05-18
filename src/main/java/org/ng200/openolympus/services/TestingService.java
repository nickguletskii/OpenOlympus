/**
 * The MIT License
 * Copyright (c) 2014 Nick Guletskii
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
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.ng200.openolympus.Pair;
import org.ng200.openolympus.StorageSpace;
import org.ng200.openolympus.cerberus.SolutionResult;
import org.ng200.openolympus.cerberus.util.Exceptions;
import org.ng200.openolympus.model.Solution;
import org.ng200.openolympus.model.Task;
import org.ng200.openolympus.model.Verdict;
import org.ng200.openolympus.repositories.SolutionRepository;
import org.ng200.openolympus.repositories.UserRepository;
import org.ng200.openolympus.repositories.VerdictRepository;
import org.ng200.openolympus.tasks.TaskContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

@Service
public class TestingService implements Runnable {

	private static final Logger logger = LoggerFactory
			.getLogger(TestingService.class);
	public static File SOLUTION_LOCATION = new File(MessageFormat.format(
			"{0}/solutions/", StorageSpace.STORAGE_PREFIX));
	public static File TASK_LOCATION = new File(MessageFormat.format(
			"{0}/tasks/checkingchains/", StorageSpace.STORAGE_PREFIX));
	static {
		if (TestingService.SOLUTION_LOCATION.exists()
				&& !TestingService.SOLUTION_LOCATION.isDirectory()) {
			TestingService.SOLUTION_LOCATION.delete();
		}
		if (!TestingService.SOLUTION_LOCATION.exists()) {
			TestingService.SOLUTION_LOCATION.mkdirs();
		}
	}
	@Autowired
	private SolutionService solutionService;
	@Autowired
	private SolutionRepository solutionRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private VerdictRepository verdictRepository;

	private final Map<Long, TaskContainer> taskContainers = new HashMap<>();

	private final Cache<Integer, Pair<String, String>> internalErrors = CacheBuilder
			.newBuilder().maximumSize(30).build();

	private int internalErrorCounter = 0;

	public List<Pair<String, String>> getInternalErrors() {
		return new ArrayList<>(this.internalErrors.asMap().values());
	}

	private TaskContainer getTaskContainer(final Task task) {
		if (!this.taskContainers.containsKey(task.getId())) {
			try {
				this.taskContainers.put(
						task.getId(),
						new TaskContainer(new File(
								TestingService.TASK_LOCATION, task
								.getTaskLocation())));
			} catch (ClassNotFoundException | InstantiationException
					| IllegalAccessException | IOException e) {
				throw new RuntimeException("Bad task", e);
			}
		}
		return this.taskContainers.get(task.getId());
	}

	private void performJudgement(final Verdict verdict) {
		try {
			final TaskContainer taskContainer = this.getTaskContainer(verdict
					.getSolution().getTask());
			SolutionResult result;
			result = taskContainer.checkSolution(verdict.getSolution(),
					verdict.getPathToTest(), false, verdict.getMaximumScore());
			verdict.setScore(result.getScore());
			verdict.setMemoryPeak(result.getMemoryPeak());
			verdict.setCpuTime(result.getCpuTime());
			verdict.setRealTime(result.getRealTime());
			switch (result.getResult()) {
			case OK:
				verdict.setMessage("solution.result.ok");
				break;
			case TIME_LIMIT:
				verdict.setMessage("solution.result.timeLimit");
				break;
			case MEMORY_LIMIT:
				verdict.setMessage("solution.result.memoryLimit");
				break;
			case OUTPUT_LIMIT:
				verdict.setMessage("solution.result.outputLimit");
				break;
			case RUNTIME_ERROR:
				verdict.setMessage("solution.result.runtimeError");
				break;
			case INTERNAL_ERROR:
				verdict.setMessage("solution.result.internalError");
				result.getErrorMessages().forEach(
						(stage, message) -> this.internalErrors.put(
								this.internalErrorCounter++,
								new Pair<String, String>(verdict.getSolution()
										.getTask().getName(), message)));
				break;
			case SECURITY_VIOLATION:
				verdict.setMessage("solution.result.securityViolation");
				verdict.setUnauthorisedSyscall(result.getUnauthorisedSyscall());
				break;
			case COMPILE_ERROR:
				verdict.setMessage("solution.result.compileError");
				final String message = result.getErrorMessages().values()
						.stream().collect(Collectors.joining("\n"));
				verdict.setAdditionalInformation(HtmlUtils.htmlEscape(message
						.substring(0, Math.min(128, message.length()))));
				break;
			case PRESENTATION_ERROR:
				verdict.setMessage("solution.result.presentationError");
				break;
			case WRONG_ANSWER:
				verdict.setMessage("solution.result.wrongAnswer");
				break;
			}
		} catch (final Exception e) {
			verdict.setMessage("solution.result.internalError");
			TestingService.logger.error("Task runtime error (id: {}) : {}",
					Long.toString(verdict.getSolution().getTask().getId()),
					Exceptions.toString(e));
			this.internalErrors.put(this.internalErrorCounter++,
					new Pair<String, String>(verdict.getSolution().getTask()
							.getName(), Exceptions.toString(e)));
		} finally {
			verdict.setTested(true);
			this.verdictRepository.save(verdict);
		}
	}

	public void reloadTasks() {
		this.taskContainers.clear();
	}

	@Override
	public void run() {
		try {
			while (!Thread.interrupted()) {
				boolean skipSleep = false;
				for (final Verdict verdict : this.solutionService
						.getPendingVerdicts()) {
					TestingService.logger.info("Testing verdict {}",
							verdict.getId());
					this.performJudgement(verdict);
					if (!verdict.isViewableWhenContestRunning()) {
						skipSleep = true;
						break;
					}
				}
				if (!skipSleep) {
					Thread.sleep(1000);
				}
			}
		} catch (final InterruptedException e) {
		}
	}

	public void testSolutionOnAllTests(final Solution solution) {
		final List<Verdict> verdicts = this
				.getTaskContainer(solution.getTask()).generateTestVerdicts(
						solution);
		this.verdictRepository.save(verdicts);
	}
}
