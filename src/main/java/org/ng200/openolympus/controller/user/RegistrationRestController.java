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

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.http.client.ClientProtocolException;
import org.ng200.openolympus.controller.user.RegistrationRestController.RegistrationResponse.Status;
import org.ng200.openolympus.dto.UserDto;
import org.ng200.openolympus.model.User;
import org.ng200.openolympus.services.CaptchaService;
import org.ng200.openolympus.services.UserService;
import org.ng200.openolympus.validation.UserDtoValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonFormat;

@RestController
public class RegistrationRestController {

	public static class RegistrationResponse {
		@JsonFormat(shape = JsonFormat.Shape.STRING)
		public static enum Status {
			OK, RECAPTCHA_ERROR, BINDING_ERROR
		}

		private Status status;
		private List<String> recaptchaErrorCodes;
		private List<FieldError> fieldErrors;
		private List<ObjectError> globalErrors;

		public RegistrationResponse(Status status,
				List<String> recaptchaErrorCodes, List<FieldError> fieldErrors,
				List<ObjectError> globalErrors) {
			this.status = status;
			this.recaptchaErrorCodes = recaptchaErrorCodes;
			this.fieldErrors = fieldErrors;
			this.globalErrors = globalErrors;
		}

		public List<FieldError> getFieldErrors() {
			return this.fieldErrors;
		}

		public List<ObjectError> getGlobalErrors() {
			return this.globalErrors;
		}

		public List<String> getRecaptchaErrorCodes() {
			return this.recaptchaErrorCodes;
		}

		public Status getStatus() {
			return this.status;
		}

		public void setFieldErrors(List<FieldError> fieldErrors) {
			this.fieldErrors = fieldErrors;
		}

		public void setGlobalErrors(List<ObjectError> globalErrors) {
			this.globalErrors = globalErrors;
		}

		public void setRecaptchaErrorCodes(List<String> recaptchaErrorCodes) {
			this.recaptchaErrorCodes = recaptchaErrorCodes;
		}

		public void setStatus(Status status) {
			this.status = status;
		}

	}

	@Value("${remoteAddress}")
	private String remoteAddress;

	@Autowired
	private UserDtoValidator userDtoValidator;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserService userService;

	@Autowired
	private CaptchaService captchaService;

	@ResponseStatus(value = HttpStatus.OK)
	@ExceptionHandler({
		BindException.class
	})
	public RegistrationResponse handleBindException(BindException exception) {
		return new RegistrationResponse(Status.BINDING_ERROR, null, exception
				.getBindingResult().getFieldErrors(), exception
				.getBindingResult().getGlobalErrors());
	}

	@RequestMapping(value = "/api/user/register", method = RequestMethod.POST)
	public RegistrationResponse registerUser(final HttpServletRequest request,
			@RequestBody @Valid final UserDto userDto,
			final BindingResult bindingResult) throws BindException,
			URISyntaxException, ClientProtocolException, IOException {
		final List<String> recaptchaErrorCodes = this.captchaService
				.checkCaptcha(userDto.getRecaptchaResponse());
		if (recaptchaErrorCodes != null && !recaptchaErrorCodes.isEmpty()) {
			return new RegistrationResponse(Status.RECAPTCHA_ERROR,
					recaptchaErrorCodes, null, null);
		}

		this.validate(request, userDto, bindingResult);

		final User user = new User(userDto.getUsername(),
				this.passwordEncoder.encode(userDto.getPassword()),
				userDto.getEmailAddress(), userDto.getFirstNameMain(),
				userDto.getMiddleNameMain(), userDto.getLastNameMain(),
				userDto.getFirstNameLocalised(),
				userDto.getMiddleNameLocalised(),
				userDto.getLastNameLocalised(), userDto.getTeacherFirstName(),
				userDto.getTeacherMiddleName(), userDto.getTeacherLastName(),
				userDto.getAddressLine1(), userDto.getAddressLine2(),
				userDto.getAddressCity(), userDto.getAddressState(),
				userDto.getAddressCountry(), userDto.getLandline(),
				userDto.getMobile(), userDto.getSchool(),
				userDto.getDateOfBirth(), UUID.randomUUID().toString());

		this.userService.saveUser(user);

		return new RegistrationResponse(Status.OK, null, null, null);
	}

	@RequestMapping(value = "/api/user/register/validate", method = RequestMethod.POST)
	private RegistrationResponse validate(final HttpServletRequest request,
			@RequestBody @Valid final UserDto userDto,
			final BindingResult bindingResult) throws BindException {

		this.userDtoValidator.validate(userDto, bindingResult);
		if (bindingResult.hasErrors()) {
			throw new BindException(bindingResult);
		}
		return new RegistrationResponse(Status.OK, null, null, null);
	}

}
