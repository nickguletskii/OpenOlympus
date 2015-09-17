package org.ng200.openolympus.model;

import javax.persistence.Column;

import org.ng200.openolympus.SecurityClearanceType;
import org.ng200.openolympus.annotations.SecurityClearanceRequired;
import org.ng200.openolympus.jooq.enums.GeneralPermissionType;
import org.ng200.openolympus.jooq.tables.pojos.User;

public class UserPrincipal extends User {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2109878792216233016L;
	private GeneralPermissionType[] permissions;
	
	@Column(name = "permissions", nullable = false)
	@SecurityClearanceRequired(minimumClearance = SecurityClearanceType.ANONYMOUS)
	public UserPrincipal setPermissions(GeneralPermissionType[] permissions) {
		this.permissions = permissions;
		return this;
	}
	
	@Column(name = "permissions", nullable = false)
	@SecurityClearanceRequired(minimumClearance = SecurityClearanceType.ANONYMOUS)
	public GeneralPermissionType[] getPermissions() {
		return permissions;
	}
}
