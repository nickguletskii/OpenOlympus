package org.ng200.openolympus.model;

import java.sql.Timestamp;
import java.time.Duration;

import org.ng200.openolympus.SecurityClearanceType;
import org.ng200.openolympus.annotations.SecurityClearanceRequired;
import org.ng200.openolympus.jooq.tables.interfaces.IContest;
import org.ng200.openolympus.security.UserKnowsAboutContest;

@SecurityClearanceRequired(minimumClearance = SecurityClearanceType.APPROVED_USER, predicates = UserKnowsAboutContest.class)
public interface IContestSecurityDescription extends IContest {

	@Override
	public Integer getId();

	@Override
	public Duration getDuration();

	@Override
	public String getName();

	@Override
	public Boolean getShowFullTestsDuringContest();

	@Override
	public Timestamp getStartTime();

}
