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
package org.ng200.openolympus.controller.task;

import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.time.Instant;
import java.util.Date;
import java.util.Locale;

import javax.validation.Valid;

import org.ng200.openolympus.Assertions;
import org.ng200.openolympus.controller.BindingResponse;
import org.ng200.openolympus.controller.BindingResponse.Status;
import org.ng200.openolympus.dto.SolutionSubmissionDto;
import org.ng200.openolympus.model.Solution;
import org.ng200.openolympus.model.Task;
import org.ng200.openolympus.model.User;
import org.ng200.openolympus.services.SolutionService;
import org.ng200.openolympus.services.StorageService;
import org.ng200.openolympus.services.TestingService;
import org.ng200.openolympus.services.UserService;
import org.ng200.openolympus.validation.SolutionDtoValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;

import com.google.common.collect.ImmutableMap;

@RestController
public class TaskViewController {

	public static class TaskDescriptionView {
		private String name;
		private String description;

		public TaskDescriptionView(String name, String description) {
			super();
			this.name = name;
			this.description = description;
		}

		public String getDescription() {
			return this.description;
		}

		public String getName() {
			return this.name;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public void setName(String name) {
			this.name = name;
		}
	}

	@Autowired
	private ThymeleafViewResolver thymeleafViewResolver;

	@Autowired
	private UserService userService;

	@Autowired
	private SolutionDtoValidator solutionDtoValidator;

	@Autowired
	private TestingService testingService;

	@Autowired
	private StorageService storageService;

	@Autowired
	private SolutionService solutionService;

	@PreAuthorize("hasAuthority('SUPERUSER') or ((@oolsec.taskInContest(#task) or (@oolsec.noLockdown() and @oolsec.noContest() and #task.published)) and hasAuthority('USER'))")
	@RequestMapping(value = "/api/task/{task}/name", method = RequestMethod.GET)
	public String getTaskName(@PathVariable(value = "task") final Task task,
			final Locale locale) throws IOException {
		Assertions.resourceExists(task);
		return task.getName();
	}

	@PreAuthorize("hasAuthority('SUPERUSER') or ((@oolsec.taskInContest(#task) or (@oolsec.noLockdown() and @oolsec.noContest() and #task.published)) and hasAuthority('USER'))")
	@RequestMapping(value = "/api/task/{task}", method = RequestMethod.GET)
	public TaskDescriptionView showTaskView(
			@PathVariable(value = "task") final Task task, final Locale locale)
			throws IOException {
		Assertions.resourceExists(task);
		return new TaskDescriptionView(task.getName(),
				this.storageService.getTaskDescription(task));
	}

	@PreAuthorize("hasAuthority('SUPERUSER') or ((@oolsec.taskInContest(#task) or (@oolsec.noLockdown() and @oolsec.noContest() and #task.published)) and hasAuthority('USER'))")
	@RequestMapping(value = "/api/task/{task}/submitSolution", method = RequestMethod.POST)
	public BindingResponse submitSolution(
			@PathVariable("task") final Task task, final Principal principal,
			@Valid final SolutionSubmissionDto solutionDto,
			final BindingResult bindingResult) throws BindException,
			IOException {
		if (bindingResult.hasErrors()) {
			throw new BindException(bindingResult);
		}

		Assertions.resourceExists(task);

		final User user = this.userService.getUserByUsername(principal
				.getName());

		this.solutionDtoValidator.validate(solutionDto, bindingResult);

		if (bindingResult.hasErrors()) {
			throw new BindException(bindingResult);
		}

		final File solutionFile = new File(
				this.storageService.createSolutionDirectory(),
				this.storageService.sanitizeName(solutionDto.getSolutionFile()
						.getOriginalFilename()));

		solutionDto.getSolutionFile().transferTo(solutionFile);

		Solution solution = new Solution(task, user, "", Date.from(Instant
				.now()));

		this.storageService.setSolutionFile(solution, solutionFile);

		solution = this.solutionService.saveSolution(solution);
		this.testingService.testSolutionOnAllTests(solution);

		final long id = solution.getId();

		return new BindingResponse(Status.OK, null,
				new ImmutableMap.Builder<String, Object>().put("id", id)
						.build());
	}
}
