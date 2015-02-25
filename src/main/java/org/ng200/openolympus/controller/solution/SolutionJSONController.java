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
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.ng200.openolympus.SecurityExpressionConstants;
import org.ng200.openolympus.controller.solution.VerdictJSONController.VerdictDto;
import org.ng200.openolympus.model.Solution;
import org.ng200.openolympus.model.Verdict;
import org.ng200.openolympus.services.SolutionService;
import org.ng200.openolympus.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

@Controller
@RequestMapping(value = "/api/solution/{id}")
public class SolutionJSONController {

	public static class SolutionDto {
		private BigDecimal score;
		private BigDecimal maximumScore;
		private List<VerdictDto> verdicts;

		public SolutionDto(List<VerdictDto> verdicts) {
			super();
			this.score = verdicts.stream().map(verdict -> verdict.getScore())
					.reduce((x, y) -> x.add(y)).orElse(null);
			this.maximumScore = verdicts.stream()
					.map(verdict -> verdict.getMaximumScore())
					.reduce((x, y) -> x.add(y)).orElse(null);
			this.verdicts = verdicts;
		}

		public BigDecimal getMaximumScore() {
			return this.maximumScore;
		}

		public BigDecimal getScore() {
			return this.score;
		}

		public List<VerdictDto> getVerdicts() {
			return this.verdicts;
		}

		public void setMaximumScore(BigDecimal maximumScore) {
			this.maximumScore = maximumScore;
		}

		public void setScore(BigDecimal score) {
			this.score = score;
		}

		public void setVerdicts(List<VerdictDto> verdicts) {
			this.verdicts = verdicts;
		}
	}

	@Autowired
	private SolutionService solutionService;

	@Autowired
	private VerdictJSONController verdictJSONController;
	private final Cache<Pair<Locale, Solution>, SolutionDto> cache = CacheBuilder
			.newBuilder().maximumSize(1000)
			.expireAfterWrite(2, TimeUnit.SECONDS).build();

	@PreAuthorize(SecurityExpressionConstants.IS_ADMIN
			+ SecurityExpressionConstants.OR
			+ '('
			+ SecurityExpressionConstants.IS_USER
			+ SecurityExpressionConstants.AND
			+ SecurityExpressionConstants.USER_IS_OWNER
			+ SecurityExpressionConstants.AND
			+ SecurityExpressionConstants.SOLUTION_INSIDE_CURRENT_CONTEST_OR_NO_CONTEST
			+ ')')
	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody SolutionDto solutionApi(
			@PathVariable(value = "id") final Solution solution,
			final Locale locale) {
		SolutionDto cached = null;
		if ((cached = this.cache.getIfPresent(new Pair<>(locale, solution))) != null) {
			return cached;
		}
		final List<Verdict> verdicts = this.solutionService
				.getVerdictsVisibleDuringContest(solution);
		final SolutionDto dto = new SolutionDto(verdicts
				.stream()
				.sorted((l, r) -> Long.compare(l.getId(), r.getId()))
				.map(verdict -> this.verdictJSONController.showVerdict(verdict,
						locale)).collect(Collectors.toList()));
		if (verdicts.stream().anyMatch(verdict -> !verdict.isTested())) {
			return dto;
		}
		this.cache.put(new Pair<>(locale, solution), dto);
		return dto;
	}
}