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
public class KeepGroupMemberInContestParticipationsList extends AbstractRoutine<Object> {

	private static final long serialVersionUID = 1703883404;

	/**
	 * The parameter <code>public.keep_group_member_in_contest_participations_list.RETURN_VALUE</code>.
	 */
	public static final Parameter<Object> RETURN_VALUE = createParameter("RETURN_VALUE", org.jooq.impl.DefaultDataType.getDefaultDataType("trigger"), false);

	/**
	 * Create a new routine call instance
	 */
	public KeepGroupMemberInContestParticipationsList() {
		super("keep_group_member_in_contest_participations_list", Public.PUBLIC, org.jooq.impl.DefaultDataType.getDefaultDataType("trigger"));

		setReturnParameter(RETURN_VALUE);
	}
}
