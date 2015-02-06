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

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.compress.archivers.ArchiveException;
import org.ng200.openolympus.Assertions;
import org.ng200.openolympus.dto.ContestDto;
import org.ng200.openolympus.model.Contest;
import org.ng200.openolympus.services.ContestService;
import org.ng200.openolympus.validation.ContestDtoValidator;
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

//TODO: Port contest modification to new API
@Controller
@RequestMapping("/contest/{contest}/edit")
public class ContestModificationController {

	public static UriComponentsBuilder getUriBuilder() {
		return MvcUriComponentsBuilder
				.fromController(ContestModificationController.class);
	}

	@Autowired
	private ContestDtoValidator contestDtoValidator;

	@Autowired
	private ContestService contestService;

	@RequestMapping(method = RequestMethod.POST)
	public String editContest(final Model model,
			final HttpServletRequest request,
			@PathVariable("contest") Contest contest,
			@Valid final ContestDto contestDto,
			final BindingResult bindingResult) throws IllegalStateException,
			IOException, ArchiveException {
		Assertions.resourceExists(contest);

		this.contestDtoValidator.validate(contestDto, contest, bindingResult);

		if (bindingResult.hasErrors()) {
			model.addAttribute("submitURL", ContestModificationController
					.getUriBuilder().buildAndExpand(contest.getId()).encode()
					.toUriString());
			return "contest/add";
		}
		contest.setName(contestDto.getName());
		contest.setDuration(contestDto.getDuration() * 60 * 1000);
		contest.setStartTime(contestDto.getStartTime());
		contest = this.contestService.saveContest(contest);
		return null;
	}

	@RequestMapping(method = RequestMethod.GET)
	public String showContestEditingForm(final Model model,
			@PathVariable("contest") final Contest contest,
			final ContestDto contestDto) {
		Assertions.resourceExists(contest);

		contestDto.setName(contest.getName());
		contestDto.setDuration(contest.getDuration() / (60 * 1000));
		contestDto.setStartTime(contest.getStartTime());
		model.addAttribute("submitURL", ContestModificationController
				.getUriBuilder().buildAndExpand(contest.getId()).encode()
				.toUriString());
		return "contest/add";
	}
}
