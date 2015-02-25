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

import java.io.File;
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
import org.ng200.openolympus.model.Solution;
import org.ng200.openolympus.model.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class StorageService implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -9146834121227344818L;

	@Value("${storagePath}")
	private String storagePath;

	@Autowired
	private TaskDescriptionProvider taskDescriptionProvider;

	@PreAuthorize(SecurityExpressionConstants.IS_ADMIN)
	public File createSolutionDirectory() throws IOException {
		final UUID uuid = UUID.randomUUID();
		final Path dir = FileSystems.getDefault()
				.getPath(this.storagePath, "solutions",
						uuid.toString() + "_" + System.currentTimeMillis());
		FileAccess.createDirectories(dir);
		return dir.toFile();
	}

	@PreAuthorize(SecurityExpressionConstants.IS_ADMIN)
	public File createTaskDescriptionFileStorage(Task task) throws IOException {
		final UUID uuid = UUID.randomUUID();
		final String idString = System.currentTimeMillis() + "_"
				+ uuid.toString();
		final Path file = FileSystems.getDefault().getPath(this.storagePath,
				"tasks", "descriptions", idString);
		FileAccess.createDirectories(file);
		FileAccess.createFile(file.resolve("source"));
		task.setDescriptionFile(idString);
		return file.toFile();
	}

	@PreAuthorize(SecurityExpressionConstants.IS_ADMIN)
	public File createTaskJudgeDirectory(Task task) throws IOException {
		final UUID uuid = UUID.randomUUID();
		final Path dir = FileSystems.getDefault().getPath(this.storagePath,
				"tasks", "judges", uuid.toString());
		FileAccess.createDirectories(dir);
		task.setTaskLocation(uuid.toString());
		return dir.toFile();
	}

	public File getSolutionFile(final Solution solution) {
		return FileSystems.getDefault()
				.getPath(this.storagePath, "solutions", solution.getFile())
				.toFile();
	}

	public String getStoragePath() {
		return this.storagePath;
	}

	public String getTaskDescription(final Task task) throws IOException {
		final File compiled = FileSystems
				.getDefault()
				.getPath(this.storagePath, "tasks", "descriptions",
						task.getDescriptionFile(), "compiled").toFile();

		if (!compiled.exists()) {
			return null;
		}

		return new String(FileAccess.readAllBytes(compiled),
				Charset.forName("UTF8"));
	}

	public String getTaskDescriptionSourcecode(Task task) throws IOException {
		final File source = FileSystems
				.getDefault()
				.getPath(this.storagePath, "tasks", "descriptions",
						task.getDescriptionFile(), "source").toFile();
		return new String(FileAccess.readAllBytes(source),
				Charset.forName("UTF8"));
	}

	public File getTaskJudgeFile(final Task task) {
		return FileSystems
				.getDefault()
				.getPath(this.storagePath, "tasks", "judges",
						task.getTaskLocation()).toFile();
	}

	public String sanitizeName(final String fileName) {
		return fileName.replaceAll("[^a-zA-Z0-9-\\._]", "");
	}

	public void setSolutionFile(final Solution solution, final File file) {
		solution.setFile(FileSystems.getDefault()
				.getPath(this.storagePath, "solutions")
				.relativize(file.toPath()).toString());
	}

	@PreAuthorize(SecurityExpressionConstants.IS_ADMIN)
	public void writeTaskDescription(Task task, InputStream inputStream)
			throws ExecuteException, IOException {
		final File source = FileSystems
				.getDefault()
				.getPath(this.storagePath, "tasks", "descriptions",
						task.getDescriptionFile(), "source").toFile();

		try (OutputStream outputStream = Files.newOutputStream(source.toPath())) {
			IOUtils.copy(inputStream, outputStream);
		}

		final File compiled = FileSystems
				.getDefault()
				.getPath(this.storagePath, "tasks", "descriptions",
						task.getDescriptionFile(), "compiled").toFile();
		this.taskDescriptionProvider.transform(source, compiled);
	}

}
