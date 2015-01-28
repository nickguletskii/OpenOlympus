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
package org.ng200.openolympus.controller.auth;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import org.ng200.openolympus.cerberus.util.Lists;
import org.ng200.openolympus.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationInformationController {
	public static class UserStatus {
		private boolean loggedIn;
		private String username;
		private List<String> roles;

		public UserStatus(boolean loggedIn, String username, List<String> roles) {
			super();
			this.loggedIn = loggedIn;
			this.username = username;
			this.roles = roles;
		}

		public List<String> getRoles() {
			return this.roles;
		}

		public String getUsername() {
			return this.username;
		}

		public boolean isLoggedIn() {
			return this.loggedIn;
		}

		public void setLoggedIn(boolean loggedIn) {
			this.loggedIn = loggedIn;
		}

		public void setRoles(List<String> roles) {
			this.roles = roles;
		}

		public void setUsername(String username) {
			this.username = username;
		}
	}

	@Autowired
	private UserService userService;

	@RequestMapping(value = "/api/security/userStatus", method = RequestMethod.GET)
	public UserStatus getUsers(Principal principal) {
		if (principal == null) {
			return new UserStatus(false, null, Lists.from());
		}
		return new UserStatus(true, principal.getName(), this.userService
				.getUserByUsername(principal.getName()).getRoles().stream()
				.map(role -> role.getRoleName()).collect(Collectors.toList()));
	}

}
