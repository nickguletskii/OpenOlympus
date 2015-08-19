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
package org.ng200.openolympus.util;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.jooq.Converter;

public class DateTimeConverter implements Converter<Timestamp, LocalDateTime> {

	/**
	 *
	 */
	private static final long serialVersionUID = -4748273119941258073L;

	@Override
	public LocalDateTime from(Timestamp databaseObject) {
		return databaseObject.toLocalDateTime();
	}

	@Override
	public Class<Timestamp> fromType() {
		return Timestamp.class;
	}

	@Override
	public Timestamp to(LocalDateTime userObject) {
		return Timestamp.valueOf(userObject);
	}

	@Override
	public Class<LocalDateTime> toType() {
		return LocalDateTime.class;
	}

}