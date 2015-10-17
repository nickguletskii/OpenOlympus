package org.ng200.openolympus.security.predicates;

import org.ng200.openolympus.SecurityClearanceType;
import org.ng200.openolympus.dto.ContestTaskAdditionDto;
import org.ng200.openolympus.jooq.enums.TaskPermissionType;
import org.ng200.openolympus.jooq.tables.pojos.Task;
import org.ng200.openolympus.jooq.tables.pojos.User;
import org.ng200.openolympus.security.DynamicSecurityPredicate;
import org.ng200.openolympus.security.annotations.CurrentUser;
import org.ng200.openolympus.security.annotations.MethodSecurityPredicate;
import org.ng200.openolympus.security.annotations.Parameter;
import org.ng200.openolympus.services.AclService;
import org.ng200.openolympus.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;

public class UserCanAddTaskPredicate implements DynamicSecurityPredicate {

	@Autowired
	private AclService aclService;

	@Autowired
	private TaskService taskService;

	@MethodSecurityPredicate
	public SecurityClearanceType vote(
			@Parameter("contestTaskAdditionDto") ContestTaskAdditionDto contestTaskAdditionDto,
			@CurrentUser User user) {
		Task task = taskService
				.getTaskByName(contestTaskAdditionDto.getTaskName());
		if (aclService.hasTaskPermission(task, user,
				TaskPermissionType.add_to_contest)) {
			return SecurityClearanceType.APPROVED_USER;
		}
		return SecurityClearanceType.ADMINISTRATIVE_USER;
	}
}
