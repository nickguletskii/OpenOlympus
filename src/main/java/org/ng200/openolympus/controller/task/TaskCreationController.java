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

import java.nio.file.Path;
import java.time.Instant;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.locks.Lock;

import javax.validation.Valid;

import org.apache.commons.compress.archivers.ArchiveException;
import org.ng200.openolympus.FileAccess;
import org.ng200.openolympus.SecurityExpressionConstants;
import org.ng200.openolympus.controller.BindingResponse;
import org.ng200.openolympus.dto.TaskCreationDto;
import org.ng200.openolympus.exceptions.GeneralNestedRuntimeException;
import org.ng200.openolympus.model.Task;
import org.ng200.openolympus.services.StorageService;
import org.ng200.openolympus.services.TaskService;
import org.ng200.openolympus.validation.TaskValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TaskCreationController extends
		TaskFilesystemManipulatingController {

	@Autowired
	private TaskValidator taskValidator;

	@Autowired
	private TaskService taskService;
	@Autowired
	private StorageService storageService;

	@PreAuthorize(SecurityExpressionConstants.IS_ADMIN)
	@RequestMapping(value = "/api/task/create", method = RequestMethod.POST)
	@ResponseBody
	@Transactional
	public Callable<BindingResponse> createTask(
			@Valid final TaskCreationDto taskCreationDto,
			final BindingResult bindingResult) throws Throwable {
		return () -> {
			this.taskValidator.validate(taskCreationDto, bindingResult, null,
					false);
			if (bindingResult.hasErrors()) {
				throw new BindException(bindingResult);
			}
			Task task = new Task(taskCreationDto.getName(), "", "",
					Date.from(Instant.now()), taskCreationDto.isPublished());
			final Path localDescriptionFile = this.storageService
					.createTaskDescriptionFileStorage(task);
			final Path judgeDir = this.storageService
					.createTaskJudgeDirectory(task);

			task = this.taskService.saveTask(task);

			final Lock lock = task.writeLock();
			lock.lock();

			try {
				this.uploadDescription(task, taskCreationDto
						.getDescriptionFile().getInputStream());
				this.uploadJudgeFile(task, taskCreationDto);

				task = this.taskService.saveTask(task);
			} catch (final ArchiveException e) {
				bindingResult.rejectValue("judgeFile", "",
						"task.add.form.errors.judgeArchive.invalid");
				throw new BindException(bindingResult);
			} catch (final Exception e) {
				try {
					throw new GeneralNestedRuntimeException("", e);
				} finally {
					FileAccess.deleteDirectoryByWalking(judgeDir);
					FileAccess.deleteDirectoryByWalking(localDescriptionFile);
				}
			} finally {
				lock.unlock();
			}

			return BindingResponse.OK;
		};
	}

}
