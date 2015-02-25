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

import java.time.Duration;
import java.time.Instant;
import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.ng200.openolympus.util.DateUtils;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

public class ContestDto {

	@NotNull(message = "empty")
	@Size(min = 2, max = 32, message = "length")
	private String name;
	@NotNull(message = "empty")
	@DateTimeFormat(iso = ISO.DATE_TIME)
	private Date startTime = DateUtils
			.roundTo30Minutes(Date.from(Instant.now()));
	@NotNull(message = "empty")
	private Duration duration;

	public Duration getDuration() {
		return this.duration;
	}

	public String getName() {
		return this.name;
	}

	public Date getStartTime() {
		return this.startTime;
	}

	public void setDuration(final Duration duration) {
		this.duration = duration;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public void setStartTime(final Date startTime) {
		this.startTime = startTime;
	}

}
