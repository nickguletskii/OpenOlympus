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

import java.nio.file.Path;
import java.util.List;
import java.util.Properties;

import org.ng200.openolympus.cerberus.Janitor;
import org.ng200.openolympus.cerberus.SolutionJudge;
import org.ng200.openolympus.cerberus.util.ExceptionalProducer;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SolutionCompilationTask implements
		ExceptionalProducer<SolutionJudge> {
	private SolutionJudge judge;
	private Properties properties;
	private List<Path> solutionFiles;

	@JsonCreator
	public SolutionCompilationTask(@JsonProperty("judge") SolutionJudge judge,
			@JsonProperty("solutionFiles") List<Path> solutionFiles,
			@JsonProperty("properties") Properties properties) {
		this.judge = judge;
		this.properties = properties;
		this.solutionFiles = solutionFiles;
	}

	public SolutionJudge getJudge() {
		return judge;
	}

	public void setJudge(SolutionJudge judge) {
		this.judge = judge;
	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	public List<Path> getSolutionFiles() {
		return solutionFiles;
	}

	public void setSolutionFiles(List<Path> solutionFiles) {
		this.solutionFiles = solutionFiles;
	}

	@Override
	public SolutionJudge run() throws Exception {
		try {
			judge.compile(solutionFiles, properties);
			return judge;
		} finally {
			if (judge != null) {
				judge.closeLocal();
				Janitor.cleanUp(judge);
			}
		}
	}

}