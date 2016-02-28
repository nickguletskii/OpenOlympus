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
import org.ng200.openolympus.jooq.enums.TaskPermissionType;
import org.ng200.openolympus.jooq.tables.records.TaskPermissionRecord;


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
public class TaskPermission extends TableImpl<TaskPermissionRecord> {

	private static final long serialVersionUID = 680203801;

	/**
	 * The reference instance of <code>public.task_permission</code>
	 */
	public static final TaskPermission TASK_PERMISSION = new TaskPermission();

	/**
	 * The class holding records for this type
	 */
	@Override
	public Class<TaskPermissionRecord> getRecordType() {
		return TaskPermissionRecord.class;
	}

	/**
	 * The column <code>public.task_permission.permission</code>.
	 */
	public final TableField<TaskPermissionRecord, TaskPermissionType> PERMISSION = createField("permission", org.jooq.util.postgres.PostgresDataType.VARCHAR.asEnumDataType(org.ng200.openolympus.jooq.enums.TaskPermissionType.class), this, "");

	/**
	 * The column <code>public.task_permission.task_id</code>.
	 */
	public final TableField<TaskPermissionRecord, Integer> TASK_ID = createField("task_id", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

	/**
	 * The column <code>public.task_permission.principal_id</code>.
	 */
	public final TableField<TaskPermissionRecord, Long> PRINCIPAL_ID = createField("principal_id", org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "");

	/**
	 * Create a <code>public.task_permission</code> table reference
	 */
	public TaskPermission() {
		this("task_permission", null);
	}

	/**
	 * Create an aliased <code>public.task_permission</code> table reference
	 */
	public TaskPermission(String alias) {
		this(alias, TASK_PERMISSION);
	}

	private TaskPermission(String alias, Table<TaskPermissionRecord> aliased) {
		this(alias, aliased, null);
	}

	private TaskPermission(String alias, Table<TaskPermissionRecord> aliased, Field<?>[] parameters) {
		super(alias, Public.PUBLIC, aliased, parameters, "");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UniqueKey<TaskPermissionRecord> getPrimaryKey() {
		return Keys.TASK_PERMISSION_PK;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<UniqueKey<TaskPermissionRecord>> getKeys() {
		return Arrays.<UniqueKey<TaskPermissionRecord>>asList(Keys.TASK_PERMISSION_PK);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ForeignKey<TaskPermissionRecord, ?>> getReferences() {
		return Arrays.<ForeignKey<TaskPermissionRecord, ?>>asList(Keys.TASK_PERMISSION__TASK_FK, Keys.TASK_PERMISSION__PRINCIPAL_FK);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TaskPermission as(String alias) {
		return new TaskPermission(alias, this);
	}

	/**
	 * Rename this table
	 */
	public TaskPermission rename(String name) {
		return new TaskPermission(name, null);
	}
}
