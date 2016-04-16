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
/**
 * This class is generated by jOOQ
 */
package org.ng200.openolympus.jooq.tables.records;


import java.time.LocalDate;

import javax.annotation.Generated;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.jooq.Record1;
import org.jooq.impl.UpdatableRecordImpl;
import org.ng200.openolympus.jooq.tables.User;
import org.ng200.openolympus.jooq.tables.interfaces.IUser;


/**
 * This class is generated by jOOQ.
 */
@Generated(
	value = {
		"http://www.jooq.org",
		"jOOQ version:3.7.2"
	},
	comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
@Entity
@Table(name = "USER", schema = "public")
public class UserRecord extends UpdatableRecordImpl<UserRecord> implements IUser {

	private static final long serialVersionUID = 589324366;

	/**
	 * Setter for <code>public.USER.id</code>.
	 */
	@Override
	public UserRecord setId(Long value) {
		setValue(0, value);
		return this;
	}

	/**
	 * Getter for <code>public.USER.id</code>.
	 */
	@Id
	@Column(name = "id", unique = true, nullable = false, precision = 64)
	@Override
	public Long getId() {
		return (Long) getValue(0);
	}

	/**
	 * Setter for <code>public.USER.username</code>.
	 */
	@Override
	public UserRecord setUsername(String value) {
		setValue(1, value);
		return this;
	}

	/**
	 * Getter for <code>public.USER.username</code>.
	 */
	@Column(name = "username", unique = true, length = 255)
	@Override
	public String getUsername() {
		return (String) getValue(1);
	}

	/**
	 * Setter for <code>public.USER.first_name_main</code>.
	 */
	@Override
	public UserRecord setFirstNameMain(String value) {
		setValue(2, value);
		return this;
	}

	/**
	 * Getter for <code>public.USER.first_name_main</code>.
	 */
	@Column(name = "first_name_main", length = 255)
	@Override
	public String getFirstNameMain() {
		return (String) getValue(2);
	}

	/**
	 * Setter for <code>public.USER.address_city</code>.
	 */
	@Override
	public UserRecord setAddressCity(String value) {
		setValue(3, value);
		return this;
	}

	/**
	 * Getter for <code>public.USER.address_city</code>.
	 */
	@Column(name = "address_city", length = 255)
	@Override
	public String getAddressCity() {
		return (String) getValue(3);
	}

	/**
	 * Setter for <code>public.USER.address_country</code>.
	 */
	@Override
	public UserRecord setAddressCountry(String value) {
		setValue(4, value);
		return this;
	}

	/**
	 * Getter for <code>public.USER.address_country</code>.
	 */
	@Column(name = "address_country", length = 255)
	@Override
	public String getAddressCountry() {
		return (String) getValue(4);
	}

	/**
	 * Setter for <code>public.USER.address_line1</code>.
	 */
	@Override
	public UserRecord setAddressLine1(String value) {
		setValue(5, value);
		return this;
	}

	/**
	 * Getter for <code>public.USER.address_line1</code>.
	 */
	@Column(name = "address_line1")
	@Override
	public String getAddressLine1() {
		return (String) getValue(5);
	}

	/**
	 * Setter for <code>public.USER.address_line2</code>.
	 */
	@Override
	public UserRecord setAddressLine2(String value) {
		setValue(6, value);
		return this;
	}

	/**
	 * Getter for <code>public.USER.address_line2</code>.
	 */
	@Column(name = "address_line2")
	@Override
	public String getAddressLine2() {
		return (String) getValue(6);
	}

	/**
	 * Setter for <code>public.USER.address_state</code>.
	 */
	@Override
	public UserRecord setAddressState(String value) {
		setValue(7, value);
		return this;
	}

	/**
	 * Getter for <code>public.USER.address_state</code>.
	 */
	@Column(name = "address_state", length = 255)
	@Override
	public String getAddressState() {
		return (String) getValue(7);
	}

	/**
	 * Setter for <code>public.USER.approval_email_sent</code>.
	 */
	@Override
	public UserRecord setApprovalEmailSent(Boolean value) {
		setValue(8, value);
		return this;
	}

	/**
	 * Getter for <code>public.USER.approval_email_sent</code>.
	 */
	@Column(name = "approval_email_sent", nullable = false)
	@Override
	public Boolean getApprovalEmailSent() {
		return (Boolean) getValue(8);
	}

	/**
	 * Setter for <code>public.USER.birth_date</code>.
	 */
	@Override
	public UserRecord setBirthDate(LocalDate value) {
		setValue(9, value);
		return this;
	}

	/**
	 * Getter for <code>public.USER.birth_date</code>.
	 */
	@Column(name = "birth_date")
	@Override
	public LocalDate getBirthDate() {
		return (LocalDate) getValue(9);
	}

	/**
	 * Setter for <code>public.USER.email_address</code>.
	 */
	@Override
	public UserRecord setEmailAddress(String value) {
		setValue(10, value);
		return this;
	}

	/**
	 * Getter for <code>public.USER.email_address</code>.
	 */
	@Column(name = "email_address", length = 255)
	@Override
	public String getEmailAddress() {
		return (String) getValue(10);
	}

	/**
	 * Setter for <code>public.USER.email_confirmation_token</code>.
	 */
	@Override
	public UserRecord setEmailConfirmationToken(String value) {
		setValue(11, value);
		return this;
	}

	/**
	 * Getter for <code>public.USER.email_confirmation_token</code>.
	 */
	@Column(name = "email_confirmation_token", length = 255)
	@Override
	public String getEmailConfirmationToken() {
		return (String) getValue(11);
	}

	/**
	 * Setter for <code>public.USER.enabled</code>.
	 */
	@Override
	public UserRecord setEnabled(Boolean value) {
		setValue(12, value);
		return this;
	}

	/**
	 * Getter for <code>public.USER.enabled</code>.
	 */
	@Column(name = "enabled", nullable = false)
	@Override
	public Boolean getEnabled() {
		return (Boolean) getValue(12);
	}

	/**
	 * Setter for <code>public.USER.first_name_localised</code>.
	 */
	@Override
	public UserRecord setFirstNameLocalised(String value) {
		setValue(13, value);
		return this;
	}

	/**
	 * Getter for <code>public.USER.first_name_localised</code>.
	 */
	@Column(name = "first_name_localised", length = 255)
	@Override
	public String getFirstNameLocalised() {
		return (String) getValue(13);
	}

	/**
	 * Setter for <code>public.USER.landline</code>.
	 */
	@Override
	public UserRecord setLandline(String value) {
		setValue(14, value);
		return this;
	}

	/**
	 * Getter for <code>public.USER.landline</code>.
	 */
	@Column(name = "landline", length = 255)
	@Override
	public String getLandline() {
		return (String) getValue(14);
	}

	/**
	 * Setter for <code>public.USER.last_name_localised</code>.
	 */
	@Override
	public UserRecord setLastNameLocalised(String value) {
		setValue(15, value);
		return this;
	}

	/**
	 * Getter for <code>public.USER.last_name_localised</code>.
	 */
	@Column(name = "last_name_localised", length = 255)
	@Override
	public String getLastNameLocalised() {
		return (String) getValue(15);
	}

	/**
	 * Setter for <code>public.USER.last_name_main</code>.
	 */
	@Override
	public UserRecord setLastNameMain(String value) {
		setValue(16, value);
		return this;
	}

	/**
	 * Getter for <code>public.USER.last_name_main</code>.
	 */
	@Column(name = "last_name_main", length = 255)
	@Override
	public String getLastNameMain() {
		return (String) getValue(16);
	}

	/**
	 * Setter for <code>public.USER.middle_name_localised</code>.
	 */
	@Override
	public UserRecord setMiddleNameLocalised(String value) {
		setValue(17, value);
		return this;
	}

	/**
	 * Getter for <code>public.USER.middle_name_localised</code>.
	 */
	@Column(name = "middle_name_localised", length = 255)
	@Override
	public String getMiddleNameLocalised() {
		return (String) getValue(17);
	}

	/**
	 * Setter for <code>public.USER.middle_name_main</code>.
	 */
	@Override
	public UserRecord setMiddleNameMain(String value) {
		setValue(18, value);
		return this;
	}

	/**
	 * Getter for <code>public.USER.middle_name_main</code>.
	 */
	@Column(name = "middle_name_main", length = 255)
	@Override
	public String getMiddleNameMain() {
		return (String) getValue(18);
	}

	/**
	 * Setter for <code>public.USER.mobile</code>.
	 */
	@Override
	public UserRecord setMobile(String value) {
		setValue(19, value);
		return this;
	}

	/**
	 * Getter for <code>public.USER.mobile</code>.
	 */
	@Column(name = "mobile", length = 255)
	@Override
	public String getMobile() {
		return (String) getValue(19);
	}

	/**
	 * Setter for <code>public.USER.password</code>.
	 */
	@Override
	public UserRecord setPassword(String value) {
		setValue(20, value);
		return this;
	}

	/**
	 * Getter for <code>public.USER.password</code>.
	 */
	@Column(name = "password", length = 255)
	@Override
	public String getPassword() {
		return (String) getValue(20);
	}

	/**
	 * Setter for <code>public.USER.school</code>.
	 */
	@Override
	public UserRecord setSchool(String value) {
		setValue(21, value);
		return this;
	}

	/**
	 * Getter for <code>public.USER.school</code>.
	 */
	@Column(name = "school", length = 255)
	@Override
	public String getSchool() {
		return (String) getValue(21);
	}

	/**
	 * Setter for <code>public.USER.teacher_first_name</code>.
	 */
	@Override
	public UserRecord setTeacherFirstName(String value) {
		setValue(22, value);
		return this;
	}

	/**
	 * Getter for <code>public.USER.teacher_first_name</code>.
	 */
	@Column(name = "teacher_first_name", length = 255)
	@Override
	public String getTeacherFirstName() {
		return (String) getValue(22);
	}

	/**
	 * Setter for <code>public.USER.teacher_last_name</code>.
	 */
	@Override
	public UserRecord setTeacherLastName(String value) {
		setValue(23, value);
		return this;
	}

	/**
	 * Getter for <code>public.USER.teacher_last_name</code>.
	 */
	@Column(name = "teacher_last_name", length = 255)
	@Override
	public String getTeacherLastName() {
		return (String) getValue(23);
	}

	/**
	 * Setter for <code>public.USER.teacher_middle_name</code>.
	 */
	@Override
	public UserRecord setTeacherMiddleName(String value) {
		setValue(24, value);
		return this;
	}

	/**
	 * Getter for <code>public.USER.teacher_middle_name</code>.
	 */
	@Column(name = "teacher_middle_name", length = 255)
	@Override
	public String getTeacherMiddleName() {
		return (String) getValue(24);
	}

	/**
	 * Setter for <code>public.USER.superuser</code>.
	 */
	@Override
	public UserRecord setSuperuser(Boolean value) {
		setValue(25, value);
		return this;
	}

	/**
	 * Getter for <code>public.USER.superuser</code>.
	 */
	@Column(name = "superuser", nullable = false)
	@Override
	public Boolean getSuperuser() {
		return (Boolean) getValue(25);
	}

	/**
	 * Setter for <code>public.USER.approved</code>.
	 */
	@Override
	public UserRecord setApproved(Boolean value) {
		setValue(26, value);
		return this;
	}

	/**
	 * Getter for <code>public.USER.approved</code>.
	 */
	@Column(name = "approved", nullable = false)
	@Override
	public Boolean getApproved() {
		return (Boolean) getValue(26);
	}

	// -------------------------------------------------------------------------
	// Primary key information
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Record1<Long> key() {
		return (Record1) super.key();
	}

	// -------------------------------------------------------------------------
	// FROM and INTO
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void from(IUser from) {
		setId(from.getId());
		setUsername(from.getUsername());
		setFirstNameMain(from.getFirstNameMain());
		setAddressCity(from.getAddressCity());
		setAddressCountry(from.getAddressCountry());
		setAddressLine1(from.getAddressLine1());
		setAddressLine2(from.getAddressLine2());
		setAddressState(from.getAddressState());
		setApprovalEmailSent(from.getApprovalEmailSent());
		setBirthDate(from.getBirthDate());
		setEmailAddress(from.getEmailAddress());
		setEmailConfirmationToken(from.getEmailConfirmationToken());
		setEnabled(from.getEnabled());
		setFirstNameLocalised(from.getFirstNameLocalised());
		setLandline(from.getLandline());
		setLastNameLocalised(from.getLastNameLocalised());
		setLastNameMain(from.getLastNameMain());
		setMiddleNameLocalised(from.getMiddleNameLocalised());
		setMiddleNameMain(from.getMiddleNameMain());
		setMobile(from.getMobile());
		setPassword(from.getPassword());
		setSchool(from.getSchool());
		setTeacherFirstName(from.getTeacherFirstName());
		setTeacherLastName(from.getTeacherLastName());
		setTeacherMiddleName(from.getTeacherMiddleName());
		setSuperuser(from.getSuperuser());
		setApproved(from.getApproved());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <E extends IUser> E into(E into) {
		into.from(this);
		return into;
	}

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * Create a detached UserRecord
	 */
	public UserRecord() {
		super(User.USER);
	}

	/**
	 * Create a detached, initialised UserRecord
	 */
	public UserRecord(Long id, String username, String firstNameMain, String addressCity, String addressCountry, String addressLine1, String addressLine2, String addressState, Boolean approvalEmailSent, LocalDate birthDate, String emailAddress, String emailConfirmationToken, Boolean enabled, String firstNameLocalised, String landline, String lastNameLocalised, String lastNameMain, String middleNameLocalised, String middleNameMain, String mobile, String password, String school, String teacherFirstName, String teacherLastName, String teacherMiddleName, Boolean superuser, Boolean approved) {
		super(User.USER);

		setValue(0, id);
		setValue(1, username);
		setValue(2, firstNameMain);
		setValue(3, addressCity);
		setValue(4, addressCountry);
		setValue(5, addressLine1);
		setValue(6, addressLine2);
		setValue(7, addressState);
		setValue(8, approvalEmailSent);
		setValue(9, birthDate);
		setValue(10, emailAddress);
		setValue(11, emailConfirmationToken);
		setValue(12, enabled);
		setValue(13, firstNameLocalised);
		setValue(14, landline);
		setValue(15, lastNameLocalised);
		setValue(16, lastNameMain);
		setValue(17, middleNameLocalised);
		setValue(18, middleNameMain);
		setValue(19, mobile);
		setValue(20, password);
		setValue(21, school);
		setValue(22, teacherFirstName);
		setValue(23, teacherLastName);
		setValue(24, teacherMiddleName);
		setValue(25, superuser);
		setValue(26, approved);
	}
}
