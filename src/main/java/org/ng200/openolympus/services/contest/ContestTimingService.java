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

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;

import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.ng200.openolympus.jooq.Routines;
import org.ng200.openolympus.jooq.Tables;
import org.ng200.openolympus.jooq.routines.GetContestEnd;
import org.ng200.openolympus.jooq.routines.GetContestEndForUser;
import org.ng200.openolympus.jooq.routines.GetContestStart;
import org.ng200.openolympus.jooq.routines.GetContestStartForUser;
import org.ng200.openolympus.jooq.tables.daos.TimeExtensionDao;
import org.ng200.openolympus.jooq.tables.pojos.Contest;
import org.ng200.openolympus.jooq.tables.pojos.TimeExtension;
import org.ng200.openolympus.jooq.tables.pojos.User;
import org.ng200.openolympus.jooqsupport.PostgresSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ContestTimingService {

	@Autowired
	private TimeExtensionDao timeExtensionDao;

	@Autowired
	private DSLContext dslContext;

	@Transactional
	public void extendTimeForUser(final Contest contest, final User user,
			final Duration time) {
		this.timeExtensionDao.insert(new TimeExtension(null, time, null, user
				.getId(), contest.getId()));
	}

	public OffsetDateTime getContestEndIncludingAllTimeExtensions(
			final Contest contest) {
		final GetContestEnd procedure = new GetContestEnd();
		procedure.setContestId(contest.getId());
		procedure.attach(this.dslContext.configuration());
		procedure.execute();
		return procedure.getReturnValue();
	}

	public OffsetDateTime getContestEndTimeForUser(Contest contest, User user) {
		final GetContestEndForUser procedure = new GetContestEndForUser();
		procedure.setContestId(contest.getId());
		procedure.setUserId(user.getId());
		procedure.attach(this.dslContext.configuration());
		procedure.execute();
		return procedure.getReturnValue();
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

	public OffsetDateTime getContestStartIncludingAllTimeExtensions(
			final Contest contest) {
		final GetContestStart procedure = new GetContestStart();
		procedure.setContestId(contest.getId());
		procedure.attach(this.dslContext.configuration());
		procedure.execute();
		return procedure.getReturnValue();
	}

	public OffsetDateTime getContestStartTimeForUser(Contest contest,
			User user) {
		final GetContestStartForUser procedure = new GetContestStartForUser();
		procedure.setContestId(contest.getId());
		procedure.setUserId(user.getId());
		procedure.attach(this.dslContext.configuration());
		procedure.execute();
		return procedure.getReturnValue();
	}

	public List<Contest> getContestsThatIntersect(
			final OffsetDateTime startDate, final OffsetDateTime endDate) {

		return this.dslContext
				.selectFrom(Routines.getContestsThatIntersect(
						PostgresSupport.CURRENT_TIMESTAMP,
						PostgresSupport.CURRENT_TIMESTAMP))
				.fetchInto(Contest.class);
	}

	public Contest getRunningContest() {
		return this.dslContext
				.selectFrom(Routines.getContestsThatIntersect(
						PostgresSupport.CURRENT_TIMESTAMP,
						PostgresSupport.CURRENT_TIMESTAMP))
				.fetchOneInto(Contest.class);
	}

	public boolean isContestInProgressForUser(final Contest contest,
			final User user) {
		return this.dslContext.select(DSL.field(
				PostgresSupport.CURRENT_TIMESTAMP.between(
						Routines.getContestStartForUser(
								contest.getId(), user.getId()),
						Routines.getContestEndForUser(
								contest.getId(), user.getId()))))
				.fetchOne().value1();
	}

	public boolean isContestOverIncludingAllTimeExtensions(
			final Contest contest) {
		return this.dslContext.select(DSL.field(
				PostgresSupport.CURRENT_TIMESTAMP.greaterThan(
						Routines.getContestEnd(
								contest.getId()))))
				.fetchOne().value1();
	}

}