package org.ng200.openolympus.controller.group;

import javax.validation.Valid;

import org.ng200.openolympus.Assertions;
import org.ng200.openolympus.SecurityExpressionConstants;
import org.ng200.openolympus.controller.BindingResponse;
import org.ng200.openolympus.jooq.tables.pojos.Group;
import org.ng200.openolympus.services.GroupService;
import org.ng200.openolympus.validation.GroupDtoValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/group/{group}/edit")
public class GroupModificationController {

	@Autowired
	private GroupDtoValidator groupDtoValidator;

	@Autowired
	private GroupService groupService;
	@RequestMapping(method = RequestMethod.POST)
	public BindingResponse editGroup(
			@PathVariable("group") Group group,
			@Valid final Group groupDto,
			final BindingResult bindingResult) throws BindException {
		Assertions.resourceExists(group);

		this.groupDtoValidator.validate(groupDto, group, bindingResult);

		if (bindingResult.hasErrors()) {
			throw new BindException(bindingResult);
		}
		group.setName(groupDto.getName());
		group = this.groupService.updateGroup(group);
		return BindingResponse.OK;
	}
	@RequestMapping(method = RequestMethod.GET)
	public Group showGroupEditingForm(
			@PathVariable("group") final Group group) {
		return group;
	}
}