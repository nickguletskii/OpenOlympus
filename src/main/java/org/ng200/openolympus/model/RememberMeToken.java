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

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "RememberMeTokens")
public class RememberMeToken {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private String username;

	@Column(unique = true)
	private String series;

	private String tokenValue;

	private Date date;

	public RememberMeToken() {

	}

	public RememberMeToken(final String username, final String series,
			final String tokenValue, final Date date) {
		super();
		this.username = username;
		this.series = series;
		this.tokenValue = tokenValue;
		this.date = date;
	}

	public Date getDate() {
		return this.date;
	}

	public long getId() {
		return this.id;
	}

	public String getSeries() {
		return this.series;
	}

	public String getTokenValue() {
		return this.tokenValue;
	}

	public String getUsername() {
		return this.username;
	}

	public void setDate(final Date date) {
		this.date = date;
	}

	public void setId(final long id) {
		this.id = id;
	}

	public void setSeries(final String series) {
		this.series = series;
	}

	public void setTokenValue(final String tokenValue) {
		this.tokenValue = tokenValue;
	}

	public void setUsername(final String username) {
		this.username = username;
	}
}
