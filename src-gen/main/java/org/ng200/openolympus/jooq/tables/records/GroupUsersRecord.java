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
import org.jooq.Record2;
import org.jooq.Record3;
import org.jooq.Row3;
import org.jooq.impl.UpdatableRecordImpl;
import org.ng200.openolympus.jooq.tables.GroupUsers;
import org.ng200.openolympus.jooq.tables.interfaces.IGroupUsers;


/**
 * This class is generated by jOOQ.
 */
@Generated(
	value = {
		"http://www.jooq.org",
		"jOOQ version:3.7.0"
	},
	comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
@Entity
@Table(name = "group_users", schema = "public", uniqueConstraints = {
	@UniqueConstraint(columnNames = {"group_id", "user_id"})
})
public class GroupUsersRecord extends UpdatableRecordImpl<GroupUsersRecord> implements Record3<Boolean, Long, Long>, IGroupUsers {

	private static final long serialVersionUID = -370865692;

	/**
	 * Setter for <code>public.group_users.can_add_others_to_group</code>.
	 */
	@Override
	public GroupUsersRecord setCanAddOthersToGroup(Boolean value) {
		setValue(0, value);
		return this;
	}

	/**
	 * Getter for <code>public.group_users.can_add_others_to_group</code>.
	 */
	@Column(name = "can_add_others_to_group", nullable = false)
	@Override
	public Boolean getCanAddOthersToGroup() {
		return (Boolean) getValue(0);
	}

	/**
	 * Setter for <code>public.group_users.group_id</code>.
	 */
	@Override
	public GroupUsersRecord setGroupId(Long value) {
		setValue(1, value);
		return this;
	}

	/**
	 * Getter for <code>public.group_users.group_id</code>.
	 */
	@Column(name = "group_id", nullable = false, precision = 64)
	@Override
	public Long getGroupId() {
		return (Long) getValue(1);
	}

	/**
	 * Setter for <code>public.group_users.user_id</code>.
	 */
	@Override
	public GroupUsersRecord setUserId(Long value) {
		setValue(2, value);
		return this;
	}

	/**
	 * Getter for <code>public.group_users.user_id</code>.
	 */
	@Column(name = "user_id", nullable = false, precision = 64)
	@Override
	public Long getUserId() {
		return (Long) getValue(2);
	}

	// -------------------------------------------------------------------------
	// Primary key information
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Record2<Long, Long> key() {
		return (Record2) super.key();
	}

	// -------------------------------------------------------------------------
	// Record3 type implementation
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Row3<Boolean, Long, Long> fieldsRow() {
		return (Row3) super.fieldsRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Row3<Boolean, Long, Long> valuesRow() {
		return (Row3) super.valuesRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Boolean> field1() {
		return GroupUsers.GROUP_USERS.CAN_ADD_OTHERS_TO_GROUP;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Long> field2() {
		return GroupUsers.GROUP_USERS.GROUP_ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Long> field3() {
		return GroupUsers.GROUP_USERS.USER_ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean value1() {
		return getCanAddOthersToGroup();
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
	public Long value3() {
		return getUserId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public GroupUsersRecord value1(Boolean value) {
		setCanAddOthersToGroup(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public GroupUsersRecord value2(Long value) {
		setGroupId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public GroupUsersRecord value3(Long value) {
		setUserId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public GroupUsersRecord values(Boolean value1, Long value2, Long value3) {
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

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * Create a detached GroupUsersRecord
	 */
	public GroupUsersRecord() {
		super(GroupUsers.GROUP_USERS);
	}

	/**
	 * Create a detached, initialised GroupUsersRecord
	 */
	public GroupUsersRecord(Boolean canAddOthersToGroup, Long groupId, Long userId) {
		super(GroupUsers.GROUP_USERS);

		setValue(0, canAddOthersToGroup);
		setValue(1, groupId);
		setValue(2, userId);
	}
}
