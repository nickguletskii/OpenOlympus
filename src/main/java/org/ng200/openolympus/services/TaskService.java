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
package org.ng200.openolympus.services;

import java.util.ArrayList;
import java.util.List;

import org.ng200.openolympus.model.Solution;
import org.ng200.openolympus.model.Task;
import org.ng200.openolympus.model.Verdict;
import org.ng200.openolympus.repositories.SolutionRepository;
import org.ng200.openolympus.repositories.TaskRepository;
import org.ng200.openolympus.repositories.VerdictRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TaskService {

	@Autowired
	private TaskRepository taskRepository;
	@Autowired
	private SolutionRepository solutionRepository;

	@Autowired
	private VerdictRepository verdictRepository;

	@Autowired
	private TestingService testingService;

	@Transactional
	public void rejudgeTask(final Task task) {
		final List<Verdict> verdictsToRemove = new ArrayList<Verdict>();
		final List<Solution> solutions = this.solutionRepository
				.findByTask(task);
		solutions
		.stream()
				.map(solution -> this.verdictRepository
				.findBySolution(solution))
				.forEach(verdictsToRemove::addAll);
		this.verdictRepository.delete(verdictsToRemove);
		solutions.forEach(this.testingService::testSolutionOnAllTests);
	}
}
