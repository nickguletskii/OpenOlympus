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
import java.security.Principal;
import java.text.MessageFormat;
import java.time.Instant;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.compress.archivers.ArchiveException;
import org.ng200.openolympus.Assertions;
import org.ng200.openolympus.StorageSpace;
import org.ng200.openolympus.controller.ContestRestrictedController;
import org.ng200.openolympus.dto.SolutionDto;
import org.ng200.openolympus.model.Solution;
import org.ng200.openolympus.model.Task;
import org.ng200.openolympus.model.User;
import org.ng200.openolympus.repositories.SolutionRepository;
import org.ng200.openolympus.repositories.TaskRepository;
import org.ng200.openolympus.repositories.UserRepository;
import org.ng200.openolympus.services.TestingService;
import org.ng200.openolympus.validation.SolutionDtoValidator;
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
import org.thymeleaf.spring4.view.ThymeleafViewResolver;

@Controller
@RequestMapping(value = "/task/{task}")
public class TaskViewController extends ContestRestrictedController {

	private static final String TASK_DESCRIPTION_PATH_TEMPLATE = "tasks/descriptions/{0}";
	private static final String SOLUTION_PATH_TEMPLATE = "{0}/solutions/{1}/{2}";
	@Autowired
	private TaskRepository taskRepository;
	@Autowired
	private ThymeleafViewResolver thymeleafViewResolver;

	@Autowired
	private SolutionDtoValidator solutionDtoValidator;

	@Autowired
	private SolutionRepository solutionRepository;
	@Autowired
	private TestingService testingService;
	@Autowired
	private UserRepository userRepository;

	@InitBinder
	protected void initBinder(final HttpServletRequest request,
			final ServletRequestDataBinder binder) throws Exception {
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
	}

	@RequestMapping(method = RequestMethod.GET)
	public String showTaskView(@PathVariable(value = "task") final Task task,
			final Model model, final Locale locale,
			final SolutionDto solutionDto, final Principal principal) {
		Assertions.resourceExists(task);

		this.assertSuperuserOrTaskAllowed(principal, task);

		model.addAttribute("taskDescriptionUUID", task.getDescriptionFile());
		model.addAttribute("taskDescriptionURI", MessageFormat.format(
				TaskViewController.TASK_DESCRIPTION_PATH_TEMPLATE,
				task.getDescriptionFile()));
		model.addAttribute("localisedDescriptionFragment", "taskDescription_"
				+ locale.getLanguage());
		model.addAttribute("task", task);

		return "tasks/task";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String submitSolution(final HttpServletRequest request,
			final Model model, final Locale locale,
			@PathVariable("task") final Task task, final Principal principal,
			@Valid final SolutionDto solutionDto,
			final BindingResult bindingResult) throws IllegalStateException,
			IOException, ArchiveException {
		Assertions.resourceExists(task);

		this.assertSuperuserOrTaskAllowed(principal, task);

		final User user = this.userRepository.findByUsername(principal
				.getName());

		this.solutionDtoValidator.validate(solutionDto, bindingResult);
		if (bindingResult.hasErrors()) {
			model.addAttribute("taskDescriptionUUID", task.getDescriptionFile());
			model.addAttribute("taskDescriptionURI", MessageFormat.format(
					TaskViewController.TASK_DESCRIPTION_PATH_TEMPLATE,
					task.getDescriptionFile()));
			model.addAttribute("localisedDescriptionFragment",
					"taskDescription_" + locale.getLanguage());
			model.addAttribute("task", task);
			return "tasks/task";
		}

		final UUID uuid = UUID.randomUUID();
		final String solutionPath = MessageFormat.format(
				TaskViewController.SOLUTION_PATH_TEMPLATE,
				StorageSpace.STORAGE_PREFIX,
				uuid.toString(),
				solutionDto.getTaskFile().getOriginalFilename()
				.replaceAll("[^a-zA-Z0-9-\\._]", ""));

		final File solutionFile = new File(solutionPath);
		solutionFile.getParentFile().mkdirs();

		solutionDto.getTaskFile().transferTo(solutionFile);

		Solution solution = new Solution(task, user,
				solutionFile.getAbsolutePath(), Date.from(Instant.now()));
		solution = this.solutionRepository.save(solution);
		this.testingService.testSolutionOnAllTests(solution);

		return MessageFormat.format("redirect:/solution?id={0}",
				Long.toString(solution.getId()));
	}
}
