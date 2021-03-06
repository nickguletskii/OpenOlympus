/**
 * The MIT License
 * Copyright (c) 2014-2016 Nick Guletskii
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
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

	private SecurityPredicatePipeline defaultSecurity() {
		return SecurityPredicatePipeline.defaultPipeline(this.dslContext);
	}

	public boolean doesCurrentSecurityContextHaveClearance(
			SecurityClearanceType securityClearanceType) {
		switch (securityClearanceType) {
		case ANONYMOUS:
			return true;
		case ADMINISTRATIVE_USER:
			return this.defaultSecurity()
					.matches(user -> user.getSuperuser())
					.isAllowed();
		case APPROVED_USER:
			return this.defaultSecurity()
					.matches(user -> user.getApproved())
					.isAllowed();
		case APPROVE_USER_REGISTRATIONS:
			return this.generalPermission(
					GeneralPermissionType.approve_user_registrations);
		case CHANGE_OTHER_USERS_PASSWORD:
			return this.generalPermission(
					GeneralPermissionType.change_other_users_password);
		case CHANGE_OTHER_USERS_PERSONAL_INFO:
			return this.generalPermission(
					GeneralPermissionType.change_other_users_personal_info);
		case CONTEST_CREATOR:
			return this.generalPermission(
					GeneralPermissionType.create_contests);
		case DELETE_USER:
			return this.generalPermission(
					GeneralPermissionType.remove_user);
		case DENIED:
			return false;
		case ENUMERATE_ALL_USERS:
			return this.generalPermission(
					GeneralPermissionType.enumerate_all_users);
		case INTERNAL:
			return this.emptySecurity().matches(user -> user.getUsername()
					.equals(NameConstants.SYSTEM_ACCOUNT_NAME))
					.isAllowed();
		case LOGGED_IN:
			return this.emptySecurity().matches(user -> true).isAllowed();
		case SUPERUSER:
			return this.defaultSecurity().isAllowed();
		case TASK_SUPERVISOR:
			return this.generalPermission(
					GeneralPermissionType.task_supervisor);
		case VIEW_OTHER_USERS_PERSONAL_INFO:
			return this.generalPermission(
					GeneralPermissionType.view_other_users_personal_info);
		case CREATE_GROUP:
			return this.generalPermission(
					GeneralPermissionType.create_groups);
		case LIST_GROUPS:
			return this.generalPermission(
					GeneralPermissionType.list_groups);
		case MANAGE_PRINCIPAL_PERMISSIONS:
			return this.generalPermission(
					GeneralPermissionType.manage_principal_permissions);
		case VIEW_ALL_SOLUTIONS:
			return this.generalPermission(
					GeneralPermissionType.view_all_solutions);
		case VIEW_ARCHIVE_DURING_CONTEST:
			return this.generalPermission(
					GeneralPermissionType.view_archive_during_contest);
		}
		throw new UnsupportedOperationException(
				"An unsupported security clearance was encountered.");
	}

	private SecurityPredicatePipeline emptySecurity() {
		return SecurityPredicatePipeline.emptyPipeline(this.dslContext);
	}

	private boolean generalPermission(
			GeneralPermissionType generalPermissionType) {
		return this.defaultSecurity().hasPermission(generalPermissionType)
				.isAllowed();
	}
}
