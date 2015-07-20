package org.ng200.openolympus.security;

import org.ng200.openolympus.SecurityClearanceType;
import org.ng200.openolympus.jooq.enums.ContestPermissionType;
import org.ng200.openolympus.jooq.tables.pojos.Contest;
import org.ng200.openolympus.jooq.tables.pojos.User;
import org.ng200.openolympus.services.ContestService;
import org.springframework.beans.factory.annotation.Autowired;

public class UserKnowsAboutContest implements SecurityClearancePredicate {

	@Autowired
	private ContestService contestService;

	@Override
	public SecurityClearanceType getRequiredClearanceForObject(User user,
			Object obj) {
		Contest contest = (Contest) obj;
		if (contestService.userKnowsAboutContest(user, contest,
				ContestPermissionType.know_about))
			return SecurityClearanceType.APPROVED_USER;
		return SecurityClearanceType.ADMINISTRATIVE_USER;
	}

}
