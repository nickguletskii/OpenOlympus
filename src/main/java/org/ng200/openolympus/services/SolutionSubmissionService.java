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

import java.io.IOException;
import java.nio.file.Path;
import java.time.OffsetDateTime;

import org.ng200.openolympus.dto.SolutionSubmissionDto;
import org.ng200.openolympus.jooq.tables.pojos.Solution;
import org.ng200.openolympus.jooq.tables.pojos.Task;
import org.ng200.openolympus.jooq.tables.pojos.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SolutionSubmissionService {

	@Autowired
	private StorageService storageService;

	@Autowired
	private SolutionService solutionService;

	@Autowired
	private TestingService testingService;

	@Transactional
	public Solution submitSolution(final Task task,
			final SolutionSubmissionDto solutionDto, final User user)
					throws IOException {
		final Path solutionFile = this.storageService.createSolutionDirectory()
				.resolve(
						this.storageService.sanitizeName(solutionDto
								.getSolutionFile().getOriginalFilename()));

		solutionDto.getSolutionFile().transferTo(solutionFile.toFile());

		Solution solution = new Solution().setTaskId(task.getId())
				.setUserId(user.getId())
				.setTimeAdded(OffsetDateTime.now())
				.setTested(false);

		this.storageService.setSolutionFile(solution, solutionFile);

		solution = this.solutionService.insertSolution(solution);
		this.testingService.testSolutionOnAllTests(solution);
		return solution;
	}
}
