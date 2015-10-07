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
package org.ng200.openolympus.controller.admin;

import java.util.Map;

import javax.validation.Valid;

import org.ng200.openolympus.Assertions;
import org.ng200.openolympus.SecurityClearanceType;
import org.ng200.openolympus.controller.BindingResponse;
import org.ng200.openolympus.jooq.enums.GeneralPermissionType;
import org.ng200.openolympus.jooq.tables.pojos.Principal;
import org.ng200.openolympus.security.annotations.SecurityAnd;
import org.ng200.openolympus.security.annotations.SecurityLeaf;
import org.ng200.openolympus.security.annotations.SecurityOr;
import org.ng200.openolympus.services.AclService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SecurityOr({
				@SecurityAnd({
								@SecurityLeaf(value = SecurityClearanceType.MANAGE_PRINCIPAL_PERMISSIONS)
		})
})
public class AdministrativePrincipalACLController {

	@Autowired
	private AclService aclService;

	@RequestMapping(value = "/api/admin/principal/{principal}/generalPermissions", method = RequestMethod.PUT)
	public BindingResponse setGeneralPermissions(
			@PathVariable(value = "principal") Principal principal,
			@Valid @RequestBody final Map<GeneralPermissionType, Boolean> generalPermissions,
			final BindingResult bindingResult) {
		Assertions.resourceExists(principal);

		principal = aclService.setPrincipalGeneralPermissions(principal,
				generalPermissions);

		return BindingResponse.OK;
	}

	@RequestMapping(value = "/api/admin/principal/{principal}/generalPermissions", method = RequestMethod.GET)
	public Map<GeneralPermissionType, Boolean> getGeneralPermissions(
			@PathVariable(value = "principal") final Principal principal) {
		Assertions.resourceExists(principal);

		return aclService.getPrincipalGeneralPermissions(principal);
	}
}
