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
package org.ng200.openolympus.validation;

import org.ng200.openolympus.dto.TaskDto;
import org.ng200.openolympus.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class TaskDtoValidator {

	@Autowired
	private TaskRepository taskRepository;

	public void validate(final TaskDto task, final Errors errors,
			final String prevName) {
		if (errors.hasErrors()) {
			return;
		}
		if (!task.getName().equals(prevName)
				&& this.taskRepository.findByName(task.getName()) != null) {
			errors.rejectValue("name", "", "task.add.form.errors.name.exists");
		}
		if (task.getDescriptionFile().getSize() == 0) {
			errors.rejectValue("descriptionFile", "",
					"task.add.form.errors.descriptionFile.empty");
		}
		if (task.getTaskZip().getSize() == 0) {
			errors.rejectValue("taskZip", "",
					"task.add.form.errors.taskZip.empty");
		}
	}

}
