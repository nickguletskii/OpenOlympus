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
		"jOOQ version:3.7.3"
	},
	comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
@Entity
@Table(name = "group_permission", schema = "public", uniqueConstraints = {
	@UniqueConstraint(columnNames = {"principal_id", "group_id", "permission"})
})
public class GroupPermission implements IGroupPermission {

	private static final long serialVersionUID = 2135228368;

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

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("GroupPermission (");

		sb.append(principalId);
		sb.append(", ").append(groupId);
		sb.append(", ").append(permission);

		sb.append(")");
		return sb.toString();
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
