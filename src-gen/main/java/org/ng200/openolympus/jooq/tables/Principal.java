/**
 * This class is generated by jOOQ
 */
package org.ng200.openolympus.jooq.tables;


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
import org.ng200.openolympus.jooq.tables.records.PrincipalRecord;


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
public class Principal extends TableImpl<PrincipalRecord> {

	private static final long serialVersionUID = 1055950692;

	/**
	 * The reference instance of <code>public.principal</code>
	 */
	public static final Principal PRINCIPAL = new Principal();

	/**
	 * The class holding records for this type
	 */
	@Override
	public Class<PrincipalRecord> getRecordType() {
		return PrincipalRecord.class;
	}

	/**
	 * The column <code>public.principal.id</code>.
	 */
	public final TableField<PrincipalRecord, Long> ID = createField("id", org.jooq.impl.SQLDataType.BIGINT.nullable(false).defaulted(true), this, "");

	/**
	 * Create a <code>public.principal</code> table reference
	 */
	public Principal() {
		this("principal", null);
	}

	/**
	 * Create an aliased <code>public.principal</code> table reference
	 */
	public Principal(String alias) {
		this(alias, PRINCIPAL);
	}

	private Principal(String alias, Table<PrincipalRecord> aliased) {
		this(alias, aliased, null);
	}

	private Principal(String alias, Table<PrincipalRecord> aliased, Field<?>[] parameters) {
		super(alias, Public.PUBLIC, aliased, parameters, "");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Identity<PrincipalRecord, Long> getIdentity() {
		return Keys.IDENTITY_PRINCIPAL;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UniqueKey<PrincipalRecord> getPrimaryKey() {
		return Keys.PRINCIPAL_PK;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<UniqueKey<PrincipalRecord>> getKeys() {
		return Arrays.<UniqueKey<PrincipalRecord>>asList(Keys.PRINCIPAL_PK);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Principal as(String alias) {
		return new Principal(alias, this);
	}

	/**
	 * Rename this table
	 */
	public Principal rename(String name) {
		return new Principal(name, null);
	}
}