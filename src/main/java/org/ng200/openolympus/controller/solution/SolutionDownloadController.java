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
package org.ng200.openolympus.controller.solution;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import org.ng200.openolympus.Assertions;
import org.ng200.openolympus.model.Role;
import org.ng200.openolympus.model.Solution;
import org.ng200.openolympus.services.SolutionService;
import org.ng200.openolympus.services.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

@Controller
@RequestMapping(value = "/solutionDownload")
public class SolutionDownloadController {

	@Autowired
	private SolutionService solutionService;

	@Autowired
	private StorageService storageService;

	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<FileSystemResource> solutionDownload(
			final HttpServletRequest request, final Model model,
			@RequestParam(value = "id") final Solution solution,
			final Principal principal) {
		if (principal == null
				|| (!solution.getUser().getUsername()
						.equals(principal.getName()) && !request
						.isUserInRole(Role.SUPERUSER))) {
			throw new InsufficientAuthenticationException(
					"You attempted to download a solution that doesn't belong to you!");
		}
		Assertions.resourceExists(solution);

		final HttpHeaders headers = new HttpHeaders();
		headers.setContentDispositionFormData("attachment", this.storageService
				.getSolutionFile(solution).getName());
		return new ResponseEntity<FileSystemResource>(new FileSystemResource(
				this.storageService.getSolutionFile(solution)), headers,
				HttpStatus.OK);
	}
}
