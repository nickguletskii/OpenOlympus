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
package org.ng200.openolympus.controller.user;

import java.math.BigDecimal;
import java.security.Principal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.ng200.openolympus.model.Solution;
import org.ng200.openolympus.model.User;
import org.ng200.openolympus.repositories.SolutionRepository;
import org.ng200.openolympus.repositories.UserRepository;
import org.ng200.openolympus.services.ContestService;
import org.ng200.openolympus.services.SolutionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.common.base.Optional;

@Controller
public class SolutionListController {

	@SuppressWarnings("unused")
	private static class SolutionDTO {
		private String user;

		private BigDecimal score;
		private BigDecimal maximumScore;
		private String taskName;
		private final String taskUrl;
		private String url;
		private Date timeAdded;

		public SolutionDTO(final String url, final String user,
				final BigDecimal score, final BigDecimal maximumScore,
				final String taskName, final String taskUrl,
				final Date timeAdded) {
			super();
			this.setUrl(url);
			this.user = user;
			this.score = score;
			this.maximumScore = maximumScore;
			this.taskName = taskName;
			this.taskUrl = taskUrl;
			this.timeAdded = timeAdded;
		}

		public BigDecimal getMaximumScore() {
			return this.maximumScore;
		}

		public BigDecimal getScore() {
			return this.score;
		}

		public String getTaskName() {
			return this.taskName;
		}

		public String getTaskUrl() {
			return this.taskUrl;
		}

		public Date getTimeAdded() {
			return this.timeAdded;
		}

		public String getUrl() {
			return this.url;
		}

		public String getUser() {
			return this.user;
		}

		public void setMaximumScore(final BigDecimal maximumScore) {
			this.maximumScore = maximumScore;
		}

		public void setScore(final BigDecimal score) {
			this.score = score;
		}

		public void setTaskName(final String taskName) {
			this.taskName = taskName;
		}

		public void setTimeAdded(final Date timeAdded) {
			this.timeAdded = timeAdded;
		}

		public void setUrl(final String url) {
			this.url = url;
		}

		public void setUser(final String user) {
			this.user = user;
		}

		@Override
		public String toString() {
			return String
					.format("SolutionDTO [user=%s, score=%s, maximumScore=%s, taskName=%s, taskUrl=%s, url=%s, timeAdded=%s]",
							this.user, this.score, this.maximumScore,
							this.taskName, this.taskUrl, this.url,
							this.timeAdded);
		}
	}

	private static final Logger logger = LoggerFactory
			.getLogger(SolutionListController.class);

	@Autowired
	private SolutionRepository solutionRepository;

	@Autowired
	private SolutionService solutionService;

	@Autowired
	private ContestService contestService;
	@Autowired
	private UserRepository userRepository;

	private long getSolutionCountForUser(final User user) {
		if (this.contestService.getRunningContest() == null) {
			return this.solutionRepository.countByUser(user);
		}
		return this.contestService
				.getRunningContest()
				.getTasks()
				.stream()
				.map(task -> this.solutionRepository.countByUserAndTask(user,
						task)).reduce((x, y) -> x + y).orElse(0l);
	}

	@RequestMapping(value = "/admin/solutions", method = RequestMethod.GET)
	public String showAllSolutions(
			@RequestParam(value = "page", defaultValue = "1") final Integer pageNumber,
			final Model model) {
		final List<SolutionDTO> lst = new ArrayList<SolutionDTO>();
		final Sort sort = new Sort(Direction.DESC, "timeAdded");
		final PageRequest pageRequest = new PageRequest(pageNumber - 1, 10,
				sort);
		for (final Solution solution : this.solutionRepository
				.findAll(pageRequest)) {
			lst.add(new SolutionDTO(MessageFormat.format("/solution?id={0}",
					Long.toString(solution.getId())), solution.getUser()
					.getUsername(), this.solutionService
					.getSolutionScore(solution), this.solutionService
					.getSolutionMaximumScore(solution), solution.getTask()
					.getName(), MessageFormat.format("/task/{0}",
							Long.toString(solution.getTask().getId())), solution
							.getTimeAdded()));
		}
		model.addAttribute("solutions", lst);
		model.addAttribute("numberOfPages",
				Math.max((this.solutionRepository.count() + 9) / 10, 1));
		model.addAttribute("currentPage", pageNumber);
		model.addAttribute("pagePrefix", "/admin/solutions?page=");

		return "tasks/solutions";
	}

	@RequestMapping(value = "/user/solutions", method = RequestMethod.GET)
	public String showUserSolutions(
			@RequestParam(value = "page", defaultValue = "1") final Integer pageNumber,
			final Model model, final Principal principal) {
		final User user = this.userRepository.findByUsername(principal
				.getName());

		final List<SolutionDTO> lst = new ArrayList<SolutionDTO>();
		final Sort sort = new Sort(Direction.DESC, "timeAdded");
		final PageRequest pageRequest = new PageRequest(pageNumber - 1, 10,
				sort);
		final List<Solution> solutions = new ArrayList<>();
		if (this.contestService.getRunningContest() != null) {
			this.contestService
			.getRunningContest()
			.getTasks()
			.forEach(
					(task) -> solutions.addAll(Optional
							.fromNullable(
									this.solutionRepository
									.findByUserAndTaskOrderByTimeAddedDesc(
											user, task,
											pageRequest)).or(
													new ArrayList<>())));
		} else {
			solutions.addAll(this.solutionRepository
					.findByUserOrderByTimeAddedDesc(user, pageRequest));
		}
		for (final Solution solution : solutions) {
			lst.add(new SolutionDTO(MessageFormat.format("/solution?id={0}",
					Long.toString(solution.getId())), solution.getUser()
					.getUsername(), this.solutionService
					.getSolutionScore(solution), this.solutionService
					.getSolutionMaximumScore(solution), solution.getTask()
					.getName(), MessageFormat.format("/task/{0}",
							Long.toString(solution.getTask().getId())), solution
							.getTimeAdded()));
		}
		SolutionListController.logger.info("Page view: {}", solutions);
		model.addAttribute("solutions", lst);
		model.addAttribute("numberOfPages",
				Math.max((this.getSolutionCountForUser(user) + 9) / 10, 1));
		model.addAttribute("currentPage", pageNumber);
		model.addAttribute("pagePrefix", "/user/solutions?page=");

		return "tasks/solutions";
	}
}
