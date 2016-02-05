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
package org.ng200.openolympus.model;

import java.time.LocalDate;

import org.ng200.openolympus.SecurityClearanceType;
import org.ng200.openolympus.annotations.SecurityClearanceRequired;
import org.ng200.openolympus.jooq.tables.interfaces.IUser;
import org.ng200.openolympus.security.PrincipalMatchesUser;

public interface IUserSecurityDescription extends IUser {
	@Override
	@SecurityClearanceRequired(minimumClearance = SecurityClearanceType.APPROVED_USER, predicates = PrincipalMatchesUser.Private.class)
	public String getAddressCity();

	@Override
	@SecurityClearanceRequired(minimumClearance = SecurityClearanceType.APPROVED_USER, predicates = PrincipalMatchesUser.Private.class)
	public String getAddressCountry();

	@Override
	@SecurityClearanceRequired(minimumClearance = SecurityClearanceType.APPROVED_USER, predicates = PrincipalMatchesUser.Private.class)
	public String getAddressLine1();

	@Override
	@SecurityClearanceRequired(minimumClearance = SecurityClearanceType.APPROVED_USER, predicates = PrincipalMatchesUser.Private.class)
	public String getAddressLine2();

	@Override
	@SecurityClearanceRequired(minimumClearance = SecurityClearanceType.APPROVED_USER, predicates = PrincipalMatchesUser.Private.class)
	public String getAddressState();

	@Override
	@SecurityClearanceRequired(minimumClearance = SecurityClearanceType.APPROVED_USER, predicates = PrincipalMatchesUser.Private.class)
	public Boolean getApprovalEmailSent();

	@Override
	@SecurityClearanceRequired(minimumClearance = SecurityClearanceType.APPROVED_USER, predicates = PrincipalMatchesUser.Private.class)
	public LocalDate getBirthDate();

	@Override
	@SecurityClearanceRequired(minimumClearance = SecurityClearanceType.APPROVED_USER, predicates = PrincipalMatchesUser.Private.class)
	public String getEmailAddress();

	@Override
	@SecurityClearanceRequired(minimumClearance = SecurityClearanceType.APPROVED_USER, predicates = PrincipalMatchesUser.Private.class)
	public String getEmailConfirmationToken();

	@Override
	@SecurityClearanceRequired(minimumClearance = SecurityClearanceType.LOGGED_IN)
	public Boolean getEnabled();

	@Override
	@SecurityClearanceRequired(minimumClearance = SecurityClearanceType.APPROVED_USER, predicates = PrincipalMatchesUser.Public.class)
	public String getFirstNameLocalised();

	@Override
	@SecurityClearanceRequired(minimumClearance = SecurityClearanceType.APPROVED_USER, predicates = PrincipalMatchesUser.Public.class)
	public String getFirstNameMain();

	@Override
	@SecurityClearanceRequired(minimumClearance = SecurityClearanceType.ANONYMOUS)
	public Long getId();

	@Override
	@SecurityClearanceRequired(minimumClearance = SecurityClearanceType.APPROVED_USER, predicates = PrincipalMatchesUser.Private.class)
	public String getLandline();

	@Override
	@SecurityClearanceRequired(minimumClearance = SecurityClearanceType.APPROVED_USER, predicates = PrincipalMatchesUser.Public.class)
	public String getLastNameLocalised();

	@Override
	@SecurityClearanceRequired(minimumClearance = SecurityClearanceType.APPROVED_USER, predicates = PrincipalMatchesUser.Public.class)
	public String getLastNameMain();

	@Override
	@SecurityClearanceRequired(minimumClearance = SecurityClearanceType.APPROVED_USER, predicates = PrincipalMatchesUser.Public.class)
	public String getMiddleNameLocalised();

	@Override
	@SecurityClearanceRequired(minimumClearance = SecurityClearanceType.APPROVED_USER, predicates = PrincipalMatchesUser.Public.class)
	public String getMiddleNameMain();

	@Override
	@SecurityClearanceRequired(minimumClearance = SecurityClearanceType.APPROVED_USER, predicates = PrincipalMatchesUser.Private.class)
	public String getMobile();

	@Override
	@SecurityClearanceRequired(minimumClearance = SecurityClearanceType.INTERNAL)
	public String getPassword();

	@Override
	@SecurityClearanceRequired(minimumClearance = SecurityClearanceType.APPROVED_USER, predicates = PrincipalMatchesUser.Public.class)
	public String getSchool();

	@Override
	@SecurityClearanceRequired(minimumClearance = SecurityClearanceType.APPROVED_USER, predicates = PrincipalMatchesUser.Public.class)
	public String getTeacherFirstName();

	@Override
	@SecurityClearanceRequired(minimumClearance = SecurityClearanceType.APPROVED_USER, predicates = PrincipalMatchesUser.Public.class)
	public String getTeacherLastName();

	@Override
	@SecurityClearanceRequired(minimumClearance = SecurityClearanceType.APPROVED_USER, predicates = PrincipalMatchesUser.Public.class)
	public String getTeacherMiddleName();

	@Override
	@SecurityClearanceRequired(minimumClearance = SecurityClearanceType.LOGGED_IN)
	public String getUsername();

}
