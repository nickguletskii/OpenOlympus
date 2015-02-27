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
package org.ng200.openolympus.jooq.tables.records;

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
public class UsersRolesRecord extends org.jooq.impl.UpdatableRecordImpl<org.ng200.openolympus.jooq.tables.records.UsersRolesRecord> implements org.jooq.Record2<java.lang.Long, java.lang.Long> {

	private static final long serialVersionUID = 954153835;

	/**
	 * Setter for <code>public.users_roles.users_id</code>.
	 */
	public void setUsersId(java.lang.Long value) {
		setValue(0, value);
	}

	/**
	 * Getter for <code>public.users_roles.users_id</code>.
	 */
	public java.lang.Long getUsersId() {
		return (java.lang.Long) getValue(0);
	}

	/**
	 * Setter for <code>public.users_roles.roles_id</code>.
	 */
	public void setRolesId(java.lang.Long value) {
		setValue(1, value);
	}

	/**
	 * Getter for <code>public.users_roles.roles_id</code>.
	 */
	public java.lang.Long getRolesId() {
		return (java.lang.Long) getValue(1);
	}

	// -------------------------------------------------------------------------
	// Primary key information
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Record2<java.lang.Long, java.lang.Long> key() {
		return (org.jooq.Record2) super.key();
	}

	// -------------------------------------------------------------------------
	// Record2 type implementation
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Row2<java.lang.Long, java.lang.Long> fieldsRow() {
		return (org.jooq.Row2) super.fieldsRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Row2<java.lang.Long, java.lang.Long> valuesRow() {
		return (org.jooq.Row2) super.valuesRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Long> field1() {
		return org.ng200.openolympus.jooq.tables.UsersRoles.USERS_ROLES.USERS_ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Long> field2() {
		return org.ng200.openolympus.jooq.tables.UsersRoles.USERS_ROLES.ROLES_ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Long value1() {
		return getUsersId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Long value2() {
		return getRolesId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UsersRolesRecord value1(java.lang.Long value) {
		setUsersId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UsersRolesRecord value2(java.lang.Long value) {
		setRolesId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UsersRolesRecord values(java.lang.Long value1, java.lang.Long value2) {
		return this;
	}

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * Create a detached UsersRolesRecord
	 */
	public UsersRolesRecord() {
		super(org.ng200.openolympus.jooq.tables.UsersRoles.USERS_ROLES);
	}

	/**
	 * Create a detached, initialised UsersRolesRecord
	 */
	public UsersRolesRecord(java.lang.Long usersId, java.lang.Long rolesId) {
		super(org.ng200.openolympus.jooq.tables.UsersRoles.USERS_ROLES);

		setValue(0, usersId);
		setValue(1, rolesId);
	}
}
