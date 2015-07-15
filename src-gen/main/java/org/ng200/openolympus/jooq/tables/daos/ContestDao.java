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
/**
 * This class is generated by jOOQ
 */
package org.ng200.openolympus.jooq.tables.daos;


import java.sql.Timestamp;
import java.time.Duration;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Configuration;
import org.jooq.impl.DAOImpl;
import org.ng200.openolympus.jooq.tables.Contest;
import org.ng200.openolympus.jooq.tables.records.ContestRecord;


/**
 * This class is generated by jOOQ.
 */
@Generated(
	value = {
		"http://www.jooq.org",
		"jOOQ version:3.6.2"
	},
	comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class ContestDao extends DAOImpl<ContestRecord, org.ng200.openolympus.jooq.tables.pojos.Contest, Integer> {

	/**
	 * Create a new ContestDao without any configuration
	 */
	public ContestDao() {
		super(Contest.CONTEST, org.ng200.openolympus.jooq.tables.pojos.Contest.class);
	}

	/**
	 * Create a new ContestDao with an attached configuration
	 */
	public ContestDao(Configuration configuration) {
		super(Contest.CONTEST, org.ng200.openolympus.jooq.tables.pojos.Contest.class, configuration);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Integer getId(org.ng200.openolympus.jooq.tables.pojos.Contest object) {
		return object.getId();
	}

	/**
	 * Fetch records that have <code>id IN (values)</code>
	 */
	public List<org.ng200.openolympus.jooq.tables.pojos.Contest> fetchById(Integer... values) {
		return fetch(Contest.CONTEST.ID, values);
	}

	/**
	 * Fetch a unique record that has <code>id = value</code>
	 */
	public org.ng200.openolympus.jooq.tables.pojos.Contest fetchOneById(Integer value) {
		return fetchOne(Contest.CONTEST.ID, value);
	}

	/**
	 * Fetch records that have <code>duration IN (values)</code>
	 */
	public List<org.ng200.openolympus.jooq.tables.pojos.Contest> fetchByDuration(Duration... values) {
		return fetch(Contest.CONTEST.DURATION, values);
	}

	/**
	 * Fetch records that have <code>name IN (values)</code>
	 */
	public List<org.ng200.openolympus.jooq.tables.pojos.Contest> fetchByName(String... values) {
		return fetch(Contest.CONTEST.NAME, values);
	}

	/**
	 * Fetch a unique record that has <code>name = value</code>
	 */
	public org.ng200.openolympus.jooq.tables.pojos.Contest fetchOneByName(String value) {
		return fetchOne(Contest.CONTEST.NAME, value);
	}

	/**
	 * Fetch records that have <code>show_full_tests_during_contest IN (values)</code>
	 */
	public List<org.ng200.openolympus.jooq.tables.pojos.Contest> fetchByShowFullTestsDuringContest(Boolean... values) {
		return fetch(Contest.CONTEST.SHOW_FULL_TESTS_DURING_CONTEST, values);
	}

	/**
	 * Fetch records that have <code>start_time IN (values)</code>
	 */
	public List<org.ng200.openolympus.jooq.tables.pojos.Contest> fetchByStartTime(Timestamp... values) {
		return fetch(Contest.CONTEST.START_TIME, values);
	}

	/**
	 * Fetch records that have <code>owner IN (values)</code>
	 */
	public List<org.ng200.openolympus.jooq.tables.pojos.Contest> fetchByOwner(Long... values) {
		return fetch(Contest.CONTEST.OWNER, values);
	}
	public org.ng200.openolympus.jooq.tables.pojos.Contest fetchOneById(String id) {
		return fetchOneById(java.lang.Integer.valueOf(id));
	}
}
