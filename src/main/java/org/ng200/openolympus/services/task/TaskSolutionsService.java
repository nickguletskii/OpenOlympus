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

import java.io.IOException;
import java.math.BigDecimal;
import java.util.concurrent.ExecutionException;

import org.jooq.Cursor;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.ng200.openolympus.jooq.Tables;
import org.ng200.openolympus.jooq.tables.pojos.Solution;
import org.ng200.openolympus.jooq.tables.pojos.Task;
import org.ng200.openolympus.jooq.tables.pojos.User;
import org.ng200.openolympus.jooq.tables.records.SolutionRecord;
import org.ng200.openolympus.services.TestingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TaskSolutionsService {

	@Autowired
	private TestingService testingService;

	@Autowired
	private DSLContext dslContext;

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

	@Transactional
	public void rejudgeTask(final Task task)
			throws ExecutionException, IOException {
		final Cursor<SolutionRecord> solutions = this.dslContext
				.selectFrom(Tables.SOLUTION)
				.where(Tables.SOLUTION.TASK_ID.eq(task.getId())).fetchLazy();
		while (solutions.hasNext()) {
			final Solution solution = solutions.fetchOneInto(Solution.class);
			rejudgeSolution(solution);
		}
	}

	@Transactional
	public void rejudgeSolution(final Solution solution) throws IOException {
		this.dslContext.deleteFrom(Tables.VERDICT)
				.where(Tables.VERDICT.SOLUTION_ID.eq(solution.getId()))
				.execute();
		this.testingService.testSolutionOnAllTests(solution);
	}
}
