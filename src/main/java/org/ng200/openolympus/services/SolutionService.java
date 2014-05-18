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
package org.ng200.openolympus.services;

import java.math.BigDecimal;
import java.util.List;

import org.ng200.openolympus.model.Solution;
import org.ng200.openolympus.model.Task;
import org.ng200.openolympus.model.User;
import org.ng200.openolympus.model.Verdict;
import org.ng200.openolympus.repositories.SolutionRepository;
import org.ng200.openolympus.repositories.VerdictRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class SolutionService {
	@Autowired
	private SolutionRepository solutionRepository;
	@Autowired
	private VerdictRepository verdictRepository;

	@Autowired
	private ContestService contestService;

	public Solution getLastSolutionByUserForTask(final User user,
			final Task task) {
		return this.solutionRepository.findByUserAndTaskOrderByTimeAddedDesc(
				user, task, new PageRequest(0, 1)).get(0);
	}

	public List<Verdict> getPendingVerdicts() {
		final List<Verdict> lst = this.verdictRepository
				.findByTestedAndIsViewableWhenContestRunningOrderByIdAsc(false,
						true);
		if (lst.isEmpty()) {
			lst.addAll(this.verdictRepository
					.findByTestedAndIsViewableWhenContestRunningOrderByIdAsc(
							false, false));
		}
		return lst;
	}

	public BigDecimal getSolutionMaximumScore(final Solution solution) {
		return this.getVerdicts(solution).stream()
				.map((verdict) -> verdict.getMaximumScore())
				.reduce((x, y) -> x.add(y)).orElse(BigDecimal.ZERO);
	}

	public BigDecimal getSolutionScore(final Solution solution) {
		if (solution == null) {
			return BigDecimal.ZERO;
		}
		return this.getVerdicts(solution).stream()
				.map((verdict) -> verdict.getScore())
				.reduce((x, y) -> x.add(y)).orElse(BigDecimal.ZERO);
	}

	public List<Verdict> getVerdicts(final Solution solution) {
		if (this.contestService.getRunningContest() == null) {
			return this.verdictRepository.findBySolutionOrderByIdAsc(solution);
		}
		return this.verdictRepository
				.findBySolutionAndIsViewableWhenContestRunningOrderByIdAsc(
						solution, true);
	}
}
