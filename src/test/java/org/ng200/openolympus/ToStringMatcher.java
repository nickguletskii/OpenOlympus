package org.ng200.openolympus;

import java.math.BigDecimal;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ToStringMatcher<T> extends BaseMatcher<Object> {

	private static final Logger logger = LoggerFactory
			.getLogger(ToStringMatcher.class);
	private Object obj;
	private Function<String, T> func;
	private BiFunction<T, T, Boolean> matchesFunc;

	public ToStringMatcher(Object obj, Function<String, T> func,
			BiFunction<T, T, Boolean> matchesFunc) {
		this.obj = obj;
		this.func = func;
		this.matchesFunc = matchesFunc;
	}

	@Override
	public void describeTo(Description description) {
		description.appendText("toStringMatches(");
		description.appendValue(obj);
		description.appendText(")");
	}

	@Override
	public boolean matches(Object item) {
		T orig = func.apply(obj.toString());
		T expected = func.apply(item.toString());
		return matchesFunc.apply(orig, expected);
	}

	public static ToStringMatcher<String> toStringEquals(Object obj) {
		return toStringEquals(obj, x -> x);
	}

	public static <T> ToStringMatcher<T> toStringEquals(Object obj,
			Function<String, T> func) {
		return new ToStringMatcher<T>(obj, func, (x, y) -> x.equals(y));
	}

	public static <T> ToStringMatcher<T> toStringEquals(Object obj,
			Function<String, T> func, BiFunction<T, T, Boolean> matchesFunc) {
		return new ToStringMatcher<T>(obj, func, matchesFunc);
	}

	public static ToStringMatcher<BigDecimal> compareBigDecimals(
			BigDecimal decimal) {
		return toStringEquals(decimal, x -> new BigDecimal(x),
				(x, y) -> x.compareTo(y) == 0);
	}

	public static ToStringMatcher<BigDecimal> compareBigDecimals(String decimal) {
		return compareBigDecimals(new BigDecimal(decimal));
	}
}
