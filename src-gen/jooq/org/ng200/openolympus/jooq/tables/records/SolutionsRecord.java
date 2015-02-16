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
public class SolutionsRecord extends org.jooq.impl.UpdatableRecordImpl<org.ng200.openolympus.jooq.tables.records.SolutionsRecord> implements org.jooq.Record8<java.lang.Long, java.lang.String, java.math.BigDecimal, java.math.BigDecimal, java.lang.Boolean, java.sql.Timestamp, java.lang.Long, java.lang.Long> {

	private static final long serialVersionUID = 2071006316;

	/**
	 * Setter for <code>public.solutions.id</code>.
	 */
	public void setId(java.lang.Long value) {
		setValue(0, value);
	}

	/**
	 * Getter for <code>public.solutions.id</code>.
	 */
	public java.lang.Long getId() {
		return (java.lang.Long) getValue(0);
	}

	/**
	 * Setter for <code>public.solutions.file</code>.
	 */
	public void setFile(java.lang.String value) {
		setValue(1, value);
	}

	/**
	 * Getter for <code>public.solutions.file</code>.
	 */
	public java.lang.String getFile() {
		return (java.lang.String) getValue(1);
	}

	/**
	 * Setter for <code>public.solutions.maximum_score</code>.
	 */
	public void setMaximumScore(java.math.BigDecimal value) {
		setValue(2, value);
	}

	/**
	 * Getter for <code>public.solutions.maximum_score</code>.
	 */
	public java.math.BigDecimal getMaximumScore() {
		return (java.math.BigDecimal) getValue(2);
	}

	/**
	 * Setter for <code>public.solutions.score</code>.
	 */
	public void setScore(java.math.BigDecimal value) {
		setValue(3, value);
	}

	/**
	 * Getter for <code>public.solutions.score</code>.
	 */
	public java.math.BigDecimal getScore() {
		return (java.math.BigDecimal) getValue(3);
	}

	/**
	 * Setter for <code>public.solutions.tested</code>.
	 */
	public void setTested(java.lang.Boolean value) {
		setValue(4, value);
	}

	/**
	 * Getter for <code>public.solutions.tested</code>.
	 */
	public java.lang.Boolean getTested() {
		return (java.lang.Boolean) getValue(4);
	}

	/**
	 * Setter for <code>public.solutions.time_added</code>.
	 */
	public void setTimeAdded(java.sql.Timestamp value) {
		setValue(5, value);
	}

	/**
	 * Getter for <code>public.solutions.time_added</code>.
	 */
	public java.sql.Timestamp getTimeAdded() {
		return (java.sql.Timestamp) getValue(5);
	}

	/**
	 * Setter for <code>public.solutions.task_id</code>.
	 */
	public void setTaskId(java.lang.Long value) {
		setValue(6, value);
	}

	/**
	 * Getter for <code>public.solutions.task_id</code>.
	 */
	public java.lang.Long getTaskId() {
		return (java.lang.Long) getValue(6);
	}

	/**
	 * Setter for <code>public.solutions.user_id</code>.
	 */
	public void setUserId(java.lang.Long value) {
		setValue(7, value);
	}

	/**
	 * Getter for <code>public.solutions.user_id</code>.
	 */
	public java.lang.Long getUserId() {
		return (java.lang.Long) getValue(7);
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
	// Record8 type implementation
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Row8<java.lang.Long, java.lang.String, java.math.BigDecimal, java.math.BigDecimal, java.lang.Boolean, java.sql.Timestamp, java.lang.Long, java.lang.Long> fieldsRow() {
		return (org.jooq.Row8) super.fieldsRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Row8<java.lang.Long, java.lang.String, java.math.BigDecimal, java.math.BigDecimal, java.lang.Boolean, java.sql.Timestamp, java.lang.Long, java.lang.Long> valuesRow() {
		return (org.jooq.Row8) super.valuesRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Long> field1() {
		return org.ng200.openolympus.jooq.tables.Solutions.SOLUTIONS.ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.String> field2() {
		return org.ng200.openolympus.jooq.tables.Solutions.SOLUTIONS.FILE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.math.BigDecimal> field3() {
		return org.ng200.openolympus.jooq.tables.Solutions.SOLUTIONS.MAXIMUM_SCORE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.math.BigDecimal> field4() {
		return org.ng200.openolympus.jooq.tables.Solutions.SOLUTIONS.SCORE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Boolean> field5() {
		return org.ng200.openolympus.jooq.tables.Solutions.SOLUTIONS.TESTED;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.sql.Timestamp> field6() {
		return org.ng200.openolympus.jooq.tables.Solutions.SOLUTIONS.TIME_ADDED;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Long> field7() {
		return org.ng200.openolympus.jooq.tables.Solutions.SOLUTIONS.TASK_ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Long> field8() {
		return org.ng200.openolympus.jooq.tables.Solutions.SOLUTIONS.USER_ID;
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
	public java.lang.String value2() {
		return getFile();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.math.BigDecimal value3() {
		return getMaximumScore();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.math.BigDecimal value4() {
		return getScore();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Boolean value5() {
		return getTested();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.sql.Timestamp value6() {
		return getTimeAdded();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Long value7() {
		return getTaskId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Long value8() {
		return getUserId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SolutionsRecord value1(java.lang.Long value) {
		setId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SolutionsRecord value2(java.lang.String value) {
		setFile(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SolutionsRecord value3(java.math.BigDecimal value) {
		setMaximumScore(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SolutionsRecord value4(java.math.BigDecimal value) {
		setScore(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SolutionsRecord value5(java.lang.Boolean value) {
		setTested(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SolutionsRecord value6(java.sql.Timestamp value) {
		setTimeAdded(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SolutionsRecord value7(java.lang.Long value) {
		setTaskId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SolutionsRecord value8(java.lang.Long value) {
		setUserId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SolutionsRecord values(java.lang.Long value1, java.lang.String value2, java.math.BigDecimal value3, java.math.BigDecimal value4, java.lang.Boolean value5, java.sql.Timestamp value6, java.lang.Long value7, java.lang.Long value8) {
		return this;
	}

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * Create a detached SolutionsRecord
	 */
	public SolutionsRecord() {
		super(org.ng200.openolympus.jooq.tables.Solutions.SOLUTIONS);
	}

	/**
	 * Create a detached, initialised SolutionsRecord
	 */
	public SolutionsRecord(java.lang.Long id, java.lang.String file, java.math.BigDecimal maximumScore, java.math.BigDecimal score, java.lang.Boolean tested, java.sql.Timestamp timeAdded, java.lang.Long taskId, java.lang.Long userId) {
		super(org.ng200.openolympus.jooq.tables.Solutions.SOLUTIONS);

		setValue(0, id);
		setValue(1, file);
		setValue(2, maximumScore);
		setValue(3, score);
		setValue(4, tested);
		setValue(5, timeAdded);
		setValue(6, taskId);
		setValue(7, userId);
	}
}
