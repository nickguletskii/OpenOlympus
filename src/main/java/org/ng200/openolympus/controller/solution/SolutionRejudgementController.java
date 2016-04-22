package org.ng200.openolympus.controller.solution;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import org.ng200.openolympus.Assertions;
import org.ng200.openolympus.SecurityClearanceType;
import org.ng200.openolympus.exceptions.ResourceNotFoundException;
import org.ng200.openolympus.jooq.enums.TaskPermissionType;
import org.ng200.openolympus.jooq.tables.pojos.Solution;
import org.ng200.openolympus.jooq.tables.pojos.Task;
import org.ng200.openolympus.security.annotations.SecurityAnd;
import org.ng200.openolympus.security.annotations.SecurityLeaf;
import org.ng200.openolympus.security.annotations.SecurityOr;
import org.ng200.openolympus.security.annotations.TaskPermissionRequired;
import org.ng200.openolympus.security.predicates.UserHasTaskPermission;
import org.ng200.openolympus.services.task.TaskSolutionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Profile("web")
@SecurityOr({
				@SecurityAnd({
								@SecurityLeaf(value = SecurityClearanceType.APPROVED_USER, predicates = UserHasTaskPermission.class)
				})
})
@TaskPermissionRequired(TaskPermissionType.rejudge)
public class SolutionRejudgementController {
	@Autowired
	private TaskSolutionsService taskSolutionsService;

	@RequestMapping(value = "/api/task/{task}/rejudgeSolution/{solution}", method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.OK)
	public void rejudgeTask(@PathVariable(value = "task") final Task task,
			@PathVariable(value = "solution") final Solution solution,
			final Model model) throws ExecutionException, IOException {
		Assertions.resourceExists(task);
		Assertions.resourceExists(solution);
		if (task.getId() != solution.getTaskId()) {
			throw new ResourceNotFoundException();
		}

		this.taskSolutionsService.rejudgeSolution(solution);
	}
}
