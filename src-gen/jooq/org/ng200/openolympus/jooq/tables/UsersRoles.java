/**
 * This class is generated by jOOQ
 */
package org.ng200.openolympus.jooq.tables;

/**
 * This class is generated by jOOQ.
 */
@javax.annotation.Generated(
	value = {
		"http://www.jooq.org",
		"jOOQ version:3.5.2"
	},
	comments = "This class is generated by jOOQ"
)
@java.lang.SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class UsersRoles extends org.jooq.impl.TableImpl<org.ng200.openolympus.jooq.tables.records.UsersRolesRecord> {

	private static final long serialVersionUID = -712563173;

	/**
	 * The reference instance of <code>public.users_roles</code>
	 */
	public static final org.ng200.openolympus.jooq.tables.UsersRoles USERS_ROLES = new org.ng200.openolympus.jooq.tables.UsersRoles();

	/**
	 * The class holding records for this type
	 */
	@Override
	public java.lang.Class<org.ng200.openolympus.jooq.tables.records.UsersRolesRecord> getRecordType() {
		return org.ng200.openolympus.jooq.tables.records.UsersRolesRecord.class;
	}

	/**
	 * The column <code>public.users_roles.users_id</code>.
	 */
	public final org.jooq.TableField<org.ng200.openolympus.jooq.tables.records.UsersRolesRecord, java.lang.Long> USERS_ID = createField("users_id", org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "");

	/**
	 * The column <code>public.users_roles.roles_id</code>.
	 */
	public final org.jooq.TableField<org.ng200.openolympus.jooq.tables.records.UsersRolesRecord, java.lang.Long> ROLES_ID = createField("roles_id", org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "");

	/**
	 * Create a <code>public.users_roles</code> table reference
	 */
	public UsersRoles() {
		this("users_roles", null);
	}

	/**
	 * Create an aliased <code>public.users_roles</code> table reference
	 */
	public UsersRoles(java.lang.String alias) {
		this(alias, org.ng200.openolympus.jooq.tables.UsersRoles.USERS_ROLES);
	}

	private UsersRoles(java.lang.String alias, org.jooq.Table<org.ng200.openolympus.jooq.tables.records.UsersRolesRecord> aliased) {
		this(alias, aliased, null);
	}

	private UsersRoles(java.lang.String alias, org.jooq.Table<org.ng200.openolympus.jooq.tables.records.UsersRolesRecord> aliased, org.jooq.Field<?>[] parameters) {
		super(alias, org.ng200.openolympus.jooq.Public.PUBLIC, aliased, parameters, "");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.UniqueKey<org.ng200.openolympus.jooq.tables.records.UsersRolesRecord> getPrimaryKey() {
		return org.ng200.openolympus.jooq.Keys.USERS_ROLES_PKEY;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.util.List<org.jooq.UniqueKey<org.ng200.openolympus.jooq.tables.records.UsersRolesRecord>> getKeys() {
		return java.util.Arrays.<org.jooq.UniqueKey<org.ng200.openolympus.jooq.tables.records.UsersRolesRecord>>asList(org.ng200.openolympus.jooq.Keys.USERS_ROLES_PKEY);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.util.List<org.jooq.ForeignKey<org.ng200.openolympus.jooq.tables.records.UsersRolesRecord, ?>> getReferences() {
		return java.util.Arrays.<org.jooq.ForeignKey<org.ng200.openolympus.jooq.tables.records.UsersRolesRecord, ?>>asList(org.ng200.openolympus.jooq.Keys.USERS_ROLES__FK_3B2CL2U4CK187O21R4UHP6PSV, org.ng200.openolympus.jooq.Keys.USERS_ROLES__FK_60LOXAV507L5MREO05V0IM1LQ);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.ng200.openolympus.jooq.tables.UsersRoles as(java.lang.String alias) {
		return new org.ng200.openolympus.jooq.tables.UsersRoles(alias, this);
	}

	/**
	 * Rename this table
	 */
	public org.ng200.openolympus.jooq.tables.UsersRoles rename(java.lang.String name) {
		return new org.ng200.openolympus.jooq.tables.UsersRoles(name, null);
	}
}
