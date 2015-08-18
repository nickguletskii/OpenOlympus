package org.ng200.openolympus.validation;

import org.ng200.openolympus.jooq.tables.pojos.Group;
import org.ng200.openolympus.services.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

@Component
public class GroupDtoValidator {

	@Autowired
	private GroupService groupService;

	public void validate(Group groupDto, Group group, BindingResult errors) {
		if (errors.hasErrors()) {
			return;
		}

		if ((group == null || !group.getName().equals(groupDto.getName()))
				&& this.groupService
						.getGroupByName(groupDto.getName()) != null) {
			errors.rejectValue("name", "",
					"group.add.form.errors.groupAlreadyExists");
		}
	}

}
