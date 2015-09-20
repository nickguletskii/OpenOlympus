package org.ng200.openolympus.security.predicates;

import org.ng200.openolympus.SecurityClearanceType;
import org.ng200.openolympus.jooq.tables.pojos.Group;
import org.ng200.openolympus.jooq.tables.pojos.User;
import org.ng200.openolympus.security.DynamicSecurityPredicate;
import org.ng200.openolympus.security.annotations.CurrentUser;
import org.ng200.openolympus.security.annotations.FindAnnotation;
import org.ng200.openolympus.security.annotations.GroupPermissionRequired;
import org.ng200.openolympus.security.annotations.MethodSecurityPredicate;
import org.ng200.openolympus.security.annotations.Parameter;
import org.ng200.openolympus.services.AclService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserHasGroupPermission implements DynamicSecurityPredicate {

	@Autowired
	private AclService aclService;

	@MethodSecurityPredicate
	public SecurityClearanceType vote(
			@FindAnnotation GroupPermissionRequired groupPermissionRequired,
			@Parameter("group") Group group,
			@CurrentUser User user) {

		return aclService.hasGroupPermission(group, user,
				groupPermissionRequired.value())
						? SecurityClearanceType.ANONYMOUS
						: SecurityClearanceType.DENIED;
	}
}
