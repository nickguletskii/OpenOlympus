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

import org.ng200.openolympus.jooq.enums.ContestPermissionType;
import org.ng200.openolympus.jooq.tables.interfaces.IContestPermission;


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
@Table(name = "contest_permission", schema = "public", uniqueConstraints = {
	@UniqueConstraint(columnNames = {"permission", "contest_id", "principal_id"})
})
public class ContestPermission implements IContestPermission {

	private static final long serialVersionUID = 75891024;

	private ContestPermissionType permission;
	private Integer               contestId;
	private Long                  principalId;

	public ContestPermission() {}

	public ContestPermission(ContestPermission value) {
		this.permission = value.permission;
		this.contestId = value.contestId;
		this.principalId = value.principalId;
	}

	public ContestPermission(
		ContestPermissionType permission,
		Integer               contestId,
		Long                  principalId
	) {
		this.permission = permission;
		this.contestId = contestId;
		this.principalId = principalId;
	}

	@Column(name = "permission", nullable = false)
	@Override
	public ContestPermissionType getPermission() {
		return this.permission;
	}

	@Override
	public ContestPermission setPermission(ContestPermissionType permission) {
		this.permission = permission;
		return this;
	}

	@Column(name = "contest_id", nullable = false, precision = 32)
	@Override
	public Integer getContestId() {
		return this.contestId;
	}

	@Override
	public ContestPermission setContestId(Integer contestId) {
		this.contestId = contestId;
		return this;
	}

	@Column(name = "principal_id", nullable = false, precision = 64)
	@Override
	public Long getPrincipalId() {
		return this.principalId;
	}

	@Override
	public ContestPermission setPrincipalId(Long principalId) {
		this.principalId = principalId;
		return this;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("ContestPermission (");

		sb.append(permission);
		sb.append(", ").append(contestId);
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
	public void from(IContestPermission from) {
		setPermission(from.getPermission());
		setContestId(from.getContestId());
		setPrincipalId(from.getPrincipalId());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <E extends IContestPermission> E into(E into) {
		into.from(this);
		return into;
	}
}
