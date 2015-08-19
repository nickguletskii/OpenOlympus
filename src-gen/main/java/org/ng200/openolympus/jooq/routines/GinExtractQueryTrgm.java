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
/**
 * This class is generated by jOOQ
 */
package org.ng200.openolympus.jooq.routines;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Parameter;
import org.jooq.impl.AbstractRoutine;
import org.ng200.openolympus.jooq.Public;

/**
 * This class is generated by jOOQ.
 */
@Generated(value = {
						"http://www.jooq.org",
						"jOOQ version:3.6.2"
}, comments = "This class is generated by jOOQ")
@SuppressWarnings({
					"all",
					"unchecked",
					"rawtypes"
})
public class GinExtractQueryTrgm extends AbstractRoutine<Object> {

	private static final long serialVersionUID = -1714959705;

	/**
	 * The parameter <code>public.gin_extract_query_trgm.RETURN_VALUE</code>.
	 */
	public static final Parameter<Object> RETURN_VALUE = AbstractRoutine
			.createParameter("RETURN_VALUE", org.jooq.impl.DefaultDataType
					.getDefaultDataType("internal"), false);

	/**
	 * The parameter <code>public.gin_extract_query_trgm._1</code>.
	 */
	public static final Parameter<String> _1 = AbstractRoutine
			.createParameter("_1", org.jooq.impl.SQLDataType.CLOB, false);

	/**
	 * The parameter <code>public.gin_extract_query_trgm._2</code>.
	 */
	public static final Parameter<Object> _2 = AbstractRoutine.createParameter(
			"_2", org.jooq.impl.DefaultDataType.getDefaultDataType("internal"),
			false);

	/**
	 * The parameter <code>public.gin_extract_query_trgm._3</code>.
	 */
	public static final Parameter<Short> _3 = AbstractRoutine
			.createParameter("_3", org.jooq.impl.SQLDataType.SMALLINT, false);

	/**
	 * The parameter <code>public.gin_extract_query_trgm._4</code>.
	 */
	public static final Parameter<Object> _4 = AbstractRoutine.createParameter(
			"_4", org.jooq.impl.DefaultDataType.getDefaultDataType("internal"),
			false);

	/**
	 * The parameter <code>public.gin_extract_query_trgm._5</code>.
	 */
	public static final Parameter<Object> _5 = AbstractRoutine.createParameter(
			"_5", org.jooq.impl.DefaultDataType.getDefaultDataType("internal"),
			false);

	/**
	 * The parameter <code>public.gin_extract_query_trgm._6</code>.
	 */
	public static final Parameter<Object> _6 = AbstractRoutine.createParameter(
			"_6", org.jooq.impl.DefaultDataType.getDefaultDataType("internal"),
			false);

	/**
	 * The parameter <code>public.gin_extract_query_trgm._7</code>.
	 */
	public static final Parameter<Object> _7 = AbstractRoutine.createParameter(
			"_7", org.jooq.impl.DefaultDataType.getDefaultDataType("internal"),
			false);

	/**
	 * Create a new routine call instance
	 */
	public GinExtractQueryTrgm() {
		super("gin_extract_query_trgm", Public.PUBLIC,
				org.jooq.impl.DefaultDataType.getDefaultDataType("internal"));

		this.setReturnParameter(GinExtractQueryTrgm.RETURN_VALUE);
		this.addInParameter(GinExtractQueryTrgm._1);
		this.addInParameter(GinExtractQueryTrgm._2);
		this.addInParameter(GinExtractQueryTrgm._3);
		this.addInParameter(GinExtractQueryTrgm._4);
		this.addInParameter(GinExtractQueryTrgm._5);
		this.addInParameter(GinExtractQueryTrgm._6);
		this.addInParameter(GinExtractQueryTrgm._7);
	}

	/**
	 * Set the <code>_1</code> parameter to the function to be used with a
	 * {@link org.jooq.Select} statement
	 */
	public GinExtractQueryTrgm set__1(Field<String> field) {
		this.setField(GinExtractQueryTrgm._1, field);
		return this;
	}

	/**
	 * Set the <code>_1</code> parameter IN value to the routine
	 */
	public void set__1(String value) {
		this.setValue(GinExtractQueryTrgm._1, value);
	}

	/**
	 * Set the <code>_2</code> parameter to the function to be used with a
	 * {@link org.jooq.Select} statement
	 */
	public GinExtractQueryTrgm set__2(Field<Object> field) {
		this.setField(GinExtractQueryTrgm._2, field);
		return this;
	}

	/**
	 * Set the <code>_2</code> parameter IN value to the routine
	 */
	public void set__2(Object value) {
		this.setValue(GinExtractQueryTrgm._2, value);
	}

	/**
	 * Set the <code>_3</code> parameter to the function to be used with a
	 * {@link org.jooq.Select} statement
	 */
	public GinExtractQueryTrgm set__3(Field<Short> field) {
		this.setField(GinExtractQueryTrgm._3, field);
		return this;
	}

	/**
	 * Set the <code>_3</code> parameter IN value to the routine
	 */
	public void set__3(Short value) {
		this.setValue(GinExtractQueryTrgm._3, value);
	}

	/**
	 * Set the <code>_4</code> parameter to the function to be used with a
	 * {@link org.jooq.Select} statement
	 */
	public GinExtractQueryTrgm set__4(Field<Object> field) {
		this.setField(GinExtractQueryTrgm._4, field);
		return this;
	}

	/**
	 * Set the <code>_4</code> parameter IN value to the routine
	 */
	public void set__4(Object value) {
		this.setValue(GinExtractQueryTrgm._4, value);
	}

	/**
	 * Set the <code>_5</code> parameter to the function to be used with a
	 * {@link org.jooq.Select} statement
	 */
	public GinExtractQueryTrgm set__5(Field<Object> field) {
		this.setField(GinExtractQueryTrgm._5, field);
		return this;
	}

	/**
	 * Set the <code>_5</code> parameter IN value to the routine
	 */
	public void set__5(Object value) {
		this.setValue(GinExtractQueryTrgm._5, value);
	}

	/**
	 * Set the <code>_6</code> parameter to the function to be used with a
	 * {@link org.jooq.Select} statement
	 */
	public GinExtractQueryTrgm set__6(Field<Object> field) {
		this.setField(GinExtractQueryTrgm._6, field);
		return this;
	}

	/**
	 * Set the <code>_6</code> parameter IN value to the routine
	 */
	public void set__6(Object value) {
		this.setValue(GinExtractQueryTrgm._6, value);
	}

	/**
	 * Set the <code>_7</code> parameter to the function to be used with a
	 * {@link org.jooq.Select} statement
	 */
	public GinExtractQueryTrgm set__7(Field<Object> field) {
		this.setField(GinExtractQueryTrgm._7, field);
		return this;
	}

	/**
	 * Set the <code>_7</code> parameter IN value to the routine
	 */
	public void set__7(Object value) {
		this.setValue(GinExtractQueryTrgm._7, value);
	}
}
