/**
 * This class is generated by jOOQ
 */
package org.ng200.openolympus.jooq.tables.records;

/**
 * This class is generated by jOOQ.
 */
@javax.annotation.Generated(
	value = {
		"http://www.jooq.org",
		"jOOQ version:3.5.0"
	},
	comments = "This class is generated by jOOQ"
)
@java.lang.SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class ContestParticipationsRecord extends org.jooq.impl.UpdatableRecordImpl<org.ng200.openolympus.jooq.tables.records.ContestParticipationsRecord> implements org.jooq.Record3<java.lang.Long, java.lang.Long, java.lang.Long> {

	private static final long serialVersionUID = -1189339262;

	/**
	 * Setter for <code>public.contest_participations.id</code>.
	 */
	public void setId(java.lang.Long value) {
		setValue(0, value);
	}

	/**
	 * Getter for <code>public.contest_participations.id</code>.
	 */
	public java.lang.Long getId() {
		return (java.lang.Long) getValue(0);
	}

	/**
	 * Setter for <code>public.contest_participations.contest_id</code>.
	 */
	public void setContestId(java.lang.Long value) {
		setValue(1, value);
	}

	/**
	 * Getter for <code>public.contest_participations.contest_id</code>.
	 */
	public java.lang.Long getContestId() {
		return (java.lang.Long) getValue(1);
	}

	/**
	 * Setter for <code>public.contest_participations.user_id</code>.
	 */
	public void setUserId(java.lang.Long value) {
		setValue(2, value);
	}

	/**
	 * Getter for <code>public.contest_participations.user_id</code>.
	 */
	public java.lang.Long getUserId() {
		return (java.lang.Long) getValue(2);
	}

	// -------------------------------------------------------------------------
	// Primary key information
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Record1<java.lang.Long> key() {
		return (org.jooq.Record1) super.key();
	}

	// -------------------------------------------------------------------------
	// Record3 type implementation
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Row3<java.lang.Long, java.lang.Long, java.lang.Long> fieldsRow() {
		return (org.jooq.Row3) super.fieldsRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Row3<java.lang.Long, java.lang.Long, java.lang.Long> valuesRow() {
		return (org.jooq.Row3) super.valuesRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Long> field1() {
		return org.ng200.openolympus.jooq.tables.ContestParticipations.CONTEST_PARTICIPATIONS.ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Long> field2() {
		return org.ng200.openolympus.jooq.tables.ContestParticipations.CONTEST_PARTICIPATIONS.CONTEST_ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Long> field3() {
		return org.ng200.openolympus.jooq.tables.ContestParticipations.CONTEST_PARTICIPATIONS.USER_ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Long value1() {
		return getId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Long value2() {
		return getContestId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Long value3() {
		return getUserId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ContestParticipationsRecord value1(java.lang.Long value) {
		setId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ContestParticipationsRecord value2(java.lang.Long value) {
		setContestId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ContestParticipationsRecord value3(java.lang.Long value) {
		setUserId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ContestParticipationsRecord values(java.lang.Long value1, java.lang.Long value2, java.lang.Long value3) {
		return this;
	}

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * Create a detached ContestParticipationsRecord
	 */
	public ContestParticipationsRecord() {
		super(org.ng200.openolympus.jooq.tables.ContestParticipations.CONTEST_PARTICIPATIONS);
	}

	/**
	 * Create a detached, initialised ContestParticipationsRecord
	 */
	public ContestParticipationsRecord(java.lang.Long id, java.lang.Long contestId, java.lang.Long userId) {
		super(org.ng200.openolympus.jooq.tables.ContestParticipations.CONTEST_PARTICIPATIONS);

		setValue(0, id);
		setValue(1, contestId);
		setValue(2, userId);
	}
}
