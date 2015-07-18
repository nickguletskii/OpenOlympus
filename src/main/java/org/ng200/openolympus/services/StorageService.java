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
package org.ng200.openolympus.services;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import org.apache.commons.exec.ExecuteException;
import org.apache.commons.io.IOUtils;
import org.ng200.openolympus.FileAccess;
import org.ng200.openolympus.SecurityExpressionConstants;
import org.ng200.openolympus.config.StorageConfiguration;
import org.ng200.openolympus.jooq.tables.pojos.Solution;
import org.ng200.openolympus.jooq.tables.pojos.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class StorageService implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -9146834121227344818L;

	@Autowired
	private StorageConfiguration storageConfig;

	@Autowired
	private transient TaskDescriptionProvider taskDescriptionProvider;

	public Path createSolutionDirectory() throws IOException {
		final UUID uuid = UUID.randomUUID();
		final Path dir = FileSystems.getDefault()
				.getPath(this.storageConfig.getStoragePath(), "solutions",
						uuid.toString() + "_" + System.currentTimeMillis());
		FileAccess.createDirectories(dir);
		return dir;
	}

	@PreAuthorize(SecurityExpressionConstants.IS_SUPERUSER)
	public Path createTaskDescriptionFileStorage(Task task) throws IOException {
		final UUID uuid = UUID.randomUUID();
		final String idString = System.currentTimeMillis() + "_"
				+ uuid.toString();
		final Path file = FileSystems.getDefault().getPath(
				this.storageConfig.getStoragePath(),
				"tasks", "descriptions", idString);
		FileAccess.createDirectories(file);
		FileAccess.createFile(file.resolve("source"));
		task.setDescriptionFile(idString);
		return file;
	}

	@PreAuthorize(SecurityExpressionConstants.IS_SUPERUSER)
	public Path createTaskJudgeDirectory(Task task) throws IOException {
		final UUID uuid = UUID.randomUUID();
		final Path dir = FileSystems.getDefault().getPath(
				this.storageConfig.getStoragePath(),
				"tasks", "judges", uuid.toString());
		FileAccess.createDirectories(dir);
		task.setTaskLocation(uuid.toString());
		return dir;
	}

	public Path getSolutionFile(final Solution solution) {
		return FileSystems.getDefault().getPath(
				this.storageConfig.getStoragePath(), "solutions",
				solution.getFile());
	}

	public String getStoragePath() {
		return this.storageConfig.getStoragePath();
	}

	public String getTaskDescription(final Task task) throws IOException {
		final Path compiled = FileSystems.getDefault().getPath(
				this.storageConfig.getStoragePath(), "tasks", "descriptions",
				task.getDescriptionFile(), "compiled");

		if (!FileAccess.exists(compiled)) {
			return null;
		}

		return new String(FileAccess.readAllBytes(compiled),
				Charset.forName("UTF8"));
	}

	public String getTaskDescriptionSourcecode(Task task) throws IOException {
		final Path source = FileSystems.getDefault().getPath(
				this.storageConfig.getStoragePath(),
				"tasks", "descriptions", task.getDescriptionFile(), "source");
		return new String(FileAccess.readAllBytes(source),
				Charset.forName("UTF8"));
	}

	public Path getTaskJudgeFile(final Task task) {
		return FileSystems.getDefault().getPath(
				this.storageConfig.getStoragePath(), "tasks",
				"judges", task.getTaskLocation());
	}

	public String sanitizeName(final String fileName) {
		return fileName.replaceAll("[^a-zA-Z0-9-\\._]", "");
	}

	public void setSolutionFile(final Solution solution, final Path file) {
		solution.setFile(FileSystems.getDefault()
				.getPath(this.storageConfig.getStoragePath(), "solutions")
				.relativize(file)
				.toString());
	}

	@PreAuthorize(SecurityExpressionConstants.IS_SUPERUSER)
	public void writeTaskDescription(Task task, InputStream inputStream)
			throws ExecuteException, IOException {
		final Path source = FileSystems.getDefault().getPath(
				this.storageConfig.getStoragePath(),
				"tasks", "descriptions", task.getDescriptionFile(), "source");

		try (OutputStream outputStream = Files.newOutputStream(source)) {
			IOUtils.copy(inputStream, outputStream);
		}

		final Path compiled = FileSystems.getDefault().getPath(
				this.storageConfig.getStoragePath(), "tasks", "descriptions",
				task.getDescriptionFile(), "compiled");
		this.taskDescriptionProvider.transform(source, compiled);
	}

}
