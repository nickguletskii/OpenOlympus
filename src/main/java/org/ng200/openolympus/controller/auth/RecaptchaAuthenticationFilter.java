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
import java.net.URISyntaxException;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.ng200.openolympus.exceptions.GeneralNestedRuntimeException;
import org.ng200.openolympus.services.CaptchaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.GenericFilterBean;

public class RecaptchaAuthenticationFilter extends GenericFilterBean {

	private final AntPathRequestMatcher LOGIN_REQUEST_MATCHER = new AntPathRequestMatcher(
			"/login", "POST");

	private static final Logger logger = LoggerFactory
			.getLogger(RecaptchaAuthenticationFilter.class);
	@Autowired
	private CaptchaService captchaService;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		if (!this.LOGIN_REQUEST_MATCHER.matches((HttpServletRequest) request)) {
			chain.doFilter(request, response);
			return;
		}
		final String recaptchaResponse = request
				.getParameter("recaptchaResponse");

		List<String> recaptchaErrors;
		try {
			recaptchaErrors = this.captchaService
					.checkCaptcha(recaptchaResponse);
		} catch (URISyntaxException e) {
			throw new GeneralNestedRuntimeException(
					"Couldn't access Recaptcha servers: ", e);
		}

		if (recaptchaErrors != null && !recaptchaErrors.isEmpty()) {
			this.recaptchaError(request, response, recaptchaErrors);
			return;
		}

		chain.doFilter(request, response);
	}

	private void recaptchaError(ServletRequest request,
			ServletResponse response, List<String> recaptchaErrors)
			throws IOException {

		response.setContentType("application/json");
		AuthenticationResponder.writeLoginStatusJson(response.getWriter(),
				"unknown", recaptchaErrors);
	}

}