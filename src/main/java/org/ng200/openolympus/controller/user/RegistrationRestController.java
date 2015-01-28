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

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import net.tanesha.recaptcha.ReCaptchaImpl;
import net.tanesha.recaptcha.ReCaptchaResponse;

import org.ng200.openolympus.controller.user.RegistrationRestController.RegistrationResponse.Status;
import org.ng200.openolympus.dto.UserDto;
import org.ng200.openolympus.exceptions.InvalidRecaptchaException;
import org.ng200.openolympus.model.User;
import org.ng200.openolympus.services.UserService;
import org.ng200.openolympus.validation.UserDtoValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
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
		private String recaptchaError;
		private List<FieldError> fieldErrors;
		private List<ObjectError> globalErrors;

		public RegistrationResponse(Status status, String recaptchaError,
				List<FieldError> fieldErrors, List<ObjectError> globalErrors) {
			this.status = status;
			this.recaptchaError = recaptchaError;
			this.fieldErrors = fieldErrors;
			this.globalErrors = globalErrors;
		}

		public List<FieldError> getFieldErrors() {
			return this.fieldErrors;
		}

		public List<ObjectError> getGlobalErrors() {
			return this.globalErrors;
		}

		public String getRecaptchaError() {
			return this.recaptchaError;
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

		public void setRecaptchaError(String recaptchaError) {
			this.recaptchaError = recaptchaError;
		}

		public void setStatus(Status status) {
			this.status = status;
		}

	}

	@Value("${remoteAddress}")
	private String remoteAddress;

	private static CustomDateEditor dateEditor = new CustomDateEditor(
			new SimpleDateFormat("YYYY-MM-DD"), true);

	@Autowired
	private UserDtoValidator userDtoValidator;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserService userService;

	private final ReCaptchaImpl reCaptcha;

	@Value("${enableCaptcha}")
	private boolean captchaEnabled;

	@Value("${recaptchaPublicKey}")
	private String recaptchaPublicKey;

	public RegistrationRestController() {
		this.reCaptcha = new ReCaptchaImpl();
	}

	// TODO: fix captcha support
	private void applyCaptcha(Model model) {
		if (this.captchaEnabled) {
			model.addAttribute("captchaEnabled", true);
			model.addAttribute("recaptchaPublicKey", this.recaptchaPublicKey);
		}
	}

	@ResponseStatus(value = HttpStatus.OK)
	@ExceptionHandler({
		BindException.class
	})
	public RegistrationResponse handleBindException(BindException exception) {
		return new RegistrationResponse(Status.BINDING_ERROR, null, exception
				.getBindingResult().getFieldErrors(), exception
				.getBindingResult().getGlobalErrors());
	}

	@ResponseStatus(value = HttpStatus.OK)
	@ExceptionHandler({
		InvalidRecaptchaException.class
	})
	public RegistrationResponse handleRecaptchaException(
			InvalidRecaptchaException exception) {
		return new RegistrationResponse(Status.RECAPTCHA_ERROR,
				exception.getMessage(), null, null);
	}

	@InitBinder
	protected void initBinder(final HttpServletRequest request,
			final ServletRequestDataBinder binder) throws Exception {
		binder.registerCustomEditor(LocalDate.class,
				RegistrationRestController.dateEditor);
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));

	}

	@RequestMapping(value = "/api/user/register", method = RequestMethod.POST)
	public RegistrationResponse registerUser(final HttpServletRequest request,
			@RequestBody @Valid final UserDto userDto,
			final BindingResult bindingResult) throws BindException,
			InvalidRecaptchaException {
		final String reCaptchaChallenge = request
				.getParameter("recaptcha_challenge_field");
		final String reCaptchaResponse = request
				.getParameter("recaptcha_response_field");
		if (this.captchaEnabled && !StringUtils.isEmpty(reCaptchaChallenge)) {
			if (!StringUtils.isEmpty(reCaptchaResponse)) {
				final ReCaptchaResponse reCaptchaCheck = this.reCaptcha
						.checkAnswer(this.remoteAddress, reCaptchaChallenge,
								reCaptchaResponse);

				if (!reCaptchaCheck.isValid()) {
					throw new InvalidRecaptchaException("invalid");
				}
			} else {
				throw new InvalidRecaptchaException("empty");
			}
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

	@Value("${recaptchaPrivateKey}")
	public void setRecaptchaPrivateKey(String key) {
		if (this.captchaEnabled) {
			this.reCaptcha.setPrivateKey(key);
		}
	}

	@RequestMapping(value = "/api/user/register/validate", method = RequestMethod.POST)
	private RegistrationResponse validate(final HttpServletRequest request,
			@RequestBody @Valid final UserDto userDto,
			final BindingResult bindingResult)
					throws InvalidRecaptchaException, BindException {

		this.userDtoValidator.validate(userDto, bindingResult);
		if (bindingResult.hasErrors()) {
			throw new BindException(bindingResult);
		}
		return new RegistrationResponse(Status.OK, null, null, null);
	}

}
