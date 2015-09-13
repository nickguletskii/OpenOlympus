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

import java.util.List;

import org.jooq.DSLContext;
import org.ng200.openolympus.jooq.Tables;
import org.ng200.openolympus.jooq.tables.daos.GroupDao;
import org.ng200.openolympus.jooq.tables.pojos.Group;
import org.ng200.openolympus.jooq.tables.pojos.GroupUsers;
import org.ng200.openolympus.jooq.tables.pojos.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GroupService extends GenericCreateUpdateRepository {

	@Autowired
	private DSLContext dslContext;

	@Autowired
	private GroupDao groupDao;

	@Transactional
	public void addUserToGroup(User user, Group group,
	        boolean canAddOthersToGroup) {
		this.insert(new GroupUsers(canAddOthersToGroup, group.getId(),
		        user.getId()),
		        Tables.GROUP_USERS);
	}

	public int countGroups() {
		return this.dslContext.selectCount().from(Tables.GROUP).execute();
	}

	public int countParticipants(Group group) {
		return this.dslContext.selectCount()
		        .from(Tables.GROUP_USERS)
		        .where(Tables.GROUP_USERS.USER_ID.eq(group.getId()))
		        .execute();
	}

	public List<Group> findAFewGroupsWithNameContaining(String name) {
		// TODO: use something better for searching...
		final String pattern = "%" + name + "%";
		return this.dslContext
		        .select(Tables.GROUP.fields())
		        .from(Tables.GROUP)
		        .where(Tables.GROUP.NAME.like(pattern)).limit(30)
		        .fetchInto(Group.class);
	}

	public Group getGroupById(final Long id) {
		return this.groupDao.findById(id);
	}

	public Group getGroupByName(final String name) {
		return this.groupDao.fetchOneByName(name);
	}

	public List<Group> getGroups(Integer pageNumber, int pageSize) {
		return this.dslContext.selectFrom(Tables.GROUP)
		        .groupBy(Tables.GROUP.ID)
		        .orderBy(Tables.GROUP.NAME)
		        .limit(pageSize)
		        .offset((pageNumber - 1) * pageSize)
		        .fetchInto(Group.class);
	}

	public List<User> getParticipants(Group group, Integer pageNumber,
	        int pageSize) {
		return this.dslContext.select(Tables.USER.fields())
		        .from(Tables.GROUP_USERS)
		        .join(Tables.USER)
		        .on(Tables.GROUP_USERS.USER_ID.eq(Tables.USER.ID))
		        .where(Tables.GROUP_USERS.GROUP_ID.eq(group.getId()))
		        .groupBy(Tables.USER.ID)
		        .orderBy(Tables.USER.USERNAME)
		        .limit(pageSize)
		        .offset((pageNumber - 1) * pageSize)
		        .fetchInto(User.class);
	}

	@Transactional
	public Group insertGroup(Group group) {
		return this.insert(group, Tables.GROUP);
	}

	@Transactional
	public void removeUserFromGroup(User user, Group group) {
		this.dslContext.delete(Tables.GROUP_USERS)
		        .where(Tables.GROUP_USERS.USER_ID.eq(user.getId())
		                .and(Tables.GROUP_USERS.GROUP_ID.eq(group.getId())))
		        .execute();
	}

	@Transactional
	public Group updateGroup(Group group) {
		return this.update(group, Tables.GROUP);
	}

}
