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
package org.ng200.openolympus.controller.group;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.ng200.openolympus.SecurityClearanceType;
import org.ng200.openolympus.jooq.enums.GroupPermissionType;
import org.ng200.openolympus.model.OlympusPrincipal;
import org.ng200.openolympus.security.annotations.GroupPermissionRequired;
import org.ng200.openolympus.security.annotations.SecurityAnd;
import org.ng200.openolympus.security.annotations.SecurityLeaf;
import org.ng200.openolympus.security.annotations.SecurityOr;
import org.ng200.openolympus.security.predicates.UserHasGroupPermission;
import org.ng200.openolympus.services.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Profile("web")
@SecurityOr({
				@SecurityAnd({
								@SecurityLeaf(value = SecurityClearanceType.APPROVED_USER, predicates = UserHasGroupPermission.class)
		})
})
@GroupPermissionRequired(GroupPermissionType.manage_acl)
public class GroupACLController {
	public static class ACLItem {
		private List<OlympusPrincipal> principals;
		private GroupPermissionType permission;

		public ACLItem() {
		}

		public ACLItem(Entry<GroupPermissionType, List<OlympusPrincipal>> e) {
			this.permission = e.getKey();
			this.principals = e.getValue();
		}

		public ACLItem(List<OlympusPrincipal> principals,
				GroupPermissionType permission) {
			this.principals = principals;
			this.permission = permission;
		}

		public GroupPermissionType getPermission() {
			return this.permission;
		}

		public List<OlympusPrincipal> getPrincipals() {
			return this.principals;
		}

		public void setPermission(GroupPermissionType permission) {
			this.permission = permission;
		}

		public void setPrincipals(List<OlympusPrincipal> principals) {
			this.principals = principals;
		}

	}

	@Autowired
	private GroupService groupService;

	@RequestMapping(value = "/api/group/{group}/acl", method = RequestMethod.PUT)
	public void getGroupACL(
			@PathVariable("group") int id,
			@RequestBody Map<String, List<Long>> dto,
			final BindingResult bindingResult, final Principal principal) {
		this.groupService.setGroupPermissionsAndPrincipals(id,
				dto.entrySet().stream().collect(
						Collectors.toMap(
								e -> GroupPermissionType.valueOf(e.getKey()),
								e -> e.getValue())));
	}

	@RequestMapping(value = "/api/group/{group}/acl", method = RequestMethod.GET)
	public Map<GroupPermissionType, ACLItem> getGroupACL(
			@PathVariable("group") int id, final Principal principal) {
		final Map<GroupPermissionType, List<OlympusPrincipal>> groupPermissionsAndPrincipalData = this.groupService
				.getGroupPermissionsAndPrincipalData(id);
		return Stream.of(GroupPermissionType.values())
				.collect(Collectors.toMap(type -> type,
						type -> new ACLItem(
								groupPermissionsAndPrincipalData.get(type),
								type)));

	}
}
