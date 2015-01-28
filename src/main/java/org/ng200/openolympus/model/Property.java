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
package org.ng200.openolympus.model;

import java.io.Serializable;
import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@Table(name = "properties", indexes = {
                                       @Index(columnList = "propertyKey")
})
public class Property implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 6194515437094749571L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	@Column(name = "propertyKey", unique = true)
	private String key;
	@Column(name = "propertyValue", unique = true)
	private Serializable value;

	public Property() {

	}

	public Property(final String key, final Serializable value) {
		this.key = key;
		this.value = value;
	}

	@SuppressWarnings("unchecked")
	public <T> Optional<T> as() {
		return Optional.ofNullable((T) this.value);
	}

	public long getId() {
		return this.id;
	}

	public String getKey() {
		return this.key;
	}

	public Serializable getValue() {
		return this.value;
	}

	public void setId(final long id) {
		this.id = id;
	}

	public void setKey(final String key) {
		this.key = key;
	}

	public void setValue(final Serializable value) {
		this.value = value;
	}
}
