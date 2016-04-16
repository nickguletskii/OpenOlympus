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
package org.ng200.openolympus.jooq.tables.records;


import javax.annotation.Generated;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.jooq.Field;
import org.jooq.Record2;
import org.jooq.Row2;
import org.jooq.impl.UpdatableRecordImpl;
import org.ng200.openolympus.jooq.tables.ContestTasks;
import org.ng200.openolympus.jooq.tables.interfaces.IContestTasks;


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
@Table(name = "contest_tasks", schema = "public", uniqueConstraints = {
	@UniqueConstraint(columnNames = {"contest_id", "task_id"})
})
public class ContestTasksRecord extends UpdatableRecordImpl<ContestTasksRecord> implements Record2<Integer, Integer>, IContestTasks {

	private static final long serialVersionUID = -1930105370;

	/**
	 * Setter for <code>public.contest_tasks.contest_id</code>.
	 */
	@Override
	public ContestTasksRecord setContestId(Integer value) {
		setValue(0, value);
		return this;
	}

	/**
	 * Getter for <code>public.contest_tasks.contest_id</code>.
	 */
	@Column(name = "contest_id", nullable = false, precision = 32)
	@Override
	public Integer getContestId() {
		return (Integer) getValue(0);
	}

	/**
	 * Setter for <code>public.contest_tasks.task_id</code>.
	 */
	@Override
	public ContestTasksRecord setTaskId(Integer value) {
		setValue(1, value);
		return this;
	}

	/**
	 * Getter for <code>public.contest_tasks.task_id</code>.
	 */
	@Column(name = "task_id", nullable = false, precision = 32)
	@Override
	public Integer getTaskId() {
		return (Integer) getValue(1);
	}

	// -------------------------------------------------------------------------
	// Primary key information
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Record2<Integer, Integer> key() {
		return (Record2) super.key();
	}

	// -------------------------------------------------------------------------
	// Record2 type implementation
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Row2<Integer, Integer> fieldsRow() {
		return (Row2) super.fieldsRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Row2<Integer, Integer> valuesRow() {
		return (Row2) super.valuesRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Integer> field1() {
		return ContestTasks.CONTEST_TASKS.CONTEST_ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Integer> field2() {
		return ContestTasks.CONTEST_TASKS.TASK_ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer value1() {
		return getContestId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer value2() {
		return getTaskId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ContestTasksRecord value1(Integer value) {
		setContestId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ContestTasksRecord value2(Integer value) {
		setTaskId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ContestTasksRecord values(Integer value1, Integer value2) {
		value1(value1);
		value2(value2);
		return this;
	}

	// -------------------------------------------------------------------------
	// FROM and INTO
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void from(IContestTasks from) {
		setContestId(from.getContestId());
		setTaskId(from.getTaskId());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <E extends IContestTasks> E into(E into) {
		into.from(this);
		return into;
	}

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * Create a detached ContestTasksRecord
	 */
	public ContestTasksRecord() {
		super(ContestTasks.CONTEST_TASKS);
	}

	/**
	 * Create a detached, initialised ContestTasksRecord
	 */
	public ContestTasksRecord(Integer contestId, Integer taskId) {
		super(ContestTasks.CONTEST_TASKS);

		setValue(0, contestId);
		setValue(1, taskId);
	}
}
