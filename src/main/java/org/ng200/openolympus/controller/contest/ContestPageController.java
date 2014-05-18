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

import java.security.Principal;
import java.util.ArrayList;

import org.ng200.openolympus.Assertions;
import org.ng200.openolympus.controller.ContestRestrictedController;
import org.ng200.openolympus.model.Contest;
import org.ng200.openolympus.model.Task;
import org.ng200.openolympus.repositories.ContestRepository;
import org.ng200.openolympus.services.ContestSecurityService;
import org.ng200.openolympus.services.ContestService;
import org.ng200.openolympus.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ContestPageController extends ContestRestrictedController {

	@Autowired
	private ContestSecurityService contestSecurityService;

	@Autowired
	private ContestRepository contestRepository;

	@Autowired
	private TaskService taskService;

	@Autowired
	private ContestService contestService;

	@RequestMapping(value = "/contest/{contest}/rejudgeTask/{task}", method = RequestMethod.POST)
	public String rejudgeTask(final Model model,
			@PathVariable(value = "contest") final Contest contest,
			@PathVariable(value = "task") final Task task) {
		Assertions.resourceExists(contest);
		Assertions.resourceExists(task);

		this.taskService.rejudgeTask(task);

		model.addAttribute("message", "contest.taskList.taskRejudged");
		return "redirect:/contest/" + contest.getId();
	}

	@RequestMapping(value = "/contest/{contest}/rejudgeTask/{task}", method = RequestMethod.GET)
	public String rejudgeTaskPrompt(final Model model,
			@PathVariable(value = "contest") final Contest contest,
			@PathVariable(value = "task") final Task task) {
		Assertions.resourceExists(contest);
		Assertions.resourceExists(task);

		model.addAttribute("contest", contest);
		model.addAttribute("task", task);
		return "contest/rejudgeTask";
	}

	@RequestMapping(value = "/contest/{contest}/removeTask/{task}", method = RequestMethod.POST)
	public String removeTask(final Model model,
			@PathVariable(value = "contest") final Contest contest,
			@PathVariable(value = "task") final Task task) {
		Assertions.resourceExists(contest);
		Assertions.resourceExists(task);

		removal: {
			if (!contest.getTasks().contains(task)) {
				break removal;
			}
			contest.getTasks().remove(task);
			this.contestRepository.save(contest);
		}
		model.addAttribute("message", "contest.taskList.taskRemoved");
		return "redirect:/contest/" + contest.getId();
	}

	@RequestMapping(value = "/contest/{contest}/removeTask/{task}", method = RequestMethod.GET)
	public String removeTaskPrompt(final Model model,
			@PathVariable(value = "contest") final Contest contest,
			@PathVariable(value = "task") final Task task) {
		Assertions.resourceExists(contest);
		Assertions.resourceExists(task);

		model.addAttribute("contest", contest);
		model.addAttribute("task", task);
		return "contest/removeTask";
	}

	@RequestMapping(value = "/contest/{contest}")
	public String showContestHub(
			final Model model,
			@PathVariable(value = "contest") final Contest contest,
			@RequestParam(value = "message", required = false) final String message,
			final Principal principal) {
		Assertions.resourceExists(contest);

		final boolean isSuperUser = this.contestSecurityService
				.isSuperuser(principal);
		final boolean shouldShowTasks = (this.contestService
				.hasContestStarted(contest) || isSuperUser);

		if (this.contestService.getRunningContest() != null) {
			this.contestSecurityService
			.assertUserIsInContestOrSuperuser(principal);
		}

		model.addAttribute("forceShow", isSuperUser);

		model.addAttribute("tasks", shouldShowTasks ? contest.getTasks()
				: new ArrayList<Contest>());

		model.addAttribute("message", message);
		return "contest/contest";
	}
}
