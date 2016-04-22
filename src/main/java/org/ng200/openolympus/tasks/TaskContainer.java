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
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.ng200.openolympus.FileAccess;
import org.ng200.openolympus.SharedTemporaryStorageFactory;
import org.ng200.openolympus.cerberus.SolutionJudge;
import org.ng200.openolympus.cerberus.SolutionJudgeFactory;
import org.ng200.openolympus.cerberus.util.Lists;
import org.ng200.openolympus.jooq.enums.VerdictStatusType;
import org.ng200.openolympus.jooq.tables.pojos.Solution;
import org.ng200.openolympus.jooq.tables.pojos.Verdict;
import org.ng200.openolympus.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TaskContainer {
	private static final Logger logger = LoggerFactory
			.getLogger(TaskContainer.class);
	private final Path path;
	private final Properties properties = new Properties();
	private String factoryClassName;
	private final SolutionJudgeFactory solutionJudgeFactory;
	private SharedTemporaryStorageFactory sharedTemporaryStorageFactory;

	public SolutionJudge createSolutionJudge() {
		return TaskContainer.this.solutionJudgeFactory
				.createJudge(
						TaskContainer.this.properties,
						this.sharedTemporaryStorageFactory);
	}

	public TaskContainer(final Path path,
			final SharedTemporaryStorageFactory sharedTemporaryStorageFactory)
			throws IOException,
			ClassNotFoundException, InstantiationException,
			IllegalAccessException {
		this.path = path;
		this.sharedTemporaryStorageFactory = sharedTemporaryStorageFactory;
		this.loadSettings();
		this.solutionJudgeFactory = this.loadFactoryClass(
				this.factoryClassName.trim()).newInstance();

		TaskContainer.logger.info("Creating solution judge factory: {}",
				this.solutionJudgeFactory
						.getClass().toString());
	}

	public List<Verdict> generateTestVerdicts(final Solution solution)
			throws IOException {
		final List<Pair<Path, Boolean>> testDirs = new ArrayList<>();
		testDirs.addAll(FileAccess.actOnChildren(
				this.path.resolve("mainTests"),
				(paths) -> paths.map(x -> new Pair<>(x, false)).collect(
						Collectors.toList())));

		if (FileAccess.exists(this.path.resolve("baseTests"))) {
			testDirs.addAll(FileAccess.actOnChildren(
					this.path.resolve("baseTests"),
					(paths) -> paths.map(x -> new Pair<>(x, true)).collect(
							Collectors.toList())));
		}

		final List<Verdict> verdicts = testDirs
				.stream()
				.sorted((x, y) -> Integer.compare(
						Integer.valueOf(x.getFirst().getFileName().toString()),
						Integer.valueOf(y.getFirst().getFileName().toString())))
				.map((test) -> {
					final String relativePath = this.path.relativize(
							test.getFirst()).toString();
					return new Verdict()
							.setSolutionId(solution.getId())
							.setMaximumScore(
									this.solutionJudgeFactory
											.getMaximumScoreForTest(
													relativePath,
													this.properties))
							.setPath(relativePath)
							.setViewableDuringContest(test.getSecond())
							.setStatus(VerdictStatusType.waiting);
				}).collect(Collectors.toList());
		return verdicts;
	}

	public ClassLoader getClassLoader() throws MalformedURLException {
		return new URLClassLoader(
				this.getClassLoaderURLs()
						.toArray(new URL[0]),
				Thread.currentThread()
						.getContextClassLoader());
	}

	public List<URL> getClassLoaderURLs() throws MalformedURLException {
		final Path jarFile = this.path.resolve("task.jar");
		final URL url = jarFile.toUri().toURL();
		final URL[] urls = new URL[] {
										url
		};
		return Lists.from(urls);
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
		return (Class<? extends SolutionJudgeFactory>) this.getClassLoader()
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

}
