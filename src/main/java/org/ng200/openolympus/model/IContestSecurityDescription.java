package org.ng200.openolympus.model;

import java.sql.Timestamp;
import java.time.Duration;

import org.ng200.openolympus.SecurityClearanceType;
import org.ng200.openolympus.annotations.SecurityClearanceRequired;
import org.ng200.openolympus.security.UserKnowsAboutContest;

@SecurityClearanceRequired(minimumClearance = SecurityClearanceType.APPROVED_USER, predicates = UserKnowsAboutContest.class)
public interface IContestSecurityDescription {

	public Integer getId();

	public Duration getDuration();

	public String getName();

	public Boolean getShowFullTestsDuringContest();

	public Timestamp getStartTime();

	public Long getOwner();
}
