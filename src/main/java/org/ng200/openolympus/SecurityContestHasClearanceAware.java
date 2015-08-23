package org.ng200.openolympus;

import org.springframework.security.core.context.SecurityContextHolder;

import com.fasterxml.jackson.databind.PropertyName;

public interface SecurityContestHasClearanceAware {

	public default boolean securityContextHasClearance(
			SecurityClearanceType requestedClearance) {
		// No clearance requested
		if (requestedClearance == null
				|| requestedClearance == SecurityClearanceType.ANONYMOUS) {
			return true;
		}
		// Not authenticated, but anonymous access is not allowed as per
		// previous step
		if (SecurityContextHolder.getContext().getAuthentication() == null) {
			return false;
		}
		// The user is logged in, therefore if the requested clearance is
		// LOGGED_IN we give clearance.
		if (requestedClearance == SecurityClearanceType.LOGGED_IN) {
			return true;
		}
		return SecurityContextHolder.getContext()
				.getAuthentication()
				.getAuthorities()
				.stream()
				.anyMatch(
						(authority) -> authority instanceof Authorities.OlympusAuthority
								&&
								((Authorities.OlympusAuthority) authority)
										.getClearanceType() == requestedClearance);
	}

}