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

import org.jooq.DSLContext;
import org.ng200.openolympus.jooq.Tables;
import org.ng200.openolympus.jooq.tables.daos.ContestTasksDao;
import org.ng200.openolympus.jooq.tables.daos.TaskDao;
import org.ng200.openolympus.jooq.tables.daos.UserDao;
import org.ng200.openolympus.jooq.tables.pojos.Contest;
import org.ng200.openolympus.jooq.tables.pojos.ContestTasks;
import org.ng200.openolympus.jooq.tables.pojos.Solution;
import org.ng200.openolympus.jooq.tables.pojos.Task;
import org.ng200.openolympus.jooq.tables.pojos.Verdict;
import org.ng200.openolympus.services.contest.ContestTimingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("oolsec")
public class SecurityService {

	@Autowired
	private ContestTimingService contestTimingService;

	@Autowired
	private ContestTasksDao contestTasksDao;
	@Autowired
	private UserDao userDao;

	@Autowired
	private TaskDao taskDao;

	@Autowired
	private DSLContext dslContext;

	public boolean canViewVerdictDuringContest(Verdict verdict) {
		return verdict.getViewableDuringContest();
	}

	public Contest getCurrentContest() {
		return this.contestTimingService.getRunningContest();
	}

	public boolean isOnLockdown() {
		// TODO: Implement lockdowns
		return false;
	}

	public boolean isSolutionInCurrentContest(Solution solution) {
		final Contest runningContest = this.contestTimingService.getRunningContest();
		return runningContest == null
				|| (this.isTaskInContest(
						this.taskDao.fetchOneById(solution.getTaskId()),
						runningContest)
						&&

		runningContest.getStartTime().toInstant()
				.isBefore(solution.getTimeAdded().toInstant())
						&& this.contestTimingService
								.getContestEndTimeForUser(runningContest,
										this.userDao.fetchOneById(
												solution.getUserId()))
								.toInstant()
								.isAfter(solution.getTimeAdded().toInstant()));
	}

	public boolean isSuperuser(final Principal principal) {
		if (principal == null) {
			return false;
		}
		return this.dslContext.select(Tables.USER.SUPERUSER).from(Tables.USER)
				.where(Tables.USER.USERNAME.eq(principal.getName())).fetchOne()
				.value1();
	}

	public boolean isTaskInContest(Task task, Contest contest) {
		return this.contestTasksDao
				.exists(new ContestTasks(contest.getId(), task
						.getId()));
	}

	public boolean isTaskInCurrentContest(Task task) {
		final Contest runningContest = this.contestTimingService.getRunningContest();
		return runningContest == null
				|| this.contestTasksDao.exists(new ContestTasks(runningContest
						.getId(), task.getId()));
	}

	public boolean noContest() {
		return this.contestTimingService.getRunningContest() == null;
	}

	public boolean noLockdown() {
		return !this.isOnLockdown();
	}
}
