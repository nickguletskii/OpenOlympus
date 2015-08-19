package org.ng200.openolympus.controller.group;

import java.security.Principal;
import java.util.List;

import org.ng200.openolympus.Assertions;

import org.ng200.openolympus.exceptions.ResourceNotFoundException;
import org.ng200.openolympus.jooq.tables.pojos.Group;
import org.ng200.openolympus.jooq.tables.pojos.User;
import org.ng200.openolympus.services.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GroupViewController {
	@Autowired
	private GroupService groupService;
	@RequestMapping(method = RequestMethod.GET, value = "/api/group/{group}/memberCount")
	@ResponseBody
	public long groupCount(
			@PathVariable(value = "group") final Group group) {
		return this.groupService.countParticipants(group);
	}
	@RequestMapping(value = "/api/group/{group}", method = RequestMethod.GET)
	public List<User> getMembers(
			@PathVariable(value = "group") final Group group,
			@RequestParam(value = "page", defaultValue = "1") final Integer pageNumber,
			final Principal principal) {
		Assertions.resourceExists(group);
		if (pageNumber < 1) {
			throw new ResourceNotFoundException();
		}

		return this.groupService
				.getParticipants(group, pageNumber, 10);
	}
}
