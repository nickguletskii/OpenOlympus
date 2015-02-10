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
package org.ng200.openolympus.controller.user;

import java.security.Principal;
import java.time.LocalDate;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.markdown4j.YumlPlugin;
import org.ng200.openolympus.controller.BindingResponse;
import org.ng200.openolympus.dto.UserInfoDto;
import org.ng200.openolympus.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserInfoController extends AbstractUserInfoController {

	@Autowired
	private UserService userService;

	@RequestMapping(value = "/api/user/personalInfo", method = RequestMethod.PATCH)
	public BindingResponse changePersonInfo(
			@Valid @RequestBody final UserInfoDto userInfoDto,
			final BindingResult bindingResult, final Principal principal)
			throws BindException {
		if (bindingResult.hasErrors()) {
			throw new BindException(bindingResult);
		}
		super.copyDtoIntoDatabase(userInfoDto, bindingResult,
				this.userService.getUserByUsername(principal.getName()));
		return BindingResponse.OK;
	}

	@RequestMapping(value = "/api/user/personalInfo", method = RequestMethod.GET) 
	public UserInfoDto showUserDetailsForm(final Principal principal) {
		final UserInfoDto userInfoDto = new UserInfoDto();
		super.initialiseDTO(userInfoDto,
				this.userService.getUserByUsername(principal.getName()));
		return userInfoDto;
	}
}
