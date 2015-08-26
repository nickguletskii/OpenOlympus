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
public class GinExtractQueryTrgm extends AbstractRoutine<Object> {

	private static final long serialVersionUID = -1714959705;

	/**
	 * The parameter <code>public.gin_extract_query_trgm.RETURN_VALUE</code>.
	 */
	public static final Parameter<Object> RETURN_VALUE = createParameter("RETURN_VALUE", org.jooq.impl.DefaultDataType.getDefaultDataType("internal"), false);

	/**
	 * The parameter <code>public.gin_extract_query_trgm._1</code>.
	 */
	public static final Parameter<String> _1 = createParameter("_1", org.jooq.impl.SQLDataType.CLOB, false);

	/**
	 * The parameter <code>public.gin_extract_query_trgm._2</code>.
	 */
	public static final Parameter<Object> _2 = createParameter("_2", org.jooq.impl.DefaultDataType.getDefaultDataType("internal"), false);

	/**
	 * The parameter <code>public.gin_extract_query_trgm._3</code>.
	 */
	public static final Parameter<Short> _3 = createParameter("_3", org.jooq.impl.SQLDataType.SMALLINT, false);

	/**
	 * The parameter <code>public.gin_extract_query_trgm._4</code>.
	 */
	public static final Parameter<Object> _4 = createParameter("_4", org.jooq.impl.DefaultDataType.getDefaultDataType("internal"), false);

	/**
	 * The parameter <code>public.gin_extract_query_trgm._5</code>.
	 */
	public static final Parameter<Object> _5 = createParameter("_5", org.jooq.impl.DefaultDataType.getDefaultDataType("internal"), false);

	/**
	 * The parameter <code>public.gin_extract_query_trgm._6</code>.
	 */
	public static final Parameter<Object> _6 = createParameter("_6", org.jooq.impl.DefaultDataType.getDefaultDataType("internal"), false);

	/**
	 * The parameter <code>public.gin_extract_query_trgm._7</code>.
	 */
	public static final Parameter<Object> _7 = createParameter("_7", org.jooq.impl.DefaultDataType.getDefaultDataType("internal"), false);

	/**
	 * Create a new routine call instance
	 */
	public GinExtractQueryTrgm() {
		super("gin_extract_query_trgm", Public.PUBLIC, org.jooq.impl.DefaultDataType.getDefaultDataType("internal"));

		setReturnParameter(RETURN_VALUE);
		addInParameter(_1);
		addInParameter(_2);
		addInParameter(_3);
		addInParameter(_4);
		addInParameter(_5);
		addInParameter(_6);
		addInParameter(_7);
	}

	/**
	 * Set the <code>_1</code> parameter IN value to the routine
	 */
	public void set__1(String value) {
		setValue(_1, value);
	}

	/**
	 * Set the <code>_1</code> parameter to the function to be used with a {@link org.jooq.Select} statement
	 */
	public GinExtractQueryTrgm set__1(Field<String> field) {
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
	public GinExtractQueryTrgm set__2(Field<Object> field) {
		setField(_2, field);
		return this;
	}

	/**
	 * Set the <code>_3</code> parameter IN value to the routine
	 */
	public void set__3(Short value) {
		setValue(_3, value);
	}

	/**
	 * Set the <code>_3</code> parameter to the function to be used with a {@link org.jooq.Select} statement
	 */
	public GinExtractQueryTrgm set__3(Field<Short> field) {
		setField(_3, field);
		return this;
	}

	/**
	 * Set the <code>_4</code> parameter IN value to the routine
	 */
	public void set__4(Object value) {
		setValue(_4, value);
	}

	/**
	 * Set the <code>_4</code> parameter to the function to be used with a {@link org.jooq.Select} statement
	 */
	public GinExtractQueryTrgm set__4(Field<Object> field) {
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
	public GinExtractQueryTrgm set__5(Field<Object> field) {
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
	public GinExtractQueryTrgm set__6(Field<Object> field) {
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
	public GinExtractQueryTrgm set__7(Field<Object> field) {
		setField(_7, field);
		return this;
	}
}