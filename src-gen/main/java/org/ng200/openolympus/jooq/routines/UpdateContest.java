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
		"jOOQ version:3.7.2"
	},
	comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class UpdateContest extends AbstractRoutine<java.lang.Void> {

	private static final long serialVersionUID = 1463824710;

	/**
	 * The parameter <code>public.update_contest.contest_id_p</code>.
	 */
	public static final Parameter<Integer> CONTEST_ID_P = createParameter("contest_id_p", org.jooq.impl.SQLDataType.INTEGER, false);

	/**
	 * Create a new routine call instance
	 */
	public UpdateContest() {
		super("update_contest", Public.PUBLIC);

		addInParameter(CONTEST_ID_P);
	}

	/**
	 * Set the <code>contest_id_p</code> parameter IN value to the routine
	 */
	public void setContestIdP(Integer value) {
		setValue(CONTEST_ID_P, value);
	}
}
