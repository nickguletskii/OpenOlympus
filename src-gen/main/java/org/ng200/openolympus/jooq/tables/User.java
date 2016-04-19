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


import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Identity;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.TableImpl;
import org.ng200.openolympus.jooq.Keys;
import org.ng200.openolympus.jooq.Public;
import org.ng200.openolympus.jooq.tables.records.UserRecord;


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
public class User extends TableImpl<UserRecord> {

	private static final long serialVersionUID = 1843223046;

	/**
	 * The reference instance of <code>public.USER</code>
	 */
	public static final User USER = new User();

	/**
	 * The class holding records for this type
	 */
	@Override
	public Class<UserRecord> getRecordType() {
		return UserRecord.class;
	}

	/**
	 * The column <code>public.USER.id</code>.
	 */
	public final TableField<UserRecord, Long> ID = createField("id", org.jooq.impl.SQLDataType.BIGINT.nullable(false).defaulted(true), this, "");

	/**
	 * The column <code>public.USER.username</code>.
	 */
	public final TableField<UserRecord, String> USERNAME = createField("username", org.jooq.impl.SQLDataType.VARCHAR.length(255), this, "");

	/**
	 * The column <code>public.USER.first_name_main</code>.
	 */
	public final TableField<UserRecord, String> FIRST_NAME_MAIN = createField("first_name_main", org.jooq.impl.SQLDataType.VARCHAR.length(255), this, "");

	/**
	 * The column <code>public.USER.address_city</code>.
	 */
	public final TableField<UserRecord, String> ADDRESS_CITY = createField("address_city", org.jooq.impl.SQLDataType.VARCHAR.length(255), this, "");

	/**
	 * The column <code>public.USER.address_country</code>.
	 */
	public final TableField<UserRecord, String> ADDRESS_COUNTRY = createField("address_country", org.jooq.impl.SQLDataType.VARCHAR.length(255), this, "");

	/**
	 * The column <code>public.USER.address_line1</code>.
	 */
	public final TableField<UserRecord, String> ADDRESS_LINE1 = createField("address_line1", org.jooq.impl.SQLDataType.CLOB, this, "");

	/**
	 * The column <code>public.USER.address_line2</code>.
	 */
	public final TableField<UserRecord, String> ADDRESS_LINE2 = createField("address_line2", org.jooq.impl.SQLDataType.CLOB, this, "");

	/**
	 * The column <code>public.USER.address_state</code>.
	 */
	public final TableField<UserRecord, String> ADDRESS_STATE = createField("address_state", org.jooq.impl.SQLDataType.VARCHAR.length(255), this, "");

	/**
	 * The column <code>public.USER.approval_email_sent</code>.
	 */
	public final TableField<UserRecord, Boolean> APPROVAL_EMAIL_SENT = createField("approval_email_sent", org.jooq.impl.SQLDataType.BOOLEAN.nullable(false), this, "");

	/**
	 * The column <code>public.USER.birth_date</code>.
	 */
	public final TableField<UserRecord, LocalDate> BIRTH_DATE = createField("birth_date", org.jooq.impl.SQLDataType.LOCALDATE, this, "");

	/**
	 * The column <code>public.USER.email_address</code>.
	 */
	public final TableField<UserRecord, String> EMAIL_ADDRESS = createField("email_address", org.jooq.impl.SQLDataType.VARCHAR.length(255), this, "");

	/**
	 * The column <code>public.USER.email_confirmation_token</code>.
	 */
	public final TableField<UserRecord, String> EMAIL_CONFIRMATION_TOKEN = createField("email_confirmation_token", org.jooq.impl.SQLDataType.VARCHAR.length(255), this, "");

	/**
	 * The column <code>public.USER.enabled</code>.
	 */
	public final TableField<UserRecord, Boolean> ENABLED = createField("enabled", org.jooq.impl.SQLDataType.BOOLEAN.nullable(false), this, "");

	/**
	 * The column <code>public.USER.first_name_localised</code>.
	 */
	public final TableField<UserRecord, String> FIRST_NAME_LOCALISED = createField("first_name_localised", org.jooq.impl.SQLDataType.VARCHAR.length(255), this, "");

	/**
	 * The column <code>public.USER.landline</code>.
	 */
	public final TableField<UserRecord, String> LANDLINE = createField("landline", org.jooq.impl.SQLDataType.VARCHAR.length(255), this, "");

	/**
	 * The column <code>public.USER.last_name_localised</code>.
	 */
	public final TableField<UserRecord, String> LAST_NAME_LOCALISED = createField("last_name_localised", org.jooq.impl.SQLDataType.VARCHAR.length(255), this, "");

	/**
	 * The column <code>public.USER.last_name_main</code>.
	 */
	public final TableField<UserRecord, String> LAST_NAME_MAIN = createField("last_name_main", org.jooq.impl.SQLDataType.VARCHAR.length(255), this, "");

	/**
	 * The column <code>public.USER.middle_name_localised</code>.
	 */
	public final TableField<UserRecord, String> MIDDLE_NAME_LOCALISED = createField("middle_name_localised", org.jooq.impl.SQLDataType.VARCHAR.length(255), this, "");

	/**
	 * The column <code>public.USER.middle_name_main</code>.
	 */
	public final TableField<UserRecord, String> MIDDLE_NAME_MAIN = createField("middle_name_main", org.jooq.impl.SQLDataType.VARCHAR.length(255), this, "");

	/**
	 * The column <code>public.USER.mobile</code>.
	 */
	public final TableField<UserRecord, String> MOBILE = createField("mobile", org.jooq.impl.SQLDataType.VARCHAR.length(255), this, "");

	/**
	 * The column <code>public.USER.password</code>.
	 */
	public final TableField<UserRecord, String> PASSWORD = createField("password", org.jooq.impl.SQLDataType.VARCHAR.length(255), this, "");

	/**
	 * The column <code>public.USER.school</code>.
	 */
	public final TableField<UserRecord, String> SCHOOL = createField("school", org.jooq.impl.SQLDataType.VARCHAR.length(255), this, "");

	/**
	 * The column <code>public.USER.teacher_first_name</code>.
	 */
	public final TableField<UserRecord, String> TEACHER_FIRST_NAME = createField("teacher_first_name", org.jooq.impl.SQLDataType.VARCHAR.length(255), this, "");

	/**
	 * The column <code>public.USER.teacher_last_name</code>.
	 */
	public final TableField<UserRecord, String> TEACHER_LAST_NAME = createField("teacher_last_name", org.jooq.impl.SQLDataType.VARCHAR.length(255), this, "");

	/**
	 * The column <code>public.USER.teacher_middle_name</code>.
	 */
	public final TableField<UserRecord, String> TEACHER_MIDDLE_NAME = createField("teacher_middle_name", org.jooq.impl.SQLDataType.VARCHAR.length(255), this, "");

	/**
	 * The column <code>public.USER.superuser</code>.
	 */
	public final TableField<UserRecord, Boolean> SUPERUSER = createField("superuser", org.jooq.impl.SQLDataType.BOOLEAN.nullable(false).defaulted(true), this, "");

	/**
	 * The column <code>public.USER.approved</code>.
	 */
	public final TableField<UserRecord, Boolean> APPROVED = createField("approved", org.jooq.impl.SQLDataType.BOOLEAN.nullable(false).defaulted(true), this, "");

	/**
	 * Create a <code>public.USER</code> table reference
	 */
	public User() {
		this("USER", null);
	}

	/**
	 * Create an aliased <code>public.USER</code> table reference
	 */
	public User(String alias) {
		this(alias, USER);
	}

	private User(String alias, Table<UserRecord> aliased) {
		this(alias, aliased, null);
	}

	private User(String alias, Table<UserRecord> aliased, Field<?>[] parameters) {
		super(alias, Public.PUBLIC, aliased, parameters, "");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Identity<UserRecord, Long> getIdentity() {
		return Keys.IDENTITY_USER;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UniqueKey<UserRecord> getPrimaryKey() {
		return Keys.USER_PK;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<UniqueKey<UserRecord>> getKeys() {
		return Arrays.<UniqueKey<UserRecord>>asList(Keys.USER_PK, Keys.UK_R43AF9AP4EDM43MMTQ01ODDJ6);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ForeignKey<UserRecord, ?>> getReferences() {
		return Arrays.<ForeignKey<UserRecord, ?>>asList(Keys.USER__USER_PRINCIPAL_ID_MAPPING);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public User as(String alias) {
		return new User(alias, this);
	}

	/**
	 * Rename this table
	 */
	public User rename(String name) {
		return new User(name, null);
	}
}
