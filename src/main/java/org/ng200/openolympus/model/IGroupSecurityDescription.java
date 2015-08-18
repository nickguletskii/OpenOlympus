package org.ng200.openolympus.model;

import org.ng200.openolympus.SecurityClearanceType;
import org.ng200.openolympus.annotations.SecurityClearanceRequired;
import org.ng200.openolympus.jooq.tables.interfaces.IGroup;
import org.ng200.openolympus.security.GroupHiddenPredicate;

@SecurityClearanceRequired(minimumClearance = SecurityClearanceType.ANONYMOUS, predicates = GroupHiddenPredicate.class)
public interface IGroupSecurityDescription extends IGroup {

	@Override
	public Long getId();

	@Override
	public String getName();

}
