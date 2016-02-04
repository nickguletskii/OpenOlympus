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
package org.ng200.openolympus.security.predicates;

import org.ng200.openolympus.SecurityClearanceType;
import org.ng200.openolympus.jooq.enums.ContestPermissionType;
import org.ng200.openolympus.jooq.tables.pojos.Contest;
import org.ng200.openolympus.jooq.tables.pojos.User;
import org.ng200.openolympus.jooq.tables.pojos.Verdict;
import org.ng200.openolympus.security.SecurityClearancePredicate;
import org.ng200.openolympus.services.AclService;
import org.ng200.openolympus.services.SolutionService;
import org.ng200.openolympus.services.contest.ContestTimingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VerdictInformationSecurityPredicate implements SecurityClearancePredicate {

	@Autowired
	private AclService aclService;

	@Autowired
	private ContestTimingService contestTimingService;

	@Autowired
	private SolutionService solutionService;

	public SecurityClearanceType getRequiredClearanceForObject(User user,
			Object obj) {
		Verdict verdict = (Verdict) obj;

		Contest runningContest = contestTimingService.getRunningContest();

		if (runningContest == null) {
			if (solutionService.getSolutionById(verdict.getSolutionId())
					.getUserId() == user.getId())
				return SecurityClearanceType.APPROVED_USER;
			return SecurityClearanceType.VIEW_ALL_SOLUTIONS;
		}
		boolean canViewAllSolutions = aclService.hasContestPermission(
				runningContest,
				user, ContestPermissionType.view_all_solutions);
		boolean canViewSolutionsDuringContest = aclService.hasContestPermission(
				runningContest, user,
				ContestPermissionType.view_results_during_contest);

		if ((verdict.getViewableDuringContest()
				|| canViewSolutionsDuringContest)
				&& canViewAllSolutions) {
			return SecurityClearanceType.APPROVED_USER;
		}

		if (solutionService.getSolutionById(verdict.getSolutionId())
				.getUserId() == user.getId()
				&& (verdict.getViewableDuringContest()
						|| canViewSolutionsDuringContest)) {
			return SecurityClearanceType.APPROVED_USER;
		}

		return SecurityClearanceType.APPROVED_USER;
	}

}
