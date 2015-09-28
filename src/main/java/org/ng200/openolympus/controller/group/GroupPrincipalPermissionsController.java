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
