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
package org.ng200.openolympus.services;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.ng200.openolympus.model.Solution;
import org.ng200.openolympus.model.Task;
import org.ng200.openolympus.model.User;
import org.ng200.openolympus.model.Verdict;
import org.ng200.openolympus.repositories.SolutionRepository;
import org.ng200.openolympus.repositories.VerdictRepository;
import org.ng200.openolympus.util.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SolutionService {
	@Autowired
	private SolutionRepository solutionRepository;
	@Autowired
	private VerdictRepository verdictRepository;
	@Autowired
	private ContestService contestService;

	@PreAuthorize("hasAuthority('SUPERUSER') or (hasAuthority('USER') and #user.username == authentication.name)")
	public long countUserSolutions(final User user) {
		return this.solutionRepository.countByUser(user);
	}

	@PreAuthorize("hasAuthority('SUPERUSER') or (hasAuthority('USER') and #user.username == authentication.name)")
	public long countUserSolutionsForTask(final User user, final Task task) {
		return this.solutionRepository.countByUserAndTask(user, task);
	}

	@PreAuthorize("hasAuthority('SUPERUSER')")
	public long getNumberOfPages(final long pageSize) {
		return PageUtils.getNumberOfPages(this.solutionRepository.count(),
				pageSize);
	}

	@PreAuthorize("hasAuthority('SUPERUSER')")
	public long getNumberOfPendingVerdicts() {
		return this.verdictRepository.countByTested(false);
	}

	@PreAuthorize("hasAuthority('SUPERUSER') or (hasAuthority('USER') and #solution.user.username == authentication.name)")
	public long getNumberOfPendingVerdicts(final Solution solution) {
		return this.verdictRepository.countBySolutionAndTested(solution, false);
	}

	@PreAuthorize("hasAuthority('SUPERUSER')")
	public List<Solution> getPage(final int pageNumber, final int pageSize) {
		return this.solutionRepository.findAll(
				new PageRequest(pageNumber - 1, pageSize, Direction.DESC,
						"timeAdded")).getContent();
	}

	@PreAuthorize("hasAuthority('SUPERUSER') or (hasAuthority('USER') and #user.username == authentication.name)")
	public List<Solution> getPage(final User user, final Integer pageNumber,
			final int pageSize, final Date startTime, final Date endTime) {
		return this.solutionRepository
				.findByUserAndTimeAddedBetweenOrderByTimeAddedDesc(user,
						startTime, endTime, new PageRequest(pageNumber - 1,
								pageSize));
	}

	@PreAuthorize("hasAuthority('SUPERUSER') or (hasAuthority('USER') and #user.username == authentication.name and @oolsec.noContest())")
	public List<Solution> getPageOutsideOfContest(final User user,
			final Integer pageNumber, final int pageSize) {
		return this.solutionRepository.findByUser(user, new PageRequest(
				pageNumber - 1, pageSize, Direction.DESC, "timeAdded"));
	}

	@PreAuthorize("hasAuthority('SUPERUSER')")
	public List<Verdict> getPendingVerdicts() {
		return this.verdictRepository.findByTestedOrderByIdAsc(false);
	}

	public long getSolutionCount() {
		return this.solutionRepository.count();
	}

	public BigDecimal getSolutionMaximumScore(final Solution solution) {
		return this.getVerdictsVisibleDuringContest(solution).stream()
				.map((verdict) -> verdict.getMaximumScore())
				.reduce((x, y) -> x.add(y)).orElse(BigDecimal.ZERO);
	}

	@PreAuthorize("hasAuthority('SUPERUSER') or (hasAuthority('USER') and #user.username == authentication.name and #task.published)")
	public List<Solution> getSolutionsByUserAndTaskNewestFirst(final User user,
			final Task task, final PageRequest pageRequest) {
		return this.solutionRepository.findByUserAndTaskOrderByTimeAddedDesc(
				user, task, pageRequest);
	}

	@PreAuthorize("hasAuthority('SUPERUSER') or (hasAuthority('USER') and #solution.user.username == authentication.name)")
	public BigDecimal getSolutionScore(final Solution solution) {
		if (solution == null) {
			return BigDecimal.ZERO;
		}
		return this.getVerdictsVisibleDuringContest(solution).stream()
				.map((verdict) -> verdict.getScore())
				.reduce((x, y) -> x.add(y)).orElse(BigDecimal.ZERO);
	}

	@PreAuthorize("hasAuthority('SUPERUSER') or (hasAuthority('USER') and #solution.user.username == authentication.name)")
	public List<Verdict> getVerdictsVisibleDuringContest(final Solution solution) {
		if (this.contestService.getRunningContest() == null) {
			return this.verdictRepository.findBySolutionOrderByIdAsc(solution);
		}
		return this.verdictRepository
				.findBySolutionAndIsViewableWhenContestRunningOrderByIdAsc(
						solution, true);
	}

	public Solution saveSolution(Solution solution) {
		return solution = this.solutionRepository.save(solution);
	}

	@Transactional
	public synchronized Verdict saveVerdict(Verdict verdict) {
		verdict = this.verdictRepository.saveAndFlush(verdict);
		return verdict;
	}

	@Transactional
	public List<Verdict> saveVerdicts(final List<Verdict> verdicts) {
		final List<Verdict> savedVerdicts = this.verdictRepository
				.save(verdicts);
		this.verdictRepository.flush();

		return savedVerdicts;
	}
}
