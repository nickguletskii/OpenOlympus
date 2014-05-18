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

import java.security.Principal;

import org.ng200.openolympus.Assertions;
import org.ng200.openolympus.model.Contest;
import org.ng200.openolympus.repositories.ContestRepository;
import org.ng200.openolympus.services.ContestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ContestListController {

	public static final int PAGE_SIZE = 10;
	@Autowired
	private ContestRepository contestRepository;

	@Autowired
	private ContestService contestService;

	@RequestMapping(value = "/contests", method = RequestMethod.GET)
	public String contestList(
			@RequestParam(value = "page", defaultValue = "1") final Integer pageNumber,
			@RequestParam(value = "message", required = false) final String message,
			final Model model, final Principal principal) {
		if (pageNumber < 1) {
			throw new ResourceNotFoundException();
		}

		final PageRequest request = new PageRequest(pageNumber - 1,
				ContestListController.PAGE_SIZE, Direction.DESC, "startTime");
		model.addAttribute("contests", this.contestRepository.findAll(request));
		model.addAttribute(
				"numberOfPages",
				Math.max((this.contestRepository.count()
						+ ContestListController.PAGE_SIZE - 1)
						/ ContestListController.PAGE_SIZE, 1));
		model.addAttribute("currentPage", pageNumber);
		model.addAttribute("pagePrefix", "/contests?page=");
		model.addAttribute("message", message);

		return "contest/contests";
	}

	@RequestMapping(value = "/contests/remove/{contest}", method = RequestMethod.POST)
	public String removeContest(final Model model,
			@PathVariable(value = "contest") final Contest contest) {
		Assertions.resourceExists(contest);

		this.contestService.delete(contest);
		model.addAttribute("message", "contests.list.removeContest.success");
		return "redirect:/contests";
	}

	@RequestMapping(value = "/contests/remove/{contest}", method = RequestMethod.GET)
	public String removeContestPrompt(final Model model,
			@PathVariable(value = "contest") final Contest contest) {
		Assertions.resourceExists(contest);

		model.addAttribute("contest", contest);
		return "contest/removeContest";
	}
}
