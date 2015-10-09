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
package org.ng200.openolympus.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Date;

import org.ng200.openolympus.jooq.tables.pojos.Solution;
import org.ng200.openolympus.jooq.tables.pojos.Task;
import org.ng200.openolympus.model.views.UnprivilegedView;

@SuppressWarnings("unused")
public class SolutionDto implements Serializable {

	public static interface SolutionDTOView extends UnprivilegedView {
	}

	/**
	 *
	 */
	private static final long serialVersionUID = 6222576840807621528L;

	private long id;
	private long userId;

	private BigDecimal score;
	private BigDecimal maximumScore;
	private OffsetDateTime submissionTime;
	private Task task;

	public SolutionDto(Solution solution, Task task) {
		this.id = solution.getId();
		this.userId = solution.getUserId();
		this.score = solution.getScore();
		this.maximumScore = solution.getMaximumScore();
		this.setTask(task);
		this.submissionTime = solution.getTimeAdded();
	}

	public long getId() {
		return this.id;
	}

	public BigDecimal getMaximumScore() {
		return this.maximumScore;
	}

	public BigDecimal getScore() {
		return this.score;
	}

	public OffsetDateTime getSubmissionTime() {
		return this.submissionTime;
	}

	public Task getTask() {
		return this.task;
	}

	public long getUserId() {
		return this.userId;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setMaximumScore(BigDecimal maximumScore) {
		this.maximumScore = maximumScore;
	}

	public void setScore(BigDecimal score) {
		this.score = score;
	}

	public void setSubmissionTime(OffsetDateTime timeAdded) {
		this.submissionTime = timeAdded;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

}