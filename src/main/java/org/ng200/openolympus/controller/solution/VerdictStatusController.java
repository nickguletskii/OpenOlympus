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
package org.ng200.openolympus.controller.solution;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Locale;

import org.ng200.openolympus.Assertions;
import org.ng200.openolympus.SecurityExpressionConstants;
import org.ng200.openolympus.exceptions.ForbiddenException;
import org.ng200.openolympus.model.Verdict;
import org.ng200.openolympus.services.ContestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.AbstractMessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VerdictStatusController {

	public static class VerdictDto {
		private long id;
		private BigDecimal score;
		private BigDecimal maximumScore;
		private Duration cpuTime;
		private Duration realTime;
		private long memoryPeak;
		private String message;
		boolean tested;
		boolean success;

		public VerdictDto(long id, BigDecimal score, BigDecimal maximumScore,
				Duration cpuTime, Duration realTime, long memoryPeak,
				String message, boolean tested, boolean success) {
			super();
			this.id = id;
			this.score = score;
			this.maximumScore = maximumScore;
			this.cpuTime = cpuTime;
			this.realTime = realTime;
			this.memoryPeak = memoryPeak;
			this.message = message;
			this.tested = tested;
			this.success = success;
		}

		public long getId() {
			return this.id;
		}

		public BigDecimal getMaximumScore() {
			return this.maximumScore;
		}

		public long getMemoryPeak() {
			return this.memoryPeak;
		}

		public String getMessage() {
			return this.message;
		}

		public BigDecimal getScore() {
			return this.score;
		}

		public boolean isSuccess() {
			return this.success;
		}

		public boolean isTested() {
			return this.tested;
		}

		public void setId(long id) {
			this.id = id;
		}

		public void setMaximumScore(BigDecimal maximumScore) {
			this.maximumScore = maximumScore;
		}

		public void setMemoryPeak(long memoryPeak) {
			this.memoryPeak = memoryPeak;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public Duration getCpuTime() {
			return cpuTime;
		}

		public void setCpuTime(Duration cpuTime) {
			this.cpuTime = cpuTime;
		}

		public Duration getRealTime() {
			return realTime;
		}

		public void setRealTime(Duration realTime) {
			this.realTime = realTime;
		}

		public void setScore(BigDecimal score) {
			this.score = score;
		}

		public void setSuccess(boolean success) {
			this.success = success;
		}

		public void setTested(boolean tested) {
			this.tested = tested;
		}

	}

	private static final Logger logger = LoggerFactory
			.getLogger(VerdictStatusController.class);

	@Autowired
	private AbstractMessageSource messageSource;

	@Autowired
	private ContestService contestService;

	@PreAuthorize(SecurityExpressionConstants.IS_ADMIN
			+ SecurityExpressionConstants.OR + '('
			+ SecurityExpressionConstants.IS_USER
			+ SecurityExpressionConstants.AND + '(' + "#verdict.solution.user"
			+ SecurityExpressionConstants.IS_OWNER + ')'
			+ SecurityExpressionConstants.AND
			+ " @oolsec.isSolutionInCurrentContest(#verdict.solution) and @oolsec.canViewVerdictDuringContest(#verdict) " + ')')
	@RequestMapping(value = "/api/verdict", method = RequestMethod.GET)
	public @ResponseBody VerdictDto showVerdict(
			@RequestParam(value = "id") final Verdict verdict,
			final Locale locale) {
		Assertions.resourceExists(verdict);

		return new VerdictDto(verdict.getId(), verdict.getScore(),
				verdict.getMaximumScore(), verdict.getCpuTime(),
				verdict.getRealTime(), verdict.getMemoryPeak(), verdict
						.getStatus().toString(), verdict.isTested(), verdict
						.getScore().signum() > 0);

	}
}