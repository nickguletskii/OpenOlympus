/**
 * The MIT License
 * Copyright (c) 2014-2015 Nick Guletskii
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

import java.sql.Timestamp;
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

/**
 * This class is generated by jOOQ.
 */
@Generated(value = {
						"http://www.jooq.org",
						"jOOQ version:3.6.2"
}, comments = "This class is generated by jOOQ")
@SuppressWarnings({
					"all",
					"unchecked",
					"rawtypes"
})
public class PersistentLogins extends TableImpl<PersistentLoginsRecord> {

	private static final long serialVersionUID = -87543165;

	/**
	 * The reference instance of <code>public.persistent_logins</code>
	 */
	public static final PersistentLogins PERSISTENT_LOGINS = new PersistentLogins();

	/**
	 * The column <code>public.persistent_logins.username</code>.
	 */
	public final TableField<PersistentLoginsRecord, String> USERNAME = AbstractTable
			.createField("username", org.jooq.impl.SQLDataType.VARCHAR
					.length(64).nullable(false), this, "");

	/**
	 * The column <code>public.persistent_logins.series</code>.
	 */
	public final TableField<PersistentLoginsRecord, String> SERIES = AbstractTable
			.createField("series", org.jooq.impl.SQLDataType.VARCHAR.length(64)
					.nullable(false), this, "");

	/**
	 * The column <code>public.persistent_logins.token</code>.
	 */
	public final TableField<PersistentLoginsRecord, String> TOKEN = AbstractTable
			.createField("token", org.jooq.impl.SQLDataType.VARCHAR.length(64)
					.nullable(false), this, "");

	/**
	 * The column <code>public.persistent_logins.last_used</code>.
	 */
	public final TableField<PersistentLoginsRecord, Timestamp> LAST_USED = AbstractTable
			.createField("last_used",
					org.jooq.impl.SQLDataType.TIMESTAMP.nullable(false), this,
					"");

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
		this(alias, PersistentLogins.PERSISTENT_LOGINS);
	}

	private PersistentLogins(String alias,
			Table<PersistentLoginsRecord> aliased) {
		this(alias, aliased, null);
	}

	private PersistentLogins(String alias,
			Table<PersistentLoginsRecord> aliased, Field<?>[] parameters) {
		super(alias, Public.PUBLIC, aliased, parameters, "");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PersistentLogins as(String alias) {
		return new PersistentLogins(alias, this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<UniqueKey<PersistentLoginsRecord>> getKeys() {
		return Arrays.<UniqueKey<PersistentLoginsRecord>> asList(
				Keys.PERSISTENT_LOGINS_PKEY);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UniqueKey<PersistentLoginsRecord> getPrimaryKey() {
		return Keys.PERSISTENT_LOGINS_PKEY;
	}

	/**
	 * The class holding records for this type
	 */
	@Override
	public Class<PersistentLoginsRecord> getRecordType() {
		return PersistentLoginsRecord.class;
	}

	/**
	 * Rename this table
	 */
	public PersistentLogins rename(String name) {
		return new PersistentLogins(name, null);
	}
}
