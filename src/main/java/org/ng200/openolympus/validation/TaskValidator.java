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
package org.ng200.openolympus.validation;

import java.io.IOException;

import org.ng200.openolympus.dto.UploadableTask;
import org.ng200.openolympus.services.task.TaskCRUDService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class TaskValidator {

	public static final int DESCRIPTION_SIZE_LIMIT = 5 * 1024 * 1024;
	@Autowired
	private TaskCRUDService taskCRUDService;

	public void validate(final UploadableTask task, final Errors errors,
			final String prevName, final boolean ignoreEmptyFiles)
					throws IOException {
		if (errors.hasErrors()) {
			return;
		}
		if (!task.getName().equals(prevName)
				&& this.taskCRUDService.getTaskByName(task.getName()) != null) {
			errors.rejectValue("name", "", "task.add.form.errors.name.exists");
		}
		if (!ignoreEmptyFiles && (task.getJudgeFile() == null
				|| task.getJudgeFile().getSize() == 0)) {
			errors.rejectValue("judgeFile", "",
					"task.add.form.errors.judgeFile.empty");
		}

	}
}
