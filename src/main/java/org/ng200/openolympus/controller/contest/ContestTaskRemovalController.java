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

import org.ng200.openolympus.Assertions;
import org.ng200.openolympus.SecurityExpressionConstants;
import org.ng200.openolympus.model.Contest;
import org.ng200.openolympus.model.Task;
import org.ng200.openolympus.services.ContestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

//TODO: Port task removal to new API

@Controller
@RequestMapping(value = "/contest/{contest}/removeTask/{task}")
public class ContestTaskRemovalController {

	@Autowired
	private ContestService contestService;

	@PreAuthorize(SecurityExpressionConstants.IS_ADMIN)
	@RequestMapping(method = RequestMethod.POST)
	public String removeTask(
			@PathVariable(value = "contest") final Contest contest,
			@PathVariable(value = "task") final Task task, final Model model) {
		Assertions.resourceExists(contest);
		Assertions.resourceExists(task);

		if (contest.getTasks().contains(task)) {
			contest.getTasks().remove(task);
			this.contestService.saveContest(contest);
		}

		return null;
	}

	@PreAuthorize(SecurityExpressionConstants.IS_ADMIN)
	@RequestMapping(method = RequestMethod.GET)
	public String removeTaskPrompt(
			@PathVariable(value = "contest") final Contest contest,
			@PathVariable(value = "task") final Task task, final Model model) {
		Assertions.resourceExists(contest);
		Assertions.resourceExists(task);

		model.addAttribute("contest", contest);
		model.addAttribute("task", task);
		return "contest/removeTask";
	}
}
