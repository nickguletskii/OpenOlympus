package org.ng200.openolympus.controller.contest;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.ng200.openolympus.jooq.enums.ContestPermissionType;
import org.ng200.openolympus.jooq.tables.pojos.Contest;
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
public class ContestPrincipalPermissionsController {

	@Autowired
	private AclService aclService;

	@RequestMapping(value = "/api/contest/{contest}/hasContestPermission/{principal}", method = RequestMethod.GET)
	public boolean hasContestPermission(
			@PathVariable("contest") Contest contest,
			@PathVariable("principal") Long principal,
			@RequestParam("permission") ContestPermissionType permission)
					throws BindException {

		return aclService.hasContestPermission(contest, principal, permission);
	}

	@RequestMapping(value = "/api/contest/{contest}/permissionsForPrincipal/{principal}", method = RequestMethod.GET)
	public List<ContestPermissionType> contestPermissions(
			@PathVariable("contest") Contest contest,
			@PathVariable("principal") Long principal)
					throws BindException {

		return Stream
				.of(ContestPermissionType.values()).filter(perm -> aclService
						.hasContestPermission(contest, principal, perm))
				.collect(Collectors.toList());
	}
}
