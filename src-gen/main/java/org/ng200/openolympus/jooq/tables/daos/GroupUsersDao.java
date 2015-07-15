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
import org.jooq.Record2;
import org.jooq.impl.DAOImpl;
import org.ng200.openolympus.jooq.tables.GroupUsers;
import org.ng200.openolympus.jooq.tables.records.GroupUsersRecord;


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
public class GroupUsersDao extends DAOImpl<GroupUsersRecord, org.ng200.openolympus.jooq.tables.pojos.GroupUsers, Record2<Long, Long>> {

	/**
	 * Create a new GroupUsersDao without any configuration
	 */
	public GroupUsersDao() {
		super(GroupUsers.GROUP_USERS, org.ng200.openolympus.jooq.tables.pojos.GroupUsers.class);
	}

	/**
	 * Create a new GroupUsersDao with an attached configuration
	 */
	public GroupUsersDao(Configuration configuration) {
		super(GroupUsers.GROUP_USERS, org.ng200.openolympus.jooq.tables.pojos.GroupUsers.class, configuration);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Record2<Long, Long> getId(org.ng200.openolympus.jooq.tables.pojos.GroupUsers object) {
		return compositeKeyRecord(object.getGroupId(), object.getUserId());
	}

	/**
	 * Fetch records that have <code>group_id IN (values)</code>
	 */
	public List<org.ng200.openolympus.jooq.tables.pojos.GroupUsers> fetchByGroupId(Long... values) {
		return fetch(GroupUsers.GROUP_USERS.GROUP_ID, values);
	}

	/**
	 * Fetch records that have <code>USER_id IN (values)</code>
	 */
	public List<org.ng200.openolympus.jooq.tables.pojos.GroupUsers> fetchByUserId(Long... values) {
		return fetch(GroupUsers.GROUP_USERS.USER_ID, values);
	}

	/**
	 * Fetch records that have <code>can_add_others_to_group IN (values)</code>
	 */
	public List<org.ng200.openolympus.jooq.tables.pojos.GroupUsers> fetchByCanAddOthersToGroup(Boolean... values) {
		return fetch(GroupUsers.GROUP_USERS.CAN_ADD_OTHERS_TO_GROUP, values);
	}
}
