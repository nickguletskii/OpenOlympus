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
package org.ng200.openolympus.controller.group;

import java.util.List;

import org.ng200.openolympus.Assertions;
import org.ng200.openolympus.jooq.tables.pojos.Group;
import org.ng200.openolympus.services.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GroupGeneralInformationController {

	@Autowired
	private GroupService groupService;

	@RequestMapping(method = RequestMethod.GET, value = "/api/group")
	public @ResponseBody Group getGroup(
			@RequestParam(value = "id", required = false) final Long id,
			@RequestParam(value = "name", required = false) final String name)
					throws MissingServletRequestParameterException {
		if (name != null) {
			return this.groupService.getGroupByName(name);
		}
		if (id == null) {
			throw new MissingServletRequestParameterException("id", "long");
		}
		return this.groupService.getGroupById(id);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/api/groupCompletion")
	public @ResponseBody List<Group> searchGroups(
			@RequestParam(value = "term", defaultValue = "") final String name) {
		Assertions.resourceExists(name);

		return this.groupService.findAFewGroupsWithNameContaining(name);
	}
}
