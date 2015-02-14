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

import static org.ng200.openolympus.SecurityExpressionConstants.AND;
import static org.ng200.openolympus.SecurityExpressionConstants.IS_ADMIN;
import static org.ng200.openolympus.SecurityExpressionConstants.IS_USER;
import static org.ng200.openolympus.SecurityExpressionConstants.NO_CONTEST_CURRENTLY;
import static org.ng200.openolympus.SecurityExpressionConstants.OR;
import static org.ng200.openolympus.SecurityExpressionConstants.TASK_PUBLISHED;
import static org.ng200.openolympus.SecurityExpressionConstants.USER_IS_OWNER;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.locks.Lock;

import javax.persistence.EntityManager;

import org.hibernate.Query;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.StatelessSession;
import org.ng200.openolympus.cerberus.util.Lists;
import org.ng200.openolympus.model.Solution;
import org.ng200.openolympus.model.Task;
import org.ng200.openolympus.model.User;
import org.ng200.openolympus.repositories.SolutionRepository;
import org.ng200.openolympus.repositories.TaskRepository;
import org.ng200.openolympus.repositories.VerdictRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TaskService {

	/**
	 * Number of tasks with names containing a string that should be returned
	 */
	private static final int LIMIT_TASKS_WITH_NAME_CONTAINING = 30;
	@Autowired
	private TaskRepository taskRepository;
	@Autowired
	private SolutionRepository solutionRepository;

	@Autowired
	private VerdictRepository verdictRepository;

	@Autowired
	private TestingService testingService;
	@Autowired
	private EntityManager entityManager;

	@Autowired
	private SecurityService securityService;

	@PreAuthorize(IS_ADMIN + OR + '(' + IS_USER + AND + NO_CONTEST_CURRENTLY
			+ ')')
	public Long countTasks() {
		return this.taskRepository.count();
	}

	@PreAuthorize(IS_ADMIN)
	public List<Task> findAFewTasksWithNameContaining(final String name) {
		return this.taskRepository.findByNameContaining(name, new PageRequest(
				0, TaskService.LIMIT_TASKS_WITH_NAME_CONTAINING));
	}

	@PreAuthorize(IS_ADMIN)
	public List<Task> findTasksNewestFirst(final int pageNumber,
			final int pageSize) {
		final PageRequest request = new PageRequest(pageNumber - 1, pageSize,
				Sort.Direction.DESC, "timeAdded");
		return this.taskRepository.findAll(request).getContent();
	}

	@PreAuthorize(IS_ADMIN + OR + '(' + IS_USER + AND + NO_CONTEST_CURRENTLY
			+ ')')
	public List<Task> findTasksNewestFirstAndAuthorized(Integer pageNumber,
			int pageSize, Principal principal) {
		if (this.securityService.isSuperuser(principal)) {
			return this.findTasksNewestFirst(pageNumber, pageSize);
		}

		final PageRequest request = new PageRequest(pageNumber - 1, pageSize,
				Sort.Direction.DESC, "timeAdded");
		return this.taskRepository.findByPublished(true, request).getContent();
	}

	@PreAuthorize(IS_ADMIN + OR + '(' + IS_USER + AND + NO_CONTEST_CURRENTLY
			+ AND + TASK_PUBLISHED + ')')
	public BigDecimal getMaximumScore(final Task task) {
		return Lists.first(
				this.taskRepository.getTaskMaximumScore(task.getId())).orElse(
				BigDecimal.ZERO);
	}

	@PreAuthorize(IS_ADMIN + OR + '(' + IS_USER + AND + USER_IS_OWNER + AND
			+ NO_CONTEST_CURRENTLY + AND + TASK_PUBLISHED + ')')
	public BigDecimal getScore(final Task task, final User user) {
		return Lists.first(
				this.taskRepository.getTaskScore(task.getId(), user.getId()))
				.orElse(BigDecimal.ZERO);
	}

	@PreAuthorize(IS_ADMIN + OR + '(' + IS_USER + AND + USER_IS_OWNER + AND
			+ NO_CONTEST_CURRENTLY + ')')
	@PostAuthorize(IS_ADMIN + OR + " #returnObject.published")
	public Task getTaskByName(final String taskName) {
		return this.taskRepository.findByName(taskName);
	}

	@PreAuthorize(IS_ADMIN)
	@Transactional
	public void rejudgeTask(final Task task) throws ExecutionException {

		final Lock lock = task.writeLock();
		lock.lock();

		try {

			this.verdictRepository.flush();
			this.solutionRepository.flush();

			this.verdictRepository.deleteBySolutionTask(task);
			this.verdictRepository.flush();

			final StatelessSession session = ((Session) this.entityManager
					.getDelegate()).getSessionFactory().openStatelessSession();
			try {
				final Query query = session
						.createQuery("from Solution where task_id=:taskID");
				query.setParameter("taskID", task.getId());
				query.setFetchSize(Integer.valueOf(1000));
				query.setReadOnly(false);
				final ScrollableResults results = query
						.scroll(ScrollMode.FORWARD_ONLY);
				try {
					while (results.next()) {
						final Solution solution = (Solution) results.get(0);
						this.testingService.testSolutionOnAllTests(solution);
					}
				} finally {
					results.close();
				}
			} finally {
				session.close();
			}
		} finally {
			lock.unlock();
		}
	}

	@PreAuthorize(IS_ADMIN)
	public Task saveTask(Task task) {
		return task = this.taskRepository.save(task);
	}
}
