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

import javax.validation.Valid;

import org.ng200.openolympus.SecurityExpressionConstants;
import org.ng200.openolympus.controller.BindingResponse;
import org.ng200.openolympus.dto.PasswordChangeDto;
import org.ng200.openolympus.jooq.tables.pojos.User;
import org.ng200.openolympus.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdministrativeChangePasswordController {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserService userService;

	@PreAuthorize(SecurityExpressionConstants.IS_SUPERUSER)
	@RequestMapping(value = "/api/admin/user/{user}/changePassword", method = RequestMethod.PATCH)
	public BindingResponse changePassword(
			@Valid @RequestBody final PasswordChangeDto passwordChangeDto,
			final BindingResult bindingResult,
			@PathVariable(value = "user") final User user) throws BindException {

		if (bindingResult.hasErrors()) {
			throw new BindException(bindingResult);
		}

		user.setPassword(this.passwordEncoder.encode(passwordChangeDto
				.getPassword()));
		this.userService.updateUser(user);
		return BindingResponse.OK;
	}

}
