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
package org.ng200.openolympus.config;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteWatchdog;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ResetCommand.ResetType;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.errors.NoWorkTreeException;
import org.eclipse.jgit.lib.Repository;
import org.ng200.openolympus.FileAccess;
import org.ng200.openolympus.gitSupport.OpenOlympusGitRepositoryResolver;
import org.ng200.openolympus.gitSupport.OpenOlympusGitServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GitConfig {

	private static final Logger logger = LoggerFactory
			.getLogger(GitConfig.class);

	@Autowired
	private WebSecurityConfig webSecurityConfig;

	public GitConfig() {
		Repository.getGlobalListenerList()
				.addRefsChangedListener(event -> {
					try (Git git = new Git(event.getRepository())) {
						// TODO: check what steps must be taken here

						final Path root = event.getRepository().getWorkTree()
								.toPath();

						Files.list(root)
								.filter(path1 -> !path1
										.equals(root.resolve(".git")))
								.forEach(path2 -> {
							try {
								if (Files.isDirectory(path2)) {
									FileAccess
											.deleteDirectoryByWalking(path2);
								} else {
									FileAccess.delete(path2);
								}
							} catch (final Exception e1) {
								throw new RuntimeException(e1);
							}
						});

						git.reset().setMode(ResetType.HARD).call();
						// TODO: expose logs, possibly use chroot
						final File buildFile = new File(
								event.getRepository().getWorkTree(),
								"build");
						if (buildFile.exists()) {
							final CommandLine commandLine = new CommandLine(
									buildFile);

							final DefaultExecutor executor = new DefaultExecutor() {
								@Override
								public boolean isFailure(int exitValue) {
									return false;
								}
							};
							executor.setWatchdog(
									new ExecuteWatchdog(10000));
							executor.setWorkingDirectory(
									event.getRepository().getWorkTree());
							executor.execute(commandLine);
						}
					} catch (NoWorkTreeException | GitAPIException
							| IOException e2) {
						GitConfig.logger.error(
								"Couldn't synchronise work tree with current ref: {}",
								e2);
					}
				});
	}

	@Bean
	public OpenOlympusGitRepositoryResolver openOlympusGitRepositoryResolver() {
		return new OpenOlympusGitRepositoryResolver();
	}

	@Bean
	public ServletRegistrationBean servletRegistrationBean() throws Exception {

		final OpenOlympusGitServlet gitServlet = new OpenOlympusGitServlet(
				this.webSecurityConfig.getBasicAuthenticationFilter());

		gitServlet.setRepositoryResolver(
				this.openOlympusGitRepositoryResolver());

		return new ServletRegistrationBean(gitServlet, "/git/*");
	}
}
