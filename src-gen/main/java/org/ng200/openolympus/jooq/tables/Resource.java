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

import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Identity;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.impl.TableImpl;
import org.ng200.openolympus.jooq.Keys;
import org.ng200.openolympus.jooq.Public;
import org.ng200.openolympus.jooq.tables.records.ResourceRecord;

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
public class Resource extends TableImpl<ResourceRecord> {

	private static final long serialVersionUID = 683939142;

	/**
	 * The reference instance of <code>public.resource</code>
	 */
	public static final Resource RESOURCE = new Resource();

	/**
	 * The column <code>public.resource.id</code>.
	 */
	public final TableField<ResourceRecord, Long> ID = AbstractTable
			.createField("id", org.jooq.impl.SQLDataType.BIGINT.nullable(false)
					.defaulted(true), this, "");

	/**
	 * The column <code>public.resource.name</code>.
	 */
	public final TableField<ResourceRecord, String> NAME = AbstractTable
			.createField("name", org.jooq.impl.SQLDataType.CLOB.nullable(false),
					this, "");

	/**
	 * The column <code>public.resource.filename</code>.
	 */
	public final TableField<ResourceRecord, String> FILENAME = AbstractTable
			.createField("filename",
					org.jooq.impl.SQLDataType.CLOB.nullable(false), this, "");

	/**
	 * The column <code>public.resource.USER_id</code>.
	 */
	public final TableField<ResourceRecord, Long> USER_ID = AbstractTable
			.createField("USER_id", org.jooq.impl.SQLDataType.BIGINT, this, "");

	/**
	 * The column <code>public.resource.task_id</code>.
	 */
	public final TableField<ResourceRecord, Integer> TASK_ID = AbstractTable
			.createField("task_id", org.jooq.impl.SQLDataType.INTEGER, this,
					"");

	/**
	 * Create a <code>public.resource</code> table reference
	 */
	public Resource() {
		this("resource", null);
	}

	/**
	 * Create an aliased <code>public.resource</code> table reference
	 */
	public Resource(String alias) {
		this(alias, Resource.RESOURCE);
	}

	private Resource(String alias, Table<ResourceRecord> aliased) {
		this(alias, aliased, null);
	}

	private Resource(String alias, Table<ResourceRecord> aliased,
			Field<?>[] parameters) {
		super(alias, Public.PUBLIC, aliased, parameters, "");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Resource as(String alias) {
		return new Resource(alias, this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Identity<ResourceRecord, Long> getIdentity() {
		return Keys.IDENTITY_RESOURCE;
	}

	/**
	 * The class holding records for this type
	 */
	@Override
	public Class<ResourceRecord> getRecordType() {
		return ResourceRecord.class;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ForeignKey<ResourceRecord, ?>> getReferences() {
		return Arrays.<ForeignKey<ResourceRecord, ?>> asList(
				Keys.RESOURCE__USER_FK, Keys.RESOURCE__TASK_FK);
	}

	/**
	 * Rename this table
	 */
	public Resource rename(String name) {
		return new Resource(name, null);
	}
}
