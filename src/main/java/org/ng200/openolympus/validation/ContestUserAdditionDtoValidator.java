/**
 * The MIT License
 * Copyright (c) 2014 Nick Guletskii
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
import org.ng200.openolympus.model.Contest;
import org.ng200.openolympus.model.User;
import org.ng200.openolympus.repositories.ContestParticipationRepository;
import org.ng200.openolympus.repositories.ContestRepository;
import org.ng200.openolympus.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class ContestUserAdditionDtoValidator {

	@Autowired
	private ContestParticipationRepository contestParticipationRepository;

	@Autowired
	private ContestRepository contestRepository;
	@Autowired
	private UserRepository userRepository;

	public void validate(final ContestUserAdditionDto target,
			final Contest contest, final Errors errors) {
		if (errors.hasErrors()) {
			return;
		}
		final User user = this.userRepository.findByUsername(target
				.getUsername());
		if (user == null) {
			errors.rejectValue("username", "",
					"contest.addUser.form.errors.userNull");
			return;
		}
		if (contest == null) {
			errors.rejectValue("contestId", "",
					"contest.addUser.form.errors.badContest");
			return;
		}
		if (this.contestParticipationRepository.findOneByContestAndUser(
				contest, user) != null) {
			errors.rejectValue("username", "",
					"contest.addUser.form.errors.userAlreadyInContest");
			return;
		}
	}
}
