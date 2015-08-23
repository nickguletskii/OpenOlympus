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

import org.ng200.openolympus.Assertions;
import org.ng200.openolympus.SecurityClearanceType;
import org.ng200.openolympus.jooq.tables.pojos.User;
import org.ng200.openolympus.security.SecurityAnd;
import org.ng200.openolympus.security.SecurityLeaf;
import org.ng200.openolympus.security.SecurityOr;
import org.ng200.openolympus.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SecurityOr({
              @SecurityAnd({
                             @SecurityLeaf(value = SecurityClearanceType.DELETE_USER)
		})
})
public class DeleteUserController {

	@Autowired
	private UserService userService;

	@RequestMapping(value = "/api/admin/users/deleteUsers", method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.OK)
	@Transactional
	public void deleteUser(@RequestBody List<Long> userIds) {
		final List<User> users = userIds.stream()
		        .map(this.userService::getUserById)
		        .collect(Collectors.toList());
		users.forEach(Assertions::resourceExists);
		users.forEach(this.userService::deleteUser);
	}

	@RequestMapping(value = "/api/admin/users/deleteUser", method = RequestMethod.DELETE)
	@ResponseStatus(value = HttpStatus.OK)
	@Transactional
	public void deleteUser(@RequestParam(value = "user") final User user) {
		Assertions.resourceExists(user);
		this.userService.deleteUser(user);
	}
}
