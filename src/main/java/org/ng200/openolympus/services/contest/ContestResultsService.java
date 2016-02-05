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
package org.ng200.openolympus.services.contest;

import java.math.BigDecimal;
import java.util.List;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Param;
import org.jooq.Record;
import org.jooq.SelectConditionStep;
import org.jooq.SelectField;
import org.jooq.Table;
import org.jooq.impl.DSL;
import org.jooq.util.postgres.PostgresDataType;
import org.ng200.openolympus.dto.UserRanking;
import org.ng200.openolympus.jooq.Routines;
import org.ng200.openolympus.jooq.Tables;
import org.ng200.openolympus.jooq.tables.pojos.Contest;
import org.ng200.openolympus.jooq.tables.pojos.Task;
import org.ng200.openolympus.jooq.tables.pojos.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.ImmutableList;

@Service
public class ContestResultsService {

	private static final SelectField<?>[] CONTEST_RANK_SELECT_FIELDS = ImmutableList
			.<SelectField<?>> builder()
			.add(
					DSL.coalesce(Tables.CONTEST_PARTICIPATION.SCORE,
							DSL.field("0")).as("score"))
			.add(
					DSL.rank()
							.over(DSL.orderBy(DSL.coalesce(
									Tables.CONTEST_PARTICIPATION.SCORE,
									DSL.field("0")).desc()))
							.as("rank"))
			.add(
					Tables.USER.fields())
			.build().toArray(new SelectField<?>[0]);

	@Autowired
	private DSLContext dslContext;

	public List<UserRanking> getContestResults(Contest contest) {
		return this.getContestResultsQuery(contest)
				.fetchInto(UserRanking.class);
	}

	public List<UserRanking> getContestResultsPage(Contest contest, int page,
			int pageSize) {
		return this.getContestResultsQuery(contest).limit(pageSize)
				.offset((page - 1) * pageSize).fetchInto(UserRanking.class);
	}

	private SelectConditionStep<Record> getContestResultsQuery(
			Contest contest) {
		return this.dslContext
				.select(ContestResultsService.CONTEST_RANK_SELECT_FIELDS)
				.from(Tables.CONTEST_PARTICIPATION)
				.rightOuterJoin(Tables.USER)
				.on(Tables.CONTEST_PARTICIPATION.USER_ID.eq(Tables.USER.ID))
				.where(Tables.CONTEST_PARTICIPATION.CONTEST_ID.eq(contest
						.getId()));
	}

	public BigDecimal getUserTaskScoreInContest(final Contest contest,
			final User user,
			final Task task) {
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

}