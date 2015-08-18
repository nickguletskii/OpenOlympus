/**
 * This class is generated by jOOQ
 */
package org.ng200.openolympus.jooq.tables.pojos;


import javax.annotation.Generated;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.ng200.openolympus.jooq.enums.GroupPermissionType;
import org.ng200.openolympus.jooq.tables.interfaces.IGroupPermission;


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
@Table(name = "group_permission", schema = "public", uniqueConstraints = {
	@UniqueConstraint(columnNames = {"principal_id", "group_id", "permission"})
})
public class GroupPermission implements IGroupPermission {

	private static final long serialVersionUID = 1983941102;

	private Long                principalId;
	private Long                groupId;
	private GroupPermissionType permission;

	public GroupPermission() {}

	public GroupPermission(GroupPermission value) {
		this.principalId = value.principalId;
		this.groupId = value.groupId;
		this.permission = value.permission;
	}

	public GroupPermission(
		Long                principalId,
		Long                groupId,
		GroupPermissionType permission
	) {
		this.principalId = principalId;
		this.groupId = groupId;
		this.permission = permission;
	}

	@Column(name = "principal_id", nullable = false, precision = 64)
	@Override
	public Long getPrincipalId() {
		return this.principalId;
	}

	@Override
	public GroupPermission setPrincipalId(Long principalId) {
		this.principalId = principalId;
		return this;
	}

	@Column(name = "group_id", nullable = false, precision = 64)
	@Override
	public Long getGroupId() {
		return this.groupId;
	}

	@Override
	public GroupPermission setGroupId(Long groupId) {
		this.groupId = groupId;
		return this;
	}

	@Column(name = "permission", nullable = false)
	@Override
	public GroupPermissionType getPermission() {
		return this.permission;
	}

	@Override
	public GroupPermission setPermission(GroupPermissionType permission) {
		this.permission = permission;
		return this;
	}

	// -------------------------------------------------------------------------
	// FROM and INTO
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void from(IGroupPermission from) {
		setPrincipalId(from.getPrincipalId());
		setGroupId(from.getGroupId());
		setPermission(from.getPermission());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <E extends IGroupPermission> E into(E into) {
		into.from(this);
		return into;
	}
}
