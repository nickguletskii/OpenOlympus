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

import java.security.Principal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;

import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Param;
import org.jooq.Record1;
import org.jooq.SelectConditionStep;
import org.jooq.impl.DSL;
import org.markdown4j.YumlPlugin;
import org.ng200.openolympus.jooq.Routines;
import org.ng200.openolympus.jooq.Tables;
import org.ng200.openolympus.jooq.tables.daos.ContestTasksDao;
import org.ng200.openolympus.jooq.tables.daos.TaskDao;
import org.ng200.openolympus.jooq.tables.daos.UserDao;
import org.ng200.openolympus.jooq.tables.pojos.Contest;
import org.ng200.openolympus.jooq.tables.pojos.ContestTasks;
import org.ng200.openolympus.jooq.tables.pojos.Solution;
import org.ng200.openolympus.jooq.tables.pojos.Task;
import org.ng200.openolympus.jooq.tables.pojos.User;
import org.ng200.openolympus.jooq.tables.pojos.Verdict;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("oolsec")
public class SecurityService {

	@Autowired
	private ContestService contestService;

	@Autowired
	private TaskService taskService;

	@Autowired
	private PropertyService propertyService;

	@Autowired
	private ContestTasksDao contestTasksDao;
	@Autowired
	private UserDao userDao;

	@Autowired
	private TaskDao taskDao;

	@Autowired
	private DSLContext dslContext;

	public boolean isContestInProgressForUser(final Contest contest,
			String username) {
		Param<Integer> contestV = DSL.val(contest.getId());
		Field<Timestamp> now = DSL.val(Timestamp.from(Instant.now()));
		Field<Long> userV = dslContext.select(Tables.USER.ID).from(Tables.USER)
				.where(Tables.USER.USERNAME.eq(username)).asField();
		return dslContext
				.select(DSL.field(Routines
						.getContestStartForUser(contestV, userV)
						.le(now)
						.and(Routines.getContestEndForUser(contestV, userV).ge(
								now)))).fetchOne().value1();
	}

	public boolean isContestOver(final Contest contest) {
		return this.contestService.getContestEndIncludingAllTimeExtensions(
				contest).isBefore(Instant.now());
	}

	public boolean isOnLockdown() {
		// TODO: Implement lockdowns
		return false;
	}

	public boolean isSolutionInCurrentContest(Solution solution) {
		final Contest runningContest = this.contestService.getRunningContest();
		return runningContest == null
				|| (this.isTaskInContest(
						taskDao.fetchOneById(solution.getTaskId()),
						runningContest)
						&&

						runningContest.getStartTime().toInstant()
								.isBefore(solution.getTimeAdded().toInstant()) && this.contestService
						.getContestEndTimeForUser(runningContest,
								userDao.fetchOneById(solution.getUserId()))
						.toInstant()
						.isAfter(solution.getTimeAdded().toInstant()));
	}

	public boolean isSuperuser(final Principal principal) {
		if (principal == null) {
			return false;
		}
		return dslContext.select(Tables.USER.SUPERUSER).from(Tables.USER)
				.where(Tables.USER.USERNAME.eq(principal.getName())).fetchOne()
				.value1();
	}

	private boolean isSuperuser(final User user) {
		if (user == null) {
			return false;
		}
		return user.getSuperuser();
	}

	public boolean isTaskInContest(Task task, Contest contest) {
		return contestTasksDao.exists(new ContestTasks(contest.getId(), task
				.getId()));
	}

	public boolean isTaskInCurrentContest(Task task) {
		final Contest runningContest = this.contestService.getRunningContest();
		return runningContest == null
				|| contestTasksDao.exists(new ContestTasks(runningContest
						.getId(), task.getId()));
	}

	public boolean noContest() {
		return this.contestService.getRunningContest() == null;
	}

	public boolean noLockdown() {
		return !this.isOnLockdown();
	}

	public boolean canViewVerdictDuringContest(Verdict verdict) {
		return verdict.getViewableDuringContest();
	}

	public Contest getCurrentContest() {
		return contestService.getRunningContest();
	}
}
