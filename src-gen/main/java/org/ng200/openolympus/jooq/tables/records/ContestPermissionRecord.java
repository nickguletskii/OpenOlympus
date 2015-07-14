/**
 * This class is generated by jOOQ
 */
package org.ng200.openolympus.jooq.tables.records;


import javax.annotation.Generated;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.jooq.Field;
import org.jooq.Record1;
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
@Table(name = "contest_permission", schema = "public")
public class ContestPermissionRecord extends UpdatableRecordImpl<ContestPermissionRecord> implements Record3<Long, ContestPermissionType, Integer>, IContestPermission {

	private static final long serialVersionUID = -1387965813;

	/**
	 * Setter for <code>public.contest_permission.id</code>.
	 */
	@Override
	public ContestPermissionRecord setId(Long value) {
		setValue(0, value);
		return this;
	}

	/**
	 * Getter for <code>public.contest_permission.id</code>.
	 */
	@Id
	@Column(name = "id", unique = true, nullable = false, precision = 64)
	@Override
	public Long getId() {
		return (Long) getValue(0);
	}

	/**
	 * Setter for <code>public.contest_permission.type</code>.
	 */
	@Override
	public ContestPermissionRecord setType(ContestPermissionType value) {
		setValue(1, value);
		return this;
	}

	/**
	 * Getter for <code>public.contest_permission.type</code>.
	 */
	@Column(name = "type", nullable = false)
	@Override
	public ContestPermissionType getType() {
		return (ContestPermissionType) getValue(1);
	}

	/**
	 * Setter for <code>public.contest_permission.contest_id</code>.
	 */
	@Override
	public ContestPermissionRecord setContestId(Integer value) {
		setValue(2, value);
		return this;
	}

	/**
	 * Getter for <code>public.contest_permission.contest_id</code>.
	 */
	@Column(name = "contest_id", precision = 32)
	@Override
	public Integer getContestId() {
		return (Integer) getValue(2);
	}

	// -------------------------------------------------------------------------
	// Primary key information
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Record1<Long> key() {
		return (Record1) super.key();
	}

	// -------------------------------------------------------------------------
	// Record3 type implementation
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Row3<Long, ContestPermissionType, Integer> fieldsRow() {
		return (Row3) super.fieldsRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Row3<Long, ContestPermissionType, Integer> valuesRow() {
		return (Row3) super.valuesRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Long> field1() {
		return ContestPermission.CONTEST_PERMISSION.ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<ContestPermissionType> field2() {
		return ContestPermission.CONTEST_PERMISSION.TYPE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Integer> field3() {
		return ContestPermission.CONTEST_PERMISSION.CONTEST_ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Long value1() {
		return getId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ContestPermissionType value2() {
		return getType();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer value3() {
		return getContestId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ContestPermissionRecord value1(Long value) {
		setId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ContestPermissionRecord value2(ContestPermissionType value) {
		setType(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ContestPermissionRecord value3(Integer value) {
		setContestId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ContestPermissionRecord values(Long value1, ContestPermissionType value2, Integer value3) {
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
		setId(from.getId());
		setType(from.getType());
		setContestId(from.getContestId());
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
	public ContestPermissionRecord(Long id, ContestPermissionType type, Integer contestId) {
		super(ContestPermission.CONTEST_PERMISSION);

		setValue(0, id);
		setValue(1, type);
		setValue(2, contestId);
	}
}