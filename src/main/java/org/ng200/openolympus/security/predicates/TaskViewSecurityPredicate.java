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
import org.ng200.openolympus.services.ContestService;
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
	private ContestService contestService;

	@MethodSecurityPredicate
	public SecurityClearanceType predicate(@CurrentUser User user,
			@Parameter("task") Task task) {
		if (aclService.hasTaskPermission(task, user, TaskPermissionType.modify)
				|| aclService.hasTaskPermission(task, user,
						TaskPermissionType.manage_acl)) {
			return SecurityClearanceType.APPROVED_USER;
		}

		Contest runningContest = contestService.getRunningContest();
		if (runningContest == null) {
			if (aclService.hasTaskPermission(task, user,
					TaskPermissionType.view))
				return SecurityClearanceType.APPROVED_USER;
			return SecurityClearanceType.SUPERUSER;
		}
		if (contestService.isTaskInContest(task, runningContest)
				&& (isUserParticipantAndContestInProgress(user, runningContest)
						|| canUserViewTasksAfterContestStarted(user,
								runningContest))
				&& isUserAllowedToViewTaskAtAll(user, task)) {
			return SecurityClearanceType.APPROVED_USER;
		}
		return SecurityClearanceType.SUPERUSER;
	}

	private boolean isUserAllowedToViewTaskAtAll(User user, Task task) {
		return aclService.hasTaskPermission(task, user,
				TaskPermissionType.view)
				|| aclService.hasTaskPermission(task, user,
						TaskPermissionType.view_during_contest);
	}

	private boolean canUserViewTasksAfterContestStarted(User user,
			Contest runningContest) {
		return aclService.hasContestPermission(runningContest, user,
				ContestPermissionType.view_tasks_after_contest_started);
	}

	private boolean isUserParticipantAndContestInProgress(User user,
			Contest runningContest) {
		return aclService.hasContestPermission(runningContest, user,
				ContestPermissionType.participate)
				&& contestService.isContestInProgressForUser(runningContest,
						user);
	}
}
