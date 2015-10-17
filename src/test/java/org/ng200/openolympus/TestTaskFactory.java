
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

	@Autowired
	private DSLContext dslContext;

	@Autowired
	private TaskPermissionDao taskPermissionDao;

	public class TestTaskBuilder {

		private String prefix = "testTask";
		private List<Pair<TaskPermissionType, Long>> permissions = new ArrayList<>();

		public String getPrefix() {
			return prefix;
		}

		public TestTaskBuilder setPrefix(String prefix) {
			this.prefix = prefix;
			return this;
		}

		public List<Pair<TaskPermissionType, Long>> getPermissions() {
			return permissions;
		}

		public TestTaskBuilder setPermissions(
				List<Pair<TaskPermissionType, Long>> permissions) {
			this.permissions = permissions;
			return this;
		}

		public Task build()
				throws Exception {
			TaskRecord taskRecord = new TaskRecord();
			taskRecord.attach(dslContext.configuration());
			String name = prefix + "_" + TestUtils.generateId();
			taskRecord.setName(name)
					.setCreatedDate(OffsetDateTime.now())
					.setDescriptionFile("")
					.setTaskLocation("");
			taskRecord.insert();

			taskRecord.refresh();

			taskPermissionDao.insert(permissions.stream()
					.map(perm -> new TaskPermission()
							.setPrincipalId(perm.getSecond())
							.setPermission(perm.getFirst())
							.setTaskId(taskRecord.getId()))
					.collect(Collectors.toList()));

			return taskRecord.into(Task.class);
		}

		public TestTaskBuilder permit(User user,
				TaskPermissionType perm) {
			return permit(user.getId(), perm);
		}

		public TestTaskBuilder permit(Long user,
				TaskPermissionType perm) {
			permissions.add(new Pair<TaskPermissionType, Long>(perm, user));
			return this;
		}

	}

	public TestTaskBuilder task() {
		return new TestTaskBuilder();
	}
}
