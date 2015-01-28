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
public class PropertiesRecord extends org.jooq.impl.UpdatableRecordImpl<org.ng200.openolympus.jooq.tables.records.PropertiesRecord> implements org.jooq.Record3<java.lang.Long, java.lang.String, byte[]> {

	private static final long serialVersionUID = -1503611429;

	/**
	 * Setter for <code>public.properties.id</code>.
	 */
	public void setId(java.lang.Long value) {
		setValue(0, value);
	}

	/**
	 * Getter for <code>public.properties.id</code>.
	 */
	public java.lang.Long getId() {
		return (java.lang.Long) getValue(0);
	}

	/**
	 * Setter for <code>public.properties.property_key</code>.
	 */
	public void setPropertyKey(java.lang.String value) {
		setValue(1, value);
	}

	/**
	 * Getter for <code>public.properties.property_key</code>.
	 */
	public java.lang.String getPropertyKey() {
		return (java.lang.String) getValue(1);
	}

	/**
	 * Setter for <code>public.properties.property_value</code>.
	 */
	public void setPropertyValue(byte[] value) {
		setValue(2, value);
	}

	/**
	 * Getter for <code>public.properties.property_value</code>.
	 */
	public byte[] getPropertyValue() {
		return (byte[]) getValue(2);
	}

	// -------------------------------------------------------------------------
	// Primary key information
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Record1<java.lang.Long> key() {
		return (org.jooq.Record1) super.key();
	}

	// -------------------------------------------------------------------------
	// Record3 type implementation
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Row3<java.lang.Long, java.lang.String, byte[]> fieldsRow() {
		return (org.jooq.Row3) super.fieldsRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Row3<java.lang.Long, java.lang.String, byte[]> valuesRow() {
		return (org.jooq.Row3) super.valuesRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Long> field1() {
		return org.ng200.openolympus.jooq.tables.Properties.PROPERTIES.ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.String> field2() {
		return org.ng200.openolympus.jooq.tables.Properties.PROPERTIES.PROPERTY_KEY;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<byte[]> field3() {
		return org.ng200.openolympus.jooq.tables.Properties.PROPERTIES.PROPERTY_VALUE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Long value1() {
		return getId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.String value2() {
		return getPropertyKey();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public byte[] value3() {
		return getPropertyValue();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PropertiesRecord value1(java.lang.Long value) {
		setId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PropertiesRecord value2(java.lang.String value) {
		setPropertyKey(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PropertiesRecord value3(byte[] value) {
		setPropertyValue(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PropertiesRecord values(java.lang.Long value1, java.lang.String value2, byte[] value3) {
		return this;
	}

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * Create a detached PropertiesRecord
	 */
	public PropertiesRecord() {
		super(org.ng200.openolympus.jooq.tables.Properties.PROPERTIES);
	}

	/**
	 * Create a detached, initialised PropertiesRecord
	 */
	public PropertiesRecord(java.lang.Long id, java.lang.String propertyKey, byte[] propertyValue) {
		super(org.ng200.openolympus.jooq.tables.Properties.PROPERTIES);

		setValue(0, id);
		setValue(1, propertyKey);
		setValue(2, propertyValue);
	}
}
