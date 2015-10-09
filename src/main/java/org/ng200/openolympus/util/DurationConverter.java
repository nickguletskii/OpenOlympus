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

import java.time.Duration;

import org.jooq.Converter;

public class DurationConverter implements Converter<Long, Duration> {

	/**
	 *
	 */
	private static final long serialVersionUID = -2243242623779870370L;

	@Override
	public Duration from(Long databaseObject) {
		if (databaseObject == null)
			return null;
		return Duration.ofMillis(databaseObject);
	}

	@Override
	public Class<Long> fromType() {
		return Long.class;
	}

	@Override
	public Long to(Duration userObject) {
		if(userObject == null)
			return null;
		return userObject.toMillis();
	}

	@Override
	public Class<Duration> toType() {
		return Duration.class;
	}

}