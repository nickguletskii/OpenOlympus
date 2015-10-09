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
import org.ng200.openolympus.jooq.enums.GroupPermissionType;
import org.ng200.openolympus.jooq.tables.records.GroupPermissionRecord;


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
public class GroupPermission extends TableImpl<GroupPermissionRecord> {

	private static final long serialVersionUID = -1319648750;

	/**
	 * The reference instance of <code>public.group_permission</code>
	 */
	public static final GroupPermission GROUP_PERMISSION = new GroupPermission();

	/**
	 * The class holding records for this type
	 */
	@Override
	public Class<GroupPermissionRecord> getRecordType() {
		return GroupPermissionRecord.class;
	}

	/**
	 * The column <code>public.group_permission.principal_id</code>.
	 */
	public final TableField<GroupPermissionRecord, Long> PRINCIPAL_ID = createField("principal_id", org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "");

	/**
	 * The column <code>public.group_permission.group_id</code>.
	 */
	public final TableField<GroupPermissionRecord, Long> GROUP_ID = createField("group_id", org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "");

	/**
	 * The column <code>public.group_permission.permission</code>.
	 */
	public final TableField<GroupPermissionRecord, GroupPermissionType> PERMISSION = createField("permission", org.jooq.util.postgres.PostgresDataType.VARCHAR.asEnumDataType(org.ng200.openolympus.jooq.enums.GroupPermissionType.class), this, "");

	/**
	 * Create a <code>public.group_permission</code> table reference
	 */
	public GroupPermission() {
		this("group_permission", null);
	}

	/**
	 * Create an aliased <code>public.group_permission</code> table reference
	 */
	public GroupPermission(String alias) {
		this(alias, GROUP_PERMISSION);
	}

	private GroupPermission(String alias, Table<GroupPermissionRecord> aliased) {
		this(alias, aliased, null);
	}

	private GroupPermission(String alias, Table<GroupPermissionRecord> aliased, Field<?>[] parameters) {
		super(alias, Public.PUBLIC, aliased, parameters, "");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UniqueKey<GroupPermissionRecord> getPrimaryKey() {
		return Keys.GROUP_PERMISSION_PK;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<UniqueKey<GroupPermissionRecord>> getKeys() {
		return Arrays.<UniqueKey<GroupPermissionRecord>>asList(Keys.GROUP_PERMISSION_PK);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ForeignKey<GroupPermissionRecord, ?>> getReferences() {
		return Arrays.<ForeignKey<GroupPermissionRecord, ?>>asList(Keys.GROUP_PERMISSION__GROUP_PERMISSION_PRINCIPAL_FK, Keys.GROUP_PERMISSION__GROUP_PERMISSION_GROUP_FK);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public GroupPermission as(String alias) {
		return new GroupPermission(alias, this);
	}

	/**
	 * Rename this table
	 */
	public GroupPermission rename(String name) {
		return new GroupPermission(name, null);
	}
}
