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

import org.ng200.openolympus.jooq.tables.pojos.Contest;
import org.ng200.openolympus.model.UserPrincipal;
import org.ng200.openolympus.services.ContestService;
import org.ng200.openolympus.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Profile("web")
public class SecurityInformationController {

	public static class SecurityInformation {

		private UserPrincipal currentPrincipal;

		private Contest currentContest;

		public UserPrincipal getCurrentPrincipal() {
			return currentPrincipal;
		}

		public void setCurrentPrincipal(UserPrincipal currentPrincipal) {
			this.currentPrincipal = currentPrincipal;
		}

		public Contest getCurrentContest() {
			return currentContest;
		}

		public void setCurrentContest(Contest currentContest) {
			this.currentContest = currentContest;
		}

		public SecurityInformation(UserPrincipal currentPrincipal,
				Contest currentContest) {
			super();
			this.currentPrincipal = currentPrincipal;
			this.currentContest = currentContest;
		}

	}

	@Autowired
	private UserService userService;

	@Autowired
	private ContestService contestService;

	@RequestMapping(value = "/api/security/status", method = RequestMethod.GET)
	public SecurityInformation securityStatys(Principal principal) {
		UserPrincipal currentPrincipal = null;
		if (principal != null) {
			currentPrincipal = this.userService
					.getUserAsPrincipalByUsername(principal
							.getName());
		}
		return new SecurityInformation(currentPrincipal,
				contestService.getRunningContest());
	}

}