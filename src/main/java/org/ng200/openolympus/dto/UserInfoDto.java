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

import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

public class UserInfoDto {

	public static final String PHONE_REGEX = "\\+?[0-9]*";

	@Email(message = "email")
	@NotNull(message = "empty")
	private String emailAddress;

	@NotNull(message = "empty")
	@Size(min = 1, message = "length")
	private String firstNameMain;

	private String middleNameMain;

	@NotNull(message = "empty")
	@Size(min = 1, message = "length")
	private String lastNameMain;

	@Size(min = 1, message = "length")
	private String firstNameLocalised;

	private String middleNameLocalised;

	@Size(min = 1, message = "length")
	private String lastNameLocalised;

	@Size(min = 1, message = "length")
	private String teacherFirstName;

	@Size(min = 1, message = "length")
	private String teacherMiddleName;

	@Size(min = 1, message = "length")
	private String teacherLastName;

	private String addressLine1;

	private String addressLine2;

	private String addressCity;

	private String addressState;

	private String addressCountry;

	@Size(min = 4, max = 16, message = "length")
	@Pattern(regexp = UserInfoDto.PHONE_REGEX, message = "pattern")
	private String landline;

	@Size(min = 4, max = 16, message = "length")
	@Pattern(regexp = UserInfoDto.PHONE_REGEX, message = "pattern")
	private String mobile;

	private String school;

	@DateTimeFormat(iso = ISO.DATE)
	private Date dateOfBirth;

	public String getAddressCity() {
		return this.addressCity;
	}

	public String getAddressCountry() {
		return this.addressCountry;
	}

	public String getAddressLine1() {
		return this.addressLine1;
	}

	public String getAddressLine2() {
		return this.addressLine2;
	}

	public String getAddressState() {
		return this.addressState;
	}

	public Date getDateOfBirth() {
		return this.dateOfBirth;
	}

	public String getEmailAddress() {
		return this.emailAddress;
	}

	public String getFirstNameLocalised() {
		return this.firstNameLocalised;
	}

	public String getFirstNameMain() {
		return this.firstNameMain;
	}

	public String getLandline() {
		return this.landline;
	}

	public String getLastNameLocalised() {
		return this.lastNameLocalised;
	}

	public String getLastNameMain() {
		return this.lastNameMain;
	}

	public String getMiddleNameLocalised() {
		return this.middleNameLocalised;
	}

	public String getMiddleNameMain() {
		return this.middleNameMain;
	}

	public String getMobile() {
		return this.mobile;
	}

	public String getSchool() {
		return this.school;
	}

	public String getTeacherFirstName() {
		return this.teacherFirstName;
	}

	public String getTeacherLastName() {
		return this.teacherLastName;
	}

	public String getTeacherMiddleName() {
		return this.teacherMiddleName;
	}

	public void setAddressCity(final String addressCity) {
		this.addressCity = addressCity;
	}

	public void setAddressCountry(final String addressCountry) {
		this.addressCountry = addressCountry;
	}

	public void setAddressLine1(final String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	public void setAddressLine2(final String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	public void setAddressState(final String addressState) {
		this.addressState = addressState;
	}

	public void setDateOfBirth(final Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public void setFirstNameLocalised(final String firstNameLocalised) {
		this.firstNameLocalised = firstNameLocalised;
	}

	public void setFirstNameMain(final String firstNameMain) {
		this.firstNameMain = firstNameMain;
	}

	public void setLandline(final String landline) {
		this.landline = landline;
	}

	public void setLastNameLocalised(final String lastNameLocalised) {
		this.lastNameLocalised = lastNameLocalised;
	}

	public void setLastNameMain(final String lastNameMain) {
		this.lastNameMain = lastNameMain;
	}

	public void setMiddleNameLocalised(final String middleNameLocalised) {
		this.middleNameLocalised = middleNameLocalised;
	}

	public void setMiddleNameMain(final String middleNameMain) {
		this.middleNameMain = middleNameMain;
	}

	public void setMobile(final String mobile) {
		this.mobile = mobile;
	}

	public void setSchool(final String school) {
		this.school = school;
	}

	public void setTeacherFirstName(final String teacherFirstName) {
		this.teacherFirstName = teacherFirstName;
	}

	public void setTeacherLastName(final String teacherLastName) {
		this.teacherLastName = teacherLastName;
	}

	public void setTeacherMiddleName(final String teacherMiddleName) {
		this.teacherMiddleName = teacherMiddleName;
	}
}
