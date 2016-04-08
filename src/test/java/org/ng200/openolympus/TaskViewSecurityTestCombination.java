package org.ng200.openolympus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import org.ng200.openolympus.jooq.enums.ContestPermissionType;
import org.ng200.openolympus.jooq.enums.GeneralPermissionType;
import org.ng200.openolympus.jooq.enums.TaskPermissionType;
import org.springframework.beans.BeanUtils;

public class TaskViewSecurityTestCombination {
	public static enum ContestStatus {
		NONE, IN_PROGRESS, HASNT_STARTED, FINISHED
	}

	private Set<GeneralPermissionType> userGeneralPermissions = new HashSet<>();

	private Set<TaskPermissionType> taskPermissions = new HashSet<>();

	private Set<ContestPermissionType> contestPermissions = new HashSet<>();

	private ContestStatus contestStatus = ContestStatus.NONE;

	private boolean taskInContest = false;

	private boolean forbidden = false;

	public boolean isTaskInContest() {
		return taskInContest;
	}

	public TaskViewSecurityTestCombination setTaskInContest(
			boolean taskInContest) {
		this.taskInContest = taskInContest;
		return this;
	}

	public Set<TaskPermissionType> getTaskPermissions() {
		return taskPermissions;
	}

	public TaskViewSecurityTestCombination setTaskPermissions(
			Set<TaskPermissionType> taskPermissions) {
		this.taskPermissions = taskPermissions;
		return this;
	}

	public Set<ContestPermissionType> getContestPermissions() {
		return contestPermissions;
	}

	public TaskViewSecurityTestCombination setContestPermissions(
			Set<ContestPermissionType> contestPermissions) {
		this.contestPermissions = contestPermissions;
		return this;
	}

	public ContestStatus getContestStatus() {
		return contestStatus;
	}

	public TaskViewSecurityTestCombination setContestStatus(
			ContestStatus contestStatus) {
		this.contestStatus = contestStatus;
		return this;
	}

	public Set<GeneralPermissionType> getUserGeneralPermissions() {
		return userGeneralPermissions;
	}

	public TaskViewSecurityTestCombination setUserGeneralPermissions(
			Set<GeneralPermissionType> userGeneralPermissions) {
		this.userGeneralPermissions = userGeneralPermissions;
		return this;
	}

	public TaskViewSecurityTestCombination permitGeneral(
			GeneralPermissionType... generalPerms) {
		this.userGeneralPermissions.addAll(Arrays.asList(generalPerms));
		return this;
	}

	public TaskViewSecurityTestCombination denyGeneral(
			GeneralPermissionType... generalPerms) {
		this.userGeneralPermissions
				.addAll(Arrays.asList(GeneralPermissionType.values()));
		this.userGeneralPermissions.removeAll(Arrays.asList(generalPerms));
		return this;
	}

	public TaskViewSecurityTestCombination permitTask(
			TaskPermissionType... taskPerms) {
		this.taskPermissions.addAll(Arrays.asList(taskPerms));
		return this;
	}

	public TaskViewSecurityTestCombination denyTask(
			TaskPermissionType... taskPerms) {
		this.taskPermissions
				.addAll(Arrays.asList(TaskPermissionType.values()));
		this.taskPermissions.removeAll(Arrays.asList(taskPerms));
		return this;
	}

	public TaskViewSecurityTestCombination permitContest(
			ContestPermissionType... contestPerms) {
		this.contestPermissions.addAll(Arrays.asList(contestPerms));
		return this;
	}

	public TaskViewSecurityTestCombination denyContest(
			ContestPermissionType... contestPerms) {
		this.contestPermissions
				.addAll(Arrays.asList(ContestPermissionType.values()));
		this.contestPermissions.removeAll(Arrays.asList(contestPerms));
		return this;
	}

	public static TaskViewSecurityTestCombination copy(
			TaskViewSecurityTestCombination combo) {
		TaskViewSecurityTestCombination newCombo = new TaskViewSecurityTestCombination();
		newCombo.setUserGeneralPermissions(new HashSet<>(combo.getUserGeneralPermissions()));
		newCombo.setContestPermissions(new HashSet<>(combo.getContestPermissions()));
		newCombo.setTaskPermissions(new HashSet<>(combo.getTaskPermissions()));
		newCombo.setForbidden(combo.isForbidden());
		newCombo.setTaskInContest(combo.isTaskInContest());
		newCombo.setContestStatus(combo.getContestStatus());
		return newCombo;
	}

	public static Stream<TaskViewSecurityTestCombination> allCombinations() {
		return Stream.of(new TaskViewSecurityTestCombination())
				.flatMap(x -> Stream.of(ContestStatus.values())
						.map(status -> copy(x).setContestStatus(status)))
				.flatMap(x -> allCombinations2(x));
	}

	private static Stream<TaskViewSecurityTestCombination> allCombinations2(
			TaskViewSecurityTestCombination combo) {
		List<TaskViewSecurityTestCombination> res = new ArrayList<>();

		// If the user can modify the task's ACL (ignores contest)
		res.add(
				copy(combo)
						.permitTask(TaskPermissionType.manage_acl)
						.setTaskInContest(false)
						.expectForbidden(false));
		res.add(
				copy(combo)
						.permitTask(TaskPermissionType.manage_acl)
						.setTaskInContest(true)
						.expectForbidden(false));
		// If the user can modify the task (ignores contest)
		res.add(
				copy(combo)
						.permitTask(TaskPermissionType.modify)
						.setTaskInContest(false)
						.expectForbidden(false));
		res.add(
				copy(combo)
						.permitTask(TaskPermissionType.modify)
						.setTaskInContest(true)
						.expectForbidden(false));
		if (combo.getContestStatus() == ContestStatus.IN_PROGRESS) {
			// If there is a current contest, the task is in that contest, the
			// user can view this task during the contest
			res.add(
					copy(combo)
							.setTaskInContest(true)
							.permitTask(TaskPermissionType.view_during_contest)
							.permitContest(ContestPermissionType.participate)
							.permitContest(
									ContestPermissionType.view_tasks_after_contest_started)
							.expectForbidden(false));
			res.add(
					copy(combo)
							.setTaskInContest(true)
							.permitTask(TaskPermissionType.view)
							.permitContest(ContestPermissionType.participate)
							.permitContest(
									ContestPermissionType.view_tasks_after_contest_started)
							.expectForbidden(false));
			// If the task is not in the contest, this shouldn't apply
			res.add(
					copy(combo)
							.setTaskInContest(false)
							.permitTask(TaskPermissionType.view_during_contest)
							.permitContest(ContestPermissionType.participate)
							.permitContest(
									ContestPermissionType.view_tasks_after_contest_started)
							.expectForbidden(true));
			// Task is in contest, but the author hasn't allowed contest
			// participants or other users to view it.
			res.add(
					copy(combo)
							.setTaskInContest(true)
							.denyTask(TaskPermissionType.view_during_contest,
									TaskPermissionType.view,
									TaskPermissionType.manage_acl,
									TaskPermissionType.modify)
							.permitContest(ContestPermissionType.participate)
							.permitContest(
									ContestPermissionType.view_tasks_after_contest_started)
							.expectForbidden(false));
			// However, if the task is viewable outside the contest, but it
			// isn't a part of the current contest, it shouldn't be viewable.
			res.add(
					copy(combo)
							.setTaskInContest(false)
							.permitTask(TaskPermissionType.view)
							.permitContest(ContestPermissionType.participate)
							.expectForbidden(true));
		}
		return res.stream();
	}

	public boolean isForbidden() {
		return forbidden;
	}

	public TaskViewSecurityTestCombination setForbidden(boolean forbidden) {
		this.forbidden = forbidden;
		return this;
	}

	public TaskViewSecurityTestCombination expectForbidden(boolean forbidden) {
		this.forbidden = forbidden;
		return this;
	}

	@Override
	public String toString() {
		return String.format(
				"TaskViewSecurityTestCombination [userGeneralPermissions=%s, taskPermissions=%s, contestPermissions=%s, contestStatus=%s, taskInContest=%s, forbidden=%s]",
				userGeneralPermissions, taskPermissions, contestPermissions,
				contestStatus, taskInContest, forbidden);
	}
}
