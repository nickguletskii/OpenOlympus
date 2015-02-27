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
package org.ng200.openolympus;

import java.math.BigDecimal;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ToStringMatcher<T> extends BaseMatcher<Object> {

	public static ToStringMatcher<BigDecimal> compareBigDecimals(
			BigDecimal decimal) {
		return ToStringMatcher.toStringEquals(decimal, x -> new BigDecimal(x),
				(x, y) -> x.compareTo(y) == 0);
	}

	public static ToStringMatcher<BigDecimal> compareBigDecimals(String decimal) {
		return ToStringMatcher.compareBigDecimals(new BigDecimal(decimal));
	}

	public static ToStringMatcher<String> toStringEquals(Object obj) {
		return ToStringMatcher.toStringEquals(obj, x -> x);
	}

	public static <T> ToStringMatcher<T> toStringEquals(Object obj,
			Function<String, T> func) {
		return new ToStringMatcher<T>(obj, func, (x, y) -> x.equals(y));
	}

	public static <T> ToStringMatcher<T> toStringEquals(Object obj,
			Function<String, T> func, BiFunction<T, T, Boolean> matchesFunc) {
		return new ToStringMatcher<T>(obj, func, matchesFunc);
	}

	private static final Logger logger = LoggerFactory
			.getLogger(ToStringMatcher.class);

	private final Object obj;

	private final Function<String, T> func;

	private final BiFunction<T, T, Boolean> matchesFunc;

	public ToStringMatcher(Object obj, Function<String, T> func,
			BiFunction<T, T, Boolean> matchesFunc) {
		this.obj = obj;
		this.func = func;
		this.matchesFunc = matchesFunc;
	}

	@Override
	public void describeTo(Description description) {
		description.appendText("toStringMatches(");
		description.appendValue(this.obj);
		description.appendText(")");
	}

	@Override
	public boolean matches(Object item) {
		final T orig = this.func.apply(this.obj.toString());
		final T expected = this.func.apply(item.toString());
		return this.matchesFunc.apply(orig, expected);
	}
}
