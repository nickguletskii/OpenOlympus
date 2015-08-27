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
package org.ng200.openolympus.controller.contest;

import java.io.IOException;
import java.sql.Timestamp;

import javax.validation.Valid;

import org.apache.commons.compress.archivers.ArchiveException;
import org.ng200.openolympus.SecurityClearanceType;
import org.ng200.openolympus.controller.BindingResponse;
import org.ng200.openolympus.controller.BindingResponse.Status;
import org.ng200.openolympus.dto.ContestDto;
import org.ng200.openolympus.jooq.tables.pojos.Contest;
import org.ng200.openolympus.security.SecurityAnd;
import org.ng200.openolympus.security.SecurityLeaf;
import org.ng200.openolympus.security.SecurityOr;
import org.ng200.openolympus.security.UserHasContestPermission;
import org.ng200.openolympus.services.ContestService;
import org.ng200.openolympus.validation.ContestDtoValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Profile;

import com.google.common.collect.ImmutableMap;

@RestController
@Profile("web")

@SecurityOr({
              @SecurityAnd({
                             @SecurityLeaf(
                                     value = SecurityClearanceType.CONTEST_ORGANISER)
		})
})
public class ContestCreationController {

	@Autowired
	private ContestDtoValidator contestDtoValidator;

	@Autowired
	private ContestService contestService;

	@RequestMapping(method = RequestMethod.POST, value = "/api/contests/create")
	public BindingResponse createContest(@Valid final ContestDto contestDto,
	        final BindingResult bindingResult) throws IllegalStateException,
	                IOException, ArchiveException, BindException {

		if (bindingResult.hasErrors()) {
			throw new BindException(bindingResult);
		}

		this.contestDtoValidator.validate(contestDto, null, bindingResult);

		if (bindingResult.hasErrors()) {
			throw new BindException(bindingResult);
		}
		Contest contest = new Contest()
		        .setDuration(contestDto.getDuration())
		        .setName(contestDto.getName())
		        .setShowFullTestsDuringContest(
		                contestDto.isShowFullTestsDuringContest())
		        .setStartTime(
		                Timestamp.from(contestDto.getStartTime().toInstant()));
		contest = this.contestService.insertContest(contest);
		return new BindingResponse(Status.OK, null,
		        new ImmutableMap.Builder<String, Object>().put("id",
		                contest.getId()).build());
	}

}
