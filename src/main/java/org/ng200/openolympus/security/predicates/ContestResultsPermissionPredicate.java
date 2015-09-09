package org.ng200.openolympus.security.predicates;

import org.ng200.openolympus.SecurityClearanceType;
import org.ng200.openolympus.jooq.enums.ContestPermissionType;
import org.ng200.openolympus.jooq.tables.pojos.Contest;
import org.ng200.openolympus.jooq.tables.pojos.User;
import org.ng200.openolympus.security.DynamicSecurityPredicate;
import org.ng200.openolympus.security.annotations.CurrentUser;
import org.ng200.openolympus.security.annotations.MethodSecurityPredicate;
import org.ng200.openolympus.security.annotations.PredicateDocumentation;
import org.ng200.openolympus.services.AclService;
import org.ng200.openolympus.services.ContestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@PredicateDocumentation({
							"The user can view contest results during the contest",
							"The contest is over and the user is permitted to view contest results after contest has ended"
})
public class ContestResultsPermissionPredicate
		implements DynamicSecurityPredicate {

	@Autowired
	private AclService aclService;

	@Autowired
	private ContestService contestService;

	@MethodSecurityPredicate
	public SecurityClearanceType predicate(@CurrentUser User user,
			Contest contest) {
		if (aclService.hasContestPermission(contest, user,
				ContestPermissionType.view_results_during_contest))
			return SecurityClearanceType.ANONYMOUS;
		if (contestService.isContestOverIncludingAllTimeExtensions(contest)
				&& aclService.hasContestPermission(contest, user,
						ContestPermissionType.view_results_after_contest))
			return SecurityClearanceType.ANONYMOUS;
		return SecurityClearanceType.DENIED;
	}
}
