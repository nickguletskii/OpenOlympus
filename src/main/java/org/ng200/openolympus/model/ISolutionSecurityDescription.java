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
import java.time.OffsetDateTime;

import org.ng200.openolympus.SecurityClearanceType;
import org.ng200.openolympus.annotations.SecurityClearanceRequired;
import org.ng200.openolympus.jooq.tables.interfaces.ISolution;
import org.ng200.openolympus.security.predicates.SolutionScoreSecurityPredicate;
import org.ng200.openolympus.security.predicates.SolutionSecurityPredicate;

@SecurityClearanceRequired(minimumClearance = SecurityClearanceType.APPROVED_USER, predicates = SolutionSecurityPredicate.class)
public interface ISolutionSecurityDescription extends ISolution {
	@Override
	public String getFile();

	@Override
	public Long getId();

	@Override
	public BigDecimal getMaximumScore();

	@Override
	@SecurityClearanceRequired(minimumClearance = SecurityClearanceType.APPROVED_USER, predicates = SolutionScoreSecurityPredicate.class)
	public BigDecimal getScore();

	@Override
	public Integer getTaskId();

	@Override
	@SecurityClearanceRequired(minimumClearance = SecurityClearanceType.APPROVED_USER, predicates = SolutionScoreSecurityPredicate.class)
	public Boolean getTested();

	@Override
	public OffsetDateTime getTimeAdded();

	@Override
	public Long getUserId();

}
