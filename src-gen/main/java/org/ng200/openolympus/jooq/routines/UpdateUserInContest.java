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
public class UpdateUserInContest extends AbstractRoutine<java.lang.Void> {

	private static final long serialVersionUID = 1811793228;

	/**
	 * The parameter <code>public.update_user_in_contest._param1</code>.
	 */
	public static final Parameter<Long> _PARAM1 = createParameter("_param1", org.jooq.impl.SQLDataType.BIGINT, false);

	/**
	 * The parameter <code>public.update_user_in_contest._param2</code>.
	 */
	public static final Parameter<Long> _PARAM2 = createParameter("_param2", org.jooq.impl.SQLDataType.BIGINT, false);

	/**
	 * Create a new routine call instance
	 */
	public UpdateUserInContest() {
		super("update_user_in_contest", Public.PUBLIC);

		addInParameter(_PARAM1);
		addInParameter(_PARAM2);
	}

	/**
	 * Set the <code>_param1</code> parameter IN value to the routine
	 */
	public void set_Param1(Long value) {
		setValue(_PARAM1, value);
	}

	/**
	 * Set the <code>_param2</code> parameter IN value to the routine
	 */
	public void set_Param2(Long value) {
		setValue(_PARAM2, value);
	}
}
