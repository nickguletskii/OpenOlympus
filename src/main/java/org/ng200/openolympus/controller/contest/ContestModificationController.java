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

import javax.validation.Valid;

import org.apache.commons.compress.archivers.ArchiveException;
import org.ng200.openolympus.Assertions;
import org.ng200.openolympus.SecurityClearanceType;
import org.ng200.openolympus.controller.BindingResponse;
import org.ng200.openolympus.dto.ContestDto;
import org.ng200.openolympus.jooq.enums.ContestPermissionType;
import org.ng200.openolympus.jooq.tables.pojos.Contest;
import org.ng200.openolympus.security.annotations.ContestPermissionRequired;
import org.ng200.openolympus.security.annotations.SecurityAnd;
import org.ng200.openolympus.security.annotations.SecurityLeaf;
import org.ng200.openolympus.security.annotations.SecurityOr;
import org.ng200.openolympus.security.predicates.UserHasContestPermission;
import org.ng200.openolympus.services.contest.ContestCRUDService;
import org.ng200.openolympus.validation.ContestDtoValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.context.annotation.Profile;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Profile("web")
@RequestMapping("/api/contest/{contest}/edit")
@SecurityOr({
				@SecurityAnd({
								@SecurityLeaf(value = SecurityClearanceType.APPROVED_USER, predicates = UserHasContestPermission.class)
		})
})
@ContestPermissionRequired(ContestPermissionType.edit)
public class ContestModificationController {

	@Autowired
	private ContestDtoValidator contestDtoValidator;

	@Autowired
	private ContestCRUDService contestCRUDService;

	@RequestMapping(method = RequestMethod.POST)
	@Caching(evict = {
						@CacheEvict(value = "contests", key = "#contest.id")
	})
	public BindingResponse editContest(
			@PathVariable("contest") Contest contest,
			@Valid final ContestDto contestDto,
			final BindingResult bindingResult) throws IllegalStateException,
					IOException, ArchiveException, BindException {
		Assertions.resourceExists(contest);

		this.contestDtoValidator.validate(contestDto, contest, bindingResult);

		if (bindingResult.hasErrors()) {
			throw new BindException(bindingResult);
		}
		contest.setName(contestDto.getName());
		contest.setDuration(contestDto.getDuration());
		contest.setStartTime(contestDto.getStartTime());
		contest.setShowFullTestsDuringContest(contestDto
				.isShowFullTestsDuringContest());
		contest = this.contestCRUDService.updateContest(contest);
		return BindingResponse.OK;
	}

	@RequestMapping(method = RequestMethod.GET)
	public Contest showContestEditingForm(
			@PathVariable("contest") final Contest contest) {
		return contest;
	}
}
