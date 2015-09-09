package org.ng200.openolympus.services;

import org.jooq.DSLContext;
import org.ng200.openolympus.NameConstants;
import org.ng200.openolympus.SecurityClearanceType;
import org.ng200.openolympus.jooq.enums.GeneralPermissionType;
import org.ng200.openolympus.security.SecurityPredicatePipeline;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SecurityClearanceVerificationService {

	@Autowired
	private DSLContext dslContext;

	public boolean doesCurrentSecurityContextHaveClearance(
	        SecurityClearanceType securityClearanceType) {
		switch (securityClearanceType) {
		case ANONYMOUS:
			return true;
		case ADMINISTRATIVE_USER:
			return defaultSecurity()
			        .matches(user -> user.getSuperuser())
			        .isAllowed();
		case APPROVED_USER:
			return defaultSecurity()
			        .matches(user -> user.getApproved())
			        .isAllowed();
		case APPROVE_USER_REGISTRATIONS:
			return generalPermission(
			        GeneralPermissionType.approve_user_registrations);
		case CHANGE_OTHER_USERS_PASSWORD:
			return generalPermission(
			        GeneralPermissionType.change_other_users_password);
		case CHANGE_OTHER_USERS_PERSONAL_INFO:
			return generalPermission(
			        GeneralPermissionType.change_other_users_personal_info);
		case CONTEST_ORGANISER:
			return generalPermission(
			        GeneralPermissionType.contest_organiser);
		case DELETE_USER:
			return generalPermission(
			        GeneralPermissionType.delete_user);
		case DENIED:
			return false;
		case ENUMERATE_ALL_USERS:
			return generalPermission(GeneralPermissionType.enumerate_all_users);
		case INTERNAL:
			return emptySecurity().matches(user -> user.getUsername()
			        .equals(NameConstants.SYSTEM_ACCOUNT_NAME))
			        .isAllowed();
		case LOGGED_IN:
			return emptySecurity().matches(user -> true).isAllowed();
		case SUPERUSER:
			return defaultSecurity().isAllowed();
		case TASK_SUPERVISOR:
			return generalPermission(
			        GeneralPermissionType.task_supervisor);
		case VIEW_OTHER_USERS_PERSONAL_INFO:
			return generalPermission(
			        GeneralPermissionType.view_other_users_personal_info);
		case CREATE_GROUP:
			return generalPermission(
			        GeneralPermissionType.create_groups);
		case LIST_GROUPS:
			return generalPermission(
			        GeneralPermissionType.list_groups);
		case MANAGE_PRINCIPAL_PERMISSIONS:
			return generalPermission(
			        GeneralPermissionType.manage_principal_permissions);
		case VIEW_ALL_SOLUTIONS:
			return generalPermission(
			        GeneralPermissionType.view_all_solutions);
		}
		throw new UnsupportedOperationException("An unsupported security clearance was encountered.");
	}

	private SecurityPredicatePipeline defaultSecurity() {
		return SecurityPredicatePipeline.defaultPipeline(dslContext);
	}

	private SecurityPredicatePipeline emptySecurity() {
		return SecurityPredicatePipeline.emptyPipeline(dslContext);
	}

	private boolean generalPermission(
	        GeneralPermissionType generalPermissionType) {
		return defaultSecurity().hasPermission(generalPermissionType)
		        .isAllowed();
	}
}
