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
/**
 * This class is generated by jOOQ
 */
package org.ng200.openolympus.jooq.tables.daos;


import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Configuration;
import org.jooq.impl.DAOImpl;
import org.ng200.openolympus.jooq.tables.ContestParticipation;
import org.ng200.openolympus.jooq.tables.records.ContestParticipationRecord;


/**
 * This class is generated by jOOQ.
 */
@Generated(
	value = {
		"http://www.jooq.org",
		"jOOQ version:3.7.2"
	},
	comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class ContestParticipationDao extends DAOImpl<ContestParticipationRecord, org.ng200.openolympus.jooq.tables.pojos.ContestParticipation, Long> {

	/**
	 * Create a new ContestParticipationDao without any configuration
	 */
	public ContestParticipationDao() {
		super(ContestParticipation.CONTEST_PARTICIPATION, org.ng200.openolympus.jooq.tables.pojos.ContestParticipation.class);
	}

	/**
	 * Create a new ContestParticipationDao with an attached configuration
	 */
	public ContestParticipationDao(Configuration configuration) {
		super(ContestParticipation.CONTEST_PARTICIPATION, org.ng200.openolympus.jooq.tables.pojos.ContestParticipation.class, configuration);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Long getId(org.ng200.openolympus.jooq.tables.pojos.ContestParticipation object) {
		return object.getId();
	}

	/**
	 * Fetch records that have <code>id IN (values)</code>
	 */
	public List<org.ng200.openolympus.jooq.tables.pojos.ContestParticipation> fetchById(Long... values) {
		return fetch(ContestParticipation.CONTEST_PARTICIPATION.ID, values);
	}

	/**
	 * Fetch a unique record that has <code>id = value</code>
	 */
	public org.ng200.openolympus.jooq.tables.pojos.ContestParticipation fetchOneById(Long value) {
		return fetchOne(ContestParticipation.CONTEST_PARTICIPATION.ID, value);
	}

	/**
	 * Fetch records that have <code>score IN (values)</code>
	 */
	public List<org.ng200.openolympus.jooq.tables.pojos.ContestParticipation> fetchByScore(BigDecimal... values) {
		return fetch(ContestParticipation.CONTEST_PARTICIPATION.SCORE, values);
	}

	/**
	 * Fetch records that have <code>user_id IN (values)</code>
	 */
	public List<org.ng200.openolympus.jooq.tables.pojos.ContestParticipation> fetchByUserId(Long... values) {
		return fetch(ContestParticipation.CONTEST_PARTICIPATION.USER_ID, values);
	}

	/**
	 * Fetch records that have <code>contest_id IN (values)</code>
	 */
	public List<org.ng200.openolympus.jooq.tables.pojos.ContestParticipation> fetchByContestId(Integer... values) {
		return fetch(ContestParticipation.CONTEST_PARTICIPATION.CONTEST_ID, values);
	}

	/**
	 * Fetch records that have <code>time_extension IN (values)</code>
	 */
	public List<org.ng200.openolympus.jooq.tables.pojos.ContestParticipation> fetchByTimeExtension(Duration... values) {
		return fetch(ContestParticipation.CONTEST_PARTICIPATION.TIME_EXTENSION, values);
	}
	public org.ng200.openolympus.jooq.tables.pojos.ContestParticipation fetchOneById(String id) {
		return fetchOneById(java.lang.Long.valueOf(id));
	}
}
