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
import java.util.List;

import org.jooq.Field;
import org.jooq.Record3;
import org.jooq.SelectConditionStep;
import org.jooq.Table;
import org.jooq.impl.DSL;
import org.ng200.openolympus.annotations.QueryProvider;
import org.ng200.openolympus.jooq.Tables;
import org.ng200.openolympus.model.Contest;
import org.ng200.openolympus.model.User;
import org.ng200.openolympus.sqlSupport.SqlQueryProvider;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	@Component
	public class ContestResultsPageQuery extends ContestResultsQuery {

		@Override
		public String getSql() {
			final SelectConditionStep<Record3<Long, BigDecimal, Integer>> query = this
					.getUnlimitedQuery();
			return SqlQueryProvider.DSL_CONTEXT.renderNamedParams(query.limit(
					DSL.param("offset", 0), DSL.param("limit", 10)));

		}

	}

	@Component
	public class ContestResultsQuery implements SqlQueryProvider {

		@Override
		public String getSql() {
			final SelectConditionStep<Record3<Long, BigDecimal, Integer>> query = this
					.getUnlimitedQuery();
			return SqlQueryProvider.DSL_CONTEXT.renderNamedParams(query);
		}

		protected SelectConditionStep<Record3<Long, BigDecimal, Integer>> getUnlimitedQuery() {
			//@formatter:off

			final SelectConditionStep<Record3<Long, BigDecimal, Integer>> query =

					DSL.select(
							Tables.CONTEST_PARTICIPATIONS.USER_ID,
							DSL.coalesce(Tables.CONTEST_PARTICIPATIONS.SCORE, DSL.field("0")),
							DSL.rank().over(DSL.orderBy(DSL.coalesce(Tables.CONTEST_PARTICIPATIONS.SCORE, DSL.field("0")).desc())))
							.from(Tables.CONTEST_PARTICIPATIONS)
							.where(Tables.CONTEST_PARTICIPATIONS.CONTEST_ID.eq(DSL.param("contest", 0l)));
			//@formatter:on
			return query;
		}
	}

	@Component
	public class RankPageQuery implements SqlQueryProvider {

		@Override
		public String getSql() {
			//@formatter:off
			final Table<?> userTasks =
					DSL.select(Tables.SOLUTIONS.USER_ID, Tables.SOLUTIONS.TASK_ID, Tables.SOLUTIONS.SCORE)
					.distinctOn(Tables.SOLUTIONS.USER_ID, Tables.SOLUTIONS.TASK_ID)
					.from(Tables.SOLUTIONS)
					.orderBy(Tables.SOLUTIONS.USER_ID.asc(), Tables.SOLUTIONS.TASK_ID.asc(), Tables.SOLUTIONS.SCORE.desc())
					.asTable("users_tasks");

			final Field<BigDecimal> user_score =
					DSL.coalesce(
							DSL.sum(userTasks.field(Tables.SOLUTIONS.SCORE)),
							DSL.field("0")
							).as("user_score");
			final String sql = SqlQueryProvider.DSL_CONTEXT.renderNamedParams(
					DSL.select(
							Tables.USERS.ID,
							user_score,
							DSL.rank().over(DSL.orderBy(DSL.coalesce(
									DSL.sum(userTasks.field(Tables.SOLUTIONS.SCORE)),
									DSL.field("0")
									)))
							)
							.from(Tables.USERS)
							.leftOuterJoin(userTasks)
							.on(Tables.USERS.ID.eq(userTasks.field(Tables.SOLUTIONS.USER_ID)))
							.groupBy(Tables.USERS.ID)
							.orderBy(user_score).limit(DSL.param("offset", 0),DSL.param("limit", 10)));

			return sql;
			//@formatter:on

		}
	}

	@Query("select count(u) from User u left outer join u.roles as role where role is null and u.approvalEmailSent = false")
	long countUnapproved();

	List<User> findByLastNameMain(String lastNameMain);

	List<User> findBySchool(String school);

	User findByUsername(String username);

	@Query(nativeQuery = true, value = "select * from users where username ilike :part or last_name_main ilike :part or last_name_localised ilike :part")
	List<User> findFirst30Like(@Param("part") String name);

	@Query("select c.user from ContestParticipation c where c.contest=:contest")
	List<User> findPartiticpants(@Param("contest") Contest contest,
			Pageable pageable);

	@Query("select u from User u left outer join u.roles as role where role is null and u.approvalEmailSent = false")
	List<User> findUnapproved(Pageable pageable);

	@Query(nativeQuery = true)
	@QueryProvider(value = ContestResultsQuery.class)
	List<Object[]> getContestResults(@Param("contest") Long contest);

	@Query(nativeQuery = true)
	@QueryProvider(value = ContestResultsPageQuery.class)
	List<Object[]> getContestResultsPage(@Param("contest") Long contest,
			@Param("limit") int limit, @Param("offset") int offset);

	@Query(nativeQuery = true)
	@QueryProvider(value = RankPageQuery.class)
	List<Object[]> getRankPage(@Param("limit") Long limit,
			@Param("offset") Long offset);

}