package org.ng200.openolympus.config;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.tools.StandardLocation;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteWatchdog;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ResetCommand.ResetType;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.errors.NoWorkTreeException;
import org.eclipse.jgit.events.RefsChangedEvent;
import org.eclipse.jgit.events.RefsChangedListener;
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

	@Autowired
	private WebSecurityConfig webSecurityConfig;

	private static final Logger logger = LoggerFactory
			.getLogger(GitConfig.class);

	public GitConfig() {
		Repository.getGlobalListenerList()
				.addRefsChangedListener(new RefsChangedListener() {

					@Override
					public void onRefsChanged(RefsChangedEvent event) {
						try (Git git = new Git(event.getRepository())) {
							// TODO: check what steps must be taken here

							Path root = event.getRepository().getWorkTree()
									.toPath();

							Files.list(root)
									.filter(path -> !path
											.equals(root.resolve(".git")))
									.forEach(path -> {
								try {
									if (Files.isDirectory(path)) {
										FileAccess
												.deleteDirectoryByWalking(path);
									} else {
										FileAccess.delete(path);
									}
								} catch (Exception e) {
									throw new RuntimeException(e);
								}
							});

							git.reset().setMode(ResetType.HARD).call();
							// TODO: expose logs, possibly use chroot
							File buildFile = new File(
									event.getRepository().getWorkTree(),
									"build");
							if (buildFile.exists()) {
								CommandLine commandLine = new CommandLine(
										buildFile);

								DefaultExecutor executor = new DefaultExecutor() {
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
								| IOException e) {
							logger.error(
									"Couldn't synchronise work tree with current ref: {}",
									e);
						}
					}
				});
	}

	@Bean
	public ServletRegistrationBean servletRegistrationBean() throws Exception {

		OpenOlympusGitServlet gitServlet = new OpenOlympusGitServlet(
				webSecurityConfig.getBasicAuthenticationFilter());

		gitServlet.setRepositoryResolver(
				openOlympusGitRepositoryResolver());

		return new ServletRegistrationBean(gitServlet, "/git/*");
	}

	@Bean
	public OpenOlympusGitRepositoryResolver openOlympusGitRepositoryResolver() {
		return new OpenOlympusGitRepositoryResolver();
	}
}
