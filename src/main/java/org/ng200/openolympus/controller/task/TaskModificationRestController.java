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
import java.util.concurrent.Callable;

import javax.validation.Valid;

import org.ng200.openolympus.SecurityClearanceType;
import org.ng200.openolympus.controller.BindingResponse;
import org.ng200.openolympus.dto.TaskModificationDto;
import org.ng200.openolympus.jooq.enums.TaskPermissionType;
import org.ng200.openolympus.jooq.tables.pojos.Task;
import org.ng200.openolympus.security.annotations.SecurityAnd;
import org.ng200.openolympus.security.annotations.SecurityLeaf;
import org.ng200.openolympus.security.annotations.SecurityOr;
import org.ng200.openolympus.security.annotations.TaskPermissionRequired;
import org.ng200.openolympus.security.predicates.UserHasTaskPermission;
import org.ng200.openolympus.services.task.TaskUploadService;
import org.ng200.openolympus.validation.TaskValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Profile("web")
@SecurityOr({
				@SecurityAnd({
								@SecurityLeaf(value = SecurityClearanceType.APPROVED_USER, predicates = UserHasTaskPermission.class)
		})
})
@TaskPermissionRequired(TaskPermissionType.modify)
public class TaskModificationRestController {
	@Autowired
	private TaskValidator taskValidator;

	@Autowired
	private TaskUploadService taskCRUDService;

	@RequestMapping(value = "/api/task/{task}/edit", method = RequestMethod.GET)
	public TaskModificationDto getTask(@PathVariable("task") final Task task)
			throws IOException {

		final TaskModificationDto taskModificationDto = new TaskModificationDto();
		taskModificationDto.setName(task.getName());
		return taskModificationDto;
	}

	@RequestMapping(value = "/api/task/{task}/edit", method = RequestMethod.POST)
	public Callable<BindingResponse> patchTask(
			@PathVariable("task") final Task task,
			@Valid final TaskModificationDto taskModificationDto,
			final BindingResult bindingResult) {
		return () -> {
			if (bindingResult.hasErrors()) {
				throw new BindException(bindingResult);
			}
			this.taskValidator.validate(taskModificationDto, bindingResult,
					task.getName(), true);
			if (bindingResult.hasErrors()) {
				throw new BindException(bindingResult);
			}

			this.taskCRUDService.patchTask(task, taskModificationDto);
			return BindingResponse.OK;
		};

	}

}
