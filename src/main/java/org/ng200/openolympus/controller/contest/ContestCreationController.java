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

import static org.ng200.openolympus.SecurityExpressionConstants.IS_ADMIN;

import java.io.IOException;
import java.util.HashSet;

import javax.validation.Valid;

import org.apache.commons.compress.archivers.ArchiveException;
import org.ng200.openolympus.controller.BindingResponse;
import org.ng200.openolympus.controller.BindingResponse.Status;
import org.ng200.openolympus.dto.ContestDto;
import org.ng200.openolympus.model.Contest;
import org.ng200.openolympus.services.ContestService;
import org.ng200.openolympus.validation.ContestDtoValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.ImmutableMap;

@RestController
public class ContestCreationController {

	@Autowired
	private ContestDtoValidator contestDtoValidator;

	@Autowired
	private ContestService contestService;

	@PreAuthorize(IS_ADMIN)
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
		Contest contest = new Contest(contestDto.getStartTime(),
				contestDto.getDuration(), contestDto.getName(), new HashSet<>());
		contest = this.contestService.saveContest(contest);
		return new BindingResponse(Status.OK, null,
				new ImmutableMap.Builder<String, Object>().put("id",
						contest.getId()).build());
	}

}
