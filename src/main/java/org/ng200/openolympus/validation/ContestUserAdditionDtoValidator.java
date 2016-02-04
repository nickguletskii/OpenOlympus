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
package org.ng200.openolympus.validation;

import org.ng200.openolympus.dto.ContestUserAdditionDto;
import org.ng200.openolympus.jooq.tables.pojos.Contest;
import org.ng200.openolympus.jooq.tables.pojos.User;
import org.ng200.openolympus.services.UserService;
import org.ng200.openolympus.services.contest.ContestUsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class ContestUserAdditionDtoValidator {

	@Autowired
	private UserService userService;

	@Autowired
	private ContestUsersService contestUsersService;

	public void validate(final ContestUserAdditionDto target,
			final Contest contest, final Errors errors) {
		if (errors.hasErrors()) {
			return;
		}
		final User user = this.userService.getUserByUsername(target
				.getUsername());
		if (user == null) {
			errors.rejectValue("username", "",
					"empty");
			return;
		}
		if (contest == null) {
			errors.rejectValue("contestId", "",
					"badContest");
			return;
		}
		if (this.contestUsersService.isUserParticipatingIn(user, contest)) {
			errors.rejectValue("username", "",
					"userAlreadyInContest");
			return;
		}
	}
}
