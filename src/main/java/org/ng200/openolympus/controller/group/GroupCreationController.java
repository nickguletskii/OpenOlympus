package org.ng200.openolympus.controller.group;

import javax.validation.Valid;

import org.ng200.openolympus.SecurityExpressionConstants;
import org.ng200.openolympus.controller.BindingResponse;
import org.ng200.openolympus.controller.BindingResponse.Status;
import org.ng200.openolympus.jooq.tables.pojos.Group;
import org.ng200.openolympus.services.GroupService;
import org.ng200.openolympus.validation.GroupDtoValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.ImmutableMap;

@RestController
public class GroupCreationController {
	@Autowired
	private GroupDtoValidator groupDtoValidator;

	@Autowired
	private GroupService groupService;

	@PreAuthorize(SecurityExpressionConstants.IS_SUPERUSER)
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
