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

import javax.validation.Valid;

import org.ng200.openolympus.SecurityClearanceType;
import org.ng200.openolympus.controller.BindingResponse;
import org.ng200.openolympus.controller.BindingResponse.Status;
import org.ng200.openolympus.jooq.tables.pojos.Group;
import org.ng200.openolympus.security.GroupPermissionRequired;
import org.ng200.openolympus.security.SecurityAnd;
import org.ng200.openolympus.security.SecurityLeaf;
import org.ng200.openolympus.security.SecurityOr;
import org.ng200.openolympus.security.UserHasContestPermission;
import org.ng200.openolympus.services.GroupService;
import org.ng200.openolympus.validation.GroupDtoValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Profile;

import com.google.common.collect.ImmutableMap;

@RestController
@Profile("web")

@SecurityOr({
              @SecurityAnd({
                             @SecurityLeaf(
                                     value = SecurityClearanceType.CREATE_GROUP)
		})
})
public class GroupCreationController {
	@Autowired
	private GroupDtoValidator groupDtoValidator;

	@Autowired
	private GroupService groupService;

	@RequestMapping(method = RequestMethod.POST, value = "/api/groups/create")
	public BindingResponse createContest(@Valid final Group groupDto,
	        final BindingResult bindingResult) throws BindException {

		if (bindingResult.hasErrors()) {
			throw new BindException(bindingResult);
		}

		this.groupDtoValidator.validate(groupDto, null, bindingResult);

		if (bindingResult.hasErrors()) {
			throw new BindException(bindingResult);
		}

		Group group = new Group();

		group.setName(groupDto.getName());

		group = this.groupService.insertGroup(group);
		return new BindingResponse(Status.OK, null,
		        new ImmutableMap.Builder<String, Object>().put("id",
		                group.getId()).build());
	}

}
