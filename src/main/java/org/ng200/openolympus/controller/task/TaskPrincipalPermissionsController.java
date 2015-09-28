package org.ng200.openolympus.controller.task;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.ng200.openolympus.jooq.enums.TaskPermissionType;
import org.ng200.openolympus.jooq.enums.TaskPermissionType;
import org.ng200.openolympus.jooq.tables.pojos.Task;
import org.ng200.openolympus.jooq.tables.pojos.Task;
import org.ng200.openolympus.services.AclService;
import org.ng200.openolympus.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Profile("web")
public class TaskPrincipalPermissionsController {

	@Autowired
	private AclService aclService;

	@Autowired
	private UserService userService;

	@RequestMapping(value = "/api/task/{task}/hasTaskPermission/{principal}", method = RequestMethod.GET)
	public boolean hasTaskPermission(
			@PathVariable("task") Task task,
			@PathVariable("principal") Long principal,
			@RequestParam("permission") TaskPermissionType permission)
					throws BindException {

		return aclService.hasTaskPermission(task, principal, permission);
	}

	@RequestMapping(value = "/api/task/{task}/permissionsForPrincipal", method = RequestMethod.GET)
	public List<TaskPermissionType> taskPermissions(
			@PathVariable("task") Task task,
			Principal principal)
					throws BindException {

		return Stream
				.of(TaskPermissionType.values()).filter(perm -> aclService
						.hasTaskPermission(task, userService
								.getUserByUsername(principal.getName()), perm))
				.collect(Collectors.toList());
	}
}
