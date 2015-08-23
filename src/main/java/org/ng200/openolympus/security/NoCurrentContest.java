package org.ng200.openolympus.security;

import org.ng200.openolympus.SecurityClearanceType;
import org.ng200.openolympus.jooq.tables.interfaces.IUser;
import org.ng200.openolympus.jooq.tables.pojos.Contest;
import org.ng200.openolympus.jooq.tables.pojos.User;
import org.ng200.openolympus.services.ContestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

public class NoCurrentContest {

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

		return SecurityClearanceType.ANONYMOUS;
	}
}
