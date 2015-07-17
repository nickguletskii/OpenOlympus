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

import static org.jooq.impl.DSL.max;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.locks.Lock;
import java.util.stream.Collectors;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.exec.ExecuteException;
import org.jooq.Condition;
import org.jooq.Cursor;
import org.jooq.DSLContext;
import org.jooq.Record1;
import org.jooq.SelectConditionStep;
import org.ng200.openolympus.FileAccess;
import org.ng200.openolympus.SecurityExpressionConstants;
import org.ng200.openolympus.dto.TaskCreationDto;
import org.ng200.openolympus.dto.UploadableTask;
import org.ng200.openolympus.exceptions.GeneralNestedRuntimeException;
import org.ng200.openolympus.jooq.Tables;
import org.ng200.openolympus.jooq.enums.TaskPermissionType;
import org.ng200.openolympus.jooq.tables.daos.TaskDao;
import org.ng200.openolympus.jooq.tables.pojos.Group;
import org.ng200.openolympus.jooq.tables.pojos.Solution;
import org.ng200.openolympus.jooq.tables.pojos.Task;
import org.ng200.openolympus.jooq.tables.pojos.User;
import org.ng200.openolympus.jooq.tables.pojos.Verdict;
import org.ng200.openolympus.jooq.tables.records.SolutionRecord;
import org.ng200.openolympus.jooq.tables.records.TaskPermissionRecord;
import org.ng200.openolympus.model.OlympusPrincipal;
import org.ng200.openolympus.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
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

	@PreAuthorize(SecurityExpressionConstants.IS_SUPERUSER
			+ SecurityExpressionConstants.OR + '('
			+ SecurityExpressionConstants.IS_USER
			+ SecurityExpressionConstants.AND
			+ SecurityExpressionConstants.NO_CONTEST_CURRENTLY + ')')
	public long countTasks() {
		return taskDao.count();
	}

	@PreAuthorize(SecurityExpressionConstants.IS_SUPERUSER)
	public List<Task> findAFewTasksWithNameContaining(final String name) {
		return dslContext.selectFrom(Tables.TASK)
				.where(Tables.TASK.NAME.like(name))
				.limit(LIMIT_TASKS_WITH_NAME_CONTAINING).fetchInto(Task.class);
	}

	@PreAuthorize(SecurityExpressionConstants.IS_SUPERUSER)
	public List<Task> findTasksNewestFirst(final int pageNumber,
			final int pageSize) {
		return dslContext.selectFrom(Tables.TASK).groupBy(Tables.TASK.ID)
				.orderBy(Tables.TASK.CREATED_DATE.desc())
				.limit(pageSize).offset((pageNumber - 1) * pageSize)
				.fetchInto(Task.class);
	}

	@PreAuthorize(SecurityExpressionConstants.IS_SUPERUSER
			+ SecurityExpressionConstants.OR + '('
			+ SecurityExpressionConstants.IS_USER
			+ SecurityExpressionConstants.AND
			+ SecurityExpressionConstants.NO_CONTEST_CURRENTLY + ')')
	public List<Task> findTasksNewestFirstAndAuthorized(Integer pageNumber,
			int pageSize, Principal principal) {
		if (this.securityService.isSuperuser(principal)) {
			return this.findTasksNewestFirst(pageNumber, pageSize);
		}
		SelectConditionStep<Record1<Long>> userId = dslContext
				.select(Tables.USER.ID)
				.from(Tables.USER)
				.where(Tables.USER.USERNAME
						.eq(principal.getName()));
		Condition taskPermissionAppliesToUser = Tables.TASK_PERMISSION.PRINCIPAL_ID
				.in(
						dslContext.select(Tables.GROUP_USERS.GROUP_ID)
								.from(Tables.GROUP_USERS)
								.where(Tables.GROUP_USERS.GROUP_ID.eq(userId)))
				.or(Tables.TASK_PERMISSION.PRINCIPAL_ID.eq(userId));
		return dslContext.select(Tables.TASK.fields())
				.from(Tables.TASK)
				.join(Tables.TASK_PERMISSION)
				.on(Tables.TASK_PERMISSION.TASK_ID
						.eq(Tables.TASK.ID))
				.where(taskPermissionAppliesToUser
						.and(Tables.TASK_PERMISSION.PERMISSION
								.eq(TaskPermissionType.view)))
				.groupBy(Tables.TASK.CREATED_DATE)
				.orderBy(Tables.TASK.CREATED_DATE.desc()).limit(pageSize)
				.offset((pageNumber - 1) * pageSize)
				.fetchInto(Task.class);
	}

	@PreAuthorize(SecurityExpressionConstants.IS_SUPERUSER
			+ SecurityExpressionConstants.OR + '('
			+ SecurityExpressionConstants.IS_USER
			+ SecurityExpressionConstants.AND
			+ SecurityExpressionConstants.NO_CONTEST_CURRENTLY
			+ SecurityExpressionConstants.AND
			+ SecurityExpressionConstants.TASK_PUBLISHED + ')')
	public BigDecimal getMaximumScore(final Task task) {
		return dslContext.select(max(Tables.SOLUTION.MAXIMUM_SCORE))
				.from(Tables.SOLUTION)
				.where(Tables.SOLUTION.TASK_ID.eq(task.getId())).fetchOne()
				.value1();
	}

	@PreAuthorize(SecurityExpressionConstants.IS_SUPERUSER
			+ SecurityExpressionConstants.OR + '('
			+ SecurityExpressionConstants.IS_USER
			+ SecurityExpressionConstants.AND
			+ SecurityExpressionConstants.USER_IS_OWNER
			+ SecurityExpressionConstants.AND
			+ SecurityExpressionConstants.NO_CONTEST_CURRENTLY
			+ SecurityExpressionConstants.AND
			+ SecurityExpressionConstants.TASK_PUBLISHED + ')')
	public BigDecimal getScore(final Task task, final User user) {
		return dslContext.select(max(Tables.SOLUTION.SCORE))
				.from(Tables.SOLUTION)
				.where(Tables.SOLUTION.TASK_ID.eq(task.getId())
						.and(Tables.SOLUTION.USER_ID.eq(user.getId())))
				.fetchOne().value1();
	}

	@PreAuthorize(SecurityExpressionConstants.IS_SUPERUSER
			+ SecurityExpressionConstants.OR + '('
			+ SecurityExpressionConstants.IS_USER
			+ SecurityExpressionConstants.AND
			+ SecurityExpressionConstants.NO_CONTEST_CURRENTLY + ')')
	@PostAuthorize(SecurityExpressionConstants.IS_SUPERUSER
			+ SecurityExpressionConstants.OR + " #returnObject.published")
	public Task getTaskByName(final String taskName) {
		return taskDao.fetchOneByName(taskName);
	}

	@PreAuthorize(SecurityExpressionConstants.IS_SUPERUSER)
	@Transactional
	public void rejudgeTask(final Task task)
			throws ExecutionException, IOException {
		dslContext.delete(Tables.VERDICT).where(Tables.VERDICT.SOLUTION_ID
				.in(dslContext.select(Tables.SOLUTION.ID)
						.from(Tables.SOLUTION)
						.where(Tables.SOLUTION.TASK_ID.eq(task.getId()))));
		Cursor<SolutionRecord> solutions = dslContext
				.selectFrom(Tables.SOLUTION)
				.where(Tables.SOLUTION.TASK_ID.eq(task.getId())).fetchLazy();
		while (solutions.hasNext()) {
			Solution solution = solutions.fetchOneInto(Solution.class);
			testingService.testSolutionOnAllTests(solution);
		}
	}

	@PreAuthorize(SecurityExpressionConstants.IS_SUPERUSER)
	public Task insertTask(Task task) {
		return insert(task, Tables.TASK);
	}

	public Task getTaskFromVerdict(Verdict verdict) {
		return dslContext.select(Tables.TASK.fields()).from(Tables.VERDICT)
				.join(Tables.SOLUTION)
				.on(Tables.SOLUTION.ID.eq(Tables.VERDICT.ID)).join(Tables.TASK)
				.on(Tables.TASK.ID.eq(Tables.SOLUTION.TASK_ID))
				.where(Tables.VERDICT.ID.eq(verdict.getId()))
				.fetchOneInto(Task.class);
	}

	public Task getById(Integer id) {
		return taskDao.findById(id);
	}

	@PreAuthorize(SecurityExpressionConstants.IS_SUPERUSER)
	public Task updateTask(Task task) {
		return update(task, Tables.TASK);
	}

	public Map<TaskPermissionType, List<OlympusPrincipal>> getTaskPermissionsAndPrincipalData(
			Task task) {
		return getTaskPermissionsAndPrincipalData(task.getId());
	}

	public Map<TaskPermissionType, List<OlympusPrincipal>> getTaskPermissionsAndPrincipalData(
			int taskId) {
		return dslContext.select(Tables.TASK_PERMISSION.PERMISSION,
				Tables.TASK_PERMISSION.PRINCIPAL_ID)
				.from(Tables.TASK_PERMISSION)
				.where(Tables.TASK_PERMISSION.TASK_ID.eq(taskId))
				.fetchGroups(Tables.TASK_PERMISSION.PERMISSION,
						(record) -> Optional.<OlympusPrincipal> ofNullable(
								dslContext.selectFrom(Tables.GROUP)
										.where(Tables.GROUP.ID
												.eq(record.value2()))
										.fetchOneInto(Group.class))
								.orElse(
										dslContext.selectFrom(Tables.USER)
												.where(Tables.USER.ID
														.eq(record.value2()))
												.fetchOneInto(User.class)));
	}

	public void createDefaultTaskACL(Task task, User owner) {
		TaskPermissionRecord permissionRecord = new TaskPermissionRecord(
				task.getId(), owner.getId(), TaskPermissionType.manage_acl);
		permissionRecord.attach(dslContext.configuration());
		permissionRecord.insert();
	}

	@PreAuthorize(SecurityExpressionConstants.IS_SUPERUSER)
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

	@PreAuthorize(SecurityExpressionConstants.IS_SUPERUSER)
	protected void uploadDescription(final Task task, InputStream inputStream)
			throws ExecuteException, IOException {
		this.storageService.writeTaskDescription(task, inputStream);
	}

	@PreAuthorize(SecurityExpressionConstants.IS_SUPERUSER)
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
					throws IOException, BindException {
		Task task = new Task().setName(taskCreationDto.getName())
				.setCreatedDate(LocalDateTime.now());

		final Path localDescriptionFile = this.storageService
				.createTaskDescriptionFileStorage(task);
		final Path judgeDir = this.storageService
				.createTaskJudgeDirectory(task);

		task = this.insertTask(task);
		this.createDefaultTaskACL(task, owner);

		final Lock lock = task.writeLock();
		lock.lock();

		try {
			this.uploadDescription(task, taskCreationDto
					.getDescriptionFile().getInputStream());
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

	@Transactional
	public void setTaskPermissionsAndPrincipals(int taskId,
			Map<TaskPermissionType, List<Long>> map) {
		dslContext.delete(Tables.TASK_PERMISSION)
				.where(Tables.TASK_PERMISSION.TASK_ID.eq(taskId)).execute();
		dslContext.batchInsert(
				map.entrySet().stream().flatMap(e -> e.getValue().stream()
						.map(id -> new Pair<>(e.getKey(), id)))
						.map(p -> {
							TaskPermissionRecord record = new TaskPermissionRecord(
									taskId,
									p.getSecond(), p.getFirst());
							record.attach(dslContext.configuration());
							return record;
						}).collect(Collectors.toList()))
				.execute();
	}
}
