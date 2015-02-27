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

/**
 * This class is generated by jOOQ.
 */
@javax.annotation.Generated(
	value = {
		"http://www.jooq.org",
		"jOOQ version:3.5.0"
	},
	comments = "This class is generated by jOOQ"
)
@java.lang.SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class ContestAtForUser extends org.jooq.impl.TableImpl<org.ng200.openolympus.jooq.tables.records.ContestAtForUserRecord> {

	private static final long serialVersionUID = -1131257637;

	/**
	 * The reference instance of <code>public.contest_at_for_user</code>
	 */
	public static final org.ng200.openolympus.jooq.tables.ContestAtForUser CONTEST_AT_FOR_USER = new org.ng200.openolympus.jooq.tables.ContestAtForUser();

	/**
	 * The class holding records for this type
	 */
	@Override
	public java.lang.Class<org.ng200.openolympus.jooq.tables.records.ContestAtForUserRecord> getRecordType() {
		return org.ng200.openolympus.jooq.tables.records.ContestAtForUserRecord.class;
	}

	/**
	 * The column <code>public.contest_at_for_user.f1</code>.
	 */
	public final org.jooq.TableField<org.ng200.openolympus.jooq.tables.records.ContestAtForUserRecord, java.lang.Long> F1 = createField("f1", org.jooq.impl.SQLDataType.BIGINT, this, "");

	/**
	 * Create a <code>public.contest_at_for_user</code> table reference
	 */
	public ContestAtForUser() {
		this("contest_at_for_user", null);
	}

	/**
	 * Create an aliased <code>public.contest_at_for_user</code> table reference
	 */
	public ContestAtForUser(java.lang.String alias) {
		this(alias, org.ng200.openolympus.jooq.tables.ContestAtForUser.CONTEST_AT_FOR_USER);
	}

	private ContestAtForUser(java.lang.String alias, org.jooq.Table<org.ng200.openolympus.jooq.tables.records.ContestAtForUserRecord> aliased) {
		this(alias, aliased, null);
	}

	private ContestAtForUser(java.lang.String alias, org.jooq.Table<org.ng200.openolympus.jooq.tables.records.ContestAtForUserRecord> aliased, org.jooq.Field<?>[] parameters) {
		super(alias, org.ng200.openolympus.jooq.Public.PUBLIC, aliased, parameters, "");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.ng200.openolympus.jooq.tables.ContestAtForUser as(java.lang.String alias) {
		return new org.ng200.openolympus.jooq.tables.ContestAtForUser(alias, this, parameters);
	}

	/**
	 * Rename this table
	 */
	public org.ng200.openolympus.jooq.tables.ContestAtForUser rename(java.lang.String name) {
		return new org.ng200.openolympus.jooq.tables.ContestAtForUser(name, null, parameters);
	}

	/**
	 * Call this table-valued function
	 */
	public org.ng200.openolympus.jooq.tables.ContestAtForUser call(java.sql.Timestamp __1, java.lang.Long __2) {
		return new org.ng200.openolympus.jooq.tables.ContestAtForUser(getName(), null, new org.jooq.Field[] { org.jooq.impl.DSL.val(__1), org.jooq.impl.DSL.val(__2) });
	}

	/**
	 * Call this table-valued function
	 */
	public org.ng200.openolympus.jooq.tables.ContestAtForUser call(org.jooq.Field<java.sql.Timestamp> __1, org.jooq.Field<java.lang.Long> __2) {
		return new org.ng200.openolympus.jooq.tables.ContestAtForUser(getName(), null, new org.jooq.Field[] { __1, __2 });
	}
}
