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

import javax.validation.Valid;

import org.ng200.openolympus.dto.UserInfoDto;
import org.ng200.openolympus.jooq.tables.pojos.User;
import org.ng200.openolympus.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;

public class AbstractUserInfoController {

	@Autowired
	private UserService userService;

	public void copyDtoIntoDatabase(@Valid final UserInfoDto userInfoDto,
			final BindingResult bindingResult, final User user) {
		user.setEmailAddress(userInfoDto.getEmailAddress());
		user.setFirstNameMain(userInfoDto.getFirstNameMain());
		user.setMiddleNameMain(userInfoDto.getMiddleNameMain());
		user.setLastNameMain(userInfoDto.getLastNameMain());
		user.setFirstNameLocalised(userInfoDto.getFirstNameLocalised());
		user.setMiddleNameLocalised(userInfoDto.getMiddleNameLocalised());
		user.setLastNameLocalised(userInfoDto.getLastNameLocalised());
		user.setTeacherFirstName(userInfoDto.getTeacherFirstName());
		user.setTeacherMiddleName(userInfoDto.getTeacherMiddleName());
		user.setTeacherLastName(userInfoDto.getTeacherLastName());
		user.setAddressLine1(userInfoDto.getAddressLine1());
		user.setAddressLine2(userInfoDto.getAddressLine2());
		user.setAddressCity(userInfoDto.getAddressCity());
		user.setAddressState(userInfoDto.getAddressState());
		user.setAddressCountry(userInfoDto.getAddressCountry());
		user.setLandline(userInfoDto.getLandline());
		user.setMobile(userInfoDto.getMobile());
		user.setSchool(userInfoDto.getSchool());
		user.setBirthDate(userInfoDto.getDateOfBirth());
		this.userService.updateUser(user);
	}

	public void initialiseDTO(final UserInfoDto userInfoDto, final User user) {
		userInfoDto.setEmailAddress(user.getEmailAddress());
		userInfoDto.setFirstNameMain(user.getFirstNameMain());
		userInfoDto.setMiddleNameMain(user.getMiddleNameMain());
		userInfoDto.setLastNameMain(user.getLastNameMain());
		userInfoDto.setFirstNameLocalised(user.getFirstNameLocalised());
		userInfoDto.setMiddleNameLocalised(user.getMiddleNameLocalised());
		userInfoDto.setLastNameLocalised(user.getLastNameLocalised());
		userInfoDto.setTeacherFirstName(user.getTeacherFirstName());
		userInfoDto.setTeacherMiddleName(user.getTeacherMiddleName());
		userInfoDto.setTeacherLastName(user.getTeacherLastName());
		userInfoDto.setAddressLine1(user.getAddressLine1());
		userInfoDto.setAddressLine2(user.getAddressLine2());
		userInfoDto.setAddressCity(user.getAddressCity());
		userInfoDto.setAddressState(user.getAddressState());
		userInfoDto.setAddressCountry(user.getAddressCountry());
		userInfoDto.setLandline(user.getLandline());
		userInfoDto.setMobile(user.getMobile());
		userInfoDto.setSchool(user.getSchool());
		userInfoDto.setDateOfBirth(user.getBirthDate());
	}

}
