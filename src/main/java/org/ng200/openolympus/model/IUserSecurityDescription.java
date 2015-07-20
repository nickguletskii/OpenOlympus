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

import java.sql.Timestamp;

import org.ng200.openolympus.SecurityClearanceType;
import org.ng200.openolympus.annotations.SecurityClearanceRequired;
import org.ng200.openolympus.security.PrincipalMatchesUser;

public interface IUserSecurityDescription {

	@SecurityClearanceRequired(minimumClearance = SecurityClearanceType.ANONYMOUS)
	public Long getId();

	@SecurityClearanceRequired(minimumClearance = SecurityClearanceType.LOGGED_IN)
	public String getUsername();

	@SecurityClearanceRequired(minimumClearance = SecurityClearanceType.APPROVED_USER, predicates = PrincipalMatchesUser.Public.class)
	public String getFirstNameMain();

	@SecurityClearanceRequired(minimumClearance = SecurityClearanceType.APPROVED_USER, predicates = PrincipalMatchesUser.Private.class)
	public String getAddressCity();

	@SecurityClearanceRequired(minimumClearance = SecurityClearanceType.APPROVED_USER, predicates = PrincipalMatchesUser.Private.class)
	public String getAddressCountry();

	@SecurityClearanceRequired(minimumClearance = SecurityClearanceType.APPROVED_USER, predicates = PrincipalMatchesUser.Private.class)
	public String getAddressLine1();

	@SecurityClearanceRequired(minimumClearance = SecurityClearanceType.APPROVED_USER, predicates = PrincipalMatchesUser.Private.class)
	public String getAddressLine2();

	@SecurityClearanceRequired(minimumClearance = SecurityClearanceType.APPROVED_USER, predicates = PrincipalMatchesUser.Private.class)
	public String getAddressState();

	@SecurityClearanceRequired(minimumClearance = SecurityClearanceType.APPROVED_USER, predicates = PrincipalMatchesUser.Private.class)
	public Boolean getApprovalEmailSent();

	@SecurityClearanceRequired(minimumClearance = SecurityClearanceType.APPROVED_USER, predicates = PrincipalMatchesUser.Private.class)
	public Timestamp getBirthDate();

	@SecurityClearanceRequired(minimumClearance = SecurityClearanceType.APPROVED_USER, predicates = PrincipalMatchesUser.Private.class)
	public String getEmailAddress();

	@SecurityClearanceRequired(minimumClearance = SecurityClearanceType.APPROVED_USER, predicates = PrincipalMatchesUser.Private.class)
	public String getEmailConfirmationToken();

	@SecurityClearanceRequired(minimumClearance = SecurityClearanceType.LOGGED_IN)
	public Boolean getEnabled();

	@SecurityClearanceRequired(minimumClearance = SecurityClearanceType.APPROVED_USER, predicates = PrincipalMatchesUser.Public.class)
	public String getFirstNameLocalised();

	@SecurityClearanceRequired(minimumClearance = SecurityClearanceType.APPROVED_USER, predicates = PrincipalMatchesUser.Private.class)
	public String getLandline();

	@SecurityClearanceRequired(minimumClearance = SecurityClearanceType.APPROVED_USER, predicates = PrincipalMatchesUser.Public.class)
	public String getLastNameLocalised();

	@SecurityClearanceRequired(minimumClearance = SecurityClearanceType.APPROVED_USER, predicates = PrincipalMatchesUser.Public.class)
	public String getLastNameMain();

	@SecurityClearanceRequired(minimumClearance = SecurityClearanceType.APPROVED_USER, predicates = PrincipalMatchesUser.Public.class)
	public String getMiddleNameLocalised();

	@SecurityClearanceRequired(minimumClearance = SecurityClearanceType.APPROVED_USER, predicates = PrincipalMatchesUser.Public.class)
	public String getMiddleNameMain();

	@SecurityClearanceRequired(minimumClearance = SecurityClearanceType.APPROVED_USER, predicates = PrincipalMatchesUser.Private.class)
	public String getMobile();

	@SecurityClearanceRequired(minimumClearance = SecurityClearanceType.INTERNAL)
	public String getPassword();

	@SecurityClearanceRequired(minimumClearance = SecurityClearanceType.APPROVED_USER, predicates = PrincipalMatchesUser.Public.class)
	public String getSchool();

	@SecurityClearanceRequired(minimumClearance = SecurityClearanceType.APPROVED_USER, predicates = PrincipalMatchesUser.Public.class)
	public String getTeacherFirstName();

	@SecurityClearanceRequired(minimumClearance = SecurityClearanceType.APPROVED_USER, predicates = PrincipalMatchesUser.Public.class)
	public String getTeacherLastName();

	@SecurityClearanceRequired(minimumClearance = SecurityClearanceType.APPROVED_USER, predicates = PrincipalMatchesUser.Public.class)
	public String getTeacherMiddleName();

}
