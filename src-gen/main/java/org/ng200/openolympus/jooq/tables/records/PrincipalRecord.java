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

import javax.annotation.Generated;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Row2;
import org.jooq.impl.UpdatableRecordImpl;
import org.ng200.openolympus.jooq.enums.GeneralPermissionType;
import org.ng200.openolympus.jooq.tables.Principal;
import org.ng200.openolympus.jooq.tables.interfaces.IPrincipal;

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
@Entity
@Table(name = "principal", schema = "public")
public class PrincipalRecord extends UpdatableRecordImpl<PrincipalRecord>
		implements Record2<Long, GeneralPermissionType[]>, IPrincipal {

	private static final long serialVersionUID = -1783300926;

	/**
	 * Create a detached PrincipalRecord
	 */
	public PrincipalRecord() {
		super(Principal.PRINCIPAL);
	}

	/**
	 * Create a detached, initialised PrincipalRecord
	 */
	public PrincipalRecord(Long id, GeneralPermissionType[] permissions) {
		super(Principal.PRINCIPAL);

		this.setValue(0, id);
		this.setValue(1, permissions);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Long> field1() {
		return Principal.PRINCIPAL.ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<GeneralPermissionType[]> field2() {
		return Principal.PRINCIPAL.PERMISSIONS;
	}

	// -------------------------------------------------------------------------
	// Primary key information
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Row2<Long, GeneralPermissionType[]> fieldsRow() {
		return (Row2) super.fieldsRow();
	}

	// -------------------------------------------------------------------------
	// Record2 type implementation
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void from(IPrincipal from) {
		this.setId(from.getId());
		this.setPermissions(from.getPermissions());
	}

	/**
	 * Getter for <code>public.principal.id</code>.
	 */
	@Id
	@Column(name = "id", unique = true, nullable = false, precision = 64)
	@Override
	public Long getId() {
		return (Long) this.getValue(0);
	}

	/**
	 * Getter for <code>public.principal.permissions</code>.
	 */
	@Column(name = "permissions", nullable = false)
	@Override
	public GeneralPermissionType[] getPermissions() {
		return (GeneralPermissionType[]) this.getValue(1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <E extends IPrincipal> E into(E into) {
		into.from(this);
		return into;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Record1<Long> key() {
		return (Record1) super.key();
	}

	/**
	 * Setter for <code>public.principal.id</code>.
	 */
	@Override
	public PrincipalRecord setId(Long value) {
		this.setValue(0, value);
		return this;
	}

	/**
	 * Setter for <code>public.principal.permissions</code>.
	 */
	@Override
	public PrincipalRecord setPermissions(GeneralPermissionType[] value) {
		this.setValue(1, value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Long value1() {
		return this.getId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PrincipalRecord value1(Long value) {
		this.setId(value);
		return this;
	}

	// -------------------------------------------------------------------------
	// FROM and INTO
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public GeneralPermissionType[] value2() {
		return this.getPermissions();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PrincipalRecord value2(GeneralPermissionType[] value) {
		this.setPermissions(value);
		return this;
	}

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PrincipalRecord values(Long value1, GeneralPermissionType[] value2) {
		this.value1(value1);
		this.value2(value2);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Row2<Long, GeneralPermissionType[]> valuesRow() {
		return (Row2) super.valuesRow();
	}
}
