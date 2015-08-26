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
	@SecurityClearanceRequired(minimumClearance = SecurityClearanceType.APPROVED_USER, predicates = VerdictSecurityPredicate.class)
	public String getAdditionalInformation();

	@Override
	@SecurityClearanceRequired(minimumClearance = SecurityClearanceType.APPROVED_USER, predicates = VerdictSecurityPredicate.class)
	public Duration getCpuTime();

	@Override
	@SecurityClearanceRequired(minimumClearance = SecurityClearanceType.APPROVED_USER)
	public Long getId();

	@Override
	@SecurityClearanceRequired(minimumClearance = SecurityClearanceType.APPROVED_USER, predicates = VerdictSecurityPredicate.class)
	public BigDecimal getMaximumScore();

	@Override
	@SecurityClearanceRequired(minimumClearance = SecurityClearanceType.APPROVED_USER, predicates = VerdictSecurityPredicate.class)
	public Long getMemoryPeak();

	@Override
	@SecurityClearanceRequired(minimumClearance = SecurityClearanceType.INTERNAL)
	public String getPath();

	@Override
	@SecurityClearanceRequired(minimumClearance = SecurityClearanceType.APPROVED_USER, predicates = VerdictSecurityPredicate.class)
	public Duration getRealTime();

	@Override
	@SecurityClearanceRequired(minimumClearance = SecurityClearanceType.APPROVED_USER, predicates = VerdictSecurityPredicate.class)
	public BigDecimal getScore();

	@Override
	@SecurityClearanceRequired(minimumClearance = SecurityClearanceType.APPROVED_USER)
	public Long getSolutionId();

	@Override
	@SecurityClearanceRequired(minimumClearance = SecurityClearanceType.APPROVED_USER, predicates = VerdictSecurityPredicate.class)
	public VerdictStatusType getStatus();

	@Override
	@SecurityClearanceRequired(minimumClearance = SecurityClearanceType.APPROVED_USER, predicates = VerdictSecurityPredicate.class)
	public Boolean getViewableDuringContest();

}