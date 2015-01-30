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
import java.util.Date;

import org.ng200.openolympus.model.Solution;
import org.ng200.openolympus.model.Task;
import org.ng200.openolympus.model.views.UnprivilegedView;

import com.fasterxml.jackson.annotation.JsonView;

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
	private Date submissionTime;
	private Task task;

	public SolutionDto(Solution solution) {
		this.id = solution.getId();
		this.userId = solution.getUser().getId();
		this.score = solution.getScore();
		this.maximumScore = solution.getMaximumScore();
		this.setTask(solution.getTask());
		this.submissionTime = solution.getTimeAdded();
	}

	@JsonView(SolutionDto.SolutionDTOView.class)
	public long getId() {
		return this.id;
	}

	@JsonView(SolutionDto.SolutionDTOView.class)
	public BigDecimal getMaximumScore() {
		return this.maximumScore;
	}

	@JsonView(SolutionDto.SolutionDTOView.class)
	public BigDecimal getScore() {
		return this.score;
	}

	@JsonView(SolutionDto.SolutionDTOView.class)
	public Date getSubmissionTime() {
		return this.submissionTime;
	}

	@JsonView({
			SolutionDto.SolutionDTOView.class,
			UnprivilegedView.class
	})
	public Task getTask() {
		return this.task;
	}

	@JsonView(SolutionDto.SolutionDTOView.class)
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

	public void setSubmissionTime(Date timeAdded) {
		this.submissionTime = timeAdded;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

}