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
@Generated(
	value = {
		"http://www.jooq.org",
		"jOOQ version:3.7.2"
	},
	comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
@Entity
@Table(name = "group_permission", schema = "public", uniqueConstraints = {
	@UniqueConstraint(columnNames = {"principal_id", "group_id", "permission"})
})
public class GroupPermissionRecord extends UpdatableRecordImpl<GroupPermissionRecord> implements Record3<Long, Long, GroupPermissionType>, IGroupPermission {

	private static final long serialVersionUID = -2021397890;

	/**
	 * Setter for <code>public.group_permission.principal_id</code>.
	 */
	@Override
	public GroupPermissionRecord setPrincipalId(Long value) {
		setValue(0, value);
		return this;
	}

	/**
	 * Getter for <code>public.group_permission.principal_id</code>.
	 */
	@Column(name = "principal_id", nullable = false, precision = 64)
	@Override
	public Long getPrincipalId() {
		return (Long) getValue(0);
	}

	/**
	 * Setter for <code>public.group_permission.group_id</code>.
	 */
	@Override
	public GroupPermissionRecord setGroupId(Long value) {
		setValue(1, value);
		return this;
	}

	/**
	 * Getter for <code>public.group_permission.group_id</code>.
	 */
	@Column(name = "group_id", nullable = false, precision = 64)
	@Override
	public Long getGroupId() {
		return (Long) getValue(1);
	}

	/**
	 * Setter for <code>public.group_permission.permission</code>.
	 */
	@Override
	public GroupPermissionRecord setPermission(GroupPermissionType value) {
		setValue(2, value);
		return this;
	}

	/**
	 * Getter for <code>public.group_permission.permission</code>.
	 */
	@Column(name = "permission", nullable = false)
	@Override
	public GroupPermissionType getPermission() {
		return (GroupPermissionType) getValue(2);
	}

	// -------------------------------------------------------------------------
	// Primary key information
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Record3<Long, Long, GroupPermissionType> key() {
		return (Record3) super.key();
	}

	// -------------------------------------------------------------------------
	// Record3 type implementation
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Row3<Long, Long, GroupPermissionType> fieldsRow() {
		return (Row3) super.fieldsRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Row3<Long, Long, GroupPermissionType> valuesRow() {
		return (Row3) super.valuesRow();
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
	public Long value1() {
		return getPrincipalId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Long value2() {
		return getGroupId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public GroupPermissionType value3() {
		return getPermission();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public GroupPermissionRecord value1(Long value) {
		setPrincipalId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public GroupPermissionRecord value2(Long value) {
		setGroupId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public GroupPermissionRecord value3(GroupPermissionType value) {
		setPermission(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public GroupPermissionRecord values(Long value1, Long value2, GroupPermissionType value3) {
		value1(value1);
		value2(value2);
		value3(value3);
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

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * Create a detached GroupPermissionRecord
	 */
	public GroupPermissionRecord() {
		super(GroupPermission.GROUP_PERMISSION);
	}

	/**
	 * Create a detached, initialised GroupPermissionRecord
	 */
	public GroupPermissionRecord(Long principalId, Long groupId, GroupPermissionType permission) {
		super(GroupPermission.GROUP_PERMISSION);

		setValue(0, principalId);
		setValue(1, groupId);
		setValue(2, permission);
	}
}
