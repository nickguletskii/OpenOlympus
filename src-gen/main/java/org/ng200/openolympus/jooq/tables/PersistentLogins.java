/**
 * This class is generated by jOOQ
 */
package org.ng200.openolympus.jooq.tables;


import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.TableImpl;
import org.ng200.openolympus.jooq.Keys;
import org.ng200.openolympus.jooq.Public;
import org.ng200.openolympus.jooq.tables.records.PersistentLoginsRecord;
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
public class PersistentLogins extends TableImpl<PersistentLoginsRecord> {

	private static final long serialVersionUID = -2080914186;

	/**
	 * The reference instance of <code>public.persistent_logins</code>
	 */
	public static final PersistentLogins PERSISTENT_LOGINS = new PersistentLogins();

	/**
	 * The class holding records for this type
	 */
	@Override
	public Class<PersistentLoginsRecord> getRecordType() {
		return PersistentLoginsRecord.class;
	}

	/**
	 * The column <code>public.persistent_logins.username</code>.
	 */
	public final TableField<PersistentLoginsRecord, String> USERNAME = createField("username", org.jooq.impl.SQLDataType.VARCHAR.length(64).nullable(false), this, "");

	/**
	 * The column <code>public.persistent_logins.series</code>.
	 */
	public final TableField<PersistentLoginsRecord, String> SERIES = createField("series", org.jooq.impl.SQLDataType.VARCHAR.length(64).nullable(false), this, "");

	/**
	 * The column <code>public.persistent_logins.token</code>.
	 */
	public final TableField<PersistentLoginsRecord, String> TOKEN = createField("token", org.jooq.impl.SQLDataType.VARCHAR.length(64).nullable(false), this, "");

	/**
	 * The column <code>public.persistent_logins.last_used</code>.
	 */
	public final TableField<PersistentLoginsRecord, OffsetDateTime> LAST_USED = createField("last_used", org.ng200.openolympus.jooqsupport.CustomTypes.OFFSETDATETIME, this, "", new DateTimeBinding());

	/**
	 * Create a <code>public.persistent_logins</code> table reference
	 */
	public PersistentLogins() {
		this("persistent_logins", null);
	}

	/**
	 * Create an aliased <code>public.persistent_logins</code> table reference
	 */
	public PersistentLogins(String alias) {
		this(alias, PERSISTENT_LOGINS);
	}

	private PersistentLogins(String alias, Table<PersistentLoginsRecord> aliased) {
		this(alias, aliased, null);
	}

	private PersistentLogins(String alias, Table<PersistentLoginsRecord> aliased, Field<?>[] parameters) {
		super(alias, Public.PUBLIC, aliased, parameters, "");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UniqueKey<PersistentLoginsRecord> getPrimaryKey() {
		return Keys.PERSISTENT_LOGINS_PKEY;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<UniqueKey<PersistentLoginsRecord>> getKeys() {
		return Arrays.<UniqueKey<PersistentLoginsRecord>>asList(Keys.PERSISTENT_LOGINS_PKEY);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PersistentLogins as(String alias) {
		return new PersistentLogins(alias, this);
	}

	/**
	 * Rename this table
	 */
	public PersistentLogins rename(String name) {
		return new PersistentLogins(name, null);
	}
}
