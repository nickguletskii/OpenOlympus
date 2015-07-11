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
import java.nio.file.Path;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.concurrent.locks.Lock;

import org.apache.commons.compress.archivers.ArchiveException;
import org.ng200.openolympus.FileAccess;
import org.ng200.openolympus.SecurityExpressionConstants;
import org.ng200.openolympus.controller.BindingResponse;
import org.ng200.openolympus.controller.BindingResponse.Status;
import org.ng200.openolympus.dto.TaskCreationDto;
import org.ng200.openolympus.exceptions.GeneralNestedRuntimeException;
import org.ng200.openolympus.jooq.tables.pojos.Task;
import org.ng200.openolympus.services.StorageService;
import org.ng200.openolympus.services.TaskService;
import org.ng200.openolympus.services.UserService;
import org.ng200.openolympus.validation.TaskValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.ImmutableMap;

@RestController
public class TaskCreationController extends
		TaskFilesystemManipulatingController {

	private static final Logger logger = LoggerFactory
			.getLogger(TaskCreationController.class);
	@Autowired
	private TaskValidator taskValidator;

	@Autowired
	private TaskService taskService;

	@Autowired
	private StorageService storageService;

	@Autowired
	private UserService userService;

	@PreAuthorize(SecurityExpressionConstants.IS_ADMIN)
	@RequestMapping(value = "/api/task/create", method = RequestMethod.POST)
	@Transactional
	private BindingResponse createTask(final TaskCreationDto taskCreationDto,
			final BindingResult bindingResult, Principal principal)
					throws IOException, BindException {
		this.taskValidator.validate(taskCreationDto, bindingResult, null,
				false);

		if (bindingResult.hasErrors()) {
			throw new BindException(bindingResult);
		}
		Long ownerId = userService.getUserByUsername(principal.getName())
				.getId();

		logger.info("Owner id: {}", ownerId);

		Task task = new Task().setName(taskCreationDto.getName())
				.setCreatedDate(LocalDateTime.now())
				.setOwnerId(ownerId);

		final Path localDescriptionFile = this.storageService
				.createTaskDescriptionFileStorage(task);
		final Path judgeDir = this.storageService
				.createTaskJudgeDirectory(task);

		task = this.taskService.insertTask(task);

		final Lock lock = task.writeLock();
		lock.lock();

		try {
			this.uploadDescription(task, taskCreationDto
					.getDescriptionFile().getInputStream());
			this.uploadJudgeFile(task, taskCreationDto);

			task = this.taskService.updateTask(task);
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

		return new BindingResponse(Status.OK,
				null, ImmutableMap.<String, Object> builder()
						.put("taskId", task.getId()).build());
	}

}
