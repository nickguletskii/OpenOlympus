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
package org.ng200.openolympus.controller.admin;

import java.util.List;
import java.util.stream.Collectors;

import javax.mail.MessagingException;

import org.apache.commons.mail.EmailException;
import org.ng200.openolympus.Assertions;
import org.ng200.openolympus.controller.auth.EmailConfirmationController;
import org.ng200.openolympus.model.Role;
import org.ng200.openolympus.model.User;
import org.ng200.openolympus.model.views.PriviligedView;
import org.ng200.openolympus.services.EmailService;
import org.ng200.openolympus.services.RoleService;
import org.ng200.openolympus.services.UserService;
import org.ng200.openolympus.util.Beans;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.google.common.collect.ImmutableMap;

@RestController
public class ApproveUserRegistrationController {

	public static class Result extends User {
		/**
		 *
		 */
		private static final long serialVersionUID = -6521631631272152983L;
		private String statusMessage;

		public Result(User user, String message) {
			this.statusMessage = message;

			Beans.copy(user, this);
		}

		@JsonView(PriviligedView.class)
		public String getStatusMessage() {
			return this.statusMessage;
		}

		public void setStatusMessage(String statusMessage) {
			this.statusMessage = statusMessage;
		}
	}

	@Value(value = "${emailConfirmationEnabled:true}")
	private boolean emailConfirmationEnabled;

	@Autowired
	private EmailService emailService;
	@Autowired
	private RoleService roleService;

	@Autowired
	private UserService userService;

	public void approveUser(User user) throws MessagingException,
			EmailException {
		Assertions.resourceExists(user);

		if (this.emailConfirmationEnabled) {

			final String link = EmailConfirmationController.getUriBuilder()
					.queryParam("user", user.getId())
					.queryParam("token", user.getEmailConfirmationToken())
					.build().encode().toUriString();

			this.emailService
					.sendEmail(
							user.getEmailAddress(),
							"Welcome to OpenOlympus!",
							"emailConfirmationEmail",
							new StringBuilder()
									.append("Welcome to OpenOlympus! Please click the following link to complete your registration: ") // TODO:
									// Localise
									// this
									// message
									.append(link).toString(),
							ImmutableMap.<String, Object> builder()
									.put("link", link).put("user", user)
									.build());
			user.setApprovalEmailSent(true);
			user = this.userService.saveUser(user);
		} else {
			final Role role = this.roleService.getRoleByName(Role.USER);

			user.getRoles().add(role);
			user.setEmailConfirmationToken(null);
			this.userService.saveUser(user);
		}
	}

	@RequestMapping(value = "/api/admin/users/approve", method = RequestMethod.POST)
	@JsonView(PriviligedView.class)
	public List<Result> approveUsers(@RequestBody List<Long> userIds) {
		return userIds.stream().map(id -> this.userService.getUserById(id))
				.filter(user -> !this.userService.isUserApproved(user))
				.map(u -> {
					try {
						this.approveUser(u);
					} catch (MessagingException | EmailException e) {
						return new Result(u, e.getMessage());
					}
					return new Result(u, "OK");
				}).collect(Collectors.toList());
	}
}
