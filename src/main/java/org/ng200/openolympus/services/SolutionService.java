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
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.jooq.DSLContext;
import org.ng200.openolympus.jooq.Routines;
import org.ng200.openolympus.jooq.Tables;
import org.ng200.openolympus.jooq.enums.VerdictStatusType;
import org.ng200.openolympus.jooq.tables.daos.SolutionDao;
import org.ng200.openolympus.jooq.tables.pojos.Contest;
import org.ng200.openolympus.jooq.tables.pojos.Solution;
import org.ng200.openolympus.jooq.tables.pojos.Task;
import org.ng200.openolympus.jooq.tables.pojos.User;
import org.ng200.openolympus.jooq.tables.pojos.Verdict;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SolutionService extends GenericCreateUpdateRepository {

	@Autowired
	private SolutionDao solutionDao;

	@Autowired
	private DSLContext dslContext;

	@Autowired
	private ContestService contestService;

	public int countUserSolutions(final User user) {
		return this.dslContext.selectCount().from(Tables.SOLUTION)
				.where(Tables.SOLUTION.USER_ID.eq(user.getId())).fetchOne()
				.value1();
	}

	public int countUserSolutionsForTask(final User user, final Task task) {
		return this.dslContext
				.selectCount()
				.from(Tables.SOLUTION)
				.where(Tables.SOLUTION.USER_ID.eq(user.getId()).and(
						Tables.SOLUTION.TASK_ID.eq(task.getId())))
				.fetchOne()
				.value1();
	}

	public long countUserSolutionsInContest(User user, Contest contest) {
		return this.dslContext
				.selectCount()
				.from(Tables.SOLUTION)
				.where(Tables.SOLUTION.USER_ID.eq(user.getId()).and(
						Tables.SOLUTION.TIME_ADDED.between(
								Routines.getContestStartForUser(
										contest.getId(), user.getId()))
								.and(
										Routines.getContestEndForUser(
												contest.getId(),
												user.getId()))))
				.execute();
	}

	public int getNumberOfPendingVerdicts() {
		return this.dslContext.selectCount().from(Tables.VERDICT)
				.where(Tables.VERDICT.STATUS.eq(VerdictStatusType.waiting))
				.fetchOne().value1();
	}

	public long getNumberOfPendingVerdicts(final Solution solution) {
		return this.dslContext
				.selectCount()
				.from(Tables.VERDICT)
				.where(Tables.VERDICT.SOLUTION_ID.eq(solution.getId()).and(
						Tables.VERDICT.STATUS.eq(VerdictStatusType.waiting)))
				.fetchOne().value1();
	}

	public List<Solution> getPage(final int pageNumber, final int pageSize) {
		return this.dslContext.selectCount().from(Tables.SOLUTION)
				.groupBy(Tables.SOLUTION.ID)
				.orderBy(Tables.SOLUTION.TIME_ADDED.desc()).limit(pageSize)
				.offset(pageSize * (pageNumber - 1)).fetchInto(Solution.class);
	}

	public List<Solution> getPage(final User user, final Integer pageNumber,
			final int pageSize, final Date startTime, final Date endTime) {
		return this.dslContext
				.selectCount()
				.from(Tables.SOLUTION)
				.where(Tables.SOLUTION.USER_ID.eq(user.getId()).and(
						Tables.SOLUTION.TIME_ADDED.between(
								Timestamp.from(startTime.toInstant()),
								Timestamp.from(endTime.toInstant()))))
				.groupBy(Tables.SOLUTION.ID)
				.orderBy(Tables.SOLUTION.TIME_ADDED.desc()).limit(pageSize)
				.offset(pageSize * (pageNumber - 1)).fetchInto(Solution.class);
	}

	public List<Solution> getPageOutsideOfContest(final User user,
			final Integer pageNumber, final int pageSize) {
		return this.dslContext.selectFrom(Tables.SOLUTION)
				.where(Tables.SOLUTION.USER_ID.eq(user.getId()))
				.groupBy(Tables.SOLUTION.ID)
				.orderBy(Tables.SOLUTION.TIME_ADDED.desc()).limit(pageSize)
				.offset(pageSize * (pageNumber - 1)).fetchInto(Solution.class);
	}

	public Stream<Verdict> getPendingVerdicts() {
		return StreamSupport.stream(
				this.dslContext
						.selectFrom(Tables.VERDICT)
						.where(Tables.VERDICT.STATUS
								.eq(VerdictStatusType.waiting))
						.spliterator(),
				false).map(record -> record.into(Verdict.class));
	}

	public long getSolutionCount() {
		return this.solutionDao.count();
	}

	public BigDecimal getSolutionMaximumScore(final Solution solution) {
		return this.getVerdictsVisibleDuringContest(solution).stream()
				.map((verdict) -> verdict.getMaximumScore())
				.reduce((x, y) -> x.add(y)).orElse(BigDecimal.ZERO);
	}

	public BigDecimal getSolutionScore(final Solution solution) {
		if (solution == null) {
			return BigDecimal.ZERO;
		}
		return this.getVerdictsVisibleDuringContest(solution).stream()
				.map((verdict) -> verdict.getScore())
				.reduce((x, y) -> x.add(y)).orElse(BigDecimal.ZERO);
	}

	public List<Verdict> getVerdictsVisibleDuringContest(
			final Solution solution) {
		// TODO: show full tests during contest should be an option
		if (this.contestService.getRunningContest() == null) {
			return this.dslContext.selectFrom(Tables.VERDICT)
					.where(Tables.VERDICT.SOLUTION_ID.eq(solution.getId()))
					.fetchInto(Verdict.class);
		}
		return this.getVerdictsVisibleDuringContest(solution);
	}

	@Transactional
	public Solution insertSolution(Solution solution) {
		return this.insert(solution, Tables.SOLUTION);
	}

	@CacheEvict(value = "solutions", key = "#verdict.solution.id")
	@Transactional
	public synchronized Verdict updateVerdict(Verdict verdict) {
		return this.update(verdict, Tables.VERDICT);
	}
}
