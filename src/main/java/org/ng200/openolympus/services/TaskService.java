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

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.locks.Lock;
import java.util.stream.Collectors;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRefNameException;
import org.eclipse.jgit.api.errors.RefAlreadyExistsException;
import org.eclipse.jgit.api.errors.RefNotFoundException;
import org.jooq.Condition;
import org.jooq.Cursor;
import org.jooq.DSLContext;
import org.jooq.Record1;
import org.jooq.SelectConditionStep;
import org.jooq.impl.DSL;
import org.ng200.openolympus.FileAccess;
import org.ng200.openolympus.dto.TaskCreationDto;
import org.ng200.openolympus.dto.TaskModificationDto;
import org.ng200.openolympus.dto.UploadableTask;
import org.ng200.openolympus.exceptions.GeneralNestedRuntimeException;
import org.ng200.openolympus.jooq.Routines;
import org.ng200.openolympus.jooq.Tables;
import org.ng200.openolympus.jooq.enums.TaskPermissionType;
import org.ng200.openolympus.jooq.tables.daos.TaskDao;
import org.ng200.openolympus.jooq.tables.pojos.Solution;
import org.ng200.openolympus.jooq.tables.pojos.Task;
import org.ng200.openolympus.jooq.tables.pojos.User;
import org.ng200.openolympus.jooq.tables.pojos.Verdict;
import org.ng200.openolympus.jooq.tables.records.SolutionRecord;
import org.ng200.openolympus.jooq.tables.records.TaskPermissionRecord;
import org.ng200.openolympus.model.OlympusPrincipal;
import org.ng200.openolympus.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;

@Service
public class TaskService extends GenericCreateUpdateRepository {

	/**
	 * Number of tasks with names containing a string that should be returned
	 */
	private static final int LIMIT_TASKS_WITH_NAME_CONTAINING = 30;

	@Autowired
	private TestingService testingService;

	@Autowired
	private SecurityService securityService;

	@Autowired
	private TaskDao taskDao;

	@Autowired
	private DSLContext dslContext;

	@Autowired
	private StorageService storageService;

	@Autowired
	private AclService aclService;

	public boolean canModifyTask(Task task, User user) {
		return user.getSuperuser()
				|| Routines.hasTaskPermission(this.dslContext.configuration(),
						task.getId(), user.getId(), TaskPermissionType.modify);
	}

	public long countTasks() {
		return this.taskDao.count();
	}

	public void createDefaultTaskACL(Task task, User owner) {
		final TaskPermissionRecord permissionRecord = new TaskPermissionRecord(
				task.getId(), owner.getId(), TaskPermissionType.manage_acl);
		permissionRecord.attach(this.dslContext.configuration());
		permissionRecord.insert();
	}

	public boolean doesUserHaveTaskPermission(Task task, User user,
			TaskPermissionType permission) {
		return Routines.hasTaskPermission(this.dslContext.configuration(),
				task.getId(), user.getId(), permission);
	}

	private void extractZipFile(final InputStream zipFile,
			final Path destination) throws Exception {
		try (ArchiveInputStream input = new ArchiveStreamFactory()
				.createArchiveInputStream(new BufferedInputStream(zipFile))) {
			ArchiveEntry entry;
			while ((entry = input.getNextEntry()) != null) {
				final Path dest = destination.resolve(entry.getName());
				if (entry.isDirectory()) {
					FileAccess.createDirectories(dest);
				} else {
					FileAccess.createDirectories(dest.getParent());
					FileAccess.createFile(dest);
					Files.copy(input, dest,
							StandardCopyOption.REPLACE_EXISTING);
				}
			}
		}
	}

	public List<Task> findAFewTasksWithNameContaining(final String name) {
		return this.dslContext.selectFrom(Tables.TASK)
				.where(Tables.TASK.NAME.toString() + " % "
						+ DSL.val(name, String.class))
				.limit(TaskService.LIMIT_TASKS_WITH_NAME_CONTAINING)
				.fetchInto(Task.class);
	}

	public List<Task> findTasksNewestFirst(final int pageNumber,
			final int pageSize) {
		return this.dslContext.selectFrom(Tables.TASK).groupBy(Tables.TASK.ID)
				.orderBy(Tables.TASK.CREATED_DATE.desc())
				.limit(pageSize).offset((pageNumber - 1) * pageSize)
				.fetchInto(Task.class);
	}

	public List<Task> findTasksNewestFirstAndAuthorized(Integer pageNumber,
			int pageSize, Principal principal) {
		if (this.securityService.isSuperuser(principal)) {
			return this.findTasksNewestFirst(pageNumber, pageSize);
		}
		final SelectConditionStep<Record1<Long>> userId = this.dslContext
				.select(Tables.USER.ID)
				.from(Tables.USER)
				.where(Tables.USER.USERNAME
						.eq(principal.getName()));
		final Condition taskPermissionAppliesToUser = Tables.TASK_PERMISSION.PRINCIPAL_ID
				.in(
						this.dslContext.select(Tables.GROUP_USERS.GROUP_ID)
								.from(Tables.GROUP_USERS)
								.where(Tables.GROUP_USERS.GROUP_ID.eq(userId)))
				.or(Tables.TASK_PERMISSION.PRINCIPAL_ID.eq(userId));
		return this.dslContext.select(Tables.TASK.fields())
				.from(Tables.TASK)
				.join(Tables.TASK_PERMISSION)
				.on(Tables.TASK_PERMISSION.TASK_ID
						.eq(Tables.TASK.ID))
				.where(taskPermissionAppliesToUser
						.and(Tables.TASK_PERMISSION.PERMISSION
								.eq(TaskPermissionType.view)))
				.groupBy(Tables.TASK.ID)
				.orderBy(Tables.TASK.CREATED_DATE.desc()).limit(pageSize)
				.offset((pageNumber - 1) * pageSize)
				.fetchInto(Task.class);
	}

	public Task getById(Integer id) {
		return this.taskDao.findById(id);
	}

	public BigDecimal getMaximumScore(final Task task) {
		return this.dslContext.select(DSL.max(Tables.SOLUTION.MAXIMUM_SCORE))
				.from(Tables.SOLUTION)
				.where(Tables.SOLUTION.TASK_ID.eq(task.getId())).fetchOne()
				.value1();
	}

	public BigDecimal getScore(final Task task, final User user) {
		return this.dslContext.select(DSL.max(Tables.SOLUTION.SCORE))
				.from(Tables.SOLUTION)
				.where(Tables.SOLUTION.TASK_ID.eq(task.getId())
						.and(Tables.SOLUTION.USER_ID.eq(user.getId())))
				.fetchOne().value1();
	}

	public Task getTaskByName(final String taskName) {
		return this.taskDao.fetchOneByName(taskName);
	}

	public Task getTaskFromVerdict(Verdict verdict) {
		return this.dslContext.select(Tables.TASK.fields()).from(Tables.VERDICT)
				.join(Tables.SOLUTION)
				.on(Tables.SOLUTION.ID.eq(Tables.VERDICT.ID)).join(Tables.TASK)
				.on(Tables.TASK.ID.eq(Tables.SOLUTION.TASK_ID))
				.where(Tables.VERDICT.ID.eq(verdict.getId()))
				.fetchOneInto(Task.class);
	}

	public Map<TaskPermissionType, List<OlympusPrincipal>> getTaskPermissionsAndPrincipalData(
			int taskId) {
		return this.dslContext.select(Tables.TASK_PERMISSION.PERMISSION,
				Tables.TASK_PERMISSION.PRINCIPAL_ID)
				.from(Tables.TASK_PERMISSION)
				.where(Tables.TASK_PERMISSION.TASK_ID.eq(taskId))
				.fetchGroups(Tables.TASK_PERMISSION.PERMISSION,
						(record) -> this.aclService
								.extractPrincipal(record.value2()));
	}

	public Map<TaskPermissionType, List<OlympusPrincipal>> getTaskPermissionsAndPrincipalData(
			Task task) {
		return this.getTaskPermissionsAndPrincipalData(task.getId());
	}

	@Transactional
	public Task insertTask(Task task) {
		return this.insert(task, Tables.TASK);
	}

	@Transactional
	public void patchTask(final Task task,
			final TaskModificationDto taskModificationDto)
					throws IOException, Exception {
		task.setName(taskModificationDto.getName());

		if (taskModificationDto.getJudgeFile() != null) {
			this.uploadJudgeFile(task, taskModificationDto);
		}

		this.updateTask(task);
		this.testingService.reloadTasks();
	}

	@Transactional
	public void rejudgeTask(final Task task)
			throws ExecutionException, IOException {
		this.dslContext.delete(Tables.VERDICT).where(Tables.VERDICT.SOLUTION_ID
				.in(this.dslContext.select(Tables.SOLUTION.ID)
						.from(Tables.SOLUTION)
						.where(Tables.SOLUTION.TASK_ID.eq(task.getId()))));
		final Cursor<SolutionRecord> solutions = this.dslContext
				.selectFrom(Tables.SOLUTION)
				.where(Tables.SOLUTION.TASK_ID.eq(task.getId())).fetchLazy();
		while (solutions.hasNext()) {
			final Solution solution = solutions.fetchOneInto(Solution.class);
			this.testingService.testSolutionOnAllTests(solution);
		}
	}

	@Transactional
	public void setTaskPermissionsAndPrincipals(int taskId,
			Map<TaskPermissionType, List<Long>> map) {
		this.dslContext.delete(Tables.TASK_PERMISSION)
				.where(Tables.TASK_PERMISSION.TASK_ID.eq(taskId)).execute();
		this.dslContext.batchInsert(
				map.entrySet().stream().flatMap(e -> e.getValue().stream()
						.map(id -> new Pair<>(e.getKey(), id)))
						.map(p -> {
							final TaskPermissionRecord record = new TaskPermissionRecord();
							record.setTaskId(taskId);
							record.setPrincipalId(p.getSecond());
							record.setPermission(p.getFirst());
							record.attach(this.dslContext.configuration());
							return record;
						}).collect(Collectors.toList()))
				.execute();
	}

	@Transactional
	public Task updateTask(Task task) {
		return this.update(task, Tables.TASK);
	}

	protected void uploadJudgeFile(final Task task,
			final UploadableTask taskDto)
					throws IOException, Exception {
		final Path judgeFile = this.storageService.getTaskJudgeFile(task);
		if (FileAccess.exists(judgeFile)) {
			FileAccess.deleteDirectoryByWalking(judgeFile);
		}
		FileAccess.createDirectories(judgeFile);

		this.extractZipFile(taskDto.getJudgeFile().getInputStream(), judgeFile);
	}

	@Transactional
	public Task uploadTask(final TaskCreationDto taskCreationDto,
			final BindingResult bindingResult, User owner)
					throws IOException, BindException,
					RefAlreadyExistsException, RefNotFoundException,
					InvalidRefNameException, GitAPIException {
		Task task = new Task().setName(taskCreationDto.getName())
				.setCreatedDate(OffsetDateTime.now());

		final Path localDescriptionFile = this.storageService
				.createTaskDescriptionDirectory(task);
		final Path judgeDir = this.storageService
				.createTaskJudgeDirectory(task);

		task = this.insertTask(task);
		this.createDefaultTaskACL(task, owner);

		final Lock lock = task.writeLock();
		lock.lock();

		try {
			this.uploadJudgeFile(task, taskCreationDto);

			task = this.updateTask(task);
		} catch (final ArchiveException e) {
			bindingResult.rejectValue("judgeFile", "",
					"task.add.form.errors.judgeArchive.invalid");
			throw new BindException(bindingResult);
		} catch (final Exception e) {
			try {
				throw new GeneralNestedRuntimeException("", e);
			} finally {
				FileAccess.deleteDirectoryByWalking(judgeDir);
				FileAccess.deleteDirectoryByWalking(localDescriptionFile);
			}
		} finally {
			lock.unlock();
		}
		return task;
	}
}
