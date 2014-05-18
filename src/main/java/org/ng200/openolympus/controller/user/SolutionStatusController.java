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
package org.ng200.openolympus.controller.user;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.ng200.openolympus.cerberus.SolutionResult;
import org.ng200.openolympus.controller.ContestRestrictedController;
import org.ng200.openolympus.model.Role;
import org.ng200.openolympus.model.Solution;
import org.ng200.openolympus.model.Verdict;
import org.ng200.openolympus.repositories.VerdictRepository;
import org.ng200.openolympus.services.SolutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SolutionStatusController extends ContestRestrictedController {
	@Autowired
	private VerdictRepository verdictRepository;

	@Autowired
	private SolutionService solutionService;

	@RequestMapping(value = "/solution", method = RequestMethod.GET)
	public String viewSolutionStatus(final HttpServletRequest request,
			final Model model,
			@RequestParam(value = "id") final Solution solution,
			final Principal principal) {
		if (principal == null
				|| (!solution.getUser().getUsername()
						.equals(principal.getName()) && !request
						.isUserInRole(Role.SUPERUSER))) {
			throw new InsufficientAuthenticationException(
					"You attempted to view a solution that doesn't belong to you!");
		}

		this.assertSuperuserOrTaskAllowed(principal, solution.getTask());
		model.addAttribute("solution", solution);
		final List<Verdict> verdicts = this.solutionService
				.getVerdicts(solution);
		model.addAttribute(
				"completeScore",
				verdicts.stream().map((x) -> x.getScore())
				.reduce((x, y) -> x.add(y)).orElse(BigDecimal.ZERO));
		model.addAttribute(
				"completeMaximumScore",
				verdicts.stream().map((x) -> x.getMaximumScore())
				.reduce((x, y) -> x.add(y)).orElse(BigDecimal.ZERO));
		model.addAttribute(
				"verdicts",
				verdicts.stream()
				.sorted((l, r) -> Long.compare(l.getId(), r.getId()))
				.collect(Collectors.toList()));
		model.addAttribute("verdictMessageStrings",
				new HashMap<SolutionResult.Result, String>() {
			/**
			 *
			 */
			private static final long serialVersionUID = 8526897014680785208L;

			{
				this.put(SolutionResult.Result.OK, "solution.result.ok");
				this.put(SolutionResult.Result.TIME_LIMIT,
						"solution.result.timeLimit");
				this.put(SolutionResult.Result.MEMORY_LIMIT,
						"solution.result.memoryLimit");
				this.put(SolutionResult.Result.OUTPUT_LIMIT,
						"solution.result.outputLimit");
				this.put(SolutionResult.Result.RUNTIME_ERROR,
						"solution.result.runtimeError");
				this.put(SolutionResult.Result.INTERNAL_ERROR,
						"solution.result.internalError");
				this.put(SolutionResult.Result.SECURITY_VIOLATION,
						"solution.result.securityViolation");
				this.put(SolutionResult.Result.COMPILE_ERROR,
						"solution.result.compileError");
				this.put(SolutionResult.Result.PRESENTATION_ERROR,
						"solution.result.presentationError");
				this.put(SolutionResult.Result.WRONG_ANSWER,
						"solution.result.wrongAnswer");

			}
		});

		return "tasks/solution";
	}
}
