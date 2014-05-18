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
package org.ng200.openolympus.controller.contest;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.ng200.openolympus.Assertions;
import org.ng200.openolympus.dto.ContestTaskAdditionDto;
import org.ng200.openolympus.model.Contest;
import org.ng200.openolympus.repositories.ContestRepository;
import org.ng200.openolympus.repositories.TaskRepository;
import org.ng200.openolympus.validation.ContestTaskAdditionDtoValidator;
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
@RequestMapping(value = "/contest/{contest}/addTask")
public class ContestTaskAdditionController {

	@Autowired
	private ContestRepository contestRepository;

	@Autowired
	private ContestTaskAdditionDtoValidator contestTaskAdditionDtoValidator;

	@Autowired
	private TaskRepository taskRepository;

	@RequestMapping(method = RequestMethod.POST)
	public String addTask(final Model model,
			@PathVariable(value = "contest") final Contest contest,
			@Valid final ContestTaskAdditionDto contestTaskAdditionDto,
			final BindingResult bindingResult) {
		Assertions.resourceExists(contest);

		this.contestTaskAdditionDtoValidator.validate(contest,
				contestTaskAdditionDto, bindingResult);
		if (bindingResult.hasErrors()) {
			return "contest/addTask";
		}
		contest.getTasks().add(
				this.taskRepository.findByName(contestTaskAdditionDto
						.getTaskName()));
		this.contestRepository.save(contest);
		model.addAttribute("message", "contest.addTask.success");
		return "redirect:/contest/" + Long.toString(contest.getId());
	}

	@InitBinder
	protected void initBinder(final HttpServletRequest request,
			final ServletRequestDataBinder binder) throws Exception {
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
	}

	@RequestMapping(method = RequestMethod.GET)
	public String showAddTaskPage(
			@PathVariable(value = "contest") final Contest contest,
			final ContestTaskAdditionDto contestTaskAdditionDto) {
		Assertions.resourceExists(contest);
		return "contest/addTask";
	}
}
