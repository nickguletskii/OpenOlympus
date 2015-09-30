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

	@Autowired
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
