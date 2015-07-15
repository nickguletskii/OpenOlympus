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
/**
 * This class is generated by jOOQ
 */
package org.ng200.openolympus.jooq.tables.interfaces;


import java.io.Serializable;

import javax.annotation.Generated;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.ng200.openolympus.jooq.enums.TaskPermissionType;


/**
 * This class is generated by jOOQ.
 */
@Generated(
	value = {
		"http://www.jooq.org",
		"jOOQ version:3.6.2"
	},
	comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
@Entity
@Table(name = "task_permission", schema = "public", uniqueConstraints = {
	@UniqueConstraint(columnNames = {"task_id", "principal_id", "permission"})
})
public interface ITaskPermission extends Serializable {

	/**
	 * Setter for <code>public.task_permission.task_id</code>.
	 */
	public ITaskPermission setTaskId(Integer value);

	/**
	 * Getter for <code>public.task_permission.task_id</code>.
	 */
	@Column(name = "task_id", nullable = false, precision = 32)
	public Integer getTaskId();

	/**
	 * Setter for <code>public.task_permission.principal_id</code>.
	 */
	public ITaskPermission setPrincipalId(Long value);

	/**
	 * Getter for <code>public.task_permission.principal_id</code>.
	 */
	@Column(name = "principal_id", nullable = false, precision = 64)
	public Long getPrincipalId();

	/**
	 * Setter for <code>public.task_permission.permission</code>.
	 */
	public ITaskPermission setPermission(TaskPermissionType value);

	/**
	 * Getter for <code>public.task_permission.permission</code>.
	 */
	@Column(name = "permission", nullable = false)
	public TaskPermissionType getPermission();

	// -------------------------------------------------------------------------
	// FROM and INTO
	// -------------------------------------------------------------------------

	/**
	 * Load data from another generated Record/POJO implementing the common interface ITaskPermission
	 */
	public void from(org.ng200.openolympus.jooq.tables.interfaces.ITaskPermission from);

	/**
	 * Copy data into another generated Record/POJO implementing the common interface ITaskPermission
	 */
	public <E extends org.ng200.openolympus.jooq.tables.interfaces.ITaskPermission> E into(E into);
}
