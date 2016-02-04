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
package org.ng200.openolympus.controller.solution;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.ng200.openolympus.SecurityClearanceType;
import org.ng200.openolympus.annotations.SecurityClearanceRequired;
import org.ng200.openolympus.jooq.tables.pojos.Solution;
import org.ng200.openolympus.jooq.tables.pojos.Verdict;
import org.ng200.openolympus.security.annotations.SecurityAnd;
import org.ng200.openolympus.security.annotations.SecurityLeaf;
import org.ng200.openolympus.security.annotations.SecurityOr;
import org.ng200.openolympus.security.predicates.SolutionIsInContestModeratedByCurrentUserSecurityPredicate;
import org.ng200.openolympus.security.predicates.SolutionScoreSecurityPredicate;
import org.ng200.openolympus.security.predicates.SolutionSecurityPredicate;
import org.ng200.openolympus.security.predicates.UserIsOwnerOfSolutionSecurityPredicate;
import org.ng200.openolympus.services.SolutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Profile("web")
@RequestMapping(value = "/api/solution/{id}")
@SecurityOr({
				@SecurityAnd({
								@SecurityLeaf(value = SecurityClearanceType.APPROVED_USER, predicates = UserIsOwnerOfSolutionSecurityPredicate.class)
		}),

				@SecurityAnd({
								@SecurityLeaf(value = SecurityClearanceType.ANONYMOUS, predicates = SolutionIsInContestModeratedByCurrentUserSecurityPredicate.class)
		}),

				@SecurityAnd({
								@SecurityLeaf(value = SecurityClearanceType.VIEW_ALL_SOLUTIONS)
		})

})
public class SolutionStatusController {

	@SecurityClearanceRequired(minimumClearance = SecurityClearanceType.APPROVED_USER, predicates = SolutionSecurityPredicate.class)
	public static class SolutionDto {
		private BigDecimal score;
		private BigDecimal maximumScore;
		private List<Verdict> verdicts;

		public SolutionDto(List<Verdict> verdicts) {
			super();
			this.verdicts = verdicts;
		}

		@SecurityClearanceRequired(minimumClearance = SecurityClearanceType.APPROVED_USER, predicates = SolutionScoreSecurityPredicate.class)
		public BigDecimal getMaximumScore() {
			return this.maximumScore;
		}

		@SecurityClearanceRequired(minimumClearance = SecurityClearanceType.APPROVED_USER, predicates = SolutionScoreSecurityPredicate.class)
		public BigDecimal getScore() {
			return this.score;
		}

		@SecurityClearanceRequired(minimumClearance = SecurityClearanceType.APPROVED_USER, predicates = SolutionScoreSecurityPredicate.class)
		public List<Verdict> getVerdicts() {
			return this.verdicts;
		}

		public void setMaximumScore(BigDecimal maximumScore) {
			this.maximumScore = maximumScore;
		}

		public void setScore(BigDecimal score) {
			this.score = score;
		}

		public void setVerdicts(List<Verdict> verdicts) {
			this.verdicts = verdicts;
		}
	}

	@Autowired
	private SolutionService solutionService;

	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody SolutionDto solutionApi(
			@PathVariable(value = "id") final Solution solution) {
		final List<Verdict> verdicts = this.solutionService
				.getVerdictsVisibleDuringContest(solution);

		final SolutionDto dto = new SolutionDto(verdicts
				.stream()
				.sorted((l, r) -> Long.compare(l.getId(), r.getId()))
				.collect(Collectors.toList()));
		return dto;
	}
}