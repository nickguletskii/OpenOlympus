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
public class GtrgmCompress extends AbstractRoutine<Object> {

	private static final long serialVersionUID = 1072492214;

	/**
	 * The parameter <code>public.gtrgm_compress.RETURN_VALUE</code>.
	 */
	public static final Parameter<Object> RETURN_VALUE = createParameter("RETURN_VALUE", org.jooq.impl.DefaultDataType.getDefaultDataType("internal"), false);

	/**
	 * The parameter <code>public.gtrgm_compress._1</code>.
	 */
	public static final Parameter<Object> _1 = createParameter("_1", org.jooq.impl.DefaultDataType.getDefaultDataType("internal"), false);

	/**
	 * Create a new routine call instance
	 */
	public GtrgmCompress() {
		super("gtrgm_compress", Public.PUBLIC, org.jooq.impl.DefaultDataType.getDefaultDataType("internal"));

		setReturnParameter(RETURN_VALUE);
		addInParameter(_1);
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
	public GtrgmCompress set__1(Field<Object> field) {
		setField(_1, field);
		return this;
	}
}
