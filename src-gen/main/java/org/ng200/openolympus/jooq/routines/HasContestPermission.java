/**
 * This class is generated by jOOQ
 */
package org.ng200.openolympus.jooq.routines;


import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Parameter;
import org.jooq.impl.AbstractRoutine;
import org.ng200.openolympus.jooq.Public;
import org.ng200.openolympus.jooq.enums.ContestPermissionType;


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
public class HasContestPermission extends AbstractRoutine<Boolean> {

	private static final long serialVersionUID = 1462871445;

	/**
	 * The parameter <code>public.has_contest_permission.RETURN_VALUE</code>.
	 */
	public static final Parameter<Boolean> RETURN_VALUE = createParameter("RETURN_VALUE", org.jooq.impl.SQLDataType.BOOLEAN, false);

	/**
	 * The parameter <code>public.has_contest_permission.contest_id_p</code>.
	 */
	public static final Parameter<Integer> CONTEST_ID_P = createParameter("contest_id_p", org.jooq.impl.SQLDataType.INTEGER, false);

	/**
	 * The parameter <code>public.has_contest_permission.principal_id_p</code>.
	 */
	public static final Parameter<Long> PRINCIPAL_ID_P = createParameter("principal_id_p", org.jooq.impl.SQLDataType.BIGINT, false);

	/**
	 * The parameter <code>public.has_contest_permission.permission_p</code>.
	 */
	public static final Parameter<ContestPermissionType> PERMISSION_P = createParameter("permission_p", org.jooq.util.postgres.PostgresDataType.VARCHAR.asEnumDataType(org.ng200.openolympus.jooq.enums.ContestPermissionType.class), false);

	/**
	 * Create a new routine call instance
	 */
	public HasContestPermission() {
		super("has_contest_permission", Public.PUBLIC, org.jooq.impl.SQLDataType.BOOLEAN);

		setReturnParameter(RETURN_VALUE);
		addInParameter(CONTEST_ID_P);
		addInParameter(PRINCIPAL_ID_P);
		addInParameter(PERMISSION_P);
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
	public HasContestPermission setContestIdP(Field<Integer> field) {
		setField(CONTEST_ID_P, field);
		return this;
	}

	/**
	 * Set the <code>principal_id_p</code> parameter IN value to the routine
	 */
	public void setPrincipalIdP(Long value) {
		setValue(PRINCIPAL_ID_P, value);
	}

	/**
	 * Set the <code>principal_id_p</code> parameter to the function to be used with a {@link org.jooq.Select} statement
	 */
	public HasContestPermission setPrincipalIdP(Field<Long> field) {
		setField(PRINCIPAL_ID_P, field);
		return this;
	}

	/**
	 * Set the <code>permission_p</code> parameter IN value to the routine
	 */
	public void setPermissionP(ContestPermissionType value) {
		setValue(PERMISSION_P, value);
	}

	/**
	 * Set the <code>permission_p</code> parameter to the function to be used with a {@link org.jooq.Select} statement
	 */
	public HasContestPermission setPermissionP(Field<ContestPermissionType> field) {
		setField(PERMISSION_P, field);
		return this;
	}
}