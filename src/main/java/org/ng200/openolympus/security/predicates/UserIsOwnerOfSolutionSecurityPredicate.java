package org.ng200.openolympus.security.predicates;

import org.ng200.openolympus.SecurityClearanceType;
import org.ng200.openolympus.jooq.tables.pojos.Solution;
import org.ng200.openolympus.jooq.tables.pojos.User;
import org.ng200.openolympus.security.DynamicSecurityPredicate;
import org.ng200.openolympus.security.annotations.CurrentUser;
import org.ng200.openolympus.security.annotations.MethodSecurityPredicate;
import org.ng200.openolympus.security.annotations.Parameter;
import org.ng200.openolympus.security.annotations.PredicateDocumentation;
import org.springframework.stereotype.Component;

@Component
@PredicateDocumentation({
							"The user is the owner of the solution"
})
public class UserIsOwnerOfSolutionSecurityPredicate
		implements DynamicSecurityPredicate {

	@MethodSecurityPredicate
	public SecurityClearanceType predicate(@CurrentUser User user,
			@Parameter("solution") Solution solution) {
		if (solution.getUserId() == user.getId())
			return SecurityClearanceType.ANONYMOUS;
		return SecurityClearanceType.DENIED;
	}

}
