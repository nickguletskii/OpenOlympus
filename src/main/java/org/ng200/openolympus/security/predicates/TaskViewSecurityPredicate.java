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
package org.ng200.openolympus.security.predicates;

import org.ng200.openolympus.SecurityClearanceType;
import org.ng200.openolympus.jooq.enums.ContestPermissionType;
import org.ng200.openolympus.jooq.enums.TaskPermissionType;
import org.ng200.openolympus.jooq.tables.pojos.Contest;
import org.ng200.openolympus.jooq.tables.pojos.Task;
import org.ng200.openolympus.jooq.tables.pojos.User;
import org.ng200.openolympus.security.DynamicSecurityPredicate;
import org.ng200.openolympus.security.annotations.CurrentUser;
import org.ng200.openolympus.security.annotations.MethodSecurityPredicate;
import org.ng200.openolympus.security.annotations.Parameter;
import org.ng200.openolympus.security.annotations.PredicateDocumentation;
import org.ng200.openolympus.services.AclService;
import org.ng200.openolympus.services.contest.ContestTasksService;
import org.ng200.openolympus.services.contest.ContestTimingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@PredicateDocumentation({
							"If the user can modify the task's ACL (ignores contest)",
							"If the user can modify the task (ignores contest)",
							"If there is no current contest and the user has permission to view the task",
							"If there is a current contest, the task is in that contest,"
									+ "the user can view this task during the contest,"
									+ "the user is a participant of this contest,"
									+ " and the contest is in progress for that user",
							"If there is a current contest, the task is in that contest,"
									+ "the user can view this task during the contest,"
									+ "the user is a participant of this contest,"
									+ " and the user is allowed to view the task after the contest started"
})
public class TaskViewSecurityPredicate implements DynamicSecurityPredicate {
	@Autowired
	private AclService aclService;

	@Autowired
	private ContestTasksService contestTasksService;
	@Autowired
	private ContestTimingService contestTimingService;

	private boolean canUserViewTasksAfterContestStarted(User user,
			Contest runningContest) {
		return this.aclService.hasContestPermission(runningContest, user,
				ContestPermissionType.view_tasks_after_contest_started);
	}

	private boolean isUserAllowedToViewTaskAtAll(User user, Task task) {
		return this.aclService.hasTaskPermission(task, user,
				TaskPermissionType.view)
				|| this.aclService.hasTaskPermission(task, user,
						TaskPermissionType.view_during_contest);
	}

	private boolean isUserParticipantAndContestInProgress(User user,
			Contest runningContest) {
		return this.aclService.hasContestPermission(runningContest, user,
				ContestPermissionType.participate)
				&& this.contestTimingService.isContestInProgressForUser(
						runningContest,
						user);
	}

	@MethodSecurityPredicate
	public SecurityClearanceType predicate(@CurrentUser User user,
			@Parameter("task") Task task) {
		if (this.aclService.hasTaskPermission(task, user,
				TaskPermissionType.modify)
				|| this.aclService.hasTaskPermission(task, user,
						TaskPermissionType.manage_acl)) {
			return SecurityClearanceType.APPROVED_USER;
		}

		final Contest runningContest = this.contestTimingService
				.getRunningContest();
		if (runningContest == null) {
			if (this.aclService.hasTaskPermission(task, user,
					TaskPermissionType.view)) {
				return SecurityClearanceType.APPROVED_USER;
			}
			return SecurityClearanceType.SUPERUSER;
		}
		if (this.contestTasksService.isTaskInContest(task, runningContest)
				&& (this.isUserParticipantAndContestInProgress(user,
						runningContest)
						|| this.canUserViewTasksAfterContestStarted(user,
								runningContest))
				&& this.isUserAllowedToViewTaskAtAll(user, task)) {
			return SecurityClearanceType.APPROVED_USER;
		}
		return SecurityClearanceType.SUPERUSER;
	}
}
