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
package org.ng200.openolympus.controller.contest;

import java.io.IOException;
import java.util.HashSet;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.compress.archivers.ArchiveException;
import org.ng200.openolympus.dto.ContestDto;
import org.ng200.openolympus.model.Contest;
import org.ng200.openolympus.repositories.ContestRepository;
import org.ng200.openolympus.validation.ContestDtoValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/contests/add")
public class ContestCreationController {

	private static final Logger logger = LoggerFactory
			.getLogger(ContestCreationController.class);

	@Autowired
	private ContestDtoValidator contestDtoValidator;

	@Autowired
	private ContestRepository contestRepository;

	@RequestMapping(method = RequestMethod.POST)
	public String createContest(final Model model,
			final HttpServletRequest request,
			@Valid final ContestDto contestDto,
			final BindingResult bindingResult) throws IllegalStateException,
			IOException, ArchiveException {

		this.contestDtoValidator.validate(contestDto, bindingResult);

		if (bindingResult.hasErrors()) {
			ContestCreationController.logger.info(bindingResult
					.getFieldErrors().toString());
			model.addAttribute("mode", "add");

			return "contest/add";
		}
		Contest contest = new Contest(contestDto.getStartTime(),
				contestDto.getDuration() * 60 * 1000, contestDto.getName(),
				new HashSet<>());
		contest = this.contestRepository.save(contest);
		return "redirect:/contests";
	}

	@InitBinder
	protected void initBinder(final HttpServletRequest request,
			final ServletRequestDataBinder binder) throws Exception {
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
	}

	@RequestMapping(method = RequestMethod.GET)
	public String showContestCreationForm(final Model model,
			final ContestDto contestDto) {
		model.addAttribute("mode", "add");
		return "contest/add";
	}
}
