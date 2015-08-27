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

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.ng200.openolympus.SecurityClearanceType;
import org.ng200.openolympus.dto.SolutionDto;
import org.ng200.openolympus.jooq.tables.pojos.Contest;
import org.ng200.openolympus.jooq.tables.pojos.Solution;
import org.ng200.openolympus.jooq.tables.pojos.User;
import org.ng200.openolympus.security.annotations.SecurityAnd;
import org.ng200.openolympus.security.annotations.SecurityLeaf;
import org.ng200.openolympus.security.annotations.SecurityOr;
import org.ng200.openolympus.services.ContestService;
import org.ng200.openolympus.services.SolutionService;
import org.ng200.openolympus.services.TaskService;
import org.ng200.openolympus.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Profile;

@RestController
@Profile("web")
public class SolutionListController {

	private static final int PAGE_SIZE = 10;

	@Autowired
	private SolutionService solutionService;

	@Autowired
	private ContestService contestService;

	@Autowired
	private UserService userService;

	@Autowired
	private TaskService taskService;

	@SecurityOr({
	              @SecurityAnd({
	                             @SecurityLeaf(SecurityClearanceType.VIEW_ALL_SOLUTIONS)
			})
	})
	@RequestMapping(value = "/api/admin/solutionsCount",
	        method = RequestMethod.GET)
	public long getSolutionCount(final User user) {
		return this.solutionService.getSolutionCount();
	}

	@SecurityOr({
	              @SecurityAnd({
	                             @SecurityLeaf(SecurityClearanceType.APPROVED_USER)
			})
	})
	@RequestMapping(value = "/api/user/solutionsCount",
	        method = RequestMethod.GET)
	public long getSolutionCountForUser(final Principal principal) {
		final User user = this.userService.getUserByUsername(principal
		        .getName());

		// TODO: replace with SQL
		if (this.contestService.getRunningContest() == null) {
			return this.solutionService.countUserSolutions(user);
		}
		return this.solutionService.countUserSolutionsInContest(user,
		        this.contestService.getRunningContest());
	}

	@SecurityOr({
	              @SecurityAnd({
	                             @SecurityLeaf(SecurityClearanceType.VIEW_ALL_SOLUTIONS)
			})
	})
	@RequestMapping(value = "/api/admin/solutions", method = RequestMethod.GET)
	public List<SolutionDto> showAllSolutions(
	        @RequestParam(value = "page",
	                defaultValue = "1") final Integer pageNumber,
	        final Model model) {
		return this.solutionService
		        .getPage(pageNumber, SolutionListController.PAGE_SIZE)
		        .stream()
		        .map(solution -> new SolutionDto(solution, this.taskService
		                .getById(solution.getTaskId())))
		        .collect(Collectors.toList());
	}

	@SecurityOr({
	              @SecurityAnd({
	                             @SecurityLeaf(SecurityClearanceType.APPROVED_USER)
			})
	})
	@RequestMapping(value = "/api/user/solutions", method = RequestMethod.GET)

	public List<SolutionDto> showUserSolutions(
	        @RequestParam(value = "page",
	                defaultValue = "1") final Integer pageNumber,
	        final Model model, final Principal principal) {
		final User user = this.userService.getUserByUsername(principal
		        .getName());
		final List<Solution> solutions = new ArrayList<>();
		final Contest contest = this.contestService.getRunningContest();
		if (contest != null) {
			solutions
			        .addAll(this.solutionService.getPage(user, pageNumber,
			                SolutionListController.PAGE_SIZE, contest
			                        .getStartTime(),
			                this.contestService
			                        .getContestEndTimeForUser(contest, user)));
		} else {
			solutions.addAll(this.solutionService.getPageOutsideOfContest(user,
			        pageNumber, SolutionListController.PAGE_SIZE));
		}

		return solutions
		        .stream()
		        .map(solution -> new SolutionDto(solution, this.taskService
		                .getById(solution.getTaskId())))
		        .map(dto -> {
			        if (contest != null) {
				        dto.setScore(null);
			        }
			        return dto;
		        }).collect(Collectors.toList());
	}
}
