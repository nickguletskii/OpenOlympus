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
package org.ng200.openolympus.tasks;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.ng200.openolympus.FileAccess;
import org.ng200.openolympus.SharedTemporaryStorageFactory;
import org.ng200.openolympus.cerberus.SolutionJudge;
import org.ng200.openolympus.cerberus.SolutionJudgeFactory;
import org.ng200.openolympus.cerberus.util.Lists;
import org.ng200.openolympus.model.Solution;
import org.ng200.openolympus.model.Verdict;
import org.ng200.openolympus.services.SolutionService;
import org.ng200.openolympus.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ForwardingConcurrentMap;

public class TaskContainer {
	private static final Logger logger = LoggerFactory
			.getLogger(TaskContainer.class);
	private final Path path;
	private final Properties properties = new Properties();
	private String factoryClassName;
	private final SolutionJudgeFactory solutionJudgeFactory;

	private final ConcurrentMap<Solution, CompletableFuture<SolutionJudge>> judgeCache = new ForwardingConcurrentMap<Solution, CompletableFuture<SolutionJudge>>() {
		private final ConcurrentHashMap<Solution, CompletableFuture<SolutionJudge>> delegate = new ConcurrentHashMap<>();

		@Override
		protected ConcurrentMap<Solution, CompletableFuture<SolutionJudge>> delegate() {
			return this.delegate;
		}

		@Override
		public CompletableFuture<SolutionJudge> remove(final Object object) {
			final CompletableFuture<SolutionJudge> futureJudge = super
					.remove(object);
			if (futureJudge != null) {
				futureJudge
						.whenComplete((judge, throwable) -> {
							if (judge != null) {
								try {
									judge.closeShared();
									TaskContainer.logger
											.info("Cleaned up after judge");
								} catch (final Exception e) {
									throw new RuntimeException(
											"Couldn't close judge: ", e);
								}
							}
							if (throwable != null) {
								throw new RuntimeException(throwable);
							}
						});
			}
			return futureJudge;
		}

	};

	private final SharedTemporaryStorageFactory sharedTemporaryStorageFactory;
	private final ExecutorService executorService;

	public TaskContainer(final Path path,
			final SharedTemporaryStorageFactory sharedTemporaryStorageFactory,
			final ExecutorService executorService) throws IOException,
			ClassNotFoundException, InstantiationException,
			IllegalAccessException {
		this.path = path;
		this.sharedTemporaryStorageFactory = sharedTemporaryStorageFactory;
		this.executorService = executorService;
		this.loadSettings();
		this.solutionJudgeFactory = this.loadFactoryClass(
				this.factoryClassName.trim()).newInstance();

		logger.info("Creating solution judge factory: {}", solutionJudgeFactory
				.getClass().toString());
	}

	public void applyToJudge(
			final Solution solution,
			final Function<CompletableFuture<SolutionJudge>, CompletableFuture<SolutionJudge>> function)
			throws ExecutionException {
		synchronized (solution) {
			this.judgeCache
					.compute(
							solution,
							(sol, futureJudge) -> {
								if (futureJudge == null) {
									futureJudge = CompletableFuture
											.supplyAsync(
													() -> TaskContainer.this.solutionJudgeFactory
															.createJudge(
																	TaskContainer.this.properties,
																	this.sharedTemporaryStorageFactory),
													this.executorService);
								}
								return function.apply(futureJudge);
							});
		}
	}

	public void collectGarbage(final SolutionService solutionService) {
		this.judgeCache
				.keySet()
				.stream()
				.filter(solution -> solutionService
						.getNumberOfPendingVerdicts(solution) == 0)
				.forEach(this.judgeCache::remove);
	}

	public List<Verdict> generateTestVerdicts(final Solution solution)
			throws IOException {
		final List<Pair<Path, Boolean>> testDirs = new ArrayList<>();
		testDirs.addAll(FileAccess.actOnChildren(
				this.path.resolve("mainTests"),
				(paths) -> paths.map(x -> new Pair<>(x, false)).collect(
						Collectors.toList())));
		testDirs.addAll(FileAccess.actOnChildren(
				this.path.resolve("baseTests"),
				(paths) -> paths.map(x -> new Pair<>(x, true)).collect(
						Collectors.toList())));

		final List<Verdict> verdicts = testDirs
				.stream()
				.sorted((x, y) -> Integer.compare(
						Integer.valueOf(x.getFirst().getFileName().toString()),
						Integer.valueOf(y.getFirst().getFileName().toString())))
				.map((test) -> {
					final String relativePath = this.path.relativize(
							test.getFirst()).toString();

					return new Verdict(solution, this.solutionJudgeFactory
							.getMaximumScoreForTest(relativePath,
									this.properties), relativePath, test
							.getSecond());
				}).collect(Collectors.toList());
		return verdicts;
	}

	public Properties getProperties() {
		return this.properties;
	}

	public List<Path> getTestFiles(final String testPath) {
		final Path base = this.path.resolve(testPath);
		try (Stream<Path> files = FileAccess.walkPaths(base)) {
			return (files.filter(p -> !p.equals(base))).collect(Collectors
					.toList());
		} catch (final IOException e) {
			throw new RuntimeException("Couldn't list files for test: ", e);
		}
	}

	@SuppressWarnings("unchecked")
	public Class<? extends SolutionJudgeFactory> loadFactoryClass(
			final String className) throws MalformedURLException,
			ClassNotFoundException {
		return (Class<? extends SolutionJudgeFactory>) getClassLoader()
				.loadClass(className);
	}

	public void loadSettings() throws IOException {
		try (BufferedReader reader = FileAccess.newBufferedReader(this.path
				.resolve("task.properties"))) {
			this.properties.load(reader);
		}
		this.factoryClassName = this.properties.getProperty("judgeFactory",
				"org.ng200.openolympus.cerberus.DefaultSolutionJudgeFactory");
	}

	public ClassLoader getClassLoader() throws MalformedURLException {
		@SuppressWarnings("resource")
		final ClassLoader classLoader = new URLClassLoader(getClassLoaderURLs()
				.toArray(new URL[0]), Thread.currentThread()
				.getContextClassLoader());
		return classLoader;
	}

	public List<URL> getClassLoaderURLs() throws MalformedURLException {
		final Path jarFile = this.path.resolve("task.jar");
		final URL url = jarFile.toUri().toURL();
		final URL[] urls = new URL[] {
			url
		};
		return Lists.from(urls);
	}

}
