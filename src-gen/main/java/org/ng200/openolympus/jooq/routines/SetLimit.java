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
public class SetLimit extends AbstractRoutine<Float> {

	private static final long serialVersionUID = -488274837;

	/**
	 * The parameter <code>public.set_limit.RETURN_VALUE</code>.
	 */
	public static final Parameter<Float> RETURN_VALUE = createParameter("RETURN_VALUE", org.jooq.impl.SQLDataType.REAL, false);

	/**
	 * The parameter <code>public.set_limit._1</code>.
	 */
	public static final Parameter<Float> _1 = createParameter("_1", org.jooq.impl.SQLDataType.REAL, false);

	/**
	 * Create a new routine call instance
	 */
	public SetLimit() {
		super("set_limit", Public.PUBLIC, org.jooq.impl.SQLDataType.REAL);

		setReturnParameter(RETURN_VALUE);
		addInParameter(_1);
	}

	/**
	 * Set the <code>_1</code> parameter IN value to the routine
	 */
	public void set__1(Float value) {
		setValue(_1, value);
	}

	/**
	 * Set the <code>_1</code> parameter to the function to be used with a {@link org.jooq.Select} statement
	 */
	public SetLimit set__1(Field<Float> field) {
		setField(_1, field);
		return this;
	}
}
