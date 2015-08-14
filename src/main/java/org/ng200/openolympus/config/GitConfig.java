package org.ng200.openolympus.config;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ResetCommand.ResetType;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.errors.NoWorkTreeException;
import org.eclipse.jgit.events.RefsChangedEvent;
import org.eclipse.jgit.events.RefsChangedListener;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryBuilder;
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
							git.clean()
									.setCleanDirectories(true)
									.setIgnore(false).call();
							git.reset().setMode(ResetType.HARD).call();
							git.clean()
									.setCleanDirectories(true)
									.setIgnore(false).call();
						} catch (NoWorkTreeException | GitAPIException e) {
							logger.error("Couldn't clean work tree: {}", e);
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
