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