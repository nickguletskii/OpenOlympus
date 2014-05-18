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

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import org.ng200.openolympus.Pair;
import org.ng200.openolympus.model.Solution;
import org.ng200.openolympus.model.Verdict;
import org.ng200.openolympus.services.SolutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

@Controller
public class SolutionJSONController {

	@Autowired
	private SolutionService solutionService;

	@Autowired
	private VerdictJSONController verdictJSONController;
	private final Cache<Pair<Locale, Solution>, List<Map<String, Object>>> cache = CacheBuilder
			.newBuilder().maximumSize(1000).build();

	@RequestMapping("/api/solution")
	public @ResponseBody List<Map<String, Object>> solutionApi(
			@RequestParam(value = "id") final Solution solution,
			final Locale locale) {
		List<Map<String, Object>> cached = null;
		if ((cached = this.cache.getIfPresent(new Pair<>(locale, solution))) != null) {
			return cached;
		}
		final List<Verdict> verdicts = this.solutionService
				.getVerdicts(solution);
		final List<Map<String, Object>> result = verdicts
				.stream()
				.sorted((l, r) -> Long.compare(l.getId(), r.getId()))
				.map(verdict -> this.verdictJSONController.showVerdict(verdict,
						locale)).collect(Collectors.toList());
		if (verdicts.stream().anyMatch(verdict -> !verdict.isTested())) {
			return result;
		}
		this.cache.put(new Pair<>(locale, solution), result);
		return result;
	}
}