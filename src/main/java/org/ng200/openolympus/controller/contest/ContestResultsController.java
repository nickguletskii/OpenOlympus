/**
 * The MIT License
 * Copyright (c) 2014-2016 Nick Guletskii
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
import java.util.List;
import java.util.stream.Collectors;

import org.ng200.openolympus.Assertions;
import org.ng200.openolympus.SecurityClearanceType;
import org.ng200.openolympus.dto.UserRanking;
import org.ng200.openolympus.exceptions.ResourceNotFoundException;
import org.ng200.openolympus.jooq.tables.pojos.Contest;
import org.ng200.openolympus.jooq.tables.pojos.Task;
import org.ng200.openolympus.model.UserDetailsImpl;
import org.ng200.openolympus.security.annotations.SecurityAnd;
import org.ng200.openolympus.security.annotations.SecurityLeaf;
import org.ng200.openolympus.security.annotations.SecurityOr;
import org.ng200.openolympus.security.predicates.ContestResultsPermissionPredicate;
import org.ng200.openolympus.services.contest.ContestResultsService;
import org.ng200.openolympus.util.Beans;
import org.ng200.openolympus.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

//TODO: Port contest results to new API
@RestController
@Profile("web")

@SecurityOr({
				@SecurityAnd({
								@SecurityLeaf(value = SecurityClearanceType.APPROVED_USER, predicates = {
																											ContestResultsPermissionPredicate.class
				})
		})
})
public class ContestResultsController {

	public class ContestUserRankingDto extends UserRanking {

		/**
		 *
		 */
		private static final long serialVersionUID = -6613906700172390312L;

		private List<Pair<Task, BigDecimal>> taskScores;

		public ContestUserRankingDto(Contest contest, UserRanking ranking) {
			Beans.copy(ranking, this);
			UserDetailsImpl.logger.info("RANKING {} {}", ranking.getRank(),
					this.getRank());
		}

		public List<Pair<Task, BigDecimal>> getTaskScores() {
			return this.taskScores;
		}

		public void setTaskScores(List<Pair<Task, BigDecimal>> taskScores) {
			this.taskScores = taskScores;
		}

	}

	@SuppressWarnings("unused")
	private static class TaskColumn {
		public String taskName;
		public List<BigDecimal> results;

		public TaskColumn(final String taskName,
				final List<BigDecimal> results) {
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

	@Autowired
	private ContestResultsService contestResultsService;

	@RequestMapping(value = "/api/contest/{contest}/testingFinished", method = RequestMethod.GET)
	public boolean hasContestTestingFinished(
			@PathVariable(value = "contest") final Contest contest) {

		Assertions.resourceExists(contest);

		return this.contestResultsService.hasContestTestingFinished(contest);
	}

	@RequestMapping(value = "/api/contest/{contest}/completeResults", method = RequestMethod.GET)

	public List<ContestUserRankingDto> showCompleteResultsPage(
			@PathVariable(value = "contest") final Contest contest,
			final Model model, final Principal principal) {
		Assertions.resourceExists(contest);

		return this.contestResultsService.getContestResults(contest).stream()
				.map(ranking -> new ContestUserRankingDto(contest, ranking))
				.collect(Collectors.toList());
	}

	@RequestMapping(value = "/api/contest/{contest}/results", method = RequestMethod.GET)

	public List<ContestUserRankingDto> showResultsPage(
			@PathVariable(value = "contest") final Contest contest,
			@RequestParam(value = "page", defaultValue = "1") final Integer pageNumber,
			final Model model, final Principal principal) {
		Assertions.resourceExists(contest);
		if (pageNumber < 1) {
			throw new ResourceNotFoundException();
		}

		Assertions.resourceExists(contest);

		return this.contestResultsService
				.getContestResultsPage(contest, pageNumber, 10).stream()
				.map(ranking -> new ContestUserRankingDto(contest, ranking))
				.collect(Collectors.toList());
	}
}
