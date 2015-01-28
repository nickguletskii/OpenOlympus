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
package org.ng200.openolympus.services;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;
import java.util.Properties;

import org.jppf.node.protocol.AbstractTask;
import org.ng200.openolympus.cerberus.Janitor;
import org.ng200.openolympus.cerberus.SolutionJudge;
import org.ng200.openolympus.cerberus.SolutionResult;
import org.ng200.openolympus.model.Verdict;
import org.ng200.openolympus.util.Pair;

public class VerdictCheckingTask extends
AbstractTask<Pair<SolutionJudge, SolutionResult>> {
	/**
	 *
	 */
	private static final long serialVersionUID = 7829416558485210184L;
	private final Verdict verdict;
	private final SolutionJudge judge;
	private final List<File> testFiles;
	private final BigDecimal maximumScore;
	private final Properties properties;

	public VerdictCheckingTask(final Verdict verdict,
			final SolutionJudge judge, final List<File> testFiles,
			final BigDecimal maximumScore, final Properties properties) {
		this.verdict = verdict;
		this.judge = judge;
		this.testFiles = testFiles;
		this.maximumScore = maximumScore;
		this.properties = properties;
	}

	@Override
	public void run() {
		try {
			if (this.verdict.isUserTest()) {
				this.setThrowable(new UnsupportedOperationException(
						"User tests aren't supported yet"));
				return;
			} else {
				try {
					final SolutionResult result = this.judge.run(
							this.testFiles, true, this.maximumScore,
							this.properties);
					this.setResult(new Pair<SolutionJudge, SolutionResult>(
							this.judge, result));
					this.judge.closeLocal();
				} catch (final Throwable throwable) {
					this.setThrowable(throwable);
					return;
				}
			}
		} finally {
			Janitor.cleanUp(this.judge);
		}
	}

}
