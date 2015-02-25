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
package org.ng200.openolympus.controller.archive;

import java.util.List;

import org.ng200.openolympus.SecurityExpressionConstants;
import org.ng200.openolympus.dto.UserRanking;
import org.ng200.openolympus.model.views.UnprivilegedView;
import org.ng200.openolympus.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

@RestController
public class ArchiveUserRankListRestController {
	private static final int PAGE_SIZE = 10;
	@Autowired
	private UserService userService;

	@PreAuthorize(SecurityExpressionConstants.IS_ADMIN
			+ SecurityExpressionConstants.OR + '('
			+ SecurityExpressionConstants.IS_USER
			+ SecurityExpressionConstants.AND
			+ SecurityExpressionConstants.NO_CONTEST_CURRENTLY + ')')
	@RequestMapping(value = "/api/archive/rankCount", method = RequestMethod.GET)
	@JsonView(UnprivilegedView.class)
	public Long countUsers() {
		return this.userService.countUsers();
	}

	@PreAuthorize(SecurityExpressionConstants.IS_ADMIN
			+ SecurityExpressionConstants.OR + '('
			+ SecurityExpressionConstants.IS_USER
			+ SecurityExpressionConstants.AND
			+ SecurityExpressionConstants.NO_CONTEST_CURRENTLY + ')')
	@RequestMapping(value = "/api/archive/rank", method = RequestMethod.GET)
	@JsonView(UnprivilegedView.class)
	public List<UserRanking> getUsers(@RequestParam("page") Long page) {
		return this.userService.getArchiveRankPage(page,
				ArchiveUserRankListRestController.PAGE_SIZE);
	}

}
