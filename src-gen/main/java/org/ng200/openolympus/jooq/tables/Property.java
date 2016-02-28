/**
 * This class is generated by jOOQ
 */
package org.ng200.openolympus.jooq.tables;


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
import org.ng200.openolympus.jooq.tables.records.PropertyRecord;


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
public class Property extends TableImpl<PropertyRecord> {

	private static final long serialVersionUID = -890047110;

	/**
	 * The reference instance of <code>public.property</code>
	 */
	public static final Property PROPERTY = new Property();

	/**
	 * The class holding records for this type
	 */
	@Override
	public Class<PropertyRecord> getRecordType() {
		return PropertyRecord.class;
	}

	/**
	 * The column <code>public.property.id</code>.
	 */
	public final TableField<PropertyRecord, Long> ID = createField("id", org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "");

	/**
	 * The column <code>public.property.property_key</code>.
	 */
	public final TableField<PropertyRecord, String> PROPERTY_KEY = createField("property_key", org.jooq.impl.SQLDataType.VARCHAR.length(255), this, "");

	/**
	 * The column <code>public.property.property_value</code>.
	 */
	public final TableField<PropertyRecord, byte[]> PROPERTY_VALUE = createField("property_value", org.jooq.impl.SQLDataType.BLOB, this, "");

	/**
	 * Create a <code>public.property</code> table reference
	 */
	public Property() {
		this("property", null);
	}

	/**
	 * Create an aliased <code>public.property</code> table reference
	 */
	public Property(String alias) {
		this(alias, PROPERTY);
	}

	private Property(String alias, Table<PropertyRecord> aliased) {
		this(alias, aliased, null);
	}

	private Property(String alias, Table<PropertyRecord> aliased, Field<?>[] parameters) {
		super(alias, Public.PUBLIC, aliased, parameters, "");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UniqueKey<PropertyRecord> getPrimaryKey() {
		return Keys.PROPERTY_PKEY;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<UniqueKey<PropertyRecord>> getKeys() {
		return Arrays.<UniqueKey<PropertyRecord>>asList(Keys.PROPERTY_PKEY, Keys.UK_8JYTV8TU3PUI7RAM00B44TN4U, Keys.UK_4B6VATGJ30955XSJR51YEGXI9);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Property as(String alias) {
		return new Property(alias, this);
	}

	/**
	 * Rename this table
	 */
	public Property rename(String name) {
		return new Property(name, null);
	}
}
