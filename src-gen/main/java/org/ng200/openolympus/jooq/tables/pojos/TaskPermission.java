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
/**
 * This class is generated by jOOQ
 */
package org.ng200.openolympus.jooq.tables.pojos;


import javax.annotation.Generated;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.ng200.openolympus.jooq.enums.TaskPermissionType;
import org.ng200.openolympus.jooq.tables.interfaces.ITaskPermission;


/**
 * This class is generated by jOOQ.
 */
@Generated(
	value = {
		"http://www.jooq.org",
		"jOOQ version:3.7.2"
	},
	comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
@Entity
@Table(name = "task_permission", schema = "public", uniqueConstraints = {
	@UniqueConstraint(columnNames = {"permission", "task_id", "principal_id"})
})
public class TaskPermission implements ITaskPermission {

	private static final long serialVersionUID = 27939196;

	private TaskPermissionType permission;
	private Integer            taskId;
	private Long               principalId;

	public TaskPermission() {}

	public TaskPermission(TaskPermission value) {
		this.permission = value.permission;
		this.taskId = value.taskId;
		this.principalId = value.principalId;
	}

	public TaskPermission(
		TaskPermissionType permission,
		Integer            taskId,
		Long               principalId
	) {
		this.permission = permission;
		this.taskId = taskId;
		this.principalId = principalId;
	}

	@Column(name = "permission", nullable = false)
	@Override
	public TaskPermissionType getPermission() {
		return this.permission;
	}

	@Override
	public TaskPermission setPermission(TaskPermissionType permission) {
		this.permission = permission;
		return this;
	}

	@Column(name = "task_id", nullable = false, precision = 32)
	@Override
	public Integer getTaskId() {
		return this.taskId;
	}

	@Override
	public TaskPermission setTaskId(Integer taskId) {
		this.taskId = taskId;
		return this;
	}

	@Column(name = "principal_id", nullable = false, precision = 64)
	@Override
	public Long getPrincipalId() {
		return this.principalId;
	}

	@Override
	public TaskPermission setPrincipalId(Long principalId) {
		this.principalId = principalId;
		return this;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("TaskPermission (");

		sb.append(permission);
		sb.append(", ").append(taskId);
		sb.append(", ").append(principalId);

		sb.append(")");
		return sb.toString();
	}

	// -------------------------------------------------------------------------
	// FROM and INTO
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void from(ITaskPermission from) {
		setPermission(from.getPermission());
		setTaskId(from.getTaskId());
		setPrincipalId(from.getPrincipalId());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <E extends ITaskPermission> E into(E into) {
		into.from(this);
		return into;
	}
}
