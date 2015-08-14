package org.ng200.openolympus.gitSupport;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.errors.RepositoryNotFoundException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryBuilder;
import org.eclipse.jgit.transport.ServiceMayNotContinueException;
import org.eclipse.jgit.transport.resolver.RepositoryResolver;
import org.eclipse.jgit.transport.resolver.ServiceNotAuthorizedException;
import org.eclipse.jgit.transport.resolver.ServiceNotEnabledException;
import org.ng200.openolympus.jooq.tables.pojos.Task;
import org.ng200.openolympus.jooq.tables.pojos.User;
import org.ng200.openolympus.services.StorageService;
import org.ng200.openolympus.services.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class OpenOlympusGitRepositoryResolver
		implements RepositoryResolver<HttpServletRequest> {

	private static final Logger logger = LoggerFactory
			.getLogger(OpenOlympusGitRepositoryResolver.class);
	private final Pattern TASK_EXTRACTION_PATTERN = Pattern
			.compile("^tasks/([0-9]+)$");

	@Autowired
	private TaskService taskService;

	@Autowired
	private StorageService storageService;

	@Override
	public Repository open(HttpServletRequest req, String name)
			throws RepositoryNotFoundException,
			ServiceNotAuthorizedException,
			ServiceNotEnabledException,
			ServiceMayNotContinueException {
		Object principal = SecurityContextHolder
				.getContext().getAuthentication()
				.getPrincipal();
		if (principal == null || !(principal instanceof User)) {
			throw new ServiceNotAuthorizedException();
		}

		User user = (User) (principal);

		Matcher matcher = TASK_EXTRACTION_PATTERN.matcher(name);
		if (!matcher.matches()) {
			throw new RepositoryNotFoundException(name);
		}

		int id = Integer.valueOf(matcher.group(1));
		
		logger.info("Id: {}", id);
		
		Task task = taskService.getById(id);

		if (!taskService.canModifyTask(task, user)) {
			throw new ServiceMayNotContinueException(
					"The user doesn't have the permissions required to access this repository.");
		}
		
		try {
			return new RepositoryBuilder()
					.setWorkTree(storageService
							.getTaskDescriptionDirectory(task)
							.toFile())
					.build();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}