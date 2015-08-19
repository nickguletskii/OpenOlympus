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

import java.util.List;

import javax.annotation.Generated;

import org.jooq.Configuration;
import org.jooq.Record3;
import org.jooq.impl.DAOImpl;
import org.ng200.openolympus.jooq.enums.GroupPermissionType;
import org.ng200.openolympus.jooq.tables.GroupPermission;
import org.ng200.openolympus.jooq.tables.records.GroupPermissionRecord;

/**
 * This class is generated by jOOQ.
 */
@Generated(value = {
						"http://www.jooq.org",
						"jOOQ version:3.6.2"
}, comments = "This class is generated by jOOQ")
@SuppressWarnings({
					"all",
					"unchecked",
					"rawtypes"
})
public class GroupPermissionDao extends
		DAOImpl<GroupPermissionRecord, org.ng200.openolympus.jooq.tables.pojos.GroupPermission, Record3<Long, Long, GroupPermissionType>> {

	/**
	 * Create a new GroupPermissionDao without any configuration
	 */
	public GroupPermissionDao() {
		super(GroupPermission.GROUP_PERMISSION,
				org.ng200.openolympus.jooq.tables.pojos.GroupPermission.class);
	}

	/**
	 * Create a new GroupPermissionDao with an attached configuration
	 */
	public GroupPermissionDao(Configuration configuration) {
		super(GroupPermission.GROUP_PERMISSION,
				org.ng200.openolympus.jooq.tables.pojos.GroupPermission.class,
				configuration);
	}

	/**
	 * Fetch records that have <code>group_id IN (values)</code>
	 */
	public List<org.ng200.openolympus.jooq.tables.pojos.GroupPermission> fetchByGroupId(
			Long... values) {
		return this.fetch(GroupPermission.GROUP_PERMISSION.GROUP_ID, values);
	}

	/**
	 * Fetch records that have <code>permission IN (values)</code>
	 */
	public List<org.ng200.openolympus.jooq.tables.pojos.GroupPermission> fetchByPermission(
			GroupPermissionType... values) {
		return this.fetch(GroupPermission.GROUP_PERMISSION.PERMISSION, values);
	}

	/**
	 * Fetch records that have <code>principal_id IN (values)</code>
	 */
	public List<org.ng200.openolympus.jooq.tables.pojos.GroupPermission> fetchByPrincipalId(
			Long... values) {
		return this.fetch(GroupPermission.GROUP_PERMISSION.PRINCIPAL_ID,
				values);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Record3<Long, Long, GroupPermissionType> getId(
			org.ng200.openolympus.jooq.tables.pojos.GroupPermission object) {
		return this.compositeKeyRecord(object.getPrincipalId(),
				object.getGroupId(), object.getPermission());
	}
}
