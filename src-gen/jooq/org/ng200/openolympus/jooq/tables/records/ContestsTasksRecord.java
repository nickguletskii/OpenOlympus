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
public class ContestsTasksRecord extends org.jooq.impl.UpdatableRecordImpl<org.ng200.openolympus.jooq.tables.records.ContestsTasksRecord> implements org.jooq.Record2<java.lang.Long, java.lang.Long> {

	private static final long serialVersionUID = 857555413;

	/**
	 * Setter for <code>public.contests_tasks.contests_id</code>.
	 */
	public void setContestsId(java.lang.Long value) {
		setValue(0, value);
	}

	/**
	 * Getter for <code>public.contests_tasks.contests_id</code>.
	 */
	public java.lang.Long getContestsId() {
		return (java.lang.Long) getValue(0);
	}

	/**
	 * Setter for <code>public.contests_tasks.tasks_id</code>.
	 */
	public void setTasksId(java.lang.Long value) {
		setValue(1, value);
	}

	/**
	 * Getter for <code>public.contests_tasks.tasks_id</code>.
	 */
	public java.lang.Long getTasksId() {
		return (java.lang.Long) getValue(1);
	}

	// -------------------------------------------------------------------------
	// Primary key information
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Record2<java.lang.Long, java.lang.Long> key() {
		return (org.jooq.Record2) super.key();
	}

	// -------------------------------------------------------------------------
	// Record2 type implementation
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Row2<java.lang.Long, java.lang.Long> fieldsRow() {
		return (org.jooq.Row2) super.fieldsRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Row2<java.lang.Long, java.lang.Long> valuesRow() {
		return (org.jooq.Row2) super.valuesRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Long> field1() {
		return org.ng200.openolympus.jooq.tables.ContestsTasks.CONTESTS_TASKS.CONTESTS_ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Long> field2() {
		return org.ng200.openolympus.jooq.tables.ContestsTasks.CONTESTS_TASKS.TASKS_ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Long value1() {
		return getContestsId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Long value2() {
		return getTasksId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ContestsTasksRecord value1(java.lang.Long value) {
		setContestsId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ContestsTasksRecord value2(java.lang.Long value) {
		setTasksId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ContestsTasksRecord values(java.lang.Long value1, java.lang.Long value2) {
		return this;
	}

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * Create a detached ContestsTasksRecord
	 */
	public ContestsTasksRecord() {
		super(org.ng200.openolympus.jooq.tables.ContestsTasks.CONTESTS_TASKS);
	}

	/**
	 * Create a detached, initialised ContestsTasksRecord
	 */
	public ContestsTasksRecord(java.lang.Long contestsId, java.lang.Long tasksId) {
		super(org.ng200.openolympus.jooq.tables.ContestsTasks.CONTESTS_TASKS);

		setValue(0, contestsId);
		setValue(1, tasksId);
	}
}
