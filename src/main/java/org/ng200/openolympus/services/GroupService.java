package org.ng200.openolympus.services;

import java.util.List;

import org.jooq.DSLContext;
import org.ng200.openolympus.SecurityExpressionConstants;
import org.ng200.openolympus.jooq.Tables;
import org.ng200.openolympus.jooq.tables.daos.GroupDao;
import org.ng200.openolympus.jooq.tables.pojos.Group;
import org.ng200.openolympus.jooq.tables.pojos.GroupUsers;
import org.ng200.openolympus.jooq.tables.pojos.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class GroupService extends GenericCreateUpdateRepository {

	@Autowired
	private DSLContext dslContext;

	@Autowired
	private GroupDao groupDao;

	@PreAuthorize(SecurityExpressionConstants.IS_ADMIN)
	public List<Group> findAFewGroupsWithNameContaining(String name) {
		// TODO: use something better for searching...
		String pattern = "%" + name + "%";
		return dslContext
				.select(Tables.GROUP.fields())
				.from(Tables.GROUP)
				.where(Tables.GROUP.NAME.like(pattern)).limit(30)
				.fetchInto(Group.class);
	}

	@PreAuthorize(SecurityExpressionConstants.IS_ADMIN)
	public Group getGroupById(final Long id) {
		return groupDao.findById(id);
	}

	public Group getGroupByName(final String name) {
		return groupDao.fetchOneByName(name);
	}

	public Group insertGroup(Group group) {
		return insert(group, Tables.GROUP);
	}

	public Group updateGroup(Group group) {
		return update(group, Tables.GROUP);
	}

	public void addUserToGroup(User user, Group group,
			boolean canAddOthersToGroup) {
		insert(new GroupUsers(group.getId(), user.getId(), canAddOthersToGroup),
				Tables.GROUP_USERS);
	}

	public void removeUserFromGroup(User user, Group group) {
		dslContext.delete(Tables.GROUP_USERS)
				.where(Tables.GROUP_USERS.USER_ID.eq(user.getId())
						.and(Tables.GROUP_USERS.GROUP_ID.eq(group.getId())))
				.execute();
	}

}
