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
package org.ng200.openolympus.jooq.tables.interfaces;


import java.io.Serializable;

import javax.annotation.Generated;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.ng200.openolympus.jooq.enums.GroupPermissionType;


/**
 * This class is generated by jOOQ.
 */
@Generated(
	value = {
		"http://www.jooq.org",
		"jOOQ version:3.7.3"
	},
	comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
@Entity
@Table(name = "group_permission", schema = "public", uniqueConstraints = {
	@UniqueConstraint(columnNames = {"principal_id", "group_id", "permission"})
})
public interface IGroupPermission extends Serializable {

	/**
	 * Setter for <code>public.group_permission.principal_id</code>.
	 */
	public IGroupPermission setPrincipalId(Long value);

	/**
	 * Getter for <code>public.group_permission.principal_id</code>.
	 */
	@Column(name = "principal_id", nullable = false, precision = 64)
	public Long getPrincipalId();

	/**
	 * Setter for <code>public.group_permission.group_id</code>.
	 */
	public IGroupPermission setGroupId(Long value);

	/**
	 * Getter for <code>public.group_permission.group_id</code>.
	 */
	@Column(name = "group_id", nullable = false, precision = 64)
	public Long getGroupId();

	/**
	 * Setter for <code>public.group_permission.permission</code>.
	 */
	public IGroupPermission setPermission(GroupPermissionType value);

	/**
	 * Getter for <code>public.group_permission.permission</code>.
	 */
	@Column(name = "permission", nullable = false)
	public GroupPermissionType getPermission();

	// -------------------------------------------------------------------------
	// FROM and INTO
	// -------------------------------------------------------------------------

	/**
	 * Load data from another generated Record/POJO implementing the common interface IGroupPermission
	 */
	public void from(org.ng200.openolympus.jooq.tables.interfaces.IGroupPermission from);

	/**
	 * Copy data into another generated Record/POJO implementing the common interface IGroupPermission
	 */
	public <E extends org.ng200.openolympus.jooq.tables.interfaces.IGroupPermission> E into(E into);
}
