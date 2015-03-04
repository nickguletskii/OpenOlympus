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
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OrderColumn;
import javax.persistence.Table;

import org.ng200.openolympus.IsoDateSerializer;
import org.ng200.openolympus.model.views.PriviligedView;
import org.ng200.openolympus.model.views.UnprivilegedView;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Entity
@Table(name = "Contests", indexes = {
	@Index(columnList = "startTime")
})
@EntityListeners(AuditingEntityListener.class)
public class Contest implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 2055252072663233101L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@CreatedDate
	private Date createdDate = Date.from(Instant.now());
	@LastModifiedDate
	private Date lastModifiedDate = Date.from(Instant.now());

	@CreatedBy
	@ManyToOne(fetch = FetchType.LAZY, cascade = {
			CascadeType.REFRESH,
			CascadeType.DETACH
	})
	private User createdBy;
	@LastModifiedBy
	@ManyToOne(fetch = FetchType.LAZY, cascade = {
			CascadeType.REFRESH,
			CascadeType.DETACH
	})
	private User lastModifiedBy;

	@OrderColumn
	private Date startTime;

	private Duration duration;

	@Column(unique = true)
	private String name;

	@ManyToMany(cascade = {
			CascadeType.MERGE,
			CascadeType.REFRESH,
			CascadeType.DETACH
	})
	private Set<Task> tasks;

	public Contest() {

	}

	public Contest(final Date startTime, final Duration duration,
			final String name, final Set<Task> tasks) {
		super();
		this.startTime = startTime;
		this.duration = duration;
		this.name = name;
		this.tasks = tasks;
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
		final Contest other = (Contest) obj;
		if (this.id != other.id) {
			return false;
		}
		return true;
	}

	public User getCreatedBy() {
		return this.createdBy;
	}

	public Date getCreatedDate() {
		return this.createdDate;
	}

	@JsonView(UnprivilegedView.class)
	public Duration getDuration() {
		return this.duration;
	}

	@JsonView(UnprivilegedView.class)
	public long getId() {
		return this.id;
	}

	public User getLastModifiedBy() {
		return this.lastModifiedBy;
	}

	public Date getLastModifiedDate() {
		return this.lastModifiedDate;
	}

	@JsonView(UnprivilegedView.class)
	public String getName() {
		return this.name;
	}

	@JsonSerialize(using = IsoDateSerializer.class)
	@JsonView(UnprivilegedView.class)
	public Date getStartTime() {
		return this.startTime;
	}

	@JsonView(UnprivilegedView.class)
	public Set<Task> getTasks() {
		return this.tasks;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (this.id ^ (this.id >>> 32));
		return result;
	}

	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public void setDuration(final Duration duration) {
		this.duration = duration;
	}

	public void setId(final long id) {
		this.id = id;
	}

	public void setLastModifiedBy(User lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public void setStartTime(final Date startTime) {
		this.startTime = startTime;
	}

	public void setTasks(final Set<Task> tasks) {
		this.tasks = tasks;
	}
}
