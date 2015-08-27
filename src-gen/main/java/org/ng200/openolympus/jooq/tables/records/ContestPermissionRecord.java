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
import org.ng200.openolympus.jooq.enums.ContestPermissionType;
import org.ng200.openolympus.jooq.tables.ContestPermission;
import org.ng200.openolympus.jooq.tables.interfaces.IContestPermission;


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
@Table(name = "contest_permission", schema = "public", uniqueConstraints = {
	@UniqueConstraint(columnNames = {"permission", "contest_id", "principal_id"})
})
public class ContestPermissionRecord extends UpdatableRecordImpl<ContestPermissionRecord> implements Record3<ContestPermissionType, Integer, Long>, IContestPermission {

	private static final long serialVersionUID = -1957940605;

	/**
	 * Setter for <code>public.contest_permission.permission</code>.
	 */
	@Override
	public ContestPermissionRecord setPermission(ContestPermissionType value) {
		setValue(0, value);
		return this;
	}

	/**
	 * Getter for <code>public.contest_permission.permission</code>.
	 */
	@Column(name = "permission", nullable = false)
	@Override
	public ContestPermissionType getPermission() {
		return (ContestPermissionType) getValue(0);
	}

	/**
	 * Setter for <code>public.contest_permission.contest_id</code>.
	 */
	@Override
	public ContestPermissionRecord setContestId(Integer value) {
		setValue(1, value);
		return this;
	}

	/**
	 * Getter for <code>public.contest_permission.contest_id</code>.
	 */
	@Column(name = "contest_id", nullable = false, precision = 32)
	@Override
	public Integer getContestId() {
		return (Integer) getValue(1);
	}

	/**
	 * Setter for <code>public.contest_permission.principal_id</code>.
	 */
	@Override
	public ContestPermissionRecord setPrincipalId(Long value) {
		setValue(2, value);
		return this;
	}

	/**
	 * Getter for <code>public.contest_permission.principal_id</code>.
	 */
	@Column(name = "principal_id", nullable = false, precision = 64)
	@Override
	public Long getPrincipalId() {
		return (Long) getValue(2);
	}

	// -------------------------------------------------------------------------
	// Primary key information
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Record3<ContestPermissionType, Integer, Long> key() {
		return (Record3) super.key();
	}

	// -------------------------------------------------------------------------
	// Record3 type implementation
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Row3<ContestPermissionType, Integer, Long> fieldsRow() {
		return (Row3) super.fieldsRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Row3<ContestPermissionType, Integer, Long> valuesRow() {
		return (Row3) super.valuesRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<ContestPermissionType> field1() {
		return ContestPermission.CONTEST_PERMISSION.PERMISSION;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Integer> field2() {
		return ContestPermission.CONTEST_PERMISSION.CONTEST_ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Long> field3() {
		return ContestPermission.CONTEST_PERMISSION.PRINCIPAL_ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ContestPermissionType value1() {
		return getPermission();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer value2() {
		return getContestId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Long value3() {
		return getPrincipalId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ContestPermissionRecord value1(ContestPermissionType value) {
		setPermission(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ContestPermissionRecord value2(Integer value) {
		setContestId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ContestPermissionRecord value3(Long value) {
		setPrincipalId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ContestPermissionRecord values(ContestPermissionType value1, Integer value2, Long value3) {
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
	public void from(IContestPermission from) {
		setPermission(from.getPermission());
		setContestId(from.getContestId());
		setPrincipalId(from.getPrincipalId());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <E extends IContestPermission> E into(E into) {
		into.from(this);
		return into;
	}

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * Create a detached ContestPermissionRecord
	 */
	public ContestPermissionRecord() {
		super(ContestPermission.CONTEST_PERMISSION);
	}

	/**
	 * Create a detached, initialised ContestPermissionRecord
	 */
	public ContestPermissionRecord(ContestPermissionType permission, Integer contestId, Long principalId) {
		super(ContestPermission.CONTEST_PERMISSION);

		setValue(0, permission);
		setValue(1, contestId);
		setValue(2, principalId);
	}
}
