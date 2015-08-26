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

import org.jooq.DSLContext;
import org.jooq.impl.TableImpl;
import org.jooq.impl.UpdatableRecordImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.google.common.base.Throwables;

public abstract class GenericCreateUpdateRepository {

	private static final Logger logger = LoggerFactory
	        .getLogger(GenericCreateUpdateRepository.class);

	@Autowired
	private DSLContext dslContext;

	@SuppressWarnings("unchecked")
	protected <T, R extends UpdatableRecordImpl<?>> T insert(T value,
	        TableImpl<R> table) {
		try {
			R record;
			record = table.getRecordType().newInstance();
			record.attach(this.dslContext.configuration());
			record.from(value);
			record.store();
			return (T) record.into(value.getClass());
		} catch (InstantiationException | IllegalAccessException e) {
			throw Throwables.propagate(e);
		}
	}

	@SuppressWarnings("unchecked")
	protected <T, R extends UpdatableRecordImpl<?>> T update(T value,
	        TableImpl<R> table) {
		try {
			R record;
			record = table.getRecordType().newInstance();
			record.attach(this.dslContext.configuration());
			record.from(value);
			record.update();
			return (T) record.into(value.getClass());
		} catch (InstantiationException | IllegalAccessException e) {
			throw Throwables.propagate(e);
		}
	}

}
