/**
 * The MIT License
 * Copyright (c) 2014 Nick Guletskii
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
package org.ng200.openolympus.controller.api;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.ng200.openolympus.Assertions;
import org.ng200.openolympus.exceptions.ForbiddenException;
import org.ng200.openolympus.model.Verdict;
import org.ng200.openolympus.services.ContestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class VerdictJSONController {

	@Autowired
	private ResourceBundleMessageSource messageSource;

	@Autowired
	private ContestService contestService;

	@RequestMapping("/api/verdict")
	public @ResponseBody Map<String, Object> showVerdict(
			@RequestParam(value = "id") final Verdict verdict,
			final Locale locale) {
		if (this.contestService.getRunningContest() != null
				&& !verdict.isViewableWhenContestRunning()) {
			throw new ForbiddenException(
					"security.contest.cantAccessDuringContest");
		}
		Assertions.resourceExists(verdict);
		final HashMap<String, Object> response = new HashMap<>();
		response.put("id", Long.toString(verdict.getId()));
		response.put("score", verdict.getScore().toString());
		response.put("maximumScore", verdict.getMaximumScore().toString());
		response.put("cpuTime",
				verdict.getCpuTime() >= 0 ? Long.toString(verdict.getCpuTime())
						: "-");
		response.put(
				"realTime",
				verdict.getRealTime() >= 0 ? Long.toString(verdict
						.getRealTime()) : "-");
		response.put("memoryPeak", verdict.getMemoryPeak() >= 0 ? NumberFormat
				.getInstance(locale).format(verdict.getMemoryPeak()) : "-");
		response.put("message", this.messageSource.getMessage(
				verdict.getMessage(),
				new Object[] { verdict.getAdditionalInformation(),
					verdict.getUnauthorisedSyscall() },
					"TRANSLATION ERROR", locale));
		response.put("tested", Boolean.toString(verdict.isTested()));
		response.put("success",
				Boolean.toString(verdict.getScore().signum() > 0));
		return response;
	}
}