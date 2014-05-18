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
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.compress.archivers.ArchiveException;
import org.ng200.openolympus.Assertions;
import org.ng200.openolympus.FileAccess;
import org.ng200.openolympus.StorageSpace;
import org.ng200.openolympus.dto.TaskModificationDto;
import org.ng200.openolympus.model.Task;
import org.ng200.openolympus.repositories.TaskRepository;
import org.ng200.openolympus.services.TestingService;
import org.ng200.openolympus.validation.TaskDtoValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/task/{task}/edit")
public class TaskModificationController extends TaskUploader {

	@Autowired
	private TaskDtoValidator taskDtoValidator;

	@Autowired
	private TaskRepository taskRepository;

	@Autowired
	private TestingService testingService;

	@RequestMapping(method = RequestMethod.POST)
	public String editTask(final Model model, final HttpServletRequest request,
			@PathVariable("task") final Task task,
			@Valid final TaskModificationDto taskModificationDto,
			final BindingResult bindingResult) throws IllegalStateException,
			IOException, ArchiveException {
		Assertions.resourceExists(task);

		if (bindingResult.hasErrors()) {
			model.addAttribute("submitURL", "/task/" + task.getId() + "/edit");
			model.addAttribute("mode", "edit");
			model.addAttribute("taskId", task.getId());
			return "tasks/edit";
		}
		if (taskModificationDto.getDescriptionFile() != null
				&& taskModificationDto.getDescriptionFile().getSize() == 0)
			taskModificationDto.setDescriptionFile(null);
		if (taskModificationDto.getDescriptionFile() == null) {
			final String descriptionPath = MessageFormat.format(
					TaskUploader.TASK_DESCRIPTION_PATH_TEMPLATE,
					StorageSpace.STORAGE_PREFIX, task.getTaskLocation());
			final File descriptionFile = new File(descriptionPath);
			FileAccess.writeString(
					Optional.of(taskModificationDto.getDescriptionText())
							.orElse(""), descriptionFile);
		}
		task.setName(taskModificationDto.getName());
		this.uploadTaskData(task, taskModificationDto);
		this.taskRepository.save(task);
		this.testingService.reloadTasks();
		return "redirect:/task/" + task.getId();
	}

	@InitBinder
	protected void initBinder(final HttpServletRequest request,
			final ServletRequestDataBinder binder) throws Exception {
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
	}

	@RequestMapping(method = RequestMethod.GET)
	public String showTaskEditingForm(final Model model,
			@PathVariable("task") final Task task,
			final TaskModificationDto taskModificationDto) throws IOException {

		model.addAttribute("submitURL", "/task/" + task.getId() + "/edit");
		model.addAttribute("mode", "edit");
		model.addAttribute("taskId", task.getId());
		taskModificationDto.setName(task.getName());
		taskModificationDto.setDescriptionText("");
		return "tasks/edit";
	}
}
