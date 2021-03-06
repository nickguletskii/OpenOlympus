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
package org.ng200.openolympus.jooq.tables;


import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.TableImpl;
import org.ng200.openolympus.jooq.Keys;
import org.ng200.openolympus.jooq.Public;
import org.ng200.openolympus.jooq.tables.records.ContestTasksRecord;


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
public class ContestTasks extends TableImpl<ContestTasksRecord> {

	private static final long serialVersionUID = 1983617921;

	/**
	 * The reference instance of <code>public.contest_tasks</code>
	 */
	public static final ContestTasks CONTEST_TASKS = new ContestTasks();

	/**
	 * The class holding records for this type
	 */
	@Override
	public Class<ContestTasksRecord> getRecordType() {
		return ContestTasksRecord.class;
	}

	/**
	 * The column <code>public.contest_tasks.contest_id</code>.
	 */
	public final TableField<ContestTasksRecord, Integer> CONTEST_ID = createField("contest_id", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

	/**
	 * The column <code>public.contest_tasks.task_id</code>.
	 */
	public final TableField<ContestTasksRecord, Integer> TASK_ID = createField("task_id", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

	/**
	 * Create a <code>public.contest_tasks</code> table reference
	 */
	public ContestTasks() {
		this("contest_tasks", null);
	}

	/**
	 * Create an aliased <code>public.contest_tasks</code> table reference
	 */
	public ContestTasks(String alias) {
		this(alias, CONTEST_TASKS);
	}

	private ContestTasks(String alias, Table<ContestTasksRecord> aliased) {
		this(alias, aliased, null);
	}

	private ContestTasks(String alias, Table<ContestTasksRecord> aliased, Field<?>[] parameters) {
		super(alias, Public.PUBLIC, aliased, parameters, "");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UniqueKey<ContestTasksRecord> getPrimaryKey() {
		return Keys.CONTEST_TASKS_PK;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<UniqueKey<ContestTasksRecord>> getKeys() {
		return Arrays.<UniqueKey<ContestTasksRecord>>asList(Keys.CONTEST_TASKS_PK);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ForeignKey<ContestTasksRecord, ?>> getReferences() {
		return Arrays.<ForeignKey<ContestTasksRecord, ?>>asList(Keys.CONTEST_TASKS__CONTEST_FK, Keys.CONTEST_TASKS__TASK_FK);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ContestTasks as(String alias) {
		return new ContestTasks(alias, this);
	}

	/**
	 * Rename this table
	 */
	public ContestTasks rename(String name) {
		return new ContestTasks(name, null);
	}
}
