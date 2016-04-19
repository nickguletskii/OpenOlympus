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
import java.time.Duration;

import javax.annotation.Generated;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.ng200.openolympus.jooq.tables.interfaces.IContestParticipation;


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
@Table(name = "contest_participation", schema = "public")
public class ContestParticipation implements IContestParticipation {

	private static final long serialVersionUID = -1095611832;

	private Long       id;
	private BigDecimal score;
	private Long       userId;
	private Integer    contestId;
	private Duration   timeExtension;

	public ContestParticipation() {}

	public ContestParticipation(ContestParticipation value) {
		this.id = value.id;
		this.score = value.score;
		this.userId = value.userId;
		this.contestId = value.contestId;
		this.timeExtension = value.timeExtension;
	}

	public ContestParticipation(
		Long       id,
		BigDecimal score,
		Long       userId,
		Integer    contestId,
		Duration   timeExtension
	) {
		this.id = id;
		this.score = score;
		this.userId = userId;
		this.contestId = contestId;
		this.timeExtension = timeExtension;
	}

	@Id
	@Column(name = "id", unique = true, nullable = false, precision = 64)
	@Override
	public Long getId() {
		return this.id;
	}

	@Override
	public ContestParticipation setId(Long id) {
		this.id = id;
		return this;
	}

	@Column(name = "score", precision = 19, scale = 2)
	@Override
	public BigDecimal getScore() {
		return this.score;
	}

	@Override
	public ContestParticipation setScore(BigDecimal score) {
		this.score = score;
		return this;
	}

	@Column(name = "user_id", nullable = false, precision = 64)
	@Override
	public Long getUserId() {
		return this.userId;
	}

	@Override
	public ContestParticipation setUserId(Long userId) {
		this.userId = userId;
		return this;
	}

	@Column(name = "contest_id", nullable = false, precision = 32)
	@Override
	public Integer getContestId() {
		return this.contestId;
	}

	@Override
	public ContestParticipation setContestId(Integer contestId) {
		this.contestId = contestId;
		return this;
	}

	@Column(name = "time_extension", nullable = false, precision = 64)
	@Override
	public Duration getTimeExtension() {
		return this.timeExtension;
	}

	@Override
	public ContestParticipation setTimeExtension(Duration timeExtension) {
		this.timeExtension = timeExtension;
		return this;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("ContestParticipation (");

		sb.append(id);
		sb.append(", ").append(score);
		sb.append(", ").append(userId);
		sb.append(", ").append(contestId);
		sb.append(", ").append(timeExtension);

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
	public void from(IContestParticipation from) {
		setId(from.getId());
		setScore(from.getScore());
		setUserId(from.getUserId());
		setContestId(from.getContestId());
		setTimeExtension(from.getTimeExtension());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <E extends IContestParticipation> E into(E into) {
		into.from(this);
		return into;
	}
}
