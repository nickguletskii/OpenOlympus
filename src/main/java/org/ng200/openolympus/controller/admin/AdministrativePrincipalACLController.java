package org.ng200.openolympus.controller.admin;

import java.util.Map;

import javax.validation.Valid;

import org.ng200.openolympus.Assertions;
import org.ng200.openolympus.controller.BindingResponse;
import org.ng200.openolympus.jooq.enums.GeneralPermissionType;
import org.ng200.openolympus.jooq.tables.pojos.Principal;
import org.ng200.openolympus.services.AclService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdministrativePrincipalACLController {

	@Autowired
	private AclService aclService;

	@RequestMapping(
	        value = "/api/admin/principal/{principal}/generalPermissions",
	        method = RequestMethod.PATCH)
	public BindingResponse setGeneralPermissions(
	        @PathVariable(value = "principal") Principal principal,
	        @Valid @RequestBody final Map<GeneralPermissionType, Boolean> generalPermissions,
	        final BindingResult bindingResult) {
		Assertions.resourceExists(principal);

		principal = aclService.setPrincipalGeneralPermissions(principal,
		        generalPermissions);

		return BindingResponse.OK;
	}

	@RequestMapping(
	        value = "/api/admin/principal/{principal}/generalPermissions",
	        method = RequestMethod.GET)
	public Map<GeneralPermissionType, Boolean> getGeneralPermissions(
	        @PathVariable(value = "principal") final Principal principal) {
		Assertions.resourceExists(principal);

		return aclService.getPrincipalGeneralPermissions(principal);
	}
}
