/**
 * The MIT License
 * Copyright (c) 2014 Nick Guletskii
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

import java.math.BigDecimal;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Verdicts", indexes = { @Index(columnList = "tested"),
		@Index(columnList = "solution_id") })
public class Verdict {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@ManyToOne(cascade = CascadeType.ALL)
	private Solution solution;

	private String message = "solution.result.waiting";

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

	public String getMessage() {
		return this.message;
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

	public long getUnauthorisedSyscall() {
		return this.unauthorisedSyscall;
	}

	public boolean isTested() {
		return this.tested;
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

	public void setMessage(final String message) {
		this.message = message;
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
}
