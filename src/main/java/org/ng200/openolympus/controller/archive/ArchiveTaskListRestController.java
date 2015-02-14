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
package org.ng200.openolympus.controller.archive;

import static org.ng200.openolympus.SecurityExpressionConstants.AND;
import static org.ng200.openolympus.SecurityExpressionConstants.IS_ADMIN;
import static org.ng200.openolympus.SecurityExpressionConstants.IS_USER;
import static org.ng200.openolympus.SecurityExpressionConstants.NO_CONTEST_CURRENTLY;
import static org.ng200.openolympus.SecurityExpressionConstants.OR;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import org.ng200.openolympus.model.Task;
import org.ng200.openolympus.model.User;
import org.ng200.openolympus.model.views.UnprivilegedView;
import org.ng200.openolympus.services.TaskService;
import org.ng200.openolympus.services.UserService;
import org.ng200.openolympus.util.Beans;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

@RestController
public class ArchiveTaskListRestController {
	public static class TaskDto extends Task {
		/**
		 *
		 */
		private static final long serialVersionUID = 1552704472938068131L;
		private BigDecimal score;
		private BigDecimal maxScore;

		public TaskDto(Task task, BigDecimal score, BigDecimal maxScore) {
			Beans.copy(task, this);
			this.score = score;
			this.maxScore = maxScore;
		}

		@JsonView(UnprivilegedView.class)
		public BigDecimal getMaxScore() {
			return this.maxScore;
		}

		@JsonView(UnprivilegedView.class)
		public BigDecimal getScore() {
			return this.score;
		}

		public void setMaxScore(BigDecimal maxScore) {
			this.maxScore = maxScore;
		}

		public void setScore(BigDecimal score) {
			this.score = score;
		}

	}

	private static final int PAGE_SIZE = 10;

	@Autowired
	private TaskService taskService;

	@Autowired
	private UserService userService;

	@PreAuthorize(IS_ADMIN + OR + '(' + IS_USER + AND + NO_CONTEST_CURRENTLY
			+ ')')
	@RequestMapping(value = "/api/archive/taskCount", method = RequestMethod.GET)
	@JsonView(UnprivilegedView.class)
	public Long countUsers() {
		return this.taskService.countTasks();
	}

	@PreAuthorize(IS_ADMIN + OR + '(' + IS_USER + AND + NO_CONTEST_CURRENTLY
			+ ')')
	@RequestMapping(value = "/api/archive/tasks", method = RequestMethod.GET)
	@JsonView(UnprivilegedView.class)
	public List<TaskDto> getTasks(@RequestParam("page") Integer page,
			Principal principal) {

		return this.taskService
				.findTasksNewestFirstAndAuthorized(page,
						ArchiveTaskListRestController.PAGE_SIZE, principal)
				.stream()
				.map(task -> {
					BigDecimal score = null;
					if (principal != null) {
						final User user = this.userService
								.getUserByUsername(principal.getName());
						if (user != null) {
							score = this.taskService.getScore(task, user);
						}
					}
					return new TaskDto(task, score, this.taskService
							.getMaximumScore(task));
				}).collect(Collectors.toList());
	}

}
