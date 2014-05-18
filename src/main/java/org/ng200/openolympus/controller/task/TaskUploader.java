/**
 * The MIT License
 * Copyright (c) 2014 Nick Guletskii
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.MessageFormat;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.io.FileUtils;
import org.ng200.openolympus.StorageSpace;
import org.ng200.openolympus.dto.TaskDto;
import org.ng200.openolympus.dto.UploadableTask;
import org.ng200.openolympus.model.Task;

public class TaskUploader {

	public static final String TASK_CHECKING_CHAIN_PATH_TEMPLATE = "{0}/tasks/checkingchains/{1}/";
	public static final String TASK_DESCRIPTION_PATH_TEMPLATE = "{0}/tasks/descriptions/{1}.html";

	public TaskUploader() {
		super();
	}

	protected void uploadTaskData(final Task task, final UploadableTask taskDto)
			throws IOException, IllegalStateException, FileNotFoundException,
			ArchiveException {
		final String descriptionPath = MessageFormat.format(
				TaskUploader.TASK_DESCRIPTION_PATH_TEMPLATE,
				StorageSpace.STORAGE_PREFIX, task.getTaskLocation());

		if (taskDto.getDescriptionFile() != null) {
			final File descriptionFile = new File(descriptionPath);
			if (descriptionFile.exists()) {
				descriptionFile.delete();
			}
			descriptionFile.getParentFile().mkdirs();
			descriptionFile.createNewFile();
			taskDto.getDescriptionFile().transferTo(descriptionFile);
		}
		if (taskDto.getTaskZip() != null) {
			final String checkingChainPath = MessageFormat.format(
					TaskUploader.TASK_CHECKING_CHAIN_PATH_TEMPLATE,
					StorageSpace.STORAGE_PREFIX, task.getTaskLocation());
			final File checkingChainFile = new File(checkingChainPath);
			if (checkingChainFile.exists()) {
				FileUtils.deleteDirectory(checkingChainFile);
			}
			checkingChainFile.mkdirs();

			try (ArchiveInputStream in = new ArchiveStreamFactory()
					.createArchiveInputStream("zip", taskDto.getTaskZip()
							.getInputStream())) {
				ZipArchiveEntry entry;
				while ((entry = (ZipArchiveEntry) in.getNextEntry()) != null) {
					final File file = new File(checkingChainFile,
							entry.getName());
					if (entry.isDirectory()) {
						file.mkdirs();
					} else {
						try (OutputStream out = new FileOutputStream(file)) {
							IOUtils.copy(in, out);
						}
					}
				}
			}
		}
	}

}