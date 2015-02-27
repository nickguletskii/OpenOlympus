/**
 * The MIT License
 * Copyright (c) 2014-2015 Nick Guletskii
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
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
		User user = this.userService.getUserByUsername(name);
		if (user != null) {
			return user;
		}

		user = new User(name, this.passwordEncoder.encode(password), "", "",
				"", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
				Date.from(Instant.now()), UUID.randomUUID().toString());
		user.setRoles(new HashSet<Role>());
		user = this.userService.saveUser(user);
		this.approveUserRegistrationController.approveUser(user);
		return user;
	}

	public void logInAsAdmin() {
		final SecurityContext context = SecurityContextHolder
				.createEmptyContext();

		final User principal = this.userService.getUserByUsername("admin");
		final Authentication auth = new UsernamePasswordAuthenticationToken(
				principal, "admin", principal.getAuthorities());
		context.setAuthentication(auth);
		SecurityContextHolder.setContext(context);
	}

}
