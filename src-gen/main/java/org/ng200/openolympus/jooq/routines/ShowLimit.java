/**
 * This class is generated by jOOQ
 */
package org.ng200.openolympus.jooq.routines;


import javax.annotation.Generated;

import org.jooq.Parameter;
import org.jooq.impl.AbstractRoutine;
import org.ng200.openolympus.jooq.Public;


/**
 * This class is generated by jOOQ.
 */
@Generated(
	value = {
		"http://www.jooq.org",
		"jOOQ version:3.7.0"
	},
	comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class ShowLimit extends AbstractRoutine<Float> {

	private static final long serialVersionUID = -242916165;

	/**
	 * The parameter <code>public.show_limit.RETURN_VALUE</code>.
	 */
	public static final Parameter<Float> RETURN_VALUE = createParameter("RETURN_VALUE", org.jooq.impl.SQLDataType.REAL, false);

	/**
	 * Create a new routine call instance
	 */
	public ShowLimit() {
		super("show_limit", Public.PUBLIC, org.jooq.impl.SQLDataType.REAL);

		setReturnParameter(RETURN_VALUE);
	}
}
