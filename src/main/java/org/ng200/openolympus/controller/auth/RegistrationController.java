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
package org.ng200.openolympus.controller.auth;

import java.text.SimpleDateFormat;
import java.time.LocalDate;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.ng200.openolympus.dto.UserDto;
import org.ng200.openolympus.model.User;
import org.ng200.openolympus.repositories.UserRepository;
import org.ng200.openolympus.validation.UserDtoValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/register")
public class RegistrationController {

	private static final Logger logger = LoggerFactory
			.getLogger(RegistrationController.class);

	private static CustomDateEditor dateEditor = new CustomDateEditor(
			new SimpleDateFormat("YYYY-MM-DD"), true);
	@Autowired
	private UserDtoValidator userDtoValidator;

	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private UserRepository userRepository;

	@InitBinder
	protected void initBinder(final HttpServletRequest request,
			final ServletRequestDataBinder binder) throws Exception {
		binder.registerCustomEditor(LocalDate.class,
				RegistrationController.dateEditor);
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
	}

	@RequestMapping(method = RequestMethod.POST)
	public String registerUser(@Valid final UserDto userDto,
			final BindingResult bindingResult) {
		this.userDtoValidator.validate(userDto, bindingResult);
		if (bindingResult.hasErrors()) {
			RegistrationController.logger.info(bindingResult.getFieldErrors()
					.toString());
			return "register";
		}

		if (this.userRepository.findByUsername(userDto.getUsername()) != null) {
			throw new IllegalStateException(
					"Duplicate user should have been caught by validator!");
		}

		final User user = new User(userDto.getUsername(),
				this.passwordEncoder.encode(userDto.getPassword()),
				userDto.getFirstNameMain(), userDto.getMiddleNameMain(),
				userDto.getLastNameMain(), userDto.getFirstNameLocalised(),
				userDto.getMiddleNameLocalised(),
				userDto.getLastNameLocalised(), userDto.getTeacherFirstName(),
				userDto.getTeacherMiddleName(), userDto.getTeacherLastName(),
				userDto.getAddressLine1(), userDto.getAddressLine2(),
				userDto.getAddressCity(), userDto.getAddressState(),
				userDto.getAddressCountry(), userDto.getLandline(),
				userDto.getMobile(), userDto.getSchool(),
				userDto.getDateOfBirth());

		this.userRepository.save(user);

		return "redirect:/login";
	}

	@RequestMapping(method = RequestMethod.GET)
	public String showRegistrationForm(final UserDto userDto) {
		return "register";
	}
}
