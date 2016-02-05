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
package org.ng200.openolympus.controller.contest;

import java.security.Principal;
import java.util.List;

import org.ng200.openolympus.SecurityClearanceType;
import org.ng200.openolympus.exceptions.ResourceNotFoundException;
import org.ng200.openolympus.jooq.tables.pojos.Contest;
import org.ng200.openolympus.security.annotations.SecurityAnd;
import org.ng200.openolympus.security.annotations.SecurityLeaf;
import org.ng200.openolympus.security.annotations.SecurityOr;
import org.ng200.openolympus.services.contest.ContestCRUDService;
import org.ng200.openolympus.services.contest.ContestTimingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Profile("web")
@SecurityOr({
				@SecurityAnd({
								@SecurityLeaf(value = SecurityClearanceType.APPROVED_USER)
		})
})
public class ContestListController {

	private static final int PAGE_SIZE = 10;

	@Autowired
	private ContestTimingService contestTimingService;

	@Autowired
	private ContestCRUDService contestCRUDService;

	@RequestMapping(method = RequestMethod.GET, value = "/api/contestsCount")
	@ResponseBody
	public long contestCount() {
		return this.contestCRUDService.countContests();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/api/contests")
	@ResponseBody
	public List<Contest> contestList(
			@RequestParam(value = "page", defaultValue = "1") Integer pageNumber,
			Principal principal) {
		if (pageNumber < 1) {
			throw new ResourceNotFoundException();
		}
		return this.contestTimingService.getContestsOrderedByTime(pageNumber,
				ContestListController.PAGE_SIZE);
	}

}
