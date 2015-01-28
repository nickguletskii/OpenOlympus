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
package org.ng200.openolympus.controller.contest;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.ng200.openolympus.Assertions;
import org.ng200.openolympus.dto.ContestUserTimeAdditionDto;
import org.ng200.openolympus.model.Contest;
import org.ng200.openolympus.services.ContestService;
import org.ng200.openolympus.services.UserService;
import org.ng200.openolympus.validation.ContestUserTimeAdditionDtoValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

//TODO: Port contest user time addition to new API
@Controller
@RequestMapping(value = "/contest/{contest}/addUserTime")
public class ContestUserTimeAdditionController {

	public static UriComponentsBuilder getUriBuilder() {
		return MvcUriComponentsBuilder
				.fromController(ContestUserTimeAdditionController.class);
	}

	@Autowired
	private ContestUserTimeAdditionDtoValidator contestUserTimeAdditionDtoValidator;

	@Autowired
	private ContestService contestService;

	@Autowired
	private UserService userService;

	@RequestMapping(method = RequestMethod.POST)
	public String addUserTime(final Model model,
			@PathVariable(value = "contest") final Contest contest,
			@Valid final ContestUserTimeAdditionDto contestUserTimeAdditionDto,
			final BindingResult bindingResult) {
		Assertions.resourceExists(contest);

		this.contestUserTimeAdditionDtoValidator.validate(contest,
				contestUserTimeAdditionDto, bindingResult);
		if (bindingResult.hasErrors()) {
			return "contest/addUserTime";
		}
		this.contestService.extendTimeForUser(contest, this.userService
				.getUserByUsername(contestUserTimeAdditionDto.getUsername()),
				contestUserTimeAdditionDto.getDuration() * 60 * 1000);
		return null;
	}

	@InitBinder
	protected void initBinder(final HttpServletRequest request,
			final ServletRequestDataBinder binder) throws Exception {
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
	}

	@RequestMapping(method = RequestMethod.GET)
	public String showUserTimeAdditionPage(
			@PathVariable(value = "contest") final Contest contest,
			final ContestUserTimeAdditionDto contestUserTimeAdditionDto) {
		Assertions.resourceExists(contest);

		return "contest/addUserTime";
	}
}
