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
package org.ng200.openolympus.controller.contest;

import javax.validation.Valid;

import org.ng200.openolympus.Assertions;

import org.ng200.openolympus.controller.BindingResponse;
import org.ng200.openolympus.controller.BindingResponse.Status;
import org.ng200.openolympus.dto.ContestTaskAdditionDto;
import org.ng200.openolympus.jooq.tables.pojos.Contest;
import org.ng200.openolympus.services.ContestService;
import org.ng200.openolympus.services.TaskService;
import org.ng200.openolympus.validation.ContestTaskAdditionDtoValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.ImmutableMap;

@RestController
public class ContestTaskAdditionController {

	@Autowired
	private TaskService taskService;
	@Autowired
	private ContestTaskAdditionDtoValidator contestTaskAdditionDtoValidator;

	@Autowired
	private ContestService contestService;
	@CacheEvict(value = "contests", key = "#contest.id")
	@RequestMapping(method = RequestMethod.POST, value = "/api/contest/{contest}/addTask")
	public BindingResponse addTask(final Model model,
			@PathVariable(value = "contest") final Contest contest,
			@Valid final ContestTaskAdditionDto contestTaskAdditionDto,
			final BindingResult bindingResult) throws BindException {
		Assertions.resourceExists(contest);

		if (bindingResult.hasErrors()) {
			throw new BindException(bindingResult);
		}
		this.contestTaskAdditionDtoValidator.validate(contest,
				contestTaskAdditionDto, bindingResult);
		if (bindingResult.hasErrors()) {
			throw new BindException(bindingResult);
		}
		contestService
				.addContestTask(contest, taskService
						.getTaskByName(contestTaskAdditionDto.getTaskName()));
		return new BindingResponse(Status.OK, null, ImmutableMap
				.<String, Object> builder()
				.put("taskName", contestTaskAdditionDto.getTaskName()).build());
	}

}
