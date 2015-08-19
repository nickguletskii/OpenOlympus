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

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.exec.ExecuteException;
import org.ng200.openolympus.FileAccess;

import org.ng200.openolympus.dto.UploadableTask;
import org.ng200.openolympus.jooq.tables.pojos.Task;
import org.ng200.openolympus.services.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;

public class TaskFilesystemManipulatingController {

	@Autowired
	private StorageService storageService;
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
	protected void uploadDescription(final Task task, InputStream inputStream)
			throws ExecuteException, IOException {
		// TODO: remove this?
		// this.storageService.writeTaskDescription(task, inputStream);
	}
	protected void uploadJudgeFile(final Task task,
			final UploadableTask taskDto)
					throws IOException, Exception {
		final Path judgeFile = this.storageService.getTaskJudgeFile(task);
		if (FileAccess.exists(judgeFile)) {
			FileAccess.deleteDirectoryByWalking(judgeFile);
		}
		FileAccess.createDirectories(judgeFile);

		this.extractZipFile(taskDto.getJudgeFile().getInputStream(), judgeFile);
	}
}