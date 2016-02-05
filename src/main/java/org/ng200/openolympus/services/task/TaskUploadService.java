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
package org.ng200.openolympus.services.task;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.OffsetDateTime;
import java.util.concurrent.locks.Lock;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRefNameException;
import org.eclipse.jgit.api.errors.RefAlreadyExistsException;
import org.eclipse.jgit.api.errors.RefNotFoundException;
import org.ng200.openolympus.FileAccess;
import org.ng200.openolympus.dto.TaskCreationDto;
import org.ng200.openolympus.dto.TaskModificationDto;
import org.ng200.openolympus.dto.UploadableTask;
import org.ng200.openolympus.exceptions.GeneralNestedRuntimeException;
import org.ng200.openolympus.jooq.tables.pojos.Task;
import org.ng200.openolympus.jooq.tables.pojos.User;
import org.ng200.openolympus.services.StorageService;
import org.ng200.openolympus.services.TestingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;

@Service
public class TaskUploadService {
	@Autowired
	private TaskCRUDService taskCRUDService;

	@Autowired
	private TaskACLService taskACLService;

	@Autowired
	private StorageService storageService;

	@Autowired
	private TestingService testingService;

	private void extractZipFile(final InputStream zipFile,
			final Path destination) throws Exception {
		try (ArchiveInputStream input = new ArchiveStreamFactory()
				.createArchiveInputStream(new BufferedInputStream(zipFile))) {
			ArchiveEntry entry;
			while ((entry = input.getNextEntry()) != null) {
				final Path dest = destination.resolve(entry.getName());
				if (entry.isDirectory()) {
					FileAccess.createDirectories(dest);
				} else {
					FileAccess.createDirectories(dest.getParent());
					FileAccess.createFile(dest);
					Files.copy(input, dest,
							StandardCopyOption.REPLACE_EXISTING);
				}
			}
		}
	}

	@Transactional
	public void patchTask(final Task task,
			final TaskModificationDto taskModificationDto)
					throws IOException, Exception {
		task.setName(taskModificationDto.getName());

		if (taskModificationDto.getJudgeFile() != null) {
			this.uploadJudgeFile(task, taskModificationDto);
		}

		this.taskCRUDService.updateTask(task);
		this.testingService.reloadTasks();
	}

	private void uploadJudgeFile(final Task task,
			final UploadableTask taskDto)
					throws IOException, Exception {
		final Path judgeFile = this.storageService.getTaskJudgeFile(task);
		if (FileAccess.exists(judgeFile)) {
			FileAccess.deleteDirectoryByWalking(judgeFile);
		}
		FileAccess.createDirectories(judgeFile);

		this.extractZipFile(taskDto.getJudgeFile().getInputStream(), judgeFile);
	}

	@Transactional
	public Task uploadTask(final TaskCreationDto taskCreationDto,
			final BindingResult bindingResult, User owner)
					throws IOException, BindException,
					RefAlreadyExistsException,
					RefNotFoundException, InvalidRefNameException,
					GitAPIException {
		Task task = new Task().setName(taskCreationDto.getName())
				.setCreatedDate(OffsetDateTime.now());

		final Path localDescriptionFile = this.storageService
				.createTaskDescriptionDirectory(task);
		final Path judgeDir = this.storageService
				.createTaskJudgeDirectory(task);

		task = this.taskCRUDService.insertTask(task);
		this.taskACLService.createDefaultTaskACL(task, owner);

		final Lock lock = task.writeLock();
		lock.lock();

		try {
			this.uploadJudgeFile(task, taskCreationDto);

			task = this.taskCRUDService.updateTask(task);
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
		return task;
	}

}