package org.ng200.openolympus.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

import org.ng200.openolympus.SecurityClearanceType;
import org.ng200.openolympus.annotations.SecurityClearanceRequired;
import org.ng200.openolympus.jooq.tables.interfaces.ISolution;
import org.ng200.openolympus.security.SolutionScoreSecurityPredicate;
import org.ng200.openolympus.security.SolutionSecurityPredicate;

@SecurityClearanceRequired(minimumClearance = SecurityClearanceType.APPROVED_USER, predicates = SolutionSecurityPredicate.class)
public interface ISolutionSecurityDescription extends ISolution {
	@Override
	public Long getId();

	@Override
	public String getFile();

	@Override
	public BigDecimal getMaximumScore();

	@Override
	@SecurityClearanceRequired(minimumClearance = SecurityClearanceType.APPROVED_USER, predicates = SolutionScoreSecurityPredicate.class)
	public BigDecimal getScore();

	@Override
	@SecurityClearanceRequired(minimumClearance = SecurityClearanceType.APPROVED_USER, predicates = SolutionScoreSecurityPredicate.class)
	public Boolean getTested();

	@Override
	public Timestamp getTimeAdded();

	@Override
	public Long getUserId();

	@Override
	public Integer getTaskId();

}
