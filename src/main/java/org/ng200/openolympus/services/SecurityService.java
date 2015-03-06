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
package org.ng200.openolympus.services;

import java.security.Principal;
import java.time.Instant;

import org.ng200.openolympus.model.Contest;
import org.ng200.openolympus.model.Role;
import org.ng200.openolympus.model.Solution;
import org.ng200.openolympus.model.Task;
import org.ng200.openolympus.model.User;
import org.ng200.openolympus.model.Verdict;
import org.ng200.openolympus.repositories.ContestParticipationRepository;
import org.ng200.openolympus.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("oolsec")
public class SecurityService {

	@Autowired
	private ContestService contestService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private TaskService taskService;

	@Autowired
	private ContestParticipationRepository contestParticipationRepository;

	@Autowired
	private RoleService roleService;

	@Autowired
	private PropertyService propertyService;

	public boolean isContestInProgressForUser(final Contest contest,
			String username) {
		return !contest.getStartTime().toInstant().isAfter(Instant.now())
				&& !this.contestService
						.getContestEndTimeForUser(contest,
								this.userRepository.findByUsername(username))
						.toInstant().isBefore(Instant.now());
	}

	public boolean isContestOver(final Contest contest) {
		return this.contestService.getContestEndIncludingAllTimeExtensions(
				contest).isBefore(Instant.now());
	}

	public boolean isOnLockdown() {
		return this.propertyService.get("isOnLockdown", false).<Boolean> as()
				.orElse(false);
	}

	public boolean isSolutionInCurrentContest(Solution solution) {
		final Contest runningContest = this.contestService.getRunningContest();
		return runningContest == null
				|| (this.isTaskInContest(solution.getTask(), runningContest)
						&&

						runningContest.getStartTime().toInstant()
								.isBefore(solution.getTimeAdded().toInstant()) && this.contestService
						.getContestEndTimeForUser(runningContest,
								solution.getUser()).toInstant()
						.isAfter(solution.getTimeAdded().toInstant()));
	}

	public boolean isSuperuser(final Principal principal) {
		if (principal == null) {
			return false;
		}
		return this.isSuperuser(this.userRepository.findByUsername(principal
				.getName()));
	}

	private boolean isSuperuser(final User user) {
		if (user == null) {
			return false;
		}
		return user.hasRole(this.roleService.getRoleByName(Role.SUPERUSER));
	}

	public boolean isTaskInContest(Task task, Contest contest) {
		if (contest.getTasks() == null)
			return false;
		return contest.getTasks().contains(task);
	}

	public boolean isTaskInCurrentContest(Task task) {
		final Contest runningContest = this.contestService.getRunningContest();
		return runningContest == null
				|| runningContest.getTasks().contains(task);
	}

	public boolean noContest() {
		return this.contestService.getRunningContest() == null;
	}

	public boolean noLockdown() {
		return !this.isOnLockdown();
	}

	public boolean canViewVerdictDuringContest(Verdict verdict) {
		final Contest runningContest = this.contestService.getRunningContest();
		return runningContest == null
				|| runningContest.isShowFullTestsDuringContest()
				|| verdict.isViewableWhenContestRunning();
	}
}
