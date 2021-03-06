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


import java.time.Duration;
import java.time.OffsetDateTime;

import javax.annotation.Generated;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.ng200.openolympus.jooq.tables.interfaces.IGetRunningContest;


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
@Table(name = "get_running_contest", schema = "public")
public class GetRunningContest implements IGetRunningContest {

	private static final long serialVersionUID = -460414598;

	private Integer        id;
	private Duration       duration;
	private String         name;
	private Boolean        showFullTestsDuringContest;
	private OffsetDateTime startTime;

	public GetRunningContest() {}

	public GetRunningContest(GetRunningContest value) {
		this.id = value.id;
		this.duration = value.duration;
		this.name = value.name;
		this.showFullTestsDuringContest = value.showFullTestsDuringContest;
		this.startTime = value.startTime;
	}

	public GetRunningContest(
		Integer        id,
		Duration       duration,
		String         name,
		Boolean        showFullTestsDuringContest,
		OffsetDateTime startTime
	) {
		this.id = id;
		this.duration = duration;
		this.name = name;
		this.showFullTestsDuringContest = showFullTestsDuringContest;
		this.startTime = startTime;
	}

	@Column(name = "id", nullable = false, precision = 32)
	@Override
	public Integer getId() {
		return this.id;
	}

	@Override
	public GetRunningContest setId(Integer id) {
		this.id = id;
		return this;
	}

	@Column(name = "duration", precision = 64)
	@Override
	public Duration getDuration() {
		return this.duration;
	}

	@Override
	public GetRunningContest setDuration(Duration duration) {
		this.duration = duration;
		return this;
	}

	@Column(name = "name", length = 255)
	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public GetRunningContest setName(String name) {
		this.name = name;
		return this;
	}

	@Column(name = "show_full_tests_during_contest", nullable = false)
	@Override
	public Boolean getShowFullTestsDuringContest() {
		return this.showFullTestsDuringContest;
	}

	@Override
	public GetRunningContest setShowFullTestsDuringContest(Boolean showFullTestsDuringContest) {
		this.showFullTestsDuringContest = showFullTestsDuringContest;
		return this;
	}

	@Column(name = "start_time")
	@Override
	public OffsetDateTime getStartTime() {
		return this.startTime;
	}

	@Override
	public GetRunningContest setStartTime(OffsetDateTime startTime) {
		this.startTime = startTime;
		return this;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("GetRunningContest (");

		sb.append(id);
		sb.append(", ").append(duration);
		sb.append(", ").append(name);
		sb.append(", ").append(showFullTestsDuringContest);
		sb.append(", ").append(startTime);

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
	public void from(IGetRunningContest from) {
		setId(from.getId());
		setDuration(from.getDuration());
		setName(from.getName());
		setShowFullTestsDuringContest(from.getShowFullTestsDuringContest());
		setStartTime(from.getStartTime());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <E extends IGetRunningContest> E into(E into) {
		into.from(this);
		return into;
	}
}
