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


import java.util.List;

import javax.annotation.Generated;

import org.jooq.Configuration;
import org.jooq.Record3;
import org.jooq.impl.DAOImpl;
import org.ng200.openolympus.jooq.enums.ContestPermissionType;
import org.ng200.openolympus.jooq.tables.ContestPermission;
import org.ng200.openolympus.jooq.tables.records.ContestPermissionRecord;


/**
 * This class is generated by jOOQ.
 */
@Generated(
	value = {
		"http://www.jooq.org",
		"jOOQ version:3.7.3"
	},
	comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class ContestPermissionDao extends DAOImpl<ContestPermissionRecord, org.ng200.openolympus.jooq.tables.pojos.ContestPermission, Record3<ContestPermissionType, Integer, Long>> {

	/**
	 * Create a new ContestPermissionDao without any configuration
	 */
	public ContestPermissionDao() {
		super(ContestPermission.CONTEST_PERMISSION, org.ng200.openolympus.jooq.tables.pojos.ContestPermission.class);
	}

	/**
	 * Create a new ContestPermissionDao with an attached configuration
	 */
	public ContestPermissionDao(Configuration configuration) {
		super(ContestPermission.CONTEST_PERMISSION, org.ng200.openolympus.jooq.tables.pojos.ContestPermission.class, configuration);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Record3<ContestPermissionType, Integer, Long> getId(org.ng200.openolympus.jooq.tables.pojos.ContestPermission object) {
		return compositeKeyRecord(object.getPermission(), object.getContestId(), object.getPrincipalId());
	}

	/**
	 * Fetch records that have <code>permission IN (values)</code>
	 */
	public List<org.ng200.openolympus.jooq.tables.pojos.ContestPermission> fetchByPermission(ContestPermissionType... values) {
		return fetch(ContestPermission.CONTEST_PERMISSION.PERMISSION, values);
	}

	/**
	 * Fetch records that have <code>contest_id IN (values)</code>
	 */
	public List<org.ng200.openolympus.jooq.tables.pojos.ContestPermission> fetchByContestId(Integer... values) {
		return fetch(ContestPermission.CONTEST_PERMISSION.CONTEST_ID, values);
	}

	/**
	 * Fetch records that have <code>principal_id IN (values)</code>
	 */
	public List<org.ng200.openolympus.jooq.tables.pojos.ContestPermission> fetchByPrincipalId(Long... values) {
		return fetch(ContestPermission.CONTEST_PERMISSION.PRINCIPAL_ID, values);
	}
}
