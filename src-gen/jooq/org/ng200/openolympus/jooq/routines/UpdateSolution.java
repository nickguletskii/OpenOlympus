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
		"jOOQ version:3.6.2"
	},
	comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class UpdateSolution extends AbstractRoutine<java.lang.Void> {

	private static final long serialVersionUID = 816509548;

	/**
	 * The parameter <code>public.update_solution._param1</code>.
	 */
	public static final Parameter<Long> _PARAM1 = createParameter("_param1", org.jooq.impl.SQLDataType.BIGINT, false);

	/**
	 * Create a new routine call instance
	 */
	public UpdateSolution() {
		super("update_solution", Public.PUBLIC);

		addInParameter(_PARAM1);
	}

	/**
	 * Set the <code>_param1</code> parameter IN value to the routine
	 */
	public void set_Param1(Long value) {
		setValue(_PARAM1, value);
	}
}
