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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.Callable;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.ng200.openolympus.controller.BindingResponse;
import org.ng200.openolympus.dto.TaskModificationDto;
import org.ng200.openolympus.model.Task;
import org.ng200.openolympus.services.StorageService;
import org.ng200.openolympus.services.TaskService;
import org.ng200.openolympus.services.TestingService;
import org.ng200.openolympus.validation.TaskValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TaskModificationRestController extends
		TaskFilesystemManipulatingController {
	@Autowired
	private TaskValidator taskValidator;

	@Autowired
	private TaskService taskService;

	@Autowired
	private TestingService testingService;

	@Autowired
	private StorageService storageService;

	@RequestMapping(value = "/api/task/{task}/edit", method = RequestMethod.GET)
	public TaskModificationDto getTask(@PathVariable("task") final Task task)
			throws IOException {

		final TaskModificationDto taskModificationDto = new TaskModificationDto();
		taskModificationDto.setPublished(task.isPublished());
		taskModificationDto.setName(task.getName());
		taskModificationDto.setDescriptionText(this.storageService
				.getTaskDescriptionSourcecode(task));
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

			task.setName(taskModificationDto.getName());
			task.setPublished(taskModificationDto.isPublished());

			if (taskModificationDto.getDescriptionFile() != null) {
				this.uploadDescription(task, taskModificationDto
						.getDescriptionFile().getInputStream());
			} else {
				this.uploadDescription(
						task,
						new ByteArrayInputStream(taskModificationDto
								.getDescriptionText().getBytes(
										Charset.forName("UTF8"))));
			}
			if (taskModificationDto.getJudgeFile() != null) {
				this.uploadJudgeFile(task, taskModificationDto);
			}

			this.taskService.saveTask(task);
			this.testingService.reloadTasks();
			return BindingResponse.OK;
		};

	}
}
