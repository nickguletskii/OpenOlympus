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
package org.ng200.openolympus.controller.task;

import java.security.Principal;

import org.ng200.openolympus.controller.ContestRestrictedController;
import org.ng200.openolympus.repositories.TaskRepository;
import org.ng200.openolympus.services.ContestSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ArchiveTaskListController extends ContestRestrictedController {
	public static final int PAGE_SIZE = 10;
	@Autowired
	private TaskRepository taskRepository;

	@Autowired
	private ContestSecurityService contestSecurityService;

	@RequestMapping(value = "/archive", method = RequestMethod.GET)
	public String listTasks(
			@RequestParam(value = "page", defaultValue = "1") final Integer pageNumber,
			final Model model, final Principal principal) {
		if (pageNumber < 1) {
			throw new ResourceNotFoundException();
		}

		this.assertSuperuserOrNoRestrictionMode(principal);

		final PageRequest request = new PageRequest(pageNumber - 1,
				ArchiveTaskListController.PAGE_SIZE, Sort.Direction.DESC,
				"timeAdded");
		model.addAttribute("tasks", this.taskRepository.findAll(request));
		model.addAttribute(
				"numberOfPages",
				Math.max((this.taskRepository.count()
						+ ArchiveTaskListController.PAGE_SIZE - 1)
						/ ArchiveTaskListController.PAGE_SIZE, 1));
		model.addAttribute("currentPage", pageNumber);
		model.addAttribute("pagePrefix", "/archive?page=");
		return "archive/archive";
	}
}
