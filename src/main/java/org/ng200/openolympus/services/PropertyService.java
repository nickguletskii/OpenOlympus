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
package org.ng200.openolympus.services;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.jooq.DSLContext;
import org.ng200.openolympus.jooq.Tables;
import org.ng200.openolympus.jooq.tables.daos.PropertyDao;
import org.ng200.openolympus.jooq.tables.pojos.Property;
import org.ng200.openolympus.jooq.tables.records.PropertyRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PropertyService {

	@Autowired
	private PropertyDao propertyDao;

	@Autowired
	private DSLContext dslContext;

	private Object deserializeObject(byte[] obj) throws IOException,
			ClassNotFoundException {
		try (ByteArrayInputStream b = new ByteArrayInputStream(obj);
				ObjectInputStream o = new ObjectInputStream(b);) {
			return o.readObject();
		}
	}

	public Property get(final String key, final Serializable defaultValue)
			throws IOException {
		Property property = this.propertyDao.fetchOneByPropertyKey(key);
		if (property == null) {
			property = new Property(null, key,
					this.serializeObject(defaultValue));
			this.propertyDao.insert(property);
		}
		return property;
	}

	private byte[] serializeObject(Serializable serializable)
			throws IOException {
		try (ByteArrayOutputStream b = new ByteArrayOutputStream();
				ObjectOutputStream o = new ObjectOutputStream(b);) {
			o.writeObject(serializable);
			return b.toByteArray();
		}
	}

	public void set(final String key, final Serializable value)
			throws IOException {
		final Property property = this.get(key, value);
		property.setPropertyValue(this.serializeObject(value));
		final PropertyRecord propertyRecord = this.dslContext
				.newRecord(Tables.PROPERTY);
		propertyRecord.from(property);
		propertyRecord.store();
	}
}
