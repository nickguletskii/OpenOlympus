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

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "TimeExtensions", indexes = {
                                           @Index(columnList = "contest_id"),
                                           @Index(columnList = "user_id"),
                                           @Index(columnList = "contest_id,user_id")
})
public class ContestPerUserTimeExtension implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -3926169904429899681L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@ManyToOne(cascade = {
	                      CascadeType.MERGE,
	                      CascadeType.REFRESH
	})
	private Contest contest;

	@ManyToOne(cascade = {
	                      CascadeType.MERGE,
	                      CascadeType.REFRESH
	})
	private User user;
	private long duration;

	public ContestPerUserTimeExtension() {
		super();
	}

	public ContestPerUserTimeExtension(final Contest contest, final User user,
			final long duration) {
		super();
		this.contest = contest;
		this.user = user;
		this.duration = duration;
	}

	public Contest getContest() {
		return this.contest;
	}

	public long getDuration() {
		return this.duration;
	}

	public long getId() {
		return this.id;
	}

	public User getUser() {
		return this.user;
	}

	public void setContest(final Contest contest) {
		this.contest = contest;
	}

	public void setDuration(final long duration) {
		this.duration = duration;
	}

	public void setId(final long id) {
		this.id = id;
	}

	public void setUser(final User user) {
		this.user = user;
	}
}
