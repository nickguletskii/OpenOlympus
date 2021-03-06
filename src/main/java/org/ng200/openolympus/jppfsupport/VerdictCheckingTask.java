/**
 * The MIT License
 * Copyright (c) 2014-2016 Nick Guletskii
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
package org.ng200.openolympus.jppfsupport;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.List;
import java.util.Properties;

import org.ng200.openolympus.cerberus.Janitor;
import org.ng200.openolympus.cerberus.SolutionJudge;
import org.ng200.openolympus.cerberus.SolutionResult;
import org.ng200.openolympus.cerberus.util.ExceptionalProducer;
import org.ng200.openolympus.util.Pair;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties({
						"taskObject"
})
public class VerdictCheckingTask implements
		ExceptionalProducer<Pair<SolutionJudge, SolutionResult>> {
	private boolean isUserTest;
	private SolutionJudge judge;
	private List<Path> testFiles;
	private BigDecimal maximumScore;
	private Properties properties;

	@JsonCreator
	public VerdictCheckingTask(
			@JsonProperty("judge") final SolutionJudge judge,
			@JsonProperty("testFiles") final List<Path> testFiles,
			@JsonProperty("maximumScore") final BigDecimal maximumScore,
			@JsonProperty("properties") final Properties properties) {
		this.judge = judge;
		this.testFiles = testFiles;
		this.maximumScore = maximumScore;
		this.properties = properties;
	}

	public SolutionJudge getJudge() {
		return this.judge;
	}

	public BigDecimal getMaximumScore() {
		return this.maximumScore;
	}

	public Properties getProperties() {
		return this.properties;
	}

	public List<Path> getTestFiles() {
		return this.testFiles;
	}

	public boolean isUserTest() {
		return this.isUserTest;
	}

	@Override
	public Pair<SolutionJudge, SolutionResult> run() throws Exception {
		if (this.isUserTest) {
			throw new UnsupportedOperationException(
					"User tests aren't supported yet");
		} else {
			try {
				final SolutionResult result = this.judge.run(this.testFiles,
						true,
						this.maximumScore, this.properties);
				return new Pair<SolutionJudge, SolutionResult>(this.judge,
						result);
			} finally {
				this.judge.closeLocal();
				Janitor.cleanUp(this.judge);
			}
		}
	}

	public void setJudge(SolutionJudge judge) {
		this.judge = judge;
	}

	public void setMaximumScore(BigDecimal maximumScore) {
		this.maximumScore = maximumScore;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	public void setTestFiles(List<Path> testFiles) {
		this.testFiles = testFiles;
	}

	public void setUserTest(boolean isUserTest) {
		this.isUserTest = isUserTest;
	}
}
