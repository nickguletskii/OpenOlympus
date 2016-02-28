/**
 * This class is generated by jOOQ
 */
package org.ng200.openolympus.jooq.tables;


import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Identity;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.TableImpl;
import org.ng200.openolympus.jooq.Keys;
import org.ng200.openolympus.jooq.Public;
import org.ng200.openolympus.jooq.tables.records.TaskRecord;
import org.ng200.openolympus.util.DateTimeBinding;


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
public class Task extends TableImpl<TaskRecord> {

	private static final long serialVersionUID = 2093789717;

	/**
	 * The reference instance of <code>public.task</code>
	 */
	public static final Task TASK = new Task();

	/**
	 * The class holding records for this type
	 */
	@Override
	public Class<TaskRecord> getRecordType() {
		return TaskRecord.class;
	}

	/**
	 * The column <code>public.task.id</code>.
	 */
	public final TableField<TaskRecord, Integer> ID = createField("id", org.jooq.impl.SQLDataType.INTEGER.nullable(false).defaulted(true), this, "");

	/**
	 * The column <code>public.task.description_file</code>.
	 */
	public final TableField<TaskRecord, String> DESCRIPTION_FILE = createField("description_file", org.jooq.impl.SQLDataType.CLOB.nullable(false), this, "");

	/**
	 * The column <code>public.task.name</code>.
	 */
	public final TableField<TaskRecord, String> NAME = createField("name", org.jooq.impl.SQLDataType.VARCHAR.length(255), this, "");

	/**
	 * The column <code>public.task.task_location</code>.
	 */
	public final TableField<TaskRecord, String> TASK_LOCATION = createField("task_location", org.jooq.impl.SQLDataType.CLOB.nullable(false), this, "");

	/**
	 * The column <code>public.task.created_date</code>.
	 */
	public final TableField<TaskRecord, OffsetDateTime> CREATED_DATE = createField("created_date", org.ng200.openolympus.jooqsupport.CustomTypes.OFFSETDATETIME, this, "", new DateTimeBinding());

	/**
	 * Create a <code>public.task</code> table reference
	 */
	public Task() {
		this("task", null);
	}

	/**
	 * Create an aliased <code>public.task</code> table reference
	 */
	public Task(String alias) {
		this(alias, TASK);
	}

	private Task(String alias, Table<TaskRecord> aliased) {
		this(alias, aliased, null);
	}

	private Task(String alias, Table<TaskRecord> aliased, Field<?>[] parameters) {
		super(alias, Public.PUBLIC, aliased, parameters, "");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Identity<TaskRecord, Integer> getIdentity() {
		return Keys.IDENTITY_TASK;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UniqueKey<TaskRecord> getPrimaryKey() {
		return Keys.TASK_PKEY;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<UniqueKey<TaskRecord>> getKeys() {
		return Arrays.<UniqueKey<TaskRecord>>asList(Keys.TASK_PKEY, Keys.TASK_NAME_UNIQUE);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Task as(String alias) {
		return new Task(alias, this);
	}

	/**
	 * Rename this table
	 */
	public Task rename(String name) {
		return new Task(name, null);
	}
}
