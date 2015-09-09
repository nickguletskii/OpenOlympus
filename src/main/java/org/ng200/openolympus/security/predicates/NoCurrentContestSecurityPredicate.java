package org.ng200.openolympus.security.predicates;

import org.ng200.openolympus.SecurityClearanceType;
import org.ng200.openolympus.jooq.tables.pojos.Contest;
import org.ng200.openolympus.jooq.tables.pojos.User;
import org.ng200.openolympus.security.DynamicSecurityPredicate;
import org.ng200.openolympus.security.annotations.MethodSecurityPredicate;
import org.ng200.openolympus.security.annotations.PredicateDocumentation;
import org.ng200.openolympus.services.ContestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@PredicateDocumentation({
	"There is no current contest"
})
public class NoCurrentContestSecurityPredicate
        implements DynamicSecurityPredicate {

	@Autowired
	private ContestService contestService;

	@MethodSecurityPredicate
	public SecurityClearanceType check() {
		Contest runningContest = contestService.getRunningContest();
		if (runningContest == null)
			return SecurityClearanceType.ANONYMOUS;

		Object auth = SecurityContextHolder.getContext().getAuthentication();

		if (auth == null || !(auth instanceof User)) {
			return SecurityClearanceType.DENIED;
		}

		User user = (User) auth;

		if (contestService.isUserInContest(runningContest, user)) {
			return SecurityClearanceType.DENIED;
		}

		return SecurityClearanceType.SUPERUSER;
	}
}
