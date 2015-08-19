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

import java.security.Principal;
import java.util.List;

import org.ng200.openolympus.Assertions;

import org.ng200.openolympus.exceptions.ResourceNotFoundException;
import org.ng200.openolympus.jooq.tables.pojos.Contest;
import org.ng200.openolympus.jooq.tables.pojos.User;
import org.ng200.openolympus.model.views.PriviligedView;
import org.ng200.openolympus.services.ContestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

@RestController
public class ContestParticipantsController {

	@Autowired
	private ContestService contestService;
	@RequestMapping(value = "/api/contest/{contest}/participants", method = RequestMethod.GET)
	
	public List<User> showParticipantsPage(
			@PathVariable(value = "contest") final Contest contest,
			@RequestParam(value = "page", defaultValue = "1") final Integer pageNumber,
			final Model model, final Principal principal) {
		Assertions.resourceExists(contest);
		if (pageNumber < 1) {
			throw new ResourceNotFoundException();
		}

		Assertions.resourceExists(contest);

		return this.contestService
				.getPariticipantsPage(contest, pageNumber, 10);
	}
}
