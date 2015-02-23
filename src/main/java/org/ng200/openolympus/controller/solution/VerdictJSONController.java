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

import static org.ng200.openolympus.SecurityExpressionConstants.AND;
import static org.ng200.openolympus.SecurityExpressionConstants.IS_ADMIN;
import static org.ng200.openolympus.SecurityExpressionConstants.IS_OWNER;
import static org.ng200.openolympus.SecurityExpressionConstants.IS_USER;
import static org.ng200.openolympus.SecurityExpressionConstants.OR;
import static org.ng200.openolympus.SecurityExpressionConstants.SOLUTION_INSIDE_CURRENT_CONTEST_OR_NO_CONTEST;
import static org.ng200.openolympus.SecurityExpressionConstants.USER_IS_OWNER;

import java.math.BigDecimal;
import java.util.Locale;

import org.ng200.openolympus.Assertions;
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
public class VerdictJSONController {

	private static final Logger logger = LoggerFactory
			.getLogger(VerdictJSONController.class);

	public static class VerdictDto {
		private long id;
		private BigDecimal score;
		private BigDecimal maximumScore;
		private long cpuTime;
		private long realTime;
		private long memoryPeak;
		private String message;
		boolean tested;
		boolean success;

		public VerdictDto(long id, BigDecimal score, BigDecimal maximumScore,
				long cpuTime, long realTime, long memoryPeak, String message,
				boolean tested, boolean success) {
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

		public long getCpuTime() {
			return this.cpuTime;
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

		public long getRealTime() {
			return this.realTime;
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

		public void setCpuTime(long cpuTime) {
			this.cpuTime = cpuTime;
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

		public void setRealTime(long realTime) {
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

	@Autowired
	private AbstractMessageSource messageSource;

	@Autowired
	private ContestService contestService;

	@PreAuthorize(IS_ADMIN + OR + '(' + IS_USER + AND + USER_IS_OWNER + AND
			+ SOLUTION_INSIDE_CURRENT_CONTEST_OR_NO_CONTEST + ')')
	@RequestMapping(value = "/api/verdict", method = RequestMethod.GET)
	public @ResponseBody VerdictDto showVerdict(
			@RequestParam(value = "id") final Verdict verdict,
			final Locale locale) {
		if (this.contestService.getRunningContest() != null
				&& !verdict.isViewableWhenContestRunning()) {
			throw new ForbiddenException(
					"security.contest.cantAccessDuringContest");
		}
		Assertions.resourceExists(verdict);

		return new VerdictDto(verdict.getId(), verdict.getScore(),
				verdict.getMaximumScore(), verdict.getCpuTime(),
				verdict.getRealTime(), verdict.getMemoryPeak(), verdict
						.getStatus().toString(), verdict.isTested(), verdict
						.getScore().signum() > 0);

	}
}