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
package org.ng200.openolympus.dto;

import javax.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

public class TaskDto {

	@NotNull(message = "task.add.form.errors.empty")
	private String name;

	@NotNull(message = "task.add.form.errors.empty")
	private MultipartFile descriptionFile;

	@NotNull(message = "task.add.form.errors.empty")
	private MultipartFile taskZip;

	public MultipartFile getDescriptionFile() {
		return this.descriptionFile;
	}

	public String getName() {
		return this.name;
	}

	public MultipartFile getTaskZip() {
		return this.taskZip;
	}

	public void setCheckerJar(final MultipartFile taskZip) {
		this.taskZip = taskZip;
	}

	public void setDescriptionFile(final MultipartFile descriptionFile) {
		this.descriptionFile = descriptionFile;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public void setTaskZip(final MultipartFile taskZip) {
		this.taskZip = taskZip;
	}
}
