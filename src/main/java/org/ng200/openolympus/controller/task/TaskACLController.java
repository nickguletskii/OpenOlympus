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
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.ng200.openolympus.jooq.enums.TaskPermissionType;
import org.ng200.openolympus.model.OlympusPrincipal;
import org.ng200.openolympus.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TaskACLController {
	public static class ACLItem {
		private List<OlympusPrincipal> principals;
		private TaskPermissionType permission;

		public ACLItem() {
		}

		public List<OlympusPrincipal> getPrincipals() {
			return principals;
		}

		public void setPrincipals(List<OlympusPrincipal> principals) {
			this.principals = principals;
		}

		public TaskPermissionType getPermission() {
			return permission;
		}

		public void setPermission(TaskPermissionType permission) {
			this.permission = permission;
		}

		public ACLItem(List<OlympusPrincipal> principals,
				TaskPermissionType permission) {
			this.principals = principals;
			this.permission = permission;
		}

		public ACLItem(Entry<TaskPermissionType, List<OlympusPrincipal>> e) {
			this.permission = e.getKey();
			this.principals = e.getValue();
		}

	}

	@Autowired
	private TaskService taskService;

	@RequestMapping(value = "/api/task/{task}/acl", method = RequestMethod.GET)
	public Map<TaskPermissionType, ACLItem> getTaskACL(
			@PathVariable("task") int id, final Principal principal) {
		return taskService.getTaskPermissionsAndPrincipalData(id).entrySet()
				.stream()
				.collect(Collectors.toMap(Entry::getKey,
						e -> new ACLItem(e)));
	}
}
