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
public class CheckImmutability extends AbstractRoutine<java.lang.Void> {

	private static final long serialVersionUID = -1456711862;

	/**
	 * The parameter <code>public.check_immutability.name_v</code>.
	 */
	public static final Parameter<String> NAME_V = createParameter("name_v", org.jooq.impl.SQLDataType.CLOB, false);

	/**
	 * The parameter <code>public.check_immutability.old_v</code>.
	 */
	public static final Parameter<Object> OLD_V = createParameter("old_v", org.jooq.impl.DefaultDataType.getDefaultDataType("anyelement"), false);

	/**
	 * The parameter <code>public.check_immutability.new_v</code>.
	 */
	public static final Parameter<Object> NEW_V = createParameter("new_v", org.jooq.impl.DefaultDataType.getDefaultDataType("anyelement"), false);

	/**
	 * Create a new routine call instance
	 */
	public CheckImmutability() {
		super("check_immutability", Public.PUBLIC);

		addInParameter(NAME_V);
		addInParameter(OLD_V);
		addInParameter(NEW_V);
	}

	/**
	 * Set the <code>name_v</code> parameter IN value to the routine
	 */
	public void setNameV(String value) {
		setValue(NAME_V, value);
	}

	/**
	 * Set the <code>old_v</code> parameter IN value to the routine
	 */
	public void setOldV(Object value) {
		setValue(OLD_V, value);
	}

	/**
	 * Set the <code>new_v</code> parameter IN value to the routine
	 */
	public void setNewV(Object value) {
		setValue(NEW_V, value);
	}
}
