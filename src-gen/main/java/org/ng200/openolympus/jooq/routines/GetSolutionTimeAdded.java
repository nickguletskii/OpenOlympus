/**
 * This class is generated by jOOQ
 */
package org.ng200.openolympus.jooq.routines;


import java.time.OffsetDateTime;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Parameter;
import org.jooq.impl.AbstractRoutine;
import org.ng200.openolympus.jooq.Public;
import org.ng200.openolympus.util.DateTimeBinding;


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
public class GetSolutionTimeAdded extends AbstractRoutine<OffsetDateTime> {

	private static final long serialVersionUID = 2023736912;

	/**
	 * The parameter <code>public.get_solution_time_added.RETURN_VALUE</code>.
	 */
	public static final Parameter<OffsetDateTime> RETURN_VALUE = createParameter("RETURN_VALUE", org.ng200.openolympus.jooqsupport.CustomTypes.TIMESTAMPTZ, false, new DateTimeBinding());

	/**
	 * The parameter <code>public.get_solution_time_added.solution_id</code>.
	 */
	public static final Parameter<Long> SOLUTION_ID = createParameter("solution_id", org.jooq.impl.SQLDataType.BIGINT, false);

	/**
	 * Create a new routine call instance
	 */
	public GetSolutionTimeAdded() {
		super("get_solution_time_added", Public.PUBLIC, org.ng200.openolympus.jooqsupport.CustomTypes.TIMESTAMPTZ, new DateTimeBinding());

		setReturnParameter(RETURN_VALUE);
		addInParameter(SOLUTION_ID);
	}

	/**
	 * Set the <code>solution_id</code> parameter IN value to the routine
	 */
	public void setSolutionId(Long value) {
		setValue(SOLUTION_ID, value);
	}

	/**
	 * Set the <code>solution_id</code> parameter to the function to be used with a {@link org.jooq.Select} statement
	 */
	public GetSolutionTimeAdded setSolutionId(Field<Long> field) {
		setField(SOLUTION_ID, field);
		return this;
	}
}
