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
package org.ng200.openolympus.jooq.tables.records;

import javax.annotation.Generated;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.jooq.Field;
import org.jooq.Record3;
import org.jooq.Row3;
import org.jooq.impl.UpdatableRecordImpl;
import org.ng200.openolympus.jooq.enums.GroupPermissionType;
import org.ng200.openolympus.jooq.tables.GroupPermission;
import org.ng200.openolympus.jooq.tables.interfaces.IGroupPermission;

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
@Entity
@Table(name = "group_permission", schema = "public", uniqueConstraints = {
																			@UniqueConstraint(columnNames = {
																												"principal_id",
																												"group_id",
																												"permission"
		})
})
public class GroupPermissionRecord
		extends UpdatableRecordImpl<GroupPermissionRecord>
		implements Record3<Long, Long, GroupPermissionType>, IGroupPermission {

	private static final long serialVersionUID = -1000165251;

	/**
	 * Create a detached GroupPermissionRecord
	 */
	public GroupPermissionRecord() {
		super(GroupPermission.GROUP_PERMISSION);
	}

	/**
	 * Create a detached, initialised GroupPermissionRecord
	 */
	public GroupPermissionRecord(Long principalId, Long groupId,
			GroupPermissionType permission) {
		super(GroupPermission.GROUP_PERMISSION);

		this.setValue(0, principalId);
		this.setValue(1, groupId);
		this.setValue(2, permission);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Long> field1() {
		return GroupPermission.GROUP_PERMISSION.PRINCIPAL_ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Long> field2() {
		return GroupPermission.GROUP_PERMISSION.GROUP_ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<GroupPermissionType> field3() {
		return GroupPermission.GROUP_PERMISSION.PERMISSION;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Row3<Long, Long, GroupPermissionType> fieldsRow() {
		return (Row3) super.fieldsRow();
	}

	// -------------------------------------------------------------------------
	// Primary key information
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void from(IGroupPermission from) {
		this.setPrincipalId(from.getPrincipalId());
		this.setGroupId(from.getGroupId());
		this.setPermission(from.getPermission());
	}

	// -------------------------------------------------------------------------
	// Record3 type implementation
	// -------------------------------------------------------------------------

	/**
	 * Getter for <code>public.group_permission.group_id</code>.
	 */
	@Column(name = "group_id", nullable = false, precision = 64)
	@Override
	public Long getGroupId() {
		return (Long) this.getValue(1);
	}

	/**
	 * Getter for <code>public.group_permission.permission</code>.
	 */
	@Column(name = "permission", nullable = false)
	@Override
	public GroupPermissionType getPermission() {
		return (GroupPermissionType) this.getValue(2);
	}

	/**
	 * Getter for <code>public.group_permission.principal_id</code>.
	 */
	@Column(name = "principal_id", nullable = false, precision = 64)
	@Override
	public Long getPrincipalId() {
		return (Long) this.getValue(0);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <E extends IGroupPermission> E into(E into) {
		into.from(this);
		return into;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Record3<Long, Long, GroupPermissionType> key() {
		return (Record3) super.key();
	}

	/**
	 * Setter for <code>public.group_permission.group_id</code>.
	 */
	@Override
	public GroupPermissionRecord setGroupId(Long value) {
		this.setValue(1, value);
		return this;
	}

	/**
	 * Setter for <code>public.group_permission.permission</code>.
	 */
	@Override
	public GroupPermissionRecord setPermission(GroupPermissionType value) {
		this.setValue(2, value);
		return this;
	}

	/**
	 * Setter for <code>public.group_permission.principal_id</code>.
	 */
	@Override
	public GroupPermissionRecord setPrincipalId(Long value) {
		this.setValue(0, value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Long value1() {
		return this.getPrincipalId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public GroupPermissionRecord value1(Long value) {
		this.setPrincipalId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Long value2() {
		return this.getGroupId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public GroupPermissionRecord value2(Long value) {
		this.setGroupId(value);
		return this;
	}

	// -------------------------------------------------------------------------
	// FROM and INTO
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public GroupPermissionType value3() {
		return this.getPermission();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public GroupPermissionRecord value3(GroupPermissionType value) {
		this.setPermission(value);
		return this;
	}

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public GroupPermissionRecord values(Long value1, Long value2,
			GroupPermissionType value3) {
		this.value1(value1);
		this.value2(value2);
		this.value3(value3);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Row3<Long, Long, GroupPermissionType> valuesRow() {
		return (Row3) super.valuesRow();
	}
}
