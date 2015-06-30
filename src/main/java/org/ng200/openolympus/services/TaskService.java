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

import java.io.IOException;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.locks.Lock;

import org.apache.commons.lang3.NotImplementedException;
import org.jooq.Cursor;
import org.jooq.DSLContext;
import org.jooq.Record1;
import org.ng200.openolympus.SecurityExpressionConstants;
import org.ng200.openolympus.cerberus.util.Lists;
import org.ng200.openolympus.jooq.Tables;
import org.ng200.openolympus.jooq.tables.daos.TaskDao;
import org.ng200.openolympus.jooq.tables.pojos.Solution;
import org.ng200.openolympus.jooq.tables.pojos.Task;
import org.ng200.openolympus.jooq.tables.pojos.User;
import org.ng200.openolympus.jooq.tables.pojos.Verdict;
import org.ng200.openolympus.jooq.tables.records.ContestRecord;
import org.ng200.openolympus.jooq.tables.records.SolutionRecord;
import org.ng200.openolympus.jooq.tables.records.TaskRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

	@PreAuthorize(SecurityExpressionConstants.IS_ADMIN
			+ SecurityExpressionConstants.OR + '('
			+ SecurityExpressionConstants.IS_USER
			+ SecurityExpressionConstants.AND
			+ SecurityExpressionConstants.NO_CONTEST_CURRENTLY + ')')
	public long countTasks() {
		return taskDao.count();
	}

	@PreAuthorize(SecurityExpressionConstants.IS_ADMIN)
	public List<Task> findAFewTasksWithNameContaining(final String name) {
		return dslContext.selectFrom(Tables.TASK)
				.where(Tables.TASK.NAME.like(name))
				.limit(LIMIT_TASKS_WITH_NAME_CONTAINING).fetchInto(Task.class);
	}

	@PreAuthorize(SecurityExpressionConstants.IS_ADMIN)
	public List<Task> findTasksNewestFirst(final int pageNumber,
			final int pageSize) {
		return dslContext.selectFrom(Tables.TASK).groupBy(Tables.TASK.ID)
				.orderBy(Tables.TASK.CREATED_DATE.desc()).limit(pageSize)
				.offset((pageNumber - 1) * pageSize).fetchInto(Task.class);
	}

	@PreAuthorize(SecurityExpressionConstants.IS_ADMIN
			+ SecurityExpressionConstants.OR + '('
			+ SecurityExpressionConstants.IS_USER
			+ SecurityExpressionConstants.AND
			+ SecurityExpressionConstants.NO_CONTEST_CURRENTLY + ')')
	public List<Task> findTasksNewestFirstAndAuthorized(Integer pageNumber,
			int pageSize, Principal principal) {
		if (this.securityService.isSuperuser(principal)) {
			return this.findTasksNewestFirst(pageNumber, pageSize);
		}
		return dslContext
				.select(Tables.TASK.fields())
				.from(Tables.TASK_PERMISSION_PRINCIPAL)
				.join(Tables.TASK_PERMISSION)
				.on(Tables.TASK_PERMISSION_PRINCIPAL.TASK_PERMISSION_ID
						.eq(Tables.TASK_PERMISSION.ID)).join(Tables.TASK)
				.on(Tables.TASK_PERMISSION.TASK_ID.eq(Tables.TASK.ID))
				.groupBy(Tables.TASK.CREATED_DATE)
				.orderBy(Tables.TASK.CREATED_DATE.desc()).limit(pageSize)
				.offset((pageNumber - 1) * pageSize).fetchInto(Task.class);
	}

	@PreAuthorize(SecurityExpressionConstants.IS_ADMIN
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

	@PreAuthorize(SecurityExpressionConstants.IS_ADMIN
			+ SecurityExpressionConstants.OR + '('
			+ SecurityExpressionConstants.IS_USER
			+ SecurityExpressionConstants.AND
			+ SecurityExpressionConstants.USER_IS_OWNER
			+ SecurityExpressionConstants.AND
			+ SecurityExpressionConstants.NO_CONTEST_CURRENTLY
			+ SecurityExpressionConstants.AND
			+ SecurityExpressionConstants.TASK_PUBLISHED + ')')
	public BigDecimal getScore(final Task task, final User user) {
		return dslContext
				.select(max(Tables.SOLUTION.SCORE))
				.from(Tables.SOLUTION)
				.where(Tables.SOLUTION.TASK_ID.eq(task.getId()).and(
						Tables.SOLUTION.USER_ID.eq(user.getId()))).fetchOne()
				.value1();
	}

	@PreAuthorize(SecurityExpressionConstants.IS_ADMIN
			+ SecurityExpressionConstants.OR + '('
			+ SecurityExpressionConstants.IS_USER
			+ SecurityExpressionConstants.AND
			+ SecurityExpressionConstants.NO_CONTEST_CURRENTLY + ')')
	@PostAuthorize(SecurityExpressionConstants.IS_ADMIN
			+ SecurityExpressionConstants.OR + " #returnObject.published")
	public Task getTaskByName(final String taskName) {
		return taskDao.fetchOneByName(taskName);
	}

	@PreAuthorize(SecurityExpressionConstants.IS_ADMIN)
	@Transactional
	public void rejudgeTask(final Task task) throws ExecutionException,
			IOException {
		dslContext.delete(Tables.VERDICT).where(
				Tables.VERDICT.SOLUTION_ID.in(dslContext
						.select(Tables.SOLUTION.ID).from(Tables.SOLUTION)
						.where(Tables.SOLUTION.TASK_ID.eq(task.getId()))));
		Cursor<SolutionRecord> solutions = dslContext
				.selectFrom(Tables.SOLUTION)
				.where(Tables.SOLUTION.TASK_ID.eq(task.getId())).fetchLazy();
		while (solutions.hasNext()) {
			Solution solution = solutions.fetchOneInto(Solution.class);
			testingService.testSolutionOnAllTests(solution);
		}
	}

	@PreAuthorize(SecurityExpressionConstants.IS_ADMIN)
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

	@PreAuthorize(SecurityExpressionConstants.IS_ADMIN)
	public Task updateTask(Task task) {
		return update(task, Tables.TASK);
	}
}
