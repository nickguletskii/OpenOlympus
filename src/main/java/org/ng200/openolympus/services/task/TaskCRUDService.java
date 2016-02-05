/**
 * The MIT License
 * Copyright (c) 2014-2016 Nick Guletskii
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
package org.ng200.openolympus.services.task;

import java.security.Principal;
import java.util.List;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record1;
import org.jooq.SelectConditionStep;
import org.jooq.impl.DSL;
import org.ng200.openolympus.jooq.Tables;
import org.ng200.openolympus.jooq.enums.TaskPermissionType;
import org.ng200.openolympus.jooq.tables.daos.TaskDao;
import org.ng200.openolympus.jooq.tables.pojos.Task;
import org.ng200.openolympus.jooq.tables.pojos.Verdict;
import org.ng200.openolympus.services.GenericCreateUpdateRepository;
import org.ng200.openolympus.services.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TaskCRUDService extends GenericCreateUpdateRepository {
	/**
	 * Number of tasks with names containing a string that should be returned
	 */
	private static final int LIMIT_TASKS_WITH_NAME_CONTAINING = 30;

	@Autowired
	private TaskDao taskDao;

	@Autowired
	private DSLContext dslContext;

	@Autowired
	private SecurityService securityService;

	public long countTasks() {
		return this.taskDao.count();
	}

	public List<Task> findAFewTasksWithNameContaining(final String name) {
		return this.dslContext.selectFrom(Tables.TASK)
				.where(Tables.TASK.NAME.toString() + " % "
						+ DSL.val(name, String.class))
				.limit(TaskCRUDService.LIMIT_TASKS_WITH_NAME_CONTAINING)
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
			int pageSize,
			Principal principal) {
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

	@Transactional
	public Task insertTask(Task task) {
		return this.insert(task, Tables.TASK);
	}

	@Transactional
	public Task updateTask(Task task) {
		return this.update(task, Tables.TASK);
	}

}