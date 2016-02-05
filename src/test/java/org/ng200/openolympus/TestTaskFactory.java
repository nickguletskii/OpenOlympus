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
package org.ng200.openolympus;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.jooq.DSLContext;
import org.ng200.openolympus.jooq.enums.TaskPermissionType;
import org.ng200.openolympus.jooq.tables.daos.TaskPermissionDao;
import org.ng200.openolympus.jooq.tables.pojos.Task;
import org.ng200.openolympus.jooq.tables.pojos.TaskPermission;
import org.ng200.openolympus.jooq.tables.pojos.User;
import org.ng200.openolympus.jooq.tables.records.TaskRecord;
import org.ng200.openolympus.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TestTaskFactory {

	public class TestTaskBuilder {

		private String prefix = "testTask";
		private List<Pair<TaskPermissionType, Long>> permissions = new ArrayList<>();

		public Task build()
				throws Exception {
			final TaskRecord taskRecord = new TaskRecord();
			taskRecord.attach(TestTaskFactory.this.dslContext.configuration());
			final String name = this.prefix + "_" + TestUtils.generateId();
			taskRecord.setName(name)
					.setCreatedDate(OffsetDateTime.now())
					.setDescriptionFile("")
					.setTaskLocation("");
			taskRecord.insert();

			taskRecord.refresh();

			TestTaskFactory.this.taskPermissionDao
					.insert(this.permissions.stream()
							.map(perm -> new TaskPermission()
									.setPrincipalId(perm.getSecond())
									.setPermission(perm.getFirst())
									.setTaskId(taskRecord.getId()))
							.collect(Collectors.toList()));

			return taskRecord.into(Task.class);
		}

		public List<Pair<TaskPermissionType, Long>> getPermissions() {
			return this.permissions;
		}

		public String getPrefix() {
			return this.prefix;
		}

		public TestTaskBuilder permit(Long user,
				TaskPermissionType perm) {
			this.permissions
					.add(new Pair<TaskPermissionType, Long>(perm, user));
			return this;
		}

		public TestTaskBuilder permit(User user,
				TaskPermissionType perm) {
			return this.permit(user.getId(), perm);
		}

		public TestTaskBuilder setPermissions(
				List<Pair<TaskPermissionType, Long>> permissions) {
			this.permissions = permissions;
			return this;
		}

		public TestTaskBuilder setPrefix(String prefix) {
			this.prefix = prefix;
			return this;
		}

	}

	@Autowired
	private DSLContext dslContext;

	@Autowired
	private TaskPermissionDao taskPermissionDao;

	public TestTaskBuilder task() {
		return new TestTaskBuilder();
	}
}
