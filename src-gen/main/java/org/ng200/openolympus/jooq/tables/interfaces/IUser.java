/**
 * This class is generated by jOOQ
 */
package org.ng200.openolympus.jooq.tables.interfaces;


import java.io.Serializable;
import java.time.LocalDate;

import javax.annotation.Generated;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


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
public interface IUser extends Serializable {

	/**
	 * Setter for <code>public.USER.id</code>.
	 */
	public IUser setId(Long value);

	/**
	 * Getter for <code>public.USER.id</code>.
	 */
	@Id
	@Column(name = "id", unique = true, nullable = false, precision = 64)
	public Long getId();

	/**
	 * Setter for <code>public.USER.username</code>.
	 */
	public IUser setUsername(String value);

	/**
	 * Getter for <code>public.USER.username</code>.
	 */
	@Column(name = "username", unique = true, length = 255)
	public String getUsername();

	/**
	 * Setter for <code>public.USER.first_name_main</code>.
	 */
	public IUser setFirstNameMain(String value);

	/**
	 * Getter for <code>public.USER.first_name_main</code>.
	 */
	@Column(name = "first_name_main", length = 255)
	public String getFirstNameMain();

	/**
	 * Setter for <code>public.USER.address_city</code>.
	 */
	public IUser setAddressCity(String value);

	/**
	 * Getter for <code>public.USER.address_city</code>.
	 */
	@Column(name = "address_city", length = 255)
	public String getAddressCity();

	/**
	 * Setter for <code>public.USER.address_country</code>.
	 */
	public IUser setAddressCountry(String value);

	/**
	 * Getter for <code>public.USER.address_country</code>.
	 */
	@Column(name = "address_country", length = 255)
	public String getAddressCountry();

	/**
	 * Setter for <code>public.USER.address_line1</code>.
	 */
	public IUser setAddressLine1(String value);

	/**
	 * Getter for <code>public.USER.address_line1</code>.
	 */
	@Column(name = "address_line1")
	public String getAddressLine1();

	/**
	 * Setter for <code>public.USER.address_line2</code>.
	 */
	public IUser setAddressLine2(String value);

	/**
	 * Getter for <code>public.USER.address_line2</code>.
	 */
	@Column(name = "address_line2")
	public String getAddressLine2();

	/**
	 * Setter for <code>public.USER.address_state</code>.
	 */
	public IUser setAddressState(String value);

	/**
	 * Getter for <code>public.USER.address_state</code>.
	 */
	@Column(name = "address_state", length = 255)
	public String getAddressState();

	/**
	 * Setter for <code>public.USER.approval_email_sent</code>.
	 */
	public IUser setApprovalEmailSent(Boolean value);

	/**
	 * Getter for <code>public.USER.approval_email_sent</code>.
	 */
	@Column(name = "approval_email_sent", nullable = false)
	public Boolean getApprovalEmailSent();

	/**
	 * Setter for <code>public.USER.birth_date</code>.
	 */
	public IUser setBirthDate(LocalDate value);

	/**
	 * Getter for <code>public.USER.birth_date</code>.
	 */
	@Column(name = "birth_date")
	public LocalDate getBirthDate();

	/**
	 * Setter for <code>public.USER.email_address</code>.
	 */
	public IUser setEmailAddress(String value);

	/**
	 * Getter for <code>public.USER.email_address</code>.
	 */
	@Column(name = "email_address", length = 255)
	public String getEmailAddress();

	/**
	 * Setter for <code>public.USER.email_confirmation_token</code>.
	 */
	public IUser setEmailConfirmationToken(String value);

	/**
	 * Getter for <code>public.USER.email_confirmation_token</code>.
	 */
	@Column(name = "email_confirmation_token", length = 255)
	public String getEmailConfirmationToken();

	/**
	 * Setter for <code>public.USER.enabled</code>.
	 */
	public IUser setEnabled(Boolean value);

	/**
	 * Getter for <code>public.USER.enabled</code>.
	 */
	@Column(name = "enabled", nullable = false)
	public Boolean getEnabled();

	/**
	 * Setter for <code>public.USER.first_name_localised</code>.
	 */
	public IUser setFirstNameLocalised(String value);

	/**
	 * Getter for <code>public.USER.first_name_localised</code>.
	 */
	@Column(name = "first_name_localised", length = 255)
	public String getFirstNameLocalised();

	/**
	 * Setter for <code>public.USER.landline</code>.
	 */
	public IUser setLandline(String value);

	/**
	 * Getter for <code>public.USER.landline</code>.
	 */
	@Column(name = "landline", length = 255)
	public String getLandline();

	/**
	 * Setter for <code>public.USER.last_name_localised</code>.
	 */
	public IUser setLastNameLocalised(String value);

	/**
	 * Getter for <code>public.USER.last_name_localised</code>.
	 */
	@Column(name = "last_name_localised", length = 255)
	public String getLastNameLocalised();

	/**
	 * Setter for <code>public.USER.last_name_main</code>.
	 */
	public IUser setLastNameMain(String value);

	/**
	 * Getter for <code>public.USER.last_name_main</code>.
	 */
	@Column(name = "last_name_main", length = 255)
	public String getLastNameMain();

	/**
	 * Setter for <code>public.USER.middle_name_localised</code>.
	 */
	public IUser setMiddleNameLocalised(String value);

	/**
	 * Getter for <code>public.USER.middle_name_localised</code>.
	 */
	@Column(name = "middle_name_localised", length = 255)
	public String getMiddleNameLocalised();

	/**
	 * Setter for <code>public.USER.middle_name_main</code>.
	 */
	public IUser setMiddleNameMain(String value);

	/**
	 * Getter for <code>public.USER.middle_name_main</code>.
	 */
	@Column(name = "middle_name_main", length = 255)
	public String getMiddleNameMain();

	/**
	 * Setter for <code>public.USER.mobile</code>.
	 */
	public IUser setMobile(String value);

	/**
	 * Getter for <code>public.USER.mobile</code>.
	 */
	@Column(name = "mobile", length = 255)
	public String getMobile();

	/**
	 * Setter for <code>public.USER.password</code>.
	 */
	public IUser setPassword(String value);

	/**
	 * Getter for <code>public.USER.password</code>.
	 */
	@Column(name = "password", length = 255)
	public String getPassword();

	/**
	 * Setter for <code>public.USER.school</code>.
	 */
	public IUser setSchool(String value);

	/**
	 * Getter for <code>public.USER.school</code>.
	 */
	@Column(name = "school", length = 255)
	public String getSchool();

	/**
	 * Setter for <code>public.USER.teacher_first_name</code>.
	 */
	public IUser setTeacherFirstName(String value);

	/**
	 * Getter for <code>public.USER.teacher_first_name</code>.
	 */
	@Column(name = "teacher_first_name", length = 255)
	public String getTeacherFirstName();

	/**
	 * Setter for <code>public.USER.teacher_last_name</code>.
	 */
	public IUser setTeacherLastName(String value);

	/**
	 * Getter for <code>public.USER.teacher_last_name</code>.
	 */
	@Column(name = "teacher_last_name", length = 255)
	public String getTeacherLastName();

	/**
	 * Setter for <code>public.USER.teacher_middle_name</code>.
	 */
	public IUser setTeacherMiddleName(String value);

	/**
	 * Getter for <code>public.USER.teacher_middle_name</code>.
	 */
	@Column(name = "teacher_middle_name", length = 255)
	public String getTeacherMiddleName();

	/**
	 * Setter for <code>public.USER.superuser</code>.
	 */
	public IUser setSuperuser(Boolean value);

	/**
	 * Getter for <code>public.USER.superuser</code>.
	 */
	@Column(name = "superuser", nullable = false)
	public Boolean getSuperuser();

	/**
	 * Setter for <code>public.USER.approved</code>.
	 */
	public IUser setApproved(Boolean value);

	/**
	 * Getter for <code>public.USER.approved</code>.
	 */
	@Column(name = "approved", nullable = false)
	public Boolean getApproved();

	// -------------------------------------------------------------------------
	// FROM and INTO
	// -------------------------------------------------------------------------

	/**
	 * Load data from another generated Record/POJO implementing the common interface IUser
	 */
	public void from(org.ng200.openolympus.jooq.tables.interfaces.IUser from);

	/**
	 * Copy data into another generated Record/POJO implementing the common interface IUser
	 */
	public <E extends org.ng200.openolympus.jooq.tables.interfaces.IUser> E into(E into);
}
