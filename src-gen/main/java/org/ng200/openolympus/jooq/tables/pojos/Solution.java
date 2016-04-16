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


import java.math.BigDecimal;
import java.time.OffsetDateTime;

import javax.annotation.Generated;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.ng200.openolympus.jooq.tables.interfaces.ISolution;
import org.ng200.openolympus.model.ISolutionSecurityDescription;


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
@Table(name = "solution", schema = "public")
public class Solution implements ISolutionSecurityDescription, ISolution {

	private static final long serialVersionUID = 776236637;

	private Long           id;
	private String         file;
	private BigDecimal     maximumScore;
	private BigDecimal     score;
	private Boolean        tested;
	private OffsetDateTime timeAdded;
	private Long           userId;
	private Integer        taskId;

	public Solution() {}

	public Solution(Solution value) {
		this.id = value.id;
		this.file = value.file;
		this.maximumScore = value.maximumScore;
		this.score = value.score;
		this.tested = value.tested;
		this.timeAdded = value.timeAdded;
		this.userId = value.userId;
		this.taskId = value.taskId;
	}

	public Solution(
		Long           id,
		String         file,
		BigDecimal     maximumScore,
		BigDecimal     score,
		Boolean        tested,
		OffsetDateTime timeAdded,
		Long           userId,
		Integer        taskId
	) {
		this.id = id;
		this.file = file;
		this.maximumScore = maximumScore;
		this.score = score;
		this.tested = tested;
		this.timeAdded = timeAdded;
		this.userId = userId;
		this.taskId = taskId;
	}

	@Id
	@Column(name = "id", unique = true, nullable = false, precision = 64)
	@Override
	public Long getId() {
		return this.id;
	}

	@Override
	public Solution setId(Long id) {
		this.id = id;
		return this;
	}

	@Column(name = "file", length = 255)
	@Override
	public String getFile() {
		return this.file;
	}

	@Override
	public Solution setFile(String file) {
		this.file = file;
		return this;
	}

	@Column(name = "maximum_score", precision = 19, scale = 2)
	@Override
	public BigDecimal getMaximumScore() {
		return this.maximumScore;
	}

	@Override
	public Solution setMaximumScore(BigDecimal maximumScore) {
		this.maximumScore = maximumScore;
		return this;
	}

	@Column(name = "score", precision = 19, scale = 2)
	@Override
	public BigDecimal getScore() {
		return this.score;
	}

	@Override
	public Solution setScore(BigDecimal score) {
		this.score = score;
		return this;
	}

	@Column(name = "tested", nullable = false)
	@Override
	public Boolean getTested() {
		return this.tested;
	}

	@Override
	public Solution setTested(Boolean tested) {
		this.tested = tested;
		return this;
	}

	@Column(name = "time_added")
	@Override
	public OffsetDateTime getTimeAdded() {
		return this.timeAdded;
	}

	@Override
	public Solution setTimeAdded(OffsetDateTime timeAdded) {
		this.timeAdded = timeAdded;
		return this;
	}

	@Column(name = "user_id", precision = 64)
	@Override
	public Long getUserId() {
		return this.userId;
	}

	@Override
	public Solution setUserId(Long userId) {
		this.userId = userId;
		return this;
	}

	@Column(name = "task_id", nullable = false, precision = 32)
	@Override
	public Integer getTaskId() {
		return this.taskId;
	}

	@Override
	public Solution setTaskId(Integer taskId) {
		this.taskId = taskId;
		return this;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("Solution (");

		sb.append(id);
		sb.append(", ").append(file);
		sb.append(", ").append(maximumScore);
		sb.append(", ").append(score);
		sb.append(", ").append(tested);
		sb.append(", ").append(timeAdded);
		sb.append(", ").append(userId);
		sb.append(", ").append(taskId);

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
	public void from(ISolution from) {
		setId(from.getId());
		setFile(from.getFile());
		setMaximumScore(from.getMaximumScore());
		setScore(from.getScore());
		setTested(from.getTested());
		setTimeAdded(from.getTimeAdded());
		setUserId(from.getUserId());
		setTaskId(from.getTaskId());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <E extends ISolution> E into(E into) {
		into.from(this);
		return into;
	}
}
