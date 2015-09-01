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

import java.math.BigDecimal;
import java.math.BigInteger;

import javax.persistence.Column;

import org.ng200.openolympus.jooq.tables.pojos.User;

public class UserRanking extends User {

	/**
	 *
	 */
	private static final long serialVersionUID = -1305111374276439473L;

	private BigDecimal score;

	private BigInteger rank;

	public UserRanking() {
	}

	@Column(name="rank")
	public BigInteger getRank() {
		return this.rank;
	}

	@Column(name="score")
	public BigDecimal getScore() {
		return this.score;
	}
	
	@Column(name="rank")
	public UserRanking setRank(BigInteger rank) {
		this.rank = rank;
		return this;
	}

	@Column(name="score")
	public UserRanking setScore(BigDecimal score) {
		this.score = score;
		return this;
	}

	@Override
	public String toString() {
		return String.format(
				"UserRanking [score=%s, rank=%s, getId()=%s, getUsername()=%s, getFirstNameMain()=%s, getAddressCity()=%s, getAddressCountry()=%s, getAddressLine1()=%s, getAddressLine2()=%s, getAddressState()=%s, getApprovalEmailSent()=%s, getBirthDate()=%s, getEmailAddress()=%s, getEmailConfirmationToken()=%s, getEnabled()=%s, getFirstNameLocalised()=%s, getLandline()=%s, getLastNameLocalised()=%s, getLastNameMain()=%s, getMiddleNameLocalised()=%s, getMiddleNameMain()=%s, getMobile()=%s, getPassword()=%s, getSchool()=%s, getTeacherFirstName()=%s, getTeacherLastName()=%s, getTeacherMiddleName()=%s, getSuperuser()=%s, getApproved()=%s]",
				score, rank, getId(), getUsername(), getFirstNameMain(),
				getAddressCity(), getAddressCountry(), getAddressLine1(),
				getAddressLine2(), getAddressState(), getApprovalEmailSent(),
				getBirthDate(), getEmailAddress(), getEmailConfirmationToken(),
				getEnabled(), getFirstNameLocalised(), getLandline(),
				getLastNameLocalised(), getLastNameMain(),
				getMiddleNameLocalised(), getMiddleNameMain(), getMobile(),
				getPassword(), getSchool(), getTeacherFirstName(),
				getTeacherLastName(), getTeacherMiddleName(), getSuperuser(),
				getApproved());
	}

}
