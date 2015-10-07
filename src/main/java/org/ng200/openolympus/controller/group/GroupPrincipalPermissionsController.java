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
package org.ng200.openolympus.controller.group;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.ng200.openolympus.jooq.enums.GroupPermissionType;
import org.ng200.openolympus.jooq.enums.GroupPermissionType;
import org.ng200.openolympus.jooq.tables.pojos.Group;
import org.ng200.openolympus.jooq.tables.pojos.Group;
import org.ng200.openolympus.services.AclService;
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
public class GroupPrincipalPermissionsController {

	@Autowired
	private AclService aclService;

	@RequestMapping(value = "/api/group/{group}/hasGroupPermission/{principal}", method = RequestMethod.GET)
	public boolean hasGroupPermission(
			@PathVariable("group") Group group,
			@PathVariable("principal") Long principal,
			@RequestParam("permission") GroupPermissionType permission)
					throws BindException {

		return aclService.hasGroupPermission(group, principal, permission);
	}
	
	@RequestMapping(value = "/api/group/{group}/permissionsForPrincipal/{principal}", method = RequestMethod.GET)
	public List<GroupPermissionType> groupPermissions(
			@PathVariable("group") Group group,
			@PathVariable("principal") Long principal)
					throws BindException {

		return Stream
				.of(GroupPermissionType.values()).filter(perm -> aclService
						.hasGroupPermission(group, principal, perm))
				.collect(Collectors.toList());
	}
}
