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
package org.ng200.openolympus.tasks;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.ng200.openolympus.FileAccess;
import org.ng200.openolympus.Pair;
import org.ng200.openolympus.cerberus.SolutionJudge;
import org.ng200.openolympus.cerberus.SolutionJudgeFactory;
import org.ng200.openolympus.cerberus.SolutionResult;
import org.ng200.openolympus.cerberus.util.Lists;
import org.ng200.openolympus.model.Solution;
import org.ng200.openolympus.model.Verdict;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalNotification;

public class TaskContainer {
	private static final Logger logger = LoggerFactory
			.getLogger(TaskContainer.class);
	private final File path;
	private Class<? extends SolutionJudgeFactory> checkingChainFactoryClass;
	private final Properties properties = new Properties();
	private String factoryClassName;
	private final SolutionJudgeFactory checkingChainFactory;

	private final LoadingCache<Solution, SolutionJudge> checkingChainCache = CacheBuilder
			.newBuilder()
			.maximumSize(20)
			.expireAfterWrite(5, TimeUnit.MINUTES)
			.removalListener(
					(final RemovalNotification<Solution, SolutionJudge> arg0) -> {
						try {
							arg0.getValue().close();
						} catch (final IOException e) {
							TaskContainer.logger.error(
									"Couldn't close solution judge: {}", e);
						}
					}).build(new CacheLoader<Solution, SolutionJudge>() {
						@Override
						public SolutionJudge load(final Solution solution)
								throws Exception {
							return TaskContainer.this.checkingChainFactory
									.createChain(TaskContainer.this.properties);
						}
					});

	public TaskContainer(final File path) throws IOException,
	ClassNotFoundException, InstantiationException,
	IllegalAccessException {
		this.path = path;
		this.loadSettings();
		this.loadClasses(this.factoryClassName.trim());
		this.checkingChainFactory = this.checkingChainFactoryClass
				.newInstance();
	}

	public SolutionResult checkSolution(final Solution solution,
			final String testPath, final boolean isUserTest,
			final BigDecimal maximumScore) {
		SolutionJudge checkingChain;
		try {
			checkingChain = this.checkingChainCache.get(solution);
		} catch (final ExecutionException e1) {
			TaskContainer.logger.error(
					"Couldn't get a judge for solution! Error: {}", e1);
			return null;
		}
		if (!checkingChain.isCompiled()) {
			checkingChain.compile(Lists.from(new File(solution.getFile())));
		}
		if (isUserTest) {
			throw new UnsupportedOperationException(
					"User tests aren't supported yet");
		} else {
			final File base = new File(this.path, testPath);
			try (Stream<File> files = FileAccess.walkFiles(base)) {
				return checkingChain.run(files.filter(p -> !p.equals(base))
						.collect(Collectors.toList()), true, maximumScore,
						this.properties);
			} catch (final IOException e) {
				TaskContainer.logger.error("Internal error: {}", e);
				throw new RuntimeException("Couldn't list files for test: ", e);
			}
		}
	}

	public List<Verdict> generateTestVerdicts(final Solution solution) {
		final List<Pair<File, Boolean>> testDirs = new ArrayList<>();
		testDirs.addAll(Arrays
				.asList(new File(this.path, "mainTests").listFiles()).stream()
				.map(x -> new Pair<>(x, false)).collect(Collectors.toList()));
		testDirs.addAll(Arrays
				.asList(new File(this.path, "baseTests").listFiles()).stream()
				.map(x -> new Pair<>(x, true)).collect(Collectors.toList()));

		final List<Verdict> verdicts = testDirs
				.stream()
				.sorted((x, y) -> Integer.compare(
						Integer.valueOf(x.getFirst().getName()),
						Integer.valueOf(y.getFirst().getName())))
						.map((test) -> {
							final String relativePath = this.path.toPath()
									.relativize(test.getFirst().toPath()).toString();

							return new Verdict(solution, this.checkingChainFactory
									.getMaximumScoreForTest(relativePath,
											this.properties), relativePath, test
											.getSecond());
						}).collect(Collectors.toList());
		return verdicts;
	}

	@SuppressWarnings("unchecked")
	public void loadClasses(final String className)
			throws MalformedURLException, ClassNotFoundException {
		try {
			this.checkingChainFactoryClass = (Class<? extends SolutionJudgeFactory>) TaskContainer.class
					.getClassLoader().loadClass(className);
		} catch (final ClassNotFoundException e) {
			final File jarFile = new File(this.path, "task.jar");
			final URL url = jarFile.toURI().toURL();
			final URL[] urls = new URL[] { url };
			@SuppressWarnings("resource")
			final ClassLoader classLoader = new URLClassLoader(urls);

			this.checkingChainFactoryClass = (Class<? extends SolutionJudgeFactory>) classLoader
					.loadClass(className);
		}
	}

	public void loadSettings() throws IOException {
		try (BufferedReader reader = FileAccess.newBufferedReader(this.path
				.toPath().resolve("task.properties"))) {
			this.properties.load(reader);
		}
		this.factoryClassName = this.properties
				.getProperty("checkingChainFactory",
						"org.ng200.openolympus.cerberus.DefaultSolutionCheckingChainFactory");
	}
}
