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
import java.math.BigDecimal;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.ng200.openolympus.cerberus.SolutionResult;

@Entity
@Table(name = "Verdicts", indexes = {
		@Index(columnList = "tested"),
		@Index(columnList = "solution_id"),
		@Index(columnList = "solution_id,score DESC")
})
public class Verdict implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 5300776182804960140L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@ManyToOne(cascade = {
			CascadeType.DETACH,
			CascadeType.MERGE,
			CascadeType.PERSIST,
			CascadeType.REFRESH
	})
	private Solution solution;

	private SolutionResult.Result status = SolutionResult.Result.WAITING;

	private BigDecimal score = BigDecimal.ZERO;

	private BigDecimal maximumScore = BigDecimal.ONE;

	private String pathToTest;
	private long cpuTime = -1;
	private long realTime = -1;
	private long memoryPeak = -1;
	private boolean isViewableWhenContestRunning;
	private long unauthorisedSyscall;
	private boolean tested = false;
	private String additionalInformation = null;

	public Verdict() {

	}

	public Verdict(final Solution solution, final BigDecimal maximumScore,
			final String pathToTest, final boolean isViewableWhenContestRunning) {
		this.solution = solution;
		this.maximumScore = maximumScore;
		this.pathToTest = pathToTest;
		this.isViewableWhenContestRunning = isViewableWhenContestRunning;
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
		final Verdict other = (Verdict) obj;
		if (this.id != other.id) {
			return false;
		}
		return true;
	}

	public String getAdditionalInformation() {
		return this.additionalInformation;
	}

	public long getCpuTime() {
		return this.cpuTime;
	}

	public long getId() {
		return this.id;
	}

	public BigDecimal getMaximumScore() {
		return this.maximumScore;
	}

	public long getMemoryPeak() {
		return this.memoryPeak;
	}

	public String getPathToTest() {
		return this.pathToTest;
	}

	public long getRealTime() {
		return this.realTime;
	}

	public BigDecimal getScore() {
		return this.score;
	}

	public Solution getSolution() {
		return this.solution;
	}

	public SolutionResult.Result getStatus() {
		return this.status;
	}

	public long getUnauthorisedSyscall() {
		return this.unauthorisedSyscall;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (this.id ^ (this.id >>> 32));
		return result;
	}

	public boolean isTested() {
		return this.tested;
	}

	public boolean isUserTest() {
		return false;
	}

	public boolean isViewableWhenContestRunning() {
		return this.isViewableWhenContestRunning;
	}

	public void setAdditionalInformation(final String additionalInformation) {
		this.additionalInformation = additionalInformation;
	}

	public void setCpuTime(final long cpuTime) {
		this.cpuTime = cpuTime;
	}

	public void setId(final long id) {
		this.id = id;
	}

	public void setMaximumScore(final BigDecimal maximumScore) {
		this.maximumScore = maximumScore;
	}

	public void setMemoryPeak(final long memoryPeak) {
		this.memoryPeak = memoryPeak;
	}

	public void setPathToTest(final String pathToTest) {
		this.pathToTest = pathToTest;
	}

	public void setRealTime(final long realTime) {
		this.realTime = realTime;
	}

	public void setScore(final BigDecimal score) {
		this.score = score;
	}

	public void setSolution(final Solution solution) {
		this.solution = solution;
	}

	public void setStatus(final SolutionResult.Result status) {
		this.status = status;
	}

	public void setTested(final boolean tested) {
		this.tested = tested;
	}

	public void setUnauthorisedSyscall(final long unauthorisedSyscall) {
		this.unauthorisedSyscall = unauthorisedSyscall;
	}

	public void setViewableWhenContestRunning(
			final boolean isViewableWhenContestRunning) {
		this.isViewableWhenContestRunning = isViewableWhenContestRunning;
	}

	@Override
	public String toString() {
		return String
				.format("Verdict [id=%s, solution=%s, status=%s, score=%s, maximumScore=%s, tested=%s]",
						this.id, this.solution, this.status, this.score,
						this.maximumScore, this.tested);
	}

}
