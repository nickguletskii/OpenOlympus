/**
 * The MIT License
 * Copyright (c) 2014-2016 Nick Guletskii
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
import org.ng200.openolympus.SecurityClearanceType;
import org.ng200.openolympus.jooq.tables.pojos.User;
import org.ng200.openolympus.security.annotations.SecurityAnd;
import org.ng200.openolympus.security.annotations.SecurityLeaf;
import org.ng200.openolympus.security.annotations.SecurityOr;
import org.ng200.openolympus.services.UserApprovalService;
import org.ng200.openolympus.services.UserService;
import org.ng200.openolympus.util.Beans;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Profile("web")
@SecurityOr({
				@SecurityAnd({
								@SecurityLeaf(value = SecurityClearanceType.APPROVE_USER_REGISTRATIONS)
		})
})
public class ApproveUserRegistrationController {

	public static class Result extends User {
		/**
		 *
		 */
		private static final long serialVersionUID = -6521631631272152983L;
		private String statusMessage;

		private ResultType resultType;

		public Result(User user, ResultType resultType, String message) {
			this.resultType = resultType;
			this.statusMessage = message;

			Beans.copy(user, this);
		}

		public ResultType getResultType() {
			return this.resultType;
		}

		public String getStatusMessage() {
			return this.statusMessage;
		}

		public void setResultType(ResultType resultType) {
			this.resultType = resultType;
		}

		public void setStatusMessage(String statusMessage) {
			this.statusMessage = statusMessage;
		}
	}

	public static enum ResultType {
		SUCCESS, FAILURE
	}

	private static final Logger logger = LoggerFactory
			.getLogger(ApproveUserRegistrationController.class);

	@Autowired
	private UserService userService;

	@Autowired
	private UserApprovalService userApprovalService;

	@ResponseBody
	@RequestMapping(value = "/api/admin/users/approve", method = RequestMethod.POST)
	public List<Result> approveUsers(@RequestBody List<Long> userIds) {
		return userIds.stream().map(id -> this.userService.getUserById(id))
				.map(u -> {
					try {
						this.userApprovalService.approveUser(u);
						return new Result(u, ResultType.SUCCESS, "OK");
					} catch (MailException | MessagingException
							| EmailException e) {
						return new Result(u, ResultType.FAILURE,
								e.getMessage());
					}
				})
				.collect(Collectors.toList());
	}
}
