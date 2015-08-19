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
public class GtrgmDistance extends AbstractRoutine<Double> {

	private static final long serialVersionUID = 1341407800;

	/**
	 * The parameter <code>public.gtrgm_distance.RETURN_VALUE</code>.
	 */
	public static final Parameter<Double> RETURN_VALUE = createParameter("RETURN_VALUE", org.jooq.impl.SQLDataType.DOUBLE, false);

	/**
	 * The parameter <code>public.gtrgm_distance._1</code>.
	 */
	public static final Parameter<Object> _1 = createParameter("_1", org.jooq.impl.DefaultDataType.getDefaultDataType("internal"), false);

	/**
	 * The parameter <code>public.gtrgm_distance._2</code>.
	 */
	public static final Parameter<String> _2 = createParameter("_2", org.jooq.impl.SQLDataType.CLOB, false);

	/**
	 * The parameter <code>public.gtrgm_distance._3</code>.
	 */
	public static final Parameter<Integer> _3 = createParameter("_3", org.jooq.impl.SQLDataType.INTEGER, false);

	/**
	 * The parameter <code>public.gtrgm_distance._4</code>.
	 */
	public static final Parameter<Long> _4 = createParameter("_4", org.jooq.impl.SQLDataType.BIGINT, false);

	/**
	 * Create a new routine call instance
	 */
	public GtrgmDistance() {
		super("gtrgm_distance", Public.PUBLIC, org.jooq.impl.SQLDataType.DOUBLE);

		setReturnParameter(RETURN_VALUE);
		addInParameter(_1);
		addInParameter(_2);
		addInParameter(_3);
		addInParameter(_4);
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
	public GtrgmDistance set__1(Field<Object> field) {
		setField(_1, field);
		return this;
	}

	/**
	 * Set the <code>_2</code> parameter IN value to the routine
	 */
	public void set__2(String value) {
		setValue(_2, value);
	}

	/**
	 * Set the <code>_2</code> parameter to the function to be used with a {@link org.jooq.Select} statement
	 */
	public GtrgmDistance set__2(Field<String> field) {
		setField(_2, field);
		return this;
	}

	/**
	 * Set the <code>_3</code> parameter IN value to the routine
	 */
	public void set__3(Integer value) {
		setValue(_3, value);
	}

	/**
	 * Set the <code>_3</code> parameter to the function to be used with a {@link org.jooq.Select} statement
	 */
	public GtrgmDistance set__3(Field<Integer> field) {
		setField(_3, field);
		return this;
	}

	/**
	 * Set the <code>_4</code> parameter IN value to the routine
	 */
	public void set__4(Long value) {
		setValue(_4, value);
	}

	/**
	 * Set the <code>_4</code> parameter to the function to be used with a {@link org.jooq.Select} statement
	 */
	public GtrgmDistance set__4(Field<Long> field) {
		setField(_4, field);
		return this;
	}
}
