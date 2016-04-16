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
		"jOOQ version:3.7.2"
	},
	comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
@Entity
@Table(name = "property", schema = "public")
public class Property implements IProperty {

	private static final long serialVersionUID = -624960886;

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

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("Property (");

		sb.append(id);
		sb.append(", ").append(propertyKey);
		sb.append(", ").append("[binary...]");

		sb.append(")");
		return sb.toString();
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
