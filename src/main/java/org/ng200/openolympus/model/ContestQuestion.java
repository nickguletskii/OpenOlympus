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
import java.time.Instant;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "ContestQuestions", indexes = {
		@Index(columnList = "contest_id"),
		@Index(columnList = "user_id"),
		@Index(columnList = "contest_id,user_id")
})
@EntityListeners(AuditingEntityListener.class)
public class ContestQuestion implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 2972281014016155410L;

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

	@ManyToOne(cascade = {
			CascadeType.MERGE,
			CascadeType.REFRESH
	})
	private User user;

	@ManyToOne(cascade = {
			CascadeType.MERGE,
			CascadeType.REFRESH
	})
	private Contest contest;

	private String question;

	private String response;

	public Contest getContest() {
		return this.contest;
	}

	public User getCreatedBy() {
		return this.createdBy;
	}

	public Date getCreatedDate() {
		return this.createdDate;
	}

	public long getId() {
		return this.id;
	}

	public User getLastModifiedBy() {
		return this.lastModifiedBy;
	}

	public Date getLastModifiedDate() {
		return this.lastModifiedDate;
	}

	public String getQuestion() {
		return this.question;
	}

	public String getResponse() {
		return this.response;
	}

	public User getUser() {
		return this.user;
	}

	public void setContest(Contest contest) {
		this.contest = contest;
	}

	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setLastModifiedBy(User lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
