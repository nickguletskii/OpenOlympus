package org.ng200.openolympus.model;

import java.math.BigDecimal;
import java.time.Duration;

import org.ng200.openolympus.SecurityClearanceType;
import org.ng200.openolympus.annotations.SecurityClearanceRequired;
import org.ng200.openolympus.jooq.enums.VerdictStatusType;
import org.ng200.openolympus.jooq.tables.interfaces.IVerdict;
import org.ng200.openolympus.security.VerdictSecurityPredicate;

public interface IVerdictSecurityDescription extends IVerdict {

	@Override
	@SecurityClearanceRequired(minimumClearance = SecurityClearanceType.APPROVED_USER)
	public Long getId();

	@Override
	@SecurityClearanceRequired(minimumClearance = SecurityClearanceType.APPROVED_USER, predicates = VerdictSecurityPredicate.class)
	public BigDecimal getScore();

	@Override
	@SecurityClearanceRequired(minimumClearance = SecurityClearanceType.APPROVED_USER, predicates = VerdictSecurityPredicate.class)
	public BigDecimal getMaximumScore();

	@Override
	@SecurityClearanceRequired(minimumClearance = SecurityClearanceType.APPROVED_USER, predicates = VerdictSecurityPredicate.class)
	public VerdictStatusType getStatus();

	@Override
	@SecurityClearanceRequired(minimumClearance = SecurityClearanceType.APPROVED_USER, predicates = VerdictSecurityPredicate.class)
	public Boolean getViewableDuringContest();

	@Override
	@SecurityClearanceRequired(minimumClearance = SecurityClearanceType.INTERNAL)
	public String getPath();

	@Override
	@SecurityClearanceRequired(minimumClearance = SecurityClearanceType.APPROVED_USER, predicates = VerdictSecurityPredicate.class)
	public Duration getCpuTime();

	@Override
	@SecurityClearanceRequired(minimumClearance = SecurityClearanceType.APPROVED_USER, predicates = VerdictSecurityPredicate.class)
	public Duration getRealTime();

	@Override
	@SecurityClearanceRequired(minimumClearance = SecurityClearanceType.APPROVED_USER, predicates = VerdictSecurityPredicate.class)
	public Long getMemoryPeak();

	@Override
	@SecurityClearanceRequired(minimumClearance = SecurityClearanceType.APPROVED_USER, predicates = VerdictSecurityPredicate.class)
	public String getAdditionalInformation();

	@Override
	@SecurityClearanceRequired(minimumClearance = SecurityClearanceType.APPROVED_USER)
	public Long getSolutionId();

}
