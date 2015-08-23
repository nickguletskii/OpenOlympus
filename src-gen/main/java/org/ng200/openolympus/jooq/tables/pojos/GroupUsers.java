/**
 * This class is generated by jOOQ
 */
package org.ng200.openolympus.jooq.tables.pojos;


import javax.annotation.Generated;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.ng200.openolympus.jooq.tables.interfaces.IGroupUsers;


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
@Entity
@Table(name = "group_users", schema = "public", uniqueConstraints = {
	@UniqueConstraint(columnNames = {"group_id", "user_id"})
})
public class GroupUsers implements IGroupUsers {

	private static final long serialVersionUID = -1239034282;

	private Boolean canAddOthersToGroup;
	private Long    groupId;
	private Long    userId;

	public GroupUsers() {}

	public GroupUsers(GroupUsers value) {
		this.canAddOthersToGroup = value.canAddOthersToGroup;
		this.groupId = value.groupId;
		this.userId = value.userId;
	}

	public GroupUsers(
		Boolean canAddOthersToGroup,
		Long    groupId,
		Long    userId
	) {
		this.canAddOthersToGroup = canAddOthersToGroup;
		this.groupId = groupId;
		this.userId = userId;
	}

	@Column(name = "can_add_others_to_group", nullable = false)
	@Override
	public Boolean getCanAddOthersToGroup() {
		return this.canAddOthersToGroup;
	}

	@Override
	public GroupUsers setCanAddOthersToGroup(Boolean canAddOthersToGroup) {
		this.canAddOthersToGroup = canAddOthersToGroup;
		return this;
	}

	@Column(name = "group_id", nullable = false, precision = 64)
	@Override
	public Long getGroupId() {
		return this.groupId;
	}

	@Override
	public GroupUsers setGroupId(Long groupId) {
		this.groupId = groupId;
		return this;
	}

	@Column(name = "user_id", nullable = false, precision = 64)
	@Override
	public Long getUserId() {
		return this.userId;
	}

	@Override
	public GroupUsers setUserId(Long userId) {
		this.userId = userId;
		return this;
	}

	// -------------------------------------------------------------------------
	// FROM and INTO
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void from(IGroupUsers from) {
		setCanAddOthersToGroup(from.getCanAddOthersToGroup());
		setGroupId(from.getGroupId());
		setUserId(from.getUserId());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <E extends IGroupUsers> E into(E into) {
		into.from(this);
		return into;
	}
}
