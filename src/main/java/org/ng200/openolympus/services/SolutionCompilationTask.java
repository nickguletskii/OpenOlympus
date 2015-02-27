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

import java.io.Serializable;
import java.util.Properties;

import org.jppf.client.taskwrapper.DataProviderHolder;
import org.jppf.node.protocol.AbstractTask;
import org.jppf.task.storage.DataProvider;
import org.ng200.openolympus.cerberus.Janitor;
import org.ng200.openolympus.cerberus.SolutionJudge;
import org.ng200.openolympus.cerberus.util.Lists;
import org.ng200.openolympus.model.Solution;

public class SolutionCompilationTask extends AbstractTask<SolutionJudge>
		implements Serializable, DataProviderHolder {
	/**
	 *
	 */
	private static final long serialVersionUID = 5491821764490073646L;
	private transient DataProvider dataProvider;
	private final SolutionJudge judge;

	private final Solution solution;
	private final Properties properties;

	public SolutionCompilationTask(SolutionJudge judge, Solution solution,
			Properties properties) {
		this.judge = judge;
		this.solution = solution;
		this.properties = properties;
	}

	@Override
	public void run() {
		try {
			final StorageService storageService = ((StorageService) this.dataProvider
					.getParameter("storageService"));
			this.judge.compile(
					Lists.from(storageService.getSolutionFile(this.solution)),
					this.properties);
		} finally {
			Janitor.cleanUp(this.judge);
			this.setResult(this.judge);
		}
	}

	@Override
	public void setDataProvider(final DataProvider dataProvider) {
		this.dataProvider = dataProvider;
	}
}
