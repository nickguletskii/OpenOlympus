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
		"jOOQ version:3.7.0"
	},
	comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class GetParticipantsGroupIdFromContestId extends AbstractRoutine<Long> {

	private static final long serialVersionUID = -1035990161;

	/**
	 * The parameter <code>public.get_participants_group_id_from_contest_id.RETURN_VALUE</code>.
	 */
	public static final Parameter<Long> RETURN_VALUE = createParameter("RETURN_VALUE", org.jooq.impl.SQLDataType.BIGINT, false);

	/**
	 * The parameter <code>public.get_participants_group_id_from_contest_id.contest_id_p</code>.
	 */
	public static final Parameter<Integer> CONTEST_ID_P = createParameter("contest_id_p", org.jooq.impl.SQLDataType.INTEGER, false);

	/**
	 * Create a new routine call instance
	 */
	public GetParticipantsGroupIdFromContestId() {
		super("get_participants_group_id_from_contest_id", Public.PUBLIC, org.jooq.impl.SQLDataType.BIGINT);

		setReturnParameter(RETURN_VALUE);
		addInParameter(CONTEST_ID_P);
	}

	/**
	 * Set the <code>contest_id_p</code> parameter IN value to the routine
	 */
	public void setContestIdP(Integer value) {
		setValue(CONTEST_ID_P, value);
	}

	/**
	 * Set the <code>contest_id_p</code> parameter to the function to be used with a {@link org.jooq.Select} statement
	 */
	public GetParticipantsGroupIdFromContestId setContestIdP(Field<Integer> field) {
		setField(CONTEST_ID_P, field);
		return this;
	}
}
