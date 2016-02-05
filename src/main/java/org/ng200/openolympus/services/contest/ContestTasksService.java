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
package org.ng200.openolympus.services.contest;

import java.util.List;

import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.ng200.openolympus.jooq.Tables;
import org.ng200.openolympus.jooq.tables.pojos.Contest;
import org.ng200.openolympus.jooq.tables.pojos.Task;
import org.ng200.openolympus.jooq.tables.records.ContestTasksRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ContestTasksService {
	@Autowired
	private DSLContext dslContext;

	@Transactional
	public void addContestTask(Contest contest, Task taskByName) {
		final ContestTasksRecord record = new ContestTasksRecord(
				contest.getId(),
				taskByName.getId());
		record.attach(this.dslContext.configuration());
		record.insert();
	}

	public List<Task> getContestTasks(Contest contest) {
		return this.dslContext
				.selectFrom(Tables.TASK)
				.where(Tables.TASK.ID.in(this.dslContext
						.select(Tables.CONTEST_TASKS.TASK_ID)
						.from(Tables.CONTEST_TASKS)
						.where(Tables.CONTEST_TASKS.CONTEST_ID.eq(contest
								.getId()))))
				.fetchInto(Task.class);
	}

	public boolean isTaskInContest(Task task, Contest contest) {
		return this.dslContext
				.select(DSL.field(DSL.exists(
						this.dslContext.select()
								.from(Tables.CONTEST_TASKS)
								.where(Tables.CONTEST_TASKS.TASK_ID
										.eq(task.getId())
										.and(Tables.CONTEST_TASKS.CONTEST_ID
												.eq(contest.getId()))))))
				.fetchOne().value1();
	}

	@Transactional
	public void removeTaskFromContest(Task task, Contest contest) {
		this.dslContext.delete(Tables.CONTEST_TASKS).where(
				Tables.CONTEST_TASKS.CONTEST_ID.eq(contest.getId())
						.and(Tables.CONTEST_TASKS.TASK_ID.eq(task.getId())))
				.execute();
	}

}