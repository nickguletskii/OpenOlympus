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
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import org.ng200.openolympus.dto.SolutionDTO;

import com.fasterxml.jackson.annotation.JsonView;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

@Entity
@Table(name = "Tasks", indexes = {
		@Index(columnList = "timeAdded"),
		@Index(columnList = "name")
})
public class Task implements Serializable {
	public interface PriviligedTaskView extends UnprivilegedTaskView {
	}

	private interface ServerTaskView extends PriviligedTaskView {
	}

	public interface UnprivilegedTaskView {
	};

	/**
	 *
	 */
	private static final long serialVersionUID = -8421313707464097312L;;

	private static final LoadingCache<Long, ReadWriteLock> locks = CacheBuilder
			.newBuilder().weakValues()
			.build(new CacheLoader<Long, ReadWriteLock>() {
				@Override
				public ReadWriteLock load(Long key) throws Exception {
					return new ReentrantReadWriteLock();
				}
			});;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	@Column(unique = true)
	private String name;
	private String descriptionFile;
	private Date timeAdded;
	private String taskLocation;
	private boolean published;

	public Task() {
	}

	public Task(final String name, final String descriptionFile,
			final String taskLocation, final Date timeAdded, boolean published) {
		super();
		this.name = name;
		this.descriptionFile = descriptionFile;
		this.taskLocation = taskLocation;
		this.timeAdded = timeAdded;
		this.published = published;
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
		final Task other = (Task) obj;
		if (this.id != other.id) {
			return false;
		}
		return true;
	}

	@JsonView(ServerTaskView.class)
	public String getDescriptionFile() {
		return this.descriptionFile;
	}

	@JsonView({
			UnprivilegedTaskView.class,
			SolutionDTO.SolutionDTOView.class
	})
	public long getId() {
		return this.id;
	}

	@JsonView(UnprivilegedTaskView.class)
	public String getName() {
		return this.name;
	}

	@JsonView(ServerTaskView.class)
	public String getTaskLocation() {
		return this.taskLocation;
	}

	@JsonView(UnprivilegedTaskView.class)
	public Date getTimeAdded() {
		return this.timeAdded;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (this.id ^ (this.id >>> 32));
		return result;
	}

	@JsonView(UnprivilegedTaskView.class)
	public boolean isPublished() {
		return this.published;
	}

	public Lock readLock() throws ExecutionException {
		return Task.locks.get(this.id).readLock();
	}

	public void setDescriptionFile(final String descriptionFile) {
		this.descriptionFile = descriptionFile;
	}

	public void setId(final long id) {
		this.id = id;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public void setPublished(boolean published) {
		this.published = published;
	}

	public void setTaskLocation(final String taskLocation) {
		this.taskLocation = taskLocation;
	}

	public void setTimeAdded(final Date timeAdded) {
		this.timeAdded = timeAdded;
	}

	public Lock writeLock() throws ExecutionException {
		return Task.locks.get(this.id).writeLock();
	}

}
