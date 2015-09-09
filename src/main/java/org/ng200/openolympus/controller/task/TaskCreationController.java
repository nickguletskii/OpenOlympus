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
package org.ng200.openolympus.controller.task;

import java.io.IOException;
import java.security.Principal;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRefNameException;
import org.eclipse.jgit.api.errors.RefAlreadyExistsException;
import org.eclipse.jgit.api.errors.RefNotFoundException;
import org.ng200.openolympus.SecurityClearanceType;
import org.ng200.openolympus.controller.BindingResponse;
import org.ng200.openolympus.controller.BindingResponse.Status;
import org.ng200.openolympus.dto.TaskCreationDto;
import org.ng200.openolympus.jooq.tables.pojos.Task;
import org.ng200.openolympus.jooq.tables.pojos.User;
import org.ng200.openolympus.security.annotations.SecurityAnd;
import org.ng200.openolympus.security.annotations.SecurityLeaf;
import org.ng200.openolympus.security.annotations.SecurityOr;
import org.ng200.openolympus.services.TaskService;
import org.ng200.openolympus.services.UserService;
import org.ng200.openolympus.validation.TaskValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.ImmutableMap;

@RestController
@Profile("web")
public class TaskCreationController {

	@Autowired
	private TaskValidator taskValidator;

	@Autowired
	private TaskService taskService;

	@Autowired
	private UserService userService;

	@SecurityOr({
					@SecurityAnd({
									@SecurityLeaf(value = SecurityClearanceType.TASK_SUPERVISOR)
			})
	})
	@RequestMapping(value = "/api/task/create", method = RequestMethod.POST)
	public BindingResponse createTask(final TaskCreationDto taskCreationDto,
			final BindingResult bindingResult, Principal principal)
					throws IOException, BindException,
					RefAlreadyExistsException, RefNotFoundException,
					InvalidRefNameException, GitAPIException {
		this.taskValidator.validate(taskCreationDto, bindingResult, null,
				false);

		if (bindingResult.hasErrors()) {
			throw new BindException(bindingResult);
		}
		final User owner = this.userService
				.getUserByUsername(principal.getName());

		final Task task = this.taskService.uploadTask(taskCreationDto,
				bindingResult,
				owner);

		return new BindingResponse(Status.OK,
				null, ImmutableMap.<String, Object> builder()
						.put("taskId", task.getId()).build());
	}

}
