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

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jooq.DSLContext;
import org.ng200.openolympus.jooq.Routines;
import org.ng200.openolympus.jooq.Tables;
import org.ng200.openolympus.jooq.enums.TaskPermissionType;
import org.ng200.openolympus.jooq.tables.pojos.Task;
import org.ng200.openolympus.jooq.tables.pojos.User;
import org.ng200.openolympus.jooq.tables.records.TaskPermissionRecord;
import org.ng200.openolympus.model.OlympusPrincipal;
import org.ng200.openolympus.services.AclService;
import org.ng200.openolympus.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TaskACLService {

	@Autowired
	private DSLContext dslContext;

	@Autowired
	private AclService aclService;

	public boolean canModifyTask(Task task, User user) {
		return user.getSuperuser()
				|| Routines.hasTaskPermission(this.dslContext.configuration(),
						task.getId(), user.getId(), TaskPermissionType.modify);
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
}