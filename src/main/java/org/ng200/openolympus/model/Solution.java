/**
 * The MIT License
 * Copyright (c) 2014 Nick Guletskii
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

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Solutions", indexes = { @Index(columnList = "timeAdded"),
		@Index(columnList = "user_id"), @Index(columnList = "task_id"),
		@Index(columnList = "timeAdded,user_id"),
		@Index(columnList = "timeAdded,task_id,user_id") })
public class Solution {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@ManyToOne(cascade = CascadeType.ALL)
	private Task task;

	@ManyToOne(cascade = CascadeType.ALL)
	private User user;
	private String file;
	private Date timeAdded;

	public Solution() {

	}

	public Solution(final Task task, final User user, final String file,
			final Date timeAdded) {
		super();
		this.task = task;
		this.user = user;
		this.file = file;
		this.timeAdded = timeAdded;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		final Solution other = (Solution) obj;
		if (this.file == null) {
			if (other.file != null) {
				return false;
			}
		} else if (!this.file.equals(other.file)) {
			return false;
		}
		return true;
	}

	public String getFile() {
		return this.file;
	}

	public long getId() {
		return this.id;
	}

	public Task getTask() {
		return this.task;
	}

	public Date getTimeAdded() {
		return this.timeAdded;
	}

	public User getUser() {
		return this.user;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((this.file == null) ? 0 : this.file.hashCode());
		return result;
	}

	public void setFile(final String file) {
		this.file = file;
	}

	public void setId(final long id) {
		this.id = id;
	}

	public void setTask(final Task task) {
		this.task = task;
	}

	public void setTimeAdded(final Date timeAdded) {
		this.timeAdded = timeAdded;
	}

	public void setUser(final User user) {
		this.user = user;
	}

}
