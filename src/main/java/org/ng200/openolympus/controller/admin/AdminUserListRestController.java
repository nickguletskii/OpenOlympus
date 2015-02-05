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
package org.ng200.openolympus.controller.admin;

import java.util.List;
import java.util.stream.Collectors;

import org.ng200.openolympus.model.User;
import org.ng200.openolympus.model.views.PriviligedView;
import org.ng200.openolympus.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

@RestController
public class AdminUserListRestController {
	private static final int PAGE_SIZE = 10;
	@Autowired
	private UserService userService;

	@RequestMapping(value = "/api/admin/users/delete", method = RequestMethod.DELETE)
	public String deleteUsers(@RequestParam("users") List<Long> userIds) {
		final List<User> users = userIds.stream()
				.map(id -> this.userService.getUserById(id))
				.collect(Collectors.toList());
		this.userService.deleteUsers(users);
		return "ok";
	}

	@RequestMapping(value = "/api/admin/users", method = RequestMethod.GET)
	@JsonView(PriviligedView.class)
	public List<User> getUsers(@RequestParam("page") Integer page) {
		return this.userService.getUsersAlphabetically(page,
				AdminUserListRestController.PAGE_SIZE);
	}

}
