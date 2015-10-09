/**
 * This class is generated by jOOQ
 */
package org.ng200.openolympus.jooq.tables.daos;


import java.util.List;

import javax.annotation.Generated;

import org.jooq.Configuration;
import org.jooq.impl.DAOImpl;
import org.ng200.openolympus.jooq.tables.Property;
import org.ng200.openolympus.jooq.tables.records.PropertyRecord;


/**
 * This class is generated by jOOQ.
 */
@Generated(
	value = {
		"http://www.jooq.org",
		"jOOQ version:3.7.0"
	},
	comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class PropertyDao extends DAOImpl<PropertyRecord, org.ng200.openolympus.jooq.tables.pojos.Property, Long> {

	/**
	 * Create a new PropertyDao without any configuration
	 */
	public PropertyDao() {
		super(Property.PROPERTY, org.ng200.openolympus.jooq.tables.pojos.Property.class);
	}

	/**
	 * Create a new PropertyDao with an attached configuration
	 */
	public PropertyDao(Configuration configuration) {
		super(Property.PROPERTY, org.ng200.openolympus.jooq.tables.pojos.Property.class, configuration);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Long getId(org.ng200.openolympus.jooq.tables.pojos.Property object) {
		return object.getId();
	}

	/**
	 * Fetch records that have <code>id IN (values)</code>
	 */
	public List<org.ng200.openolympus.jooq.tables.pojos.Property> fetchById(Long... values) {
		return fetch(Property.PROPERTY.ID, values);
	}

	/**
	 * Fetch a unique record that has <code>id = value</code>
	 */
	public org.ng200.openolympus.jooq.tables.pojos.Property fetchOneById(Long value) {
		return fetchOne(Property.PROPERTY.ID, value);
	}

	/**
	 * Fetch records that have <code>property_key IN (values)</code>
	 */
	public List<org.ng200.openolympus.jooq.tables.pojos.Property> fetchByPropertyKey(String... values) {
		return fetch(Property.PROPERTY.PROPERTY_KEY, values);
	}

	/**
	 * Fetch a unique record that has <code>property_key = value</code>
	 */
	public org.ng200.openolympus.jooq.tables.pojos.Property fetchOneByPropertyKey(String value) {
		return fetchOne(Property.PROPERTY.PROPERTY_KEY, value);
	}

	/**
	 * Fetch records that have <code>property_value IN (values)</code>
	 */
	public List<org.ng200.openolympus.jooq.tables.pojos.Property> fetchByPropertyValue(byte[]... values) {
		return fetch(Property.PROPERTY.PROPERTY_VALUE, values);
	}

	/**
	 * Fetch a unique record that has <code>property_value = value</code>
	 */
	public org.ng200.openolympus.jooq.tables.pojos.Property fetchOneByPropertyValue(byte[] value) {
		return fetchOne(Property.PROPERTY.PROPERTY_VALUE, value);
	}
	public org.ng200.openolympus.jooq.tables.pojos.Property fetchOneById(String id) {
		return fetchOneById(java.lang.Long.valueOf(id));
	}
}
