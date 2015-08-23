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
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Param;
import org.jooq.Record;
import org.jooq.Record3;
import org.jooq.SelectConditionStep;
import org.jooq.Table;
import org.jooq.impl.DSL;
import org.jooq.util.postgres.PostgresDataType;
import org.ng200.openolympus.dto.UserRanking;
import org.ng200.openolympus.jooq.Routines;
import org.ng200.openolympus.jooq.Tables;
import org.ng200.openolympus.jooq.enums.ContestPermissionType;
import org.ng200.openolympus.jooq.routines.GetContestEnd;
import org.ng200.openolympus.jooq.routines.GetContestEndForUser;
import org.ng200.openolympus.jooq.tables.daos.ContestDao;
import org.ng200.openolympus.jooq.tables.daos.ContestParticipationDao;
import org.ng200.openolympus.jooq.tables.daos.TimeExtensionDao;
import org.ng200.openolympus.jooq.tables.pojos.Contest;
import org.ng200.openolympus.jooq.tables.pojos.ContestParticipation;
import org.ng200.openolympus.jooq.tables.pojos.Task;
import org.ng200.openolympus.jooq.tables.pojos.TimeExtension;
import org.ng200.openolympus.jooq.tables.pojos.User;
import org.ng200.openolympus.jooq.tables.records.ContestTasksRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ContestService extends GenericCreateUpdateRepository {

	@Autowired
	private ContestDao contestDao;
	@Autowired
	private ContestParticipationDao contestParticipationDao;

	@Autowired
	private TimeExtensionDao timeExtensionDao;

	@Autowired
	private DSLContext dslContext;

	public void addContestParticipant(final Contest contest, final User user) {
		this.contestParticipationDao.insert(new ContestParticipation(null, null,
		        user.getId(), contest.getId()));
	}

	public void addContestTask(Contest contest, Task taskByName) {
		final ContestTasksRecord record = new ContestTasksRecord(
		        contest.getId(),
		        taskByName.getId());
		record.attach(this.dslContext.configuration());
		record.insert();
	}

	public long countContests() {
		return this.contestDao.count();
	}

	@Transactional
	public void deleteContest(Contest contest) {
		this.contestDao.delete(contest);
	}

	public void extendTimeForUser(final Contest contest, final User user,
	        final Duration time) {
		this.timeExtensionDao.insert(new TimeExtension(null, time, null, user
		        .getId(), contest.getId()));
	}

	public Contest getContestByName(final String name) {
		return this.contestDao.fetchOneByName(name);
	}

	public Instant getContestEndIncludingAllTimeExtensions(
	        final Contest contest) {
		final GetContestEnd procedure = new GetContestEnd();
		procedure.setContestId(contest.getId());
		procedure.attach(this.dslContext.configuration());
		procedure.execute();
		return procedure.getReturnValue().toInstant();
	}

	public Date getContestEndTimeForUser(Contest contest, User user) {
		final GetContestEndForUser procedure = new GetContestEndForUser();
		procedure.setContestId(contest.getId());
		procedure.setUserId(user.getId());
		procedure.attach(this.dslContext.configuration());
		procedure.execute();
		return Date.from(procedure.getReturnValue().toInstant());
	}

	public List<UserRanking> getContestResults(Contest contest) {
		return this.getContestResultsQuery(contest)
		        .fetchInto(UserRanking.class);
	}

	public List<UserRanking> getContestResultsPage(Contest contest, int page,
	        int pageSize) {
		return this.getContestResultsQuery(contest).limit(pageSize)
		        .offset((page - 1) * pageSize).fetchInto(UserRanking.class);
	}

	private SelectConditionStep<Record3<Long, BigDecimal, Integer>> getContestResultsQuery(
	        Contest contest) {
		return this.dslContext
		        .select(Tables.CONTEST_PARTICIPATION.USER_ID,
		                DSL.coalesce(Tables.CONTEST_PARTICIPATION.SCORE,
		                        DSL.field("0")).as("score"),
		                DSL.rank()
		                        .over(DSL.orderBy(DSL.coalesce(
		                                Tables.CONTEST_PARTICIPATION.SCORE,
		                                DSL.field("0")).desc()))
		                        .as("rank"))
		        .from(Tables.CONTEST_PARTICIPATION)
		        .where(Tables.CONTEST_PARTICIPATION.CONTEST_ID.eq(contest
		                .getId()));
	}

	public List<Contest> getContestsOrderedByTime(final Integer pageNumber,
	        final int pageSize) {
		return this.dslContext.selectFrom(Tables.CONTEST)
		        .groupBy(Tables.CONTEST.ID)
		        .orderBy(Tables.CONTEST.START_TIME.desc())
		        .limit(pageSize)
		        .offset((pageNumber - 1) * pageSize)
		        .fetchInto(Contest.class);
	}

	public List<Contest> getContestsThatIntersect(final Date startDate,
	        final Date endDate) {

		return this.dslContext
		        .selectFrom(Routines.getContestsThatIntersect(
		                Timestamp.from(startDate.toInstant()),
		                Timestamp.from(endDate.toInstant())))
		        .fetchInto(Contest.class);
	}

	public List<Task> getContestTasks(Contest contest) {
		return this.dslContext
		        .selectFrom(Tables.TASK)
		        .where(Tables.TASK.ID.in(this.dslContext
		                .select(Tables.CONTEST_TASKS.TASK_ID)
		                .from(Tables.CONTEST_TASKS)
		                .where(Tables.CONTEST_TASKS.CONTEST_ID.eq(contest
		                        .getId()))))
		        .fetchInto(Task.class);
	}

	public List<User> getPariticipantsPage(Contest contest, Integer pageNumber,
	        int pageSize) {
		return this.dslContext
		        .select(Tables.USER.fields())
		        .from(Tables.CONTEST_PARTICIPATION)
		        .leftOuterJoin(Tables.USER)
		        .on(Tables.CONTEST_PARTICIPATION.USER_ID.eq(Tables.USER.ID))
		        .where(Tables.CONTEST_PARTICIPATION.CONTEST_ID.eq(contest
		                .getId()))
		        .limit(pageSize)
		        .offset((pageNumber - 1) * pageSize).fetchInto(User.class);
	}

	public Contest getRunningContest() {
		return this.dslContext
		        .selectFrom(Routines.getContestsThatIntersect(
		                DSL.field("LOCALTIMESTAMP", Timestamp.class),
		                DSL.field("LOCALTIMESTAMP", Timestamp.class)))
		        .fetchOneInto(Contest.class);
	}

	public BigDecimal getUserTaskScoreInContest(final Contest contest,
	        final User user, final Task task) {
		return this.dslContext
		        .select(Tables.SOLUTION.SCORE)
		        .from(Tables.SOLUTION)
		        .where(Tables.SOLUTION.TASK_ID
		                .eq(task.getId())
		                .and(Tables.SOLUTION.USER_ID.eq(user.getId()))
		                .and(Tables.SOLUTION.TIME_ADDED.ge(Routines
		                        .getContestStartForUser(contest.getId(),
		                                user.getId())))
		                .and(Tables.SOLUTION.TIME_ADDED.le(Routines
		                        .getContestEndForUser(contest.getId(),
		                                user.getId()))))
		        .groupBy(Tables.SOLUTION.ID)
		        .orderBy(Tables.SOLUTION.TIME_ADDED.desc()).limit(1).fetchOne()
		        .value1();
	}

	public boolean hasContestStarted(final Contest contest) {
		if (contest == null) {
			return false;
		}
		return !contest.getStartTime().toInstant().isAfter(Instant.now());
	}

	@SuppressWarnings("unchecked")
	public boolean hasContestTestingFinished(Contest contest) {
		final Param<Integer> contestF = DSL.val(contest.getId(),
		        PostgresDataType.INTEGER);
		final Condition solutionWithinTimeBounds = Tables.SOLUTION.TIME_ADDED
		        .between(Routines.getContestStartForUser(contestF,
		                Tables.SOLUTION.USER_ID),
		                Routines.getContestEndForUser(contestF,
		                        Tables.SOLUTION.USER_ID));

		final Table<Record> currentTasks = this.dslContext.select()
		        .from(Tables.CONTEST_TASKS)
		        .where(Tables.CONTEST_TASKS.CONTEST_ID.eq(contestF))
		        .asTable("current_tasks");
		final Table<?> solutionsTasks = DSL
		        .select(Tables.SOLUTION.USER_ID, Tables.SOLUTION.TASK_ID)
		        .distinctOn(Tables.SOLUTION.USER_ID, Tables.SOLUTION.TASK_ID)
		        .from(Tables.SOLUTION)
		        .rightOuterJoin(currentTasks)
		        .on(Tables.SOLUTION.TASK_ID.eq((Field<Integer>) currentTasks
		                .field("task_id")))
		        .where(solutionWithinTimeBounds.and(Tables.SOLUTION.TESTED
		                .eq(false)))
		        .orderBy(Tables.SOLUTION.USER_ID.asc(),
		                Tables.SOLUTION.TASK_ID.asc(),
		                Tables.SOLUTION.TIME_ADDED.desc())
		        .asTable("solutions_tasks");
		final Table<?> userTasks = DSL
		        .select(solutionsTasks.field(Tables.SOLUTION.USER_ID),
		                solutionsTasks.field(Tables.SOLUTION.TASK_ID),
		                solutionsTasks.field(Tables.SOLUTION.TESTED))
		        .from(Tables.CONTEST_PARTICIPATION)
		        .rightOuterJoin(solutionsTasks)
		        .on(Tables.CONTEST_PARTICIPATION.USER_ID
		                .eq((Field<Long>) solutionsTasks.field("user_id")))
		        .where(Tables.CONTEST_PARTICIPATION.CONTEST_ID.eq(contestF))
		        .asTable("users_tasks");
		return this.dslContext
		        .select(DSL
		                .decode()
		                .when(DSL.exists(this.dslContext.select(
		                        userTasks.field(Tables.SOLUTION.USER_ID)).from(
		                                userTasks)),
		                        false)
		                .otherwise(true))
		        .fetchOne()
		        .value1();
	}

	public Contest insertContest(Contest contest) {
		return this.insert(contest, Tables.CONTEST);
	}

	public boolean isContestInProgressForUser(final Contest contest,
	        final User user) {
		if (Instant.now().isBefore(contest.getStartTime().toInstant())) {
			return false;
		}
		return !this.getContestEndTimeForUser(contest, user).toInstant()
		        .isAfter(Instant.now());
	}

	public boolean isContestOverIncludingAllTimeExtensions(
	        final Contest contest) {
		return this.getContestEndIncludingAllTimeExtensions(contest).isBefore(
		        Instant.now());
	}

	public boolean isUserParticipatingIn(final User user,
	        final Contest contest) {
		return this.dslContext
		        .select(DSL
		                .decode()
		                .when(DSL
		                        .exists(this.dslContext
		                                .select(Tables.CONTEST_PARTICIPATION.ID)
		                                .from(Tables.CONTEST_PARTICIPATION)
		                                .where(Tables.CONTEST_PARTICIPATION.CONTEST_ID
		                                        .eq(contest.getId())
		                                        .and(Tables.CONTEST_PARTICIPATION.USER_ID
		                                                .eq(user.getId())))),
		                        true)
		                .otherwise(false))
		        .fetchOne().value1();
	}

	public void removeTaskFromContest(Task task, Contest contest) {
		this.dslContext.delete(Tables.CONTEST_TASKS).where(
		        Tables.CONTEST_TASKS.CONTEST_ID.eq(contest.getId())
		                .and(Tables.CONTEST_TASKS.TASK_ID.eq(task.getId())))
		        .execute();
	}

	@Transactional
	public void removeUserFromContest(Contest contest, User user) {
		this.dslContext
		        .delete(Tables.CONTEST_PARTICIPATION)
		        .where(Tables.CONTEST_PARTICIPATION.CONTEST_ID.eq(contest
		                .getId()),
		                Tables.CONTEST_PARTICIPATION.USER_ID.eq(user.getId()))
		        .execute();
	}

	public Contest updateContest(Contest contest) {
		return this.update(contest, Tables.CONTEST);
	}

	public boolean userKnowsAboutContest(User user, Contest contest,
	        ContestPermissionType knowAbout) {
		return Routines.hasContestPermission(this.dslContext.configuration(),
		        contest.getId(), user.getId(), ContestPermissionType.know_about)
		        .booleanValue()
		        || Routines
		                .hasContestPermission(this.dslContext.configuration(),
		                        contest.getId(), user.getId(),
		                        ContestPermissionType.manage_acl)
		                .booleanValue();
	}

	public boolean isUserInContest(Contest contest, User user) {
		return Routines.hasContestPermission(this.dslContext.configuration(),
		        contest.getId(), user.getId(),
		        ContestPermissionType.participate);
	}

}
