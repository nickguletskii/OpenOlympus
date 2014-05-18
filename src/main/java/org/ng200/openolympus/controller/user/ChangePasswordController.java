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
package org.ng200.openolympus.controller.user;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.ng200.openolympus.dto.PasswordChangeDto;
import org.ng200.openolympus.model.User;
import org.ng200.openolympus.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/user/changePassword")
public class ChangePasswordController {

	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private UserRepository userRepository;

	@RequestMapping(method = RequestMethod.POST)
	public String changePassword(@Valid final PasswordChangeDto userDto,
			final BindingResult bindingResult, final Principal principal) {
		if (bindingResult.hasErrors()) {
			return "user/changePassword";
		}
		final User user = this.userRepository.findByUsername(principal
				.getName());
		if (!this.passwordEncoder.matches(userDto.getExistingPassword(),
				user.getPassword())) {
			bindingResult
			.rejectValue("existingPassword", "",
					"user.changePassword.form.errors.existingPasswordDoesntMatch");
		}
		if (!userDto.getPassword().equals(userDto.getPasswordConfirmation())) {
			bindingResult
			.rejectValue("passwordConfirmation", "",
					"user.changePassword.form.errors.passwordConfirmationDoesntMatch");
		}
		if (bindingResult.hasErrors()) {
			return "user/changePassword";
		}

		user.setPassword(this.passwordEncoder.encode(userDto.getPassword()));
		this.userRepository.save(user);
		return "redirect:/user";
	}

	@InitBinder
	protected void initBinder(final HttpServletRequest request,
			final ServletRequestDataBinder binder) throws Exception {
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
	}

	@RequestMapping(method = RequestMethod.GET)
	public String showPasswordChangeForm(final PasswordChangeDto userDto) {
		return "user/changePassword";
	}
}
