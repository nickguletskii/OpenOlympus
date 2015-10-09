/**
 * This class is generated by jOOQ
 */
package org.ng200.openolympus.jooq.routines;


import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Parameter;
import org.jooq.impl.AbstractRoutine;
import org.ng200.openolympus.jooq.Public;
import org.ng200.openolympus.jooq.enums.GeneralPermissionType;


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
public class HasGeneralPermission extends AbstractRoutine<Boolean> {

	private static final long serialVersionUID = -1261558395;

	/**
	 * The parameter <code>public.has_general_permission.RETURN_VALUE</code>.
	 */
	public static final Parameter<Boolean> RETURN_VALUE = createParameter("RETURN_VALUE", org.jooq.impl.SQLDataType.BOOLEAN, false);

	/**
	 * The parameter <code>public.has_general_permission.principal_id_p</code>.
	 */
	public static final Parameter<Long> PRINCIPAL_ID_P = createParameter("principal_id_p", org.jooq.impl.SQLDataType.BIGINT, false);

	/**
	 * The parameter <code>public.has_general_permission.permission_p</code>.
	 */
	public static final Parameter<GeneralPermissionType> PERMISSION_P = createParameter("permission_p", org.jooq.util.postgres.PostgresDataType.VARCHAR.asEnumDataType(org.ng200.openolympus.jooq.enums.GeneralPermissionType.class), false);

	/**
	 * Create a new routine call instance
	 */
	public HasGeneralPermission() {
		super("has_general_permission", Public.PUBLIC, org.jooq.impl.SQLDataType.BOOLEAN);

		setReturnParameter(RETURN_VALUE);
		addInParameter(PRINCIPAL_ID_P);
		addInParameter(PERMISSION_P);
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
	public HasGeneralPermission setPrincipalIdP(Field<Long> field) {
		setField(PRINCIPAL_ID_P, field);
		return this;
	}

	/**
	 * Set the <code>permission_p</code> parameter IN value to the routine
	 */
	public void setPermissionP(GeneralPermissionType value) {
		setValue(PERMISSION_P, value);
	}

	/**
	 * Set the <code>permission_p</code> parameter to the function to be used with a {@link org.jooq.Select} statement
	 */
	public HasGeneralPermission setPermissionP(Field<GeneralPermissionType> field) {
		setField(PERMISSION_P, field);
		return this;
	}
}
