package org.ng200.openolympus.security.predicates;

import org.ng200.openolympus.SecurityClearanceType;
import org.ng200.openolympus.jooq.enums.ContestPermissionType;
import org.ng200.openolympus.jooq.tables.pojos.Contest;
import org.ng200.openolympus.jooq.tables.pojos.User;
import org.ng200.openolympus.security.DynamicSecurityPredicate;
import org.ng200.openolympus.security.annotations.CurrentUser;
import org.ng200.openolympus.security.annotations.MethodSecurityPredicate;
import org.ng200.openolympus.security.annotations.Parameter;
import org.ng200.openolympus.services.AclService;
import org.ng200.openolympus.services.ContestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserContestViewSecurityPredicate
		implements DynamicSecurityPredicate {

	@Autowired
	private AclService aclService;

	@Autowired
	private ContestService contestService;

	@MethodSecurityPredicate
	public SecurityClearanceType predicate(@CurrentUser User user,
			@Parameter("contest") Contest contest) {
		if (aclService.hasContestPermission(contest, user,
				ContestPermissionType.list_tasks,
				ContestPermissionType.manage_acl))
			return SecurityClearanceType.ANONYMOUS;
		if (contestService.isContestInProgressForUser(contest, user)
				&& aclService.hasContestPermission(contest, user,
						ContestPermissionType.participate))
			return SecurityClearanceType.ANONYMOUS;
		return SecurityClearanceType.DENIED;
	}
}
