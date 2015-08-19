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
import java.sql.Timestamp;

import org.ng200.openolympus.jooq.tables.pojos.User;
import org.ng200.openolympus.util.Beans;

public class UserRanking extends User {

	/**
	 *
	 */
	private static final long serialVersionUID = -1305111374276439473L;

	private BigDecimal score;

	private BigInteger rank;

	public UserRanking() {
	}

	public UserRanking(BigInteger rank, User user, BigDecimal score) {
		this.rank = rank;
		this.score = score;

		Beans.copy(user, this);
	}

	public UserRanking(Long id, String username, String firstNameMain,
			String addressCity, String addressCountry, String addressLine1,
			String addressLine2, String addressState,
			Boolean approvalEmailSent, Timestamp birthDate,
			String emailAddress, String emailConfirmationToken,
			Boolean enabled, String firstNameLocalised, String landline,
			String lastNameLocalised, String lastNameMain,
			String middleNameLocalised, String middleNameMain, String mobile,
			String password, String school, String teacherFirstName,
			String teacherLastName, String teacherMiddleName,
			Boolean superuser, Boolean approved, BigDecimal score,
			BigInteger rank) {
		super(id, username, firstNameMain, addressCity, addressCountry,
				addressLine1, addressLine2, addressState, approvalEmailSent,
				birthDate, emailAddress, emailConfirmationToken, enabled,
				firstNameLocalised, landline, lastNameLocalised, lastNameMain,
				middleNameLocalised, middleNameMain, mobile, password, school,
				teacherFirstName, teacherLastName, teacherMiddleName,
				superuser, approved);
		this.score = score;
		this.rank = rank;
	}

	public BigInteger getRank() {
		return this.rank;
	}

	public BigDecimal getScore() {
		return this.score;
	}

	public void setRank(BigInteger rank) {
		this.rank = rank;
	}

	public void setScore(BigDecimal score) {
		this.score = score;
	}

}
