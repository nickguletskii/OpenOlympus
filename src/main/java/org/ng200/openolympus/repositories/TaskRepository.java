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

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.jooq.Table;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.ng200.openolympus.annotations.QueryProvider;
import org.ng200.openolympus.jooq.Tables;
import org.ng200.openolympus.model.Task;
import org.ng200.openolympus.sqlSupport.SqlQueryProvider;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

	public static class TaskMaximumScoreQuery implements SqlQueryProvider {

		@Override
		public String getSql() {
			//@formatter:off
			final Table<?> table =
					DSL.select(Tables.SOLUTIONS.MAXIMUM_SCORE)
					.from(Tables.SOLUTIONS)
					.where(Tables.SOLUTIONS.TASK_ID.eq(DSL.param("taskID", 0l)))
					.asTable("solution_score_table");

			final String sql = SqlQueryProvider.DSL_CONTEXT.renderNamedParams(
					DSL.select(DSL.max(table.field(Tables.SOLUTIONS.MAXIMUM_SCORE))).from(table)
					);
			return sql;
			//@formatter:on
		}

	}

	public static class TaskScoreInContestQuery implements SqlQueryProvider {

		@Override
		public String getSql() {
			//@formatter:off
			final Table<?> table =
					DSL.select(Tables.SOLUTIONS.TIME_ADDED, Tables.SOLUTIONS.SCORE)
					.from(Tables.SOLUTIONS)
					.where(
							Tables.SOLUTIONS.TASK_ID.eq(DSL.param("taskID", 0l))
							.and(Tables.SOLUTIONS.USER_ID.eq(DSL.param("userID", 0l)))
							.and(Tables.SOLUTIONS.TIME_ADDED.ge(DSL.param("contestStart", SQLDataType.TIMESTAMP)))
							.and(Tables.SOLUTIONS.TIME_ADDED.le(DSL.param("contestEnd", SQLDataType.TIMESTAMP)))
							)
							.orderBy(Tables.SOLUTIONS.TIME_ADDED.desc())
							.asTable("solution_score_table");

			final String sql = SqlQueryProvider.DSL_CONTEXT.renderNamedParams(
					DSL.select(table.field(Tables.SOLUTIONS.SCORE)).from(table).limit(1)
					);
			return sql;
			//@formatter:on
		}

	}

	public static class TaskScoreQuery implements SqlQueryProvider {

		@Override
		public String getSql() {
			//@formatter:off
			final Table<?> table =
					DSL.select(Tables.SOLUTIONS.SCORE)
					.from(Tables.SOLUTIONS)
					.where(
							Tables.SOLUTIONS.TASK_ID.eq(DSL.param("taskID", 0l))
							.and(Tables.SOLUTIONS.USER_ID.eq(DSL.param("userID", 0l))))
							.groupBy(Tables.SOLUTIONS.ID)
							.asTable("solution_score_table");

			final String sql = SqlQueryProvider.DSL_CONTEXT.renderNamedParams(
					DSL.select(DSL.max(table.field(Tables.SOLUTIONS.SCORE))).from(table)
					);
			return sql;
			//@formatter:on
		}

	}

	public Task findByName(String name);

	public List<Task> findByNameContaining(String name, Pageable pageable);

	public Slice<Task> findByPublished(boolean published, Pageable pageable);

	@Query(nativeQuery = true)
	@QueryProvider(TaskMaximumScoreQuery.class)
	public List<BigDecimal> getTaskMaximumScore(@Param("taskID") Long taskID);

	@Query(nativeQuery = true)
	@QueryProvider(TaskScoreQuery.class)
	public List<BigDecimal> getTaskScore(@Param("taskID") Long taskID,
			@Param("userID") Long userID);

	@Query(nativeQuery = true)
	@QueryProvider(TaskScoreInContestQuery.class)
	public List<BigDecimal> getTaskScoreForContest(
			@Param("contestStart") Date contestStart,
			@Param("contestEnd") Date contestEnd, @Param("taskID") Long taskID,
			@Param("userID") Long userID);
}