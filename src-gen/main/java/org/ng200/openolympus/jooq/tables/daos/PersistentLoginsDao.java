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


import java.time.OffsetDateTime;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Configuration;
import org.jooq.impl.DAOImpl;
import org.ng200.openolympus.jooq.tables.PersistentLogins;
import org.ng200.openolympus.jooq.tables.records.PersistentLoginsRecord;


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
public class PersistentLoginsDao extends DAOImpl<PersistentLoginsRecord, org.ng200.openolympus.jooq.tables.pojos.PersistentLogins, String> {

	/**
	 * Create a new PersistentLoginsDao without any configuration
	 */
	public PersistentLoginsDao() {
		super(PersistentLogins.PERSISTENT_LOGINS, org.ng200.openolympus.jooq.tables.pojos.PersistentLogins.class);
	}

	/**
	 * Create a new PersistentLoginsDao with an attached configuration
	 */
	public PersistentLoginsDao(Configuration configuration) {
		super(PersistentLogins.PERSISTENT_LOGINS, org.ng200.openolympus.jooq.tables.pojos.PersistentLogins.class, configuration);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getId(org.ng200.openolympus.jooq.tables.pojos.PersistentLogins object) {
		return object.getSeries();
	}

	/**
	 * Fetch records that have <code>username IN (values)</code>
	 */
	public List<org.ng200.openolympus.jooq.tables.pojos.PersistentLogins> fetchByUsername(String... values) {
		return fetch(PersistentLogins.PERSISTENT_LOGINS.USERNAME, values);
	}

	/**
	 * Fetch records that have <code>series IN (values)</code>
	 */
	public List<org.ng200.openolympus.jooq.tables.pojos.PersistentLogins> fetchBySeries(String... values) {
		return fetch(PersistentLogins.PERSISTENT_LOGINS.SERIES, values);
	}

	/**
	 * Fetch a unique record that has <code>series = value</code>
	 */
	public org.ng200.openolympus.jooq.tables.pojos.PersistentLogins fetchOneBySeries(String value) {
		return fetchOne(PersistentLogins.PERSISTENT_LOGINS.SERIES, value);
	}

	/**
	 * Fetch records that have <code>token IN (values)</code>
	 */
	public List<org.ng200.openolympus.jooq.tables.pojos.PersistentLogins> fetchByToken(String... values) {
		return fetch(PersistentLogins.PERSISTENT_LOGINS.TOKEN, values);
	}

	/**
	 * Fetch records that have <code>last_used IN (values)</code>
	 */
	public List<org.ng200.openolympus.jooq.tables.pojos.PersistentLogins> fetchByLastUsed(OffsetDateTime... values) {
		return fetch(PersistentLogins.PERSISTENT_LOGINS.LAST_USED, values);
	}
}
