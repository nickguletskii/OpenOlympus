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
package org.ng200.openolympus.jooq.routines;


import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Parameter;
import org.jooq.impl.AbstractRoutine;
import org.ng200.openolympus.jooq.Public;


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
public class GtrgmSame extends AbstractRoutine<Object> {

	private static final long serialVersionUID = -1135914743;

	/**
	 * The parameter <code>public.gtrgm_same.RETURN_VALUE</code>.
	 */
	public static final Parameter<Object> RETURN_VALUE = createParameter("RETURN_VALUE", org.jooq.impl.DefaultDataType.getDefaultDataType("internal"), false);

	/**
	 * The parameter <code>public.gtrgm_same._1</code>.
	 */
	public static final Parameter<Object> _1 = createParameter("_1", org.jooq.impl.DefaultDataType.getDefaultDataType("USER-DEFINED"), false);

	/**
	 * The parameter <code>public.gtrgm_same._2</code>.
	 */
	public static final Parameter<Object> _2 = createParameter("_2", org.jooq.impl.DefaultDataType.getDefaultDataType("USER-DEFINED"), false);

	/**
	 * The parameter <code>public.gtrgm_same._3</code>.
	 */
	public static final Parameter<Object> _3 = createParameter("_3", org.jooq.impl.DefaultDataType.getDefaultDataType("internal"), false);

	/**
	 * Create a new routine call instance
	 */
	public GtrgmSame() {
		super("gtrgm_same", Public.PUBLIC, org.jooq.impl.DefaultDataType.getDefaultDataType("internal"));

		setReturnParameter(RETURN_VALUE);
		addInParameter(_1);
		addInParameter(_2);
		addInParameter(_3);
	}

	/**
	 * Set the <code>_1</code> parameter IN value to the routine
	 */
	public void set__1(Object value) {
		setValue(_1, value);
	}

	/**
	 * Set the <code>_1</code> parameter to the function to be used with a {@link org.jooq.Select} statement
	 */
	public GtrgmSame set__1(Field<Object> field) {
		setField(_1, field);
		return this;
	}

	/**
	 * Set the <code>_2</code> parameter IN value to the routine
	 */
	public void set__2(Object value) {
		setValue(_2, value);
	}

	/**
	 * Set the <code>_2</code> parameter to the function to be used with a {@link org.jooq.Select} statement
	 */
	public GtrgmSame set__2(Field<Object> field) {
		setField(_2, field);
		return this;
	}

	/**
	 * Set the <code>_3</code> parameter IN value to the routine
	 */
	public void set__3(Object value) {
		setValue(_3, value);
	}

	/**
	 * Set the <code>_3</code> parameter to the function to be used with a {@link org.jooq.Select} statement
	 */
	public GtrgmSame set__3(Field<Object> field) {
		setField(_3, field);
		return this;
	}
}
