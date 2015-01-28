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

import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.ng200.openolympus.Assertions;
import org.ng200.openolympus.exceptions.ResourceNotFoundException;
import org.ng200.openolympus.model.Contest;
import org.ng200.openolympus.model.Task;
import org.ng200.openolympus.model.User;
import org.ng200.openolympus.services.ContestService;
import org.ng200.openolympus.services.SolutionService;
import org.ng200.openolympus.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

//TODO: Port contest results to new API
@Controller
public class ContestResultsController {

	@SuppressWarnings("unused")
	private static class TaskColumn {
		public String taskName;
		public List<BigDecimal> results;

		public TaskColumn(final String taskName, final List<BigDecimal> results) {
			super();
			this.taskName = taskName;
			this.results = results;
		}

		public List<BigDecimal> getResults() {
			return this.results;
		}

		public String getTaskName() {
			return this.taskName;
		}

		public void setResults(final List<BigDecimal> results) {
			this.results = results;
		}

		public void setTaskName(final String taskName) {
			this.taskName = taskName;
		}

	}

	private static final int PAGE_SIZE = 10;

	@Autowired
	private ContestService contestService;

	@Autowired
	private SolutionService solutionService;

	@Autowired
	private TaskService taskService;

	private List<User> getContestParticipantsOrderedByPlace(
			final Contest contest) {
		return this.contestService
				.getContestParticipants(contest)
				.stream()
				.sorted((l, r) -> -(this.getUserScoreInContest(l, contest)
						.compareTo(this.getUserScoreInContest(r, contest))))
						.collect(Collectors.toList());
	}

	private List<BigDecimal> getContestTotalScores(final Contest contest,
			final List<User> contestUsers) {
		return contestUsers.stream()
				.map(user -> this.getUserScoreInContest(user, contest))
				.collect(Collectors.toList());
	}

	private BigDecimal getUserScoreForTaskInContest(final User user,
			final Contest contest, final Task task) {
		return this.contestService.getUserTaskScoreInContest(contest, user,
				task);
	}

	private BigDecimal getUserScoreInContest(final User user,
			final Contest contest) {
		return contest
				.getTasks()
				.stream()
				.map(task -> this.getUserScoreForTaskInContest(user, contest,
						task)).reduce((ls, rs) -> ls.add(rs))
						.orElse(BigDecimal.ZERO);
	}

	@RequestMapping(value = "/contest/{contest}/completeResults", method = RequestMethod.GET)
	public String showCompleteResultsPage(
			@PathVariable(value = "contest") final Contest contest,
			final Model model, final Principal principal) {
		Assertions.resourceExists(contest);

		final List<User> contestUsers = this
				.getContestParticipantsOrderedByPlace(contest);

		final List<BigDecimal> contestTotalScores = this.getContestTotalScores(
				contest, contestUsers);

		return this.showResultsPage(contest, null, model, contestUsers,
				contestTotalScores, 0, contestUsers.size(), null);
	}

	private String showResultsPage(final Contest contest,
			final Integer pageNumber, final Model model,
			final List<User> contestUsers,
			final List<BigDecimal> contestTotalScores, final int startOfPage,
			final int endOfPage, final Integer numberOfPages) {
		final List<Integer> contestPlaces = new ArrayList<>(endOfPage);

		int currentPlace = 0;
		BigDecimal lastScore = new BigDecimal("-1");
		for (int i = 0; i < endOfPage; i++) {
			if (lastScore.compareTo(contestTotalScores.get(i)) != 0) {
				currentPlace++;
			}
			contestPlaces.add(currentPlace);
			lastScore = contestTotalScores.get(i);
		}

		final List<Integer> places = contestPlaces.subList(startOfPage,
				endOfPage);

		final List<User> users = contestUsers.subList(startOfPage, endOfPage);

		final List<BigDecimal> totalScores = contestTotalScores.subList(
				startOfPage, endOfPage);

		final List<TaskColumn> taskColumns = contest
				.getTasks()
				.stream()
				.map(task -> new TaskColumn(task.getName(), users
						.stream()
						.map(user -> this.getUserScoreForTaskInContest(user,
								contest, task)).collect(Collectors.toList())))
								.collect(Collectors.toList());

		model.addAttribute("places", places);
		model.addAttribute("users", users);
		model.addAttribute("taskColumns", taskColumns);
		model.addAttribute("totalScores", totalScores);
		model.addAttribute("numberOfPages", numberOfPages);
		model.addAttribute("currentPage", pageNumber);

		model.addAttribute("pagePrefix", "/contest/" + contest.getId()
				+ "/results");

		return "contest/results";
	}

	@RequestMapping(value = "/contest/{contest}/results", method = RequestMethod.GET)
	public String showResultsPage(
			@PathVariable(value = "contest") final Contest contest,
			@RequestParam(value = "page", defaultValue = "1") final Integer pageNumber,
			final Model model, final Principal principal) {
		Assertions.resourceExists(contest);
		if (pageNumber < 1) {
			throw new ResourceNotFoundException();
		}

		final List<User> contestUsers = this
				.getContestParticipantsOrderedByPlace(contest);

		final List<BigDecimal> contestTotalScores = this.getContestTotalScores(
				contest, contestUsers);

		final int startOfPage = Math.min((pageNumber - 1)
				* ContestResultsController.PAGE_SIZE, contestUsers.size());
		final int endOfPage = Math.min(pageNumber
				* ContestResultsController.PAGE_SIZE, contestUsers.size());

		return this.showResultsPage(
				contest,
				pageNumber,
				model,
				contestUsers,
				contestTotalScores,
				startOfPage,
				endOfPage,
				Math.max((contestUsers.size()
						+ ContestResultsController.PAGE_SIZE - 1)
						/ ContestResultsController.PAGE_SIZE, 1));
	}
}
