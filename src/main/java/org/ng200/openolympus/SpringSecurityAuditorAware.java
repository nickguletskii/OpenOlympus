package org.ng200.openolympus;

import java.security.Principal;

import org.ng200.openolympus.model.User;
import org.ng200.openolympus.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SpringSecurityAuditorAware implements AuditorAware<User> {

	@Autowired
	private UserService userService;

	public User getCurrentAuditor() {

		Authentication authentication = SecurityContextHolder.getContext()
				.getAuthentication();

		if (authentication == null || !authentication.isAuthenticated()) {
			return null;
		}

		return userService.getUserByUsername(((Principal) authentication
				.getPrincipal()).getName());
	}
}