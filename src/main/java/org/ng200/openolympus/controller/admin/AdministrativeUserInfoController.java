/**
 * The MIT License
 * Copyright (c) 2014 Nick Guletskii
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

import org.ng200.openolympus.Assertions;
import org.ng200.openolympus.controller.user.AbstractUserInfoController;
import org.ng200.openolympus.dto.UserInfoDto;
import org.ng200.openolympus.model.User;
import org.ng200.openolympus.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/admin/user/{user}/personalInfo")
public class AdministrativeUserInfoController extends
		AbstractUserInfoController {
	@Autowired
	private UserRepository userRepository;

	@RequestMapping(method = RequestMethod.POST)
	public String changePersonInfo(Model model,
			@Valid final UserInfoDto userInfoDto,
			final BindingResult bindingResult,
			@PathVariable(value = "user") User user) {
		Assertions.resourceExists(user);
		if (bindingResult.hasErrors()) {
			model.addAttribute("postUrl", "/admin/user/" + user.getId()
					+ "/personalInfo");
			model.addAttribute("changePasswordLink",
					"/admin/user/" + user.getId() + "/changePassword");
			return "user/personalInfo";
		}
		super.copyDtoIntoDatabase(userInfoDto, bindingResult, user);
		return "redirect:/admin/users";
	}

	@RequestMapping(method = RequestMethod.GET)
	public String showUserDetailsForm(Model model,
			final UserInfoDto userInfoDto,
			@PathVariable(value = "user") User user) {
		model.addAttribute("postUrl", "/admin/user/" + user.getId()
				+ "/personalInfo");
		model.addAttribute("changePasswordLink", "/admin/user/" + user.getId()
				+ "/changePassword");
		super.initialiseDTO(userInfoDto, user);
		return "user/personalInfo";
	}
}
