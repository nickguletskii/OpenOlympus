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
package org.ng200.openolympus.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "UserRoles")
public class Role implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -2149859120994737000L;

	public static final String USER = "USER";

	public static final String SYSTEM = "SYSTEM";
	
	public static final String SUPERUSER = "SUPERUSER";

	public static final Set<String> DEFAULT_ROLE_SET = Collections
			.unmodifiableSet(new HashSet<String>() {
				private static final long serialVersionUID = 1L;
				{
					this.add(Role.USER);
					this.add(Role.SUPERUSER);
					this.add(Role.SYSTEM);
				}
			});
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(unique = true)
	private String roleName;

	private String displayName;

	public Role() {

	}

	public Role(final String roleName, final String displayName) {
		super();
		this.roleName = roleName;
		this.displayName = displayName;
	}

	public String getDisplayName() {
		return this.displayName;
	}

	public long getId() {
		return this.id;
	}

	public String getRoleName() {
		return this.roleName;
	}

	public void setDisplayName(final String displayName) {
		this.displayName = displayName;
	}

	public void setId(final long id) {
		this.id = id;
	}

	public void setRoleName(final String roleName) {
		this.roleName = roleName;
	}

}
