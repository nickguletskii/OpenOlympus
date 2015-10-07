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
