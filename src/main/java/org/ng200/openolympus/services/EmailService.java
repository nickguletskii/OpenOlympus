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

import java.util.Map;

import javax.mail.MessagingException;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.ng200.openolympus.SecurityExpressionConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

	@Value("${emailHost}")
	private String emailHost;

	@Value("${emailHostPort}")
	private int emailHostPort;

	@Value("${emailProtocol}")
	private String emailProtocol;

	@Value("${emailLogin}")
	private String emailLogin;

	@Value("${emailPassword}")
	private String emailPassword;

	@PreAuthorize(SecurityExpressionConstants.IS_ADMIN)
	public void sendEmail(String emailAddress, String subject, String view, String alternativeText,
			Map<String, Object> variables) throws MessagingException, EmailException {
		final HtmlEmail email = new HtmlEmail();
		email.setHostName(this.emailHost);
		email.setSmtpPort(this.emailHostPort);
		email.setAuthenticator(new DefaultAuthenticator(this.emailLogin, this.emailPassword));
		email.setSSL(true);
		email.setFrom(this.emailLogin);
		email.setSubject(subject);

		email.setHtmlMsg(null); // TODO: fix emails

		email.setTextMsg(alternativeText);

		email.addTo(emailAddress);
		email.send();

	}
}
