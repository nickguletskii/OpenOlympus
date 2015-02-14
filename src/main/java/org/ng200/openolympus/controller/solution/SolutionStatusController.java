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
package org.ng200.openolympus.controller.solution;

import static org.ng200.openolympus.SecurityExpressionConstants.AND;
import static org.ng200.openolympus.SecurityExpressionConstants.IS_ADMIN;
import static org.ng200.openolympus.SecurityExpressionConstants.IS_USER;
import static org.ng200.openolympus.SecurityExpressionConstants.OR;
import static org.ng200.openolympus.SecurityExpressionConstants.SOLUTION_INSIDE_CURRENT_CONTEST_OR_NO_CONTEST;
import static org.ng200.openolympus.SecurityExpressionConstants.USER_IS_OWNER;

import java.security.Principal;
import java.util.List;

import org.ng200.openolympus.model.Solution;
import org.ng200.openolympus.model.Verdict;
import org.ng200.openolympus.services.SolutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class SolutionStatusController {

	@Autowired
	private SolutionService solutionService;

	@PreAuthorize(IS_ADMIN + OR + '(' + IS_USER + AND + USER_IS_OWNER + AND
			+ SOLUTION_INSIDE_CURRENT_CONTEST_OR_NO_CONTEST + ')')
	@RequestMapping(value = "/api/solution/{id}/verdicts", method = RequestMethod.GET)
	public List<Verdict> viewSolutionStatus(
			@PathVariable(value = "id") final Solution solution,
			final Principal principal) {
		return this.solutionService.getVerdictsVisibleDuringContest(solution);
	}
}
