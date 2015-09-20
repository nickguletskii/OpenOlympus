package org.ng200.openolympus.security.predicates;

import org.ng200.openolympus.SecurityClearanceType;
import org.ng200.openolympus.jooq.tables.pojos.Solution;
import org.ng200.openolympus.jooq.tables.pojos.User;
import org.ng200.openolympus.security.DynamicSecurityPredicate;
import org.ng200.openolympus.security.annotations.CurrentUser;
import org.ng200.openolympus.security.annotations.MethodSecurityPredicate;
import org.ng200.openolympus.security.annotations.Parameter;
import org.ng200.openolympus.security.annotations.PredicateDocumentation;
import org.ng200.openolympus.services.AclService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@PredicateDocumentation({
							"The solution is in a contest in which the user can view all solutions"
})
public class SolutionIsInContestModeratedByCurrentUserSecurityPredicate
		implements DynamicSecurityPredicate {

	@Autowired
	private AclService aclService;

	@MethodSecurityPredicate
	public SecurityClearanceType predicate(@CurrentUser User user,
			@Parameter("solution") Solution solution) {
		if (aclService.solutionIsModeratedByUser(solution, user))
			return SecurityClearanceType.ANONYMOUS;
		return SecurityClearanceType.DENIED;
	}
}
