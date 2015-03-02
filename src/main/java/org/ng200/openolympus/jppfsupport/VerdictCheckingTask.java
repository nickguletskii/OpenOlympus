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
	/**
	 *
	 */
	private static final long serialVersionUID = 7829416558485210184L;

	private boolean isUserTest;
	private SolutionJudge judge;
	private List<Path> testFiles;
	private BigDecimal maximumScore;
	private Properties properties;

	public boolean isUserTest() {
		return isUserTest;
	}

	public void setUserTest(boolean isUserTest) {
		this.isUserTest = isUserTest;
	}

	public SolutionJudge getJudge() {
		return judge;
	}

	public void setJudge(SolutionJudge judge) {
		this.judge = judge;
	}

	public List<Path> getTestFiles() {
		return testFiles;
	}

	public void setTestFiles(List<Path> testFiles) {
		this.testFiles = testFiles;
	}

	public BigDecimal getMaximumScore() {
		return maximumScore;
	}

	public void setMaximumScore(BigDecimal maximumScore) {
		this.maximumScore = maximumScore;
	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

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

	@Override
	public Pair<SolutionJudge, SolutionResult> run() throws Exception {
		if (isUserTest) {
			throw new UnsupportedOperationException(
					"User tests aren't supported yet");
		} else {
			try {
				final SolutionResult result = judge.run(testFiles, true,
						maximumScore, properties);
				return new Pair<SolutionJudge, SolutionResult>(judge, result);
			} finally {
				judge.closeLocal();
				Janitor.cleanUp(judge);
			}
		}
	}
}
