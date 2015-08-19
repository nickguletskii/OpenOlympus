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
package org.ng200.openolympus.gitSupport;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

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
		final Object principal = SecurityContextHolder
				.getContext().getAuthentication()
				.getPrincipal();
		if (principal == null || !(principal instanceof User)) {
			throw new ServiceNotAuthorizedException();
		}

		final User user = (User) (principal);

		final Matcher matcher = this.TASK_EXTRACTION_PATTERN.matcher(name);
		if (!matcher.matches()) {
			throw new RepositoryNotFoundException(name);
		}

		final int id = Integer.valueOf(matcher.group(1));

		OpenOlympusGitRepositoryResolver.logger.info("Id: {}", id);

		final Task task = this.taskService.getById(id);

		if (!this.taskService.canModifyTask(task, user)) {
			throw new ServiceMayNotContinueException(
					"The user doesn't have the permissions required to access this repository.");
		}

		try {
			return new RepositoryBuilder()
					.setWorkTree(this.storageService
							.getTaskDescriptionDirectory(task)
							.toFile())
					.build();
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}
}