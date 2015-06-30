/**
 * This class is generated by jOOQ
 */
package org.ng200.openolympus.jooq.tables.pojos;


import javax.annotation.Generated;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.ng200.openolympus.jooq.tables.interfaces.IProperty;


/**
 * This class is generated by jOOQ.
 */
@Generated(
	value = {
		"http://www.jooq.org",
		"jOOQ version:3.6.2"
	},
	comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
@Entity
@Table(name = "property", schema = "public")
public class Property implements IProperty {

	private static final long serialVersionUID = 1927779880;

	private Long   id;
	private String propertyKey;
	private byte[] propertyValue;

	public Property() {}

	public Property(Property value) {
		this.id = value.id;
		this.propertyKey = value.propertyKey;
		this.propertyValue = value.propertyValue;
	}

	public Property(
		Long   id,
		String propertyKey,
		byte[] propertyValue
	) {
		this.id = id;
		this.propertyKey = propertyKey;
		this.propertyValue = propertyValue;
	}

	@Id
	@Column(name = "id", unique = true, nullable = false, precision = 64)
	@Override
	public Long getId() {
		return this.id;
	}

	@Override
	public Property setId(Long id) {
		this.id = id;
		return this;
	}

	@Column(name = "property_key", unique = true, length = 255)
	@Override
	public String getPropertyKey() {
		return this.propertyKey;
	}

	@Override
	public Property setPropertyKey(String propertyKey) {
		this.propertyKey = propertyKey;
		return this;
	}

	@Column(name = "property_value", unique = true)
	@Override
	public byte[] getPropertyValue() {
		return this.propertyValue;
	}

	@Override
	public Property setPropertyValue(byte[] propertyValue) {
		this.propertyValue = propertyValue;
		return this;
	}

	// -------------------------------------------------------------------------
	// FROM and INTO
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void from(IProperty from) {
		setId(from.getId());
		setPropertyKey(from.getPropertyKey());
		setPropertyValue(from.getPropertyValue());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <E extends IProperty> E into(E into) {
		into.from(this);
		return into;
	}
}
