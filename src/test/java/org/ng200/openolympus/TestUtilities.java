package org.ng200.openolympus;

import java.time.Instant;
import java.util.Date;
import java.util.HashSet;
import java.util.UUID;

import javax.mail.MessagingException;

import org.apache.commons.mail.EmailException;
import org.ng200.openolympus.controller.admin.ApproveUserRegistrationController;
import org.ng200.openolympus.model.Role;
import org.ng200.openolympus.model.User;
import org.ng200.openolympus.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class TestUtilities {

	@Autowired
	private UserService userService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private ApproveUserRegistrationController approveUserRegistrationController;

	public User createTestUser(String name, String password)
			throws MessagingException, EmailException {
		User user = userService.getUserByUsername(name);
		if (user != null)
			return user;

		user = new User(name, this.passwordEncoder.encode(password), "", "",
				"", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
				Date.from(Instant.now()), UUID.randomUUID().toString());
		user.setRoles(new HashSet<Role>());
		user = userService.saveUser(user);
		approveUserRegistrationController.approveUser(user);
		return user;
	}

	public void logInAsAdmin() {
		SecurityContext context = SecurityContextHolder.createEmptyContext();

		User principal = userService.getUserByUsername("admin");
		Authentication auth = new UsernamePasswordAuthenticationToken(
				principal, "admin", principal.getAuthorities());
		context.setAuthentication(auth);
		SecurityContextHolder.setContext(context);
	}

}
