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
		final Task task = this.taskService
				.getTaskByName(contestTaskAdditionDto.getTaskName());
		if (this.aclService.hasTaskPermission(task, user,
				TaskPermissionType.add_to_contest)) {
			return SecurityClearanceType.APPROVED_USER;
		}
		return SecurityClearanceType.ADMINISTRATIVE_USER;
	}
}
