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
		"jOOQ version:3.6.2"
	},
	comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class GinTrgmConsistent extends AbstractRoutine<Boolean> {

	private static final long serialVersionUID = -2089337891;

	/**
	 * The parameter <code>public.gin_trgm_consistent.RETURN_VALUE</code>.
	 */
	public static final Parameter<Boolean> RETURN_VALUE = createParameter("RETURN_VALUE", org.jooq.impl.SQLDataType.BOOLEAN, false);

	/**
	 * The parameter <code>public.gin_trgm_consistent._1</code>.
	 */
	public static final Parameter<Object> _1 = createParameter("_1", org.jooq.impl.DefaultDataType.getDefaultDataType("internal"), false);

	/**
	 * The parameter <code>public.gin_trgm_consistent._2</code>.
	 */
	public static final Parameter<Short> _2 = createParameter("_2", org.jooq.impl.SQLDataType.SMALLINT, false);

	/**
	 * The parameter <code>public.gin_trgm_consistent._3</code>.
	 */
	public static final Parameter<String> _3 = createParameter("_3", org.jooq.impl.SQLDataType.CLOB, false);

	/**
	 * The parameter <code>public.gin_trgm_consistent._4</code>.
	 */
	public static final Parameter<Integer> _4 = createParameter("_4", org.jooq.impl.SQLDataType.INTEGER, false);

	/**
	 * The parameter <code>public.gin_trgm_consistent._5</code>.
	 */
	public static final Parameter<Object> _5 = createParameter("_5", org.jooq.impl.DefaultDataType.getDefaultDataType("internal"), false);

	/**
	 * The parameter <code>public.gin_trgm_consistent._6</code>.
	 */
	public static final Parameter<Object> _6 = createParameter("_6", org.jooq.impl.DefaultDataType.getDefaultDataType("internal"), false);

	/**
	 * The parameter <code>public.gin_trgm_consistent._7</code>.
	 */
	public static final Parameter<Object> _7 = createParameter("_7", org.jooq.impl.DefaultDataType.getDefaultDataType("internal"), false);

	/**
	 * The parameter <code>public.gin_trgm_consistent._8</code>.
	 */
	public static final Parameter<Object> _8 = createParameter("_8", org.jooq.impl.DefaultDataType.getDefaultDataType("internal"), false);

	/**
	 * Create a new routine call instance
	 */
	public GinTrgmConsistent() {
		super("gin_trgm_consistent", Public.PUBLIC, org.jooq.impl.SQLDataType.BOOLEAN);

		setReturnParameter(RETURN_VALUE);
		addInParameter(_1);
		addInParameter(_2);
		addInParameter(_3);
		addInParameter(_4);
		addInParameter(_5);
		addInParameter(_6);
		addInParameter(_7);
		addInParameter(_8);
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
	public GinTrgmConsistent set__1(Field<Object> field) {
		setField(_1, field);
		return this;
	}

	/**
	 * Set the <code>_2</code> parameter IN value to the routine
	 */
	public void set__2(Short value) {
		setValue(_2, value);
	}

	/**
	 * Set the <code>_2</code> parameter to the function to be used with a {@link org.jooq.Select} statement
	 */
	public GinTrgmConsistent set__2(Field<Short> field) {
		setField(_2, field);
		return this;
	}

	/**
	 * Set the <code>_3</code> parameter IN value to the routine
	 */
	public void set__3(String value) {
		setValue(_3, value);
	}

	/**
	 * Set the <code>_3</code> parameter to the function to be used with a {@link org.jooq.Select} statement
	 */
	public GinTrgmConsistent set__3(Field<String> field) {
		setField(_3, field);
		return this;
	}

	/**
	 * Set the <code>_4</code> parameter IN value to the routine
	 */
	public void set__4(Integer value) {
		setValue(_4, value);
	}

	/**
	 * Set the <code>_4</code> parameter to the function to be used with a {@link org.jooq.Select} statement
	 */
	public GinTrgmConsistent set__4(Field<Integer> field) {
		setField(_4, field);
		return this;
	}

	/**
	 * Set the <code>_5</code> parameter IN value to the routine
	 */
	public void set__5(Object value) {
		setValue(_5, value);
	}

	/**
	 * Set the <code>_5</code> parameter to the function to be used with a {@link org.jooq.Select} statement
	 */
	public GinTrgmConsistent set__5(Field<Object> field) {
		setField(_5, field);
		return this;
	}

	/**
	 * Set the <code>_6</code> parameter IN value to the routine
	 */
	public void set__6(Object value) {
		setValue(_6, value);
	}

	/**
	 * Set the <code>_6</code> parameter to the function to be used with a {@link org.jooq.Select} statement
	 */
	public GinTrgmConsistent set__6(Field<Object> field) {
		setField(_6, field);
		return this;
	}

	/**
	 * Set the <code>_7</code> parameter IN value to the routine
	 */
	public void set__7(Object value) {
		setValue(_7, value);
	}

	/**
	 * Set the <code>_7</code> parameter to the function to be used with a {@link org.jooq.Select} statement
	 */
	public GinTrgmConsistent set__7(Field<Object> field) {
		setField(_7, field);
		return this;
	}

	/**
	 * Set the <code>_8</code> parameter IN value to the routine
	 */
	public void set__8(Object value) {
		setValue(_8, value);
	}

	/**
	 * Set the <code>_8</code> parameter to the function to be used with a {@link org.jooq.Select} statement
	 */
	public GinTrgmConsistent set__8(Field<Object> field) {
		setField(_8, field);
		return this;
	}
}
