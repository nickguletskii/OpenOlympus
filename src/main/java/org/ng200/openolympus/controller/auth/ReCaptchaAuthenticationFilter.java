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

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import net.tanesha.recaptcha.ReCaptchaImpl;
import net.tanesha.recaptcha.ReCaptchaResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

//TODO: Write ReCaptcha support for the new API
public class ReCaptchaAuthenticationFilter extends GenericFilterBean {

	private final AntPathRequestMatcher LOGIN_REQUEST_MATCHER = new AntPathRequestMatcher(
			"/login", "POST");

	@Value("${enableCaptcha}")
	private boolean captchaEnabled;

	private final ReCaptchaImpl reCaptcha;
	private final Logger logger = LoggerFactory
			.getLogger(ReCaptchaAuthenticationFilter.class);

	@Value("${remoteAddress}")
	private String remoteAddress;

	public ReCaptchaAuthenticationFilter() {
		this.reCaptcha = new ReCaptchaImpl();
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		if (!this.LOGIN_REQUEST_MATCHER.matches((HttpServletRequest) request)) {
			chain.doFilter(request, response);
			return;
		}

		final String reCaptchaChallenge = request
				.getParameter("recaptcha_challenge_field");
		final String reCaptchaResponse = request
				.getParameter("recaptcha_response_field");

		if (this.captchaEnabled && !StringUtils.isEmpty(reCaptchaChallenge)) {
			if (!StringUtils.isEmpty(reCaptchaResponse)) {
				final ReCaptchaResponse reCaptchaCheck = this.reCaptcha
						.checkAnswer(this.remoteAddress, reCaptchaChallenge,
								reCaptchaResponse);

				if (!reCaptchaCheck.isValid()) {
					this.reCaptchaError(
							request,
							response,
							"ReCaptcha failed : "
									+ reCaptchaCheck.getErrorMessage());
					return;
				} else {
					chain.doFilter(request, response);
					return;
				}
			} else {
				this.reCaptchaError(request, response,
						"ReCaptcha failed : empty answer");
				return;
			}
		}

		chain.doFilter(request, response);
	}

	private void reCaptchaError(ServletRequest request,
			ServletResponse response, String errorMsg) throws IOException {
		this.logger.warn("ReCaptcha failed : {}", errorMsg);

		response.setContentType("application/json");
		AuthenticationResponder.writeLoginStatusJson(response.getWriter(),
				"unknown", "failed");
	}

	@Value("${recaptchaPrivateKey}")
	public void setRecaptchaPrivateKey(String key) {
		if (this.captchaEnabled) {
			this.reCaptcha.setPrivateKey(key);
		}
	}
}