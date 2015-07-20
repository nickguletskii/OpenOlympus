package org.ng200.openolympus.security;

import org.ng200.openolympus.SecurityClearanceType;
import org.ng200.openolympus.jooq.tables.pojos.Group;
import org.ng200.openolympus.jooq.tables.pojos.User;

public class GroupHiddenPredicate implements SecurityClearancePredicate {

	@Override
	public SecurityClearanceType getRequiredClearanceForObject(User user,
			Object obj) {
		Group group = (Group) obj;
		if (group.getHidden())
			return SecurityClearanceType.ADMINISTRATIVE_USER;
		return SecurityClearanceType.APPROVED_USER;
	}

}
