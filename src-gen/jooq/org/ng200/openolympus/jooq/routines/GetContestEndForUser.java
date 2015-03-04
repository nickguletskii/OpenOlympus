/**
 * This class is generated by jOOQ
 */
package org.ng200.openolympus.jooq.routines;

/**
 * This class is generated by jOOQ.
 */
@javax.annotation.Generated(
	value = {
		"http://www.jooq.org",
		"jOOQ version:3.5.2"
	},
	comments = "This class is generated by jOOQ"
)
@java.lang.SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class GetContestEndForUser extends org.jooq.impl.AbstractRoutine<java.sql.Timestamp> {

	private static final long serialVersionUID = 493723764;

	/**
	 * The parameter <code>public.get_contest_end_for_user.RETURN_VALUE</code>.
	 */
	public static final org.jooq.Parameter<java.sql.Timestamp> RETURN_VALUE = createParameter("RETURN_VALUE", org.jooq.impl.SQLDataType.TIMESTAMP, false);

	/**
	 * The parameter <code>public.get_contest_end_for_user._1</code>.
	 */
	public static final org.jooq.Parameter<java.lang.Long> _1 = createParameter("_1", org.jooq.impl.SQLDataType.BIGINT, false);

	/**
	 * The parameter <code>public.get_contest_end_for_user._2</code>.
	 */
	public static final org.jooq.Parameter<java.lang.Long> _2 = createParameter("_2", org.jooq.impl.SQLDataType.BIGINT, false);

	/**
	 * Create a new routine call instance
	 */
	public GetContestEndForUser() {
		super("get_contest_end_for_user", org.ng200.openolympus.jooq.Public.PUBLIC, org.jooq.impl.SQLDataType.TIMESTAMP);

		setReturnParameter(RETURN_VALUE);
		addInParameter(_1);
		addInParameter(_2);
	}

	/**
	 * Set the <code>_1</code> parameter IN value to the routine
	 */
	public void set__1(java.lang.Long value) {
		setValue(org.ng200.openolympus.jooq.routines.GetContestEndForUser._1, value);
	}

	/**
	 * Set the <code>_1</code> parameter to the function to be used with a {@link org.jooq.Select} statement
	 */
	public void set__1(org.jooq.Field<java.lang.Long> field) {
		setField(_1, field);
	}

	/**
	 * Set the <code>_2</code> parameter IN value to the routine
	 */
	public void set__2(java.lang.Long value) {
		setValue(org.ng200.openolympus.jooq.routines.GetContestEndForUser._2, value);
	}

	/**
	 * Set the <code>_2</code> parameter to the function to be used with a {@link org.jooq.Select} statement
	 */
	public void set__2(org.jooq.Field<java.lang.Long> field) {
		setField(_2, field);
	}
}