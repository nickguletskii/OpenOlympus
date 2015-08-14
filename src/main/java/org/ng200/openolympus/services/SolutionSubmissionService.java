package org.ng200.openolympus.services;

import java.io.IOException;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.time.Instant;

import org.ng200.openolympus.dto.SolutionSubmissionDto;
import org.ng200.openolympus.jooq.tables.pojos.Solution;
import org.ng200.openolympus.jooq.tables.pojos.Task;
import org.ng200.openolympus.jooq.tables.pojos.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SolutionSubmissionService {

	@Autowired
	private StorageService storageService;

	@Autowired
	private SolutionService solutionService;

	@Autowired
	private TestingService testingService;

	public Solution submitTask(final Task task,
			final SolutionSubmissionDto solutionDto, final User user)
					throws IOException {
		final Path solutionFile = this.storageService.createSolutionDirectory()
				.resolve(
						this.storageService.sanitizeName(solutionDto
								.getSolutionFile().getOriginalFilename()));

		solutionDto.getSolutionFile().transferTo(solutionFile.toFile());

		Solution solution = new Solution().setTaskId(task.getId())
				.setUserId(user.getId())
				.setTimeAdded(Timestamp.from(Instant.now()))
				.setTested(false);

		this.storageService.setSolutionFile(solution, solutionFile);

		solution = this.solutionService.insertSolution(solution);
		this.testingService.testSolutionOnAllTests(solution);
		return solution;
	}
}
