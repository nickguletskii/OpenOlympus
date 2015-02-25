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
package org.ng200.openolympus.repositories;

import java.sql.Timestamp;
import java.util.Date;

import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.impl.DSL;
import org.jooq.types.DayToSecond;
import org.jooq.util.postgres.PostgresDataType;
import org.ng200.openolympus.annotations.QueryProvider;
import org.ng200.openolympus.jooq.Tables;
import org.ng200.openolympus.model.Contest;
import org.ng200.openolympus.repositories.UserRepository.ContestResultsQuery;
import org.ng200.openolympus.sqlSupport.SqlQueryProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository
public interface ContestRepository extends JpaRepository<Contest, Long> {

	@Component
	public class ContestTestingFinishedQuery extends ContestResultsQuery {

		@Override
		public String getSql() {
			//@formatter:off
			final Field<DayToSecond> timeExtensions = DSL.select(
					DSL.field("coalesce(sum(\"public\".\"time_extensions\".\"duration\"), 0) * INTERVAL '1 MILLISECOND'")
					)
					.from(Tables.TIME_EXTENSIONS)
					.where(Tables.TIME_EXTENSIONS.CONTEST_ID.eq(DSL.param("contest", 0l))
							.and(Tables.TIME_EXTENSIONS.USER_ID.eq(Tables.SOLUTIONS.USER_ID)))
							.asField().cast(PostgresDataType.INTERVALDAYTOSECOND);

			final Condition solutionWithinTimeBounds =
					Tables.SOLUTIONS.TIME_ADDED.between(DSL.param("contestStartTime", new Timestamp(0)),
							DSL.param("contestEndTime", new Timestamp(0)).add(
									timeExtensions
									)
							);

			final Table<Record> currentTasks = DSL.select()
					.from(Tables.CONTESTS_TASKS)
					.where(Tables.CONTESTS_TASKS.CONTESTS_ID.eq(DSL.param("contest", 0l)))
					.asTable("current_tasks");
			final Table<?> solutionsTasks = DSL.select(Tables.SOLUTIONS.USER_ID, Tables.SOLUTIONS.TASK_ID)
					.distinctOn(Tables.SOLUTIONS.USER_ID, Tables.SOLUTIONS.TASK_ID)
					.from(Tables.SOLUTIONS)
					.rightOuterJoin(currentTasks)
					.on("solutions.task_id = current_tasks.tasks_id")
					.where(solutionWithinTimeBounds
							.and("solutions.tested = FALSE"))
							.orderBy(Tables.SOLUTIONS.USER_ID.asc(), Tables.SOLUTIONS.TASK_ID.asc(), Tables.SOLUTIONS.TIME_ADDED.desc())
							.asTable("solutions_tasks");
			final Table<?> userTasks =
					DSL.select(
							solutionsTasks.field(Tables.SOLUTIONS.USER_ID),
							solutionsTasks.field(Tables.SOLUTIONS.TASK_ID),
							solutionsTasks.field(Tables.SOLUTIONS.TESTED))
							.from(Tables.CONTEST_PARTICIPATIONS)
							.rightOuterJoin(
									solutionsTasks
									).on("contest_participations.user_id = solutions_tasks.user_id")
									.where(Tables.CONTEST_PARTICIPATIONS.CONTEST_ID.eq(DSL.param("contest", 0l)))
									.asTable("users_tasks");

			return SqlQueryProvider.DSL_CONTEXT.renderNamedParams( DSL.select(DSL.decode().when(DSL.exists(
					DSL.select(userTasks.field(Tables.SOLUTIONS.USER_ID)).from(userTasks)
					), DSL.field("FALSE")).otherwise(DSL.field("TRUE"))));
			//@formatter:on
		}
	}

	public Contest findByName(String name);

	@Query("select c from Contest c where\n:startTime >= c.startTime and :startTime <= add_time(c.startTime, c.duration)\nor :endTime >= c.startTime and :endTime <= add_time(c.startTime, c.duration)\nor c.startTime >= :startTime and c.startTime <= :endTime\nor add_time(c.startTime, c.duration) >= :startTime and add_time(c.startTime, c.duration) <= :endTime ")
	public Contest findIntersects(@Param("startTime") Date start,
			@Param("endTime") Date end);

	@Query(nativeQuery = true)
	@QueryProvider(value = ContestTestingFinishedQuery.class)
	boolean hasContestTestingFinished(@Param("contest") Long contest,
			@Param("contestStartTime") Date contestStartTime,
			@Param("contestEndTime") Date contestEndTime);
}
