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
package org.ng200.openolympus.controller.task;

import java.io.IOException;
import java.nio.file.Path;
import java.security.Principal;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.ng200.openolympus.Assertions;
import org.ng200.openolympus.FileAccess;
import org.ng200.openolympus.SecurityClearanceType;
import org.ng200.openolympus.controller.BindingResponse;
import org.ng200.openolympus.controller.BindingResponse.Status;
import org.ng200.openolympus.dto.SolutionSubmissionDto;
import org.ng200.openolympus.exceptions.ForbiddenException;
import org.ng200.openolympus.exceptions.ResourceNotFoundException;
import org.ng200.openolympus.exceptions.TaskDescriptionIncorrectFormatException;
import org.ng200.openolympus.jooq.enums.TaskPermissionType;
import org.ng200.openolympus.jooq.tables.pojos.Solution;
import org.ng200.openolympus.jooq.tables.pojos.Task;
import org.ng200.openolympus.jooq.tables.pojos.User;
import org.ng200.openolympus.security.annotations.SecurityAnd;
import org.ng200.openolympus.security.annotations.SecurityLeaf;
import org.ng200.openolympus.security.annotations.SecurityOr;
import org.ng200.openolympus.security.annotations.TaskPermissionRequired;
import org.ng200.openolympus.security.predicates.TaskViewSecurityPredicate;
import org.ng200.openolympus.services.SolutionSubmissionService;
import org.ng200.openolympus.services.StorageService;
import org.ng200.openolympus.services.UserService;
import org.ng200.openolympus.validation.SolutionDtoValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.util.AntPathMatcher;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.HandlerMapping;

import com.google.common.collect.ImmutableMap;

@RestController
@Profile("web")
@SecurityOr({
				@SecurityAnd({
								@SecurityLeaf(value = SecurityClearanceType.APPROVED_USER, predicates = TaskViewSecurityPredicate.class)
		})
})
public class TaskViewController {

	public static class TaskDescriptionView {
		private String name;
		private String description;

		public TaskDescriptionView(String name, String description) {
			super();
			this.name = name;
			this.description = description;
		}

		public String getDescription() {
			return this.description;
		}

		public String getName() {
			return this.name;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public void setName(String name) {
			this.name = name;
		}
	}

	@Autowired
	private UserService userService;

	@Autowired
	private SolutionDtoValidator solutionDtoValidator;

	@Autowired
	private StorageService storageService;

	@Autowired
	private SolutionSubmissionService solutionSubmissionService;

	@ResponseBody
	@RequestMapping(value = "/api/task/{task}/data/**", method = RequestMethod.GET)
	public FileSystemResource getTaskData(
			@PathVariable(value = "task") final Task task,
			HttpServletRequest request)
					throws IOException,
					TaskDescriptionIncorrectFormatException {
		Assertions.resourceExists(task);

		final String pathWithinHandler = (String) request.getAttribute(
				HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
		final String bestMatchPattern = (String) request
				.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);

		final AntPathMatcher apm = new AntPathMatcher();
		final String relativePath = apm.extractPathWithinPattern(
				bestMatchPattern,
				pathWithinHandler);

		final Path taskDescriptionDir = this.storageService
				.getTaskDescriptionDirectory(task);
		final Path path = taskDescriptionDir.resolve(relativePath);

		if (!path.toAbsolutePath()
				.startsWith(taskDescriptionDir.toAbsolutePath())) {
			throw new ForbiddenException(
					"Security: Attempt to serve file outside of current task scope!");
		}

		if (!FileAccess.exists(path)) {
			throw new ResourceNotFoundException(
					"The requested file doesn't exist!");
		}

		return new FileSystemResource(path.toAbsolutePath().toFile());
	}

	@ResponseBody
	@RequestMapping(value = "/api/task/{task}/name", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
	public String getTaskName(@PathVariable(value = "task") final Task task,
			final Locale locale) throws IOException {
		Assertions.resourceExists(task);
		return task.getName();
	}

	@RequestMapping(value = "/api/task/{task}/submitSolution", method = RequestMethod.POST)
	public BindingResponse submitSolution(
			@PathVariable("task") final Task task, final Principal principal,
			@Valid final SolutionSubmissionDto solutionDto,
			final BindingResult bindingResult) throws BindException,
					IOException {
		if (bindingResult.hasErrors()) {
			throw new BindException(bindingResult);
		}

		Assertions.resourceExists(task);

		final User user = this.userService.getUserByUsername(principal
				.getName());

		this.solutionDtoValidator.validate(solutionDto, bindingResult);

		if (bindingResult.hasErrors()) {
			throw new BindException(bindingResult);
		}

		final Solution solution = this.solutionSubmissionService.submitSolution(
				task,
				solutionDto, user);

		final long id = solution.getId();

		return new BindingResponse(Status.OK, null,
				new ImmutableMap.Builder<String, Object>().put("id", id)
						.build());
	}
}
