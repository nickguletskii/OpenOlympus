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

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.compress.archivers.ArchiveException;
import org.ng200.openolympus.Assertions;
import org.ng200.openolympus.dto.TaskDto;
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
			@Valid final TaskDto taskDto, final BindingResult bindingResult)
					throws IllegalStateException, IOException, ArchiveException {
		Assertions.resourceExists(task);

		this.taskDtoValidator.validate(taskDto, bindingResult, task.getName());

		if (bindingResult.hasErrors()) {
			model.addAttribute("submitURL", "/task/" + task.getId() + "/edit");
			model.addAttribute("mode", "edit");
			return "tasks/add";
		}
		task.setName(taskDto.getName());
		this.uploadTaskData(task, taskDto);
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
			@PathVariable("task") final Task task, final TaskDto taskDto) {
		model.addAttribute("submitURL", "/task/" + task.getId() + "/edit");
		model.addAttribute("mode", "edit");
		taskDto.setName(task.getName());
		return "tasks/add";
	}
}
