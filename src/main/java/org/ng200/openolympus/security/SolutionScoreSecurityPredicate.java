package org.ng200.openolympus.security;

import org.ng200.openolympus.SecurityClearanceType;
import org.ng200.openolympus.jooq.tables.pojos.User;

public class SolutionScoreSecurityPredicate
		implements SecurityClearancePredicate {

	@Override
	public SecurityClearanceType getRequiredClearanceForObject(User user,
			Object obj) {
		// TODO Auto-generated method stub
		return SecurityClearanceType.APPROVED_USER;
	}

}
