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
package org.ng200.openolympus.services.contest;

import java.util.List;

import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.ng200.openolympus.jooq.Routines;
import org.ng200.openolympus.jooq.Tables;
import org.ng200.openolympus.jooq.enums.ContestPermissionType;
import org.ng200.openolympus.jooq.tables.daos.ContestParticipationDao;
import org.ng200.openolympus.jooq.tables.pojos.Contest;
import org.ng200.openolympus.jooq.tables.pojos.ContestParticipation;
import org.ng200.openolympus.jooq.tables.pojos.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ContestUsersService {

	@Autowired
	private ContestParticipationDao contestParticipationDao;

	@Autowired
	private DSLContext dslContext;

	@Transactional
	public void addContestParticipant(final Contest contest, final User user) {
		this.contestParticipationDao.insert(new ContestParticipation(null, null,
				user.getId(), contest.getId()));
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

	public boolean isUserInContest(Contest contest, User user) {
		return Routines.hasContestPermission(this.dslContext.configuration(),
				contest.getId(), user.getId(),
				ContestPermissionType.participate);
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

	@Transactional
	public void removeUserFromContest(Contest contest, User user) {
		this.dslContext
				.delete(Tables.CONTEST_PARTICIPATION)
				.where(Tables.CONTEST_PARTICIPATION.CONTEST_ID.eq(contest
						.getId()),
						Tables.CONTEST_PARTICIPATION.USER_ID.eq(user.getId()))
				.execute();
	}

	public boolean userKnowsAboutContest(User user, Contest contest) {
		return Routines.hasContestPermission(this.dslContext.configuration(),
				contest.getId(), user.getId(), ContestPermissionType.know_about)
				.booleanValue()
				|| Routines
						.hasContestPermission(this.dslContext.configuration(),
								contest.getId(), user.getId(),
								ContestPermissionType.manage_acl)
						.booleanValue();
	}

}
