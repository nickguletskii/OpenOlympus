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
package org.ng200.openolympus.services;

import java.security.SecureRandom;
import java.util.Base64;

import javax.mail.MessagingException;

import org.apache.commons.mail.EmailException;
import org.ng200.openolympus.Assertions;
import org.ng200.openolympus.controller.auth.EmailConfirmationController;
import org.ng200.openolympus.jooq.tables.pojos.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class UserApprovalService {

	private static final Logger logger = LoggerFactory
			.getLogger(UserApprovalService.class);

	@Value("${openolympus.userApproval.emailConfirmationEnabled:false}")
	public boolean emailConfirmationEnabled = false;

	@Autowired
	private MailProperties mailProperties;

	@Autowired
	private UserService userService;

	@Autowired(required=false)
	private JavaMailSender javaMailSender;

	private final SecureRandom random = new SecureRandom();

	public String generateToken() {
		final byte[] bytes = new byte[128];
		random.nextBytes(bytes);
		return Base64.getEncoder().encodeToString(bytes);
	}

	public void approveUser(User user) throws MessagingException,
			EmailException {
		Assertions.resourceExists(user);

		if (this.emailConfirmationEnabled) {

			user.setEmailConfirmationToken(generateToken());

			final String link = EmailConfirmationController.getUriBuilder()
					.queryParam("user", user.getId())
					.queryParam("token", user.getEmailConfirmationToken())
					.build().encode().toUriString();

			SimpleMailMessage mailMessage = new SimpleMailMessage();
			mailMessage.setFrom(mailProperties.getUsername());
			mailMessage.setTo(user.getEmailAddress());
			mailMessage.setSubject("OpenOlympus registration");
			mailMessage.setText(
					"Thank you for registering. You have been approved. " +
							"Please follow the following link to complete the registration process.\n\n"
							+
							link);
			javaMailSender.send(mailMessage);

			user.setApprovalEmailSent(true);
			user = this.userService.updateUser(user);
		} else {
			user.setApproved(true);
			user.setEmailConfirmationToken(null);
			this.userService.updateUser(user);
		}
	}

}
