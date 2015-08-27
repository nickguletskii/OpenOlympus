package org.ng200.openolympus.security.predicates;

import org.ng200.openolympus.SecurityClearanceType;
import org.ng200.openolympus.jooq.tables.pojos.Task;
import org.ng200.openolympus.jooq.tables.pojos.User;
import org.ng200.openolympus.security.DynamicSecurityPredicate;
import org.ng200.openolympus.security.annotations.CurrentUser;
import org.ng200.openolympus.security.annotations.FindAnnotation;
import org.ng200.openolympus.security.annotations.MethodSecurityPredicate;
import org.ng200.openolympus.security.annotations.TaskPermissionRequired;
import org.ng200.openolympus.services.AclService;
import org.springframework.beans.factory.annotation.Autowired;

public class UserHasTaskPermission implements DynamicSecurityPredicate {
	@Autowired
	private AclService aclService;

	@MethodSecurityPredicate
	public SecurityClearanceType vote(
	        @FindAnnotation TaskPermissionRequired taskPermissionRequired,
	        Task task,
	        @CurrentUser User user) {

		return aclService.hasTaskPermission(task, user,
		        taskPermissionRequired.value())
		                ? SecurityClearanceType.ANONYMOUS
		                : SecurityClearanceType.DENIED;
	}
}
