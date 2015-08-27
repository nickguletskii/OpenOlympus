package org.ng200.openolympus.security.predicates;

import org.jooq.DSLContext;
import org.ng200.openolympus.SecurityClearanceType;
import org.ng200.openolympus.jooq.tables.pojos.Contest;
import org.ng200.openolympus.jooq.tables.pojos.User;
import org.ng200.openolympus.security.DynamicSecurityPredicate;
import org.ng200.openolympus.security.annotations.ContestPermissionRequired;
import org.ng200.openolympus.security.annotations.CurrentUser;
import org.ng200.openolympus.security.annotations.FindAnnotation;
import org.ng200.openolympus.security.annotations.MethodSecurityPredicate;
import org.ng200.openolympus.services.AclService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserHasContestPermission implements DynamicSecurityPredicate {

	@Autowired
	private AclService aclService;

	@MethodSecurityPredicate
	public SecurityClearanceType vote(
	        @FindAnnotation ContestPermissionRequired contestPermissionRequired,
	        Contest contest,
	        @CurrentUser User user) {

		return aclService.hasContestPermission(contest, user,
		        contestPermissionRequired.value())
		                ? SecurityClearanceType.ANONYMOUS
		                : SecurityClearanceType.DENIED;
	}
}
