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
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Table;
import org.jooq.conf.ParamType;
import org.jooq.impl.DSL;
import org.ng200.openolympus.dto.UserRanking;
import org.ng200.openolympus.jooq.Tables;
import org.ng200.openolympus.jooq.tables.daos.UserDao;
import org.ng200.openolympus.jooq.tables.pojos.User;
import org.ng200.openolympus.model.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.ImmutableList;

@Service
public class UserService extends GenericCreateUpdateRepository {

	@Autowired
	private UserDao userDao;

	@Autowired
	private DSLContext dslContext;

	public UserService() {

	}

	public long countUnapprovedUsers() {
		return this.dslContext.selectCount().from(Tables.USER)
				.where(Tables.USER.APPROVED.eq(false)).execute();
	}

	public long countUsers() {
		return this.userDao.count();
	}

	@Transactional
	public void deleteUser(final User user) {
		this.userDao.delete(user);

	}

	@Transactional
	public void deleteUsers(List<User> users) {
		this.userDao.delete(users);
	}

	public List<User> findAFewUsersWithNameContaining(final String name) {
		// TODO: use something better for searching...
		final String pattern = "%" + name + "%";
		return this.dslContext
				.select(Tables.USER.fields())
				.from(Tables.USER)
				.where(

		Tables.USER.USERNAME.like(pattern)
				.or(Tables.USER.FIRST_NAME_MAIN.like(pattern))
				.or(Tables.USER.MIDDLE_NAME_MAIN.like(pattern))
				.or(Tables.USER.LAST_NAME_MAIN.like(pattern))
				.or(Tables.USER.FIRST_NAME_LOCALISED.like(pattern))
				.or(Tables.USER.MIDDLE_NAME_LOCALISED.like(pattern))
				.or(Tables.USER.LAST_NAME_LOCALISED.like(pattern))

		).limit(30).fetchInto(User.class);
	}

	public List<UserRanking> getArchiveRankPage(final int page,
			final int pageSize) {

		final Table<?> userTasks = this.dslContext
				.select(Tables.SOLUTION.USER_ID, Tables.SOLUTION.TASK_ID,
						Tables.SOLUTION.SCORE)
				.distinctOn(Tables.SOLUTION.USER_ID, Tables.SOLUTION.TASK_ID)
				.from(Tables.SOLUTION)
				.groupBy(Tables.SOLUTION.USER_ID, Tables.SOLUTION.TASK_ID,
						Tables.SOLUTION.SCORE)
				.orderBy(Tables.SOLUTION.USER_ID.asc(),
						Tables.SOLUTION.TASK_ID.asc(),
						Tables.SOLUTION.SCORE.desc())
				.asTable("users_tasks");

		final Field<BigDecimal> user_score = DSL
				.coalesce(DSL.sum(userTasks.field(Tables.SOLUTION.SCORE)),
						DSL.field("0"))
				.as("user_score");
		final List<Field<?>> fields = ImmutableList
				.<Field<?>> builder()
				.add(user_score)
				.add(

		DSL.rank()
				.over(DSL.orderBy(DSL.coalesce(
						DSL.sum(userTasks.field(Tables.SOLUTION.SCORE)),
						DSL.field("0"))))
				.as("rank"))
				.add(Tables.USER.fields())

		.build();
		return this.dslContext
				.select(fields)
				.from(Tables.USER)
				.leftOuterJoin(userTasks)
				.on(Tables.USER.ID.eq(userTasks.field(Tables.SOLUTION.USER_ID)))
				.groupBy(Tables.USER.ID).orderBy(user_score).limit(pageSize)
				.offset(pageSize * (page - 1)).fetchInto(UserRanking.class);
	}

	public List<User> getPendingUsers(int pageNumber, int pageSize) {
		return this.dslContext.selectFrom(Tables.USER)
				.where(Tables.USER.APPROVED.eq(false))
				.limit(pageSize).offset((pageNumber - 1) * pageSize)
				.fetchInto(User.class);
	}

	public User getUserById(final Long id) {
		return this.userDao.findById(id);
	}

	public User getUserByUsername(final String username) {
		return this.userDao.fetchOneByUsername(username);
	}

	public List<User> getUsersAlphabetically(final Integer pageNumber,
			final int pageSize) {
		return this.dslContext.selectFrom(Tables.USER)
				.groupBy(Tables.USER.ID)
				.orderBy(Tables.USER.USERNAME).limit(pageSize)
				.offset(pageSize * (pageNumber - 1)).fetchInto(User.class);
	}

	@Transactional
	public User insertUser(User user) {
		return this.insert(user, Tables.USER);
	}

	@Transactional
	public User updateUser(User user) {
		return this.update(user, Tables.USER);
	}

	private static final Field<?>[] USER_PRINCIPAL_FIELDS = Stream
			.concat(Arrays.stream(Tables.USER.fields()),
					Stream.of(Tables.PRINCIPAL.PERMISSIONS))
			.toArray(Field<?>[]::new);

	public UserPrincipal getUserAsPrincipalByUsername(String name) {
		System.out.println(dslContext
				.select(USER_PRINCIPAL_FIELDS)
				.from(Tables.USER)
				.leftOuterJoin(Tables.PRINCIPAL)
				.on(Tables.USER.ID.eq(Tables.PRINCIPAL.ID))
				.where(Tables.USER.USERNAME.eq(name))
				.getSQL(ParamType.INLINED));
		return dslContext
				.select(USER_PRINCIPAL_FIELDS)
				.from(Tables.USER)
				.leftOuterJoin(Tables.PRINCIPAL)
				.on(Tables.USER.ID.eq(Tables.PRINCIPAL.ID))
				.where(Tables.USER.USERNAME.eq(name))
				.fetchOneInto(UserPrincipal.class);
	}
}
