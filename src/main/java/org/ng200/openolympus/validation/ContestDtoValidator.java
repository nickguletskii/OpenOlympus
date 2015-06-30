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

import java.util.Date;

import org.ng200.openolympus.dto.ContestDto;
import org.ng200.openolympus.jooq.tables.pojos.Contest;
import org.ng200.openolympus.services.ContestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class ContestDtoValidator {

	@Autowired
	private ContestService contestService;

	public void validate(final ContestDto contestDto, final Contest contest,
			final Errors errors) {
		if (errors.hasErrors()) {
			return;
		}

		if ((contest == null || !contest.getName().equals(contestDto.getName()))
				&& this.contestService.getContestByName(contestDto.getName()) != null) {
			errors.rejectValue("name", "",
					"contest.add.form.errors.contestAlreadyExists");
		}

		final Date start = contestDto.getStartTime();
		final Date end = Date.from(contestDto.getStartTime().toInstant()
				.plus(contestDto.getDuration()));
		final Contest runningContest = this.contestService
				.getContestThatIntersects(start, end);
		if (runningContest != null && !runningContest.equals(contest)) {
			errors.rejectValue("startTime", "",
					"contest.add.form.errors.contestIntersects");
		}
	}
}
