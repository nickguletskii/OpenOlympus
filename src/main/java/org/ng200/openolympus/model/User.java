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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonView;

@Entity
@Table(name = "Users", indexes = {
                                  @Index(columnList = "username")
})
public class User implements UserDetails, Serializable {
	public interface PriviligedUserView extends UnprivilegedUserView {
	};

	private interface ServerUserView extends PriviligedUserView {
	};

	public interface UnprivilegedUserView {
	};

	/**
	 *
	 */
	private static final long serialVersionUID = -5794146256232400018L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(unique = true)
	private String username;

	private String password;

	private String emailAddress;

	private String firstNameMain;

	private String middleNameMain;

	private String lastNameMain;

	private String firstNameLocalised;

	private String middleNameLocalised;

	private String lastNameLocalised;

	private String teacherFirstName;

	private String teacherMiddleName;

	private String teacherLastName;

	private String addressLine1;

	private String addressLine2;

	private String addressCity;

	private String addressState;

	private String addressCountry;

	private String landline;

	private String mobile;

	private String school;

	private Date birthDate;

	private String emailConfirmationToken;

	@ManyToMany(fetch = FetchType.EAGER)
	private Set<Role> roles;

	private final boolean enabled = true;

	protected User() {
	}

	public User(final String username, final String password,
			String emailAddress, final String firstNameMain,
			final String middleNameMain, final String lastNameMain,
			final String firstNameLocalised, final String middleNameLocalised,
			final String lastNameLocalised, final String teacherFirstName,
			final String teacherMiddleName, final String teacherLastName,
			final String addressLine1, final String addressLine2,
			final String addressCity, final String addressState,
			final String addressCountry, final String landline,
			final String mobile, final String school, final Date birthDate,
			final String emailConfirmationToken) {
		super();
		this.username = username;
		this.password = password;
		this.emailAddress = emailAddress;
		this.firstNameMain = firstNameMain;
		this.middleNameMain = middleNameMain;
		this.lastNameMain = lastNameMain;
		this.firstNameLocalised = firstNameLocalised;
		this.middleNameLocalised = middleNameLocalised;
		this.lastNameLocalised = lastNameLocalised;
		this.teacherFirstName = teacherFirstName;
		this.teacherMiddleName = teacherMiddleName;
		this.teacherLastName = teacherLastName;
		this.addressLine1 = addressLine1;
		this.addressLine2 = addressLine2;
		this.addressCity = addressCity;
		this.addressState = addressState;
		this.addressCountry = addressCountry;
		this.landline = landline;
		this.mobile = mobile;
		this.school = school;
		this.birthDate = birthDate;
		this.emailConfirmationToken = emailConfirmationToken;
	}

	@JsonView(PriviligedUserView.class)
	public String getAddressCity() {
		return this.addressCity;
	}

	@JsonView(PriviligedUserView.class)
	public String getAddressCountry() {
		return this.addressCountry;
	}

	@JsonView(PriviligedUserView.class)
	public String getAddressLine1() {
		return this.addressLine1;
	}

	@JsonView(PriviligedUserView.class)
	public String getAddressLine2() {
		return this.addressLine2;
	}

	@JsonView(PriviligedUserView.class)
	public String getAddressState() {
		return this.addressState;
	}

	@JsonView(PriviligedUserView.class)
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		final Collection<GrantedAuthority> authorities = new ArrayList<>();
		final Set<Role> userRoles = this.roles;
		if (userRoles != null) {
			for (final Role role : userRoles) {
				final SimpleGrantedAuthority authority = new SimpleGrantedAuthority(
						role.getRoleName());
				authorities.add(authority);
			}
		}
		return authorities;
	}

	@JsonView(PriviligedUserView.class)
	public Date getBirthDate() {
		return this.birthDate;
	}

	@JsonView(PriviligedUserView.class)
	public String getEmailAddress() {
		return this.emailAddress;
	}

	@JsonView(ServerUserView.class)
	public String getEmailConfirmationToken() {
		return this.emailConfirmationToken;
	}

	@JsonView(UnprivilegedUserView.class)
	public String getFirstNameLocalised() {
		return this.firstNameLocalised;
	}

	@JsonView(UnprivilegedUserView.class)
	public String getFirstNameMain() {
		return this.firstNameMain;
	}

	@JsonView(UnprivilegedUserView.class)
	public long getId() {
		return this.id;
	}

	@JsonView(PriviligedUserView.class)
	public String getLandline() {
		return this.landline;
	}

	@JsonView(UnprivilegedUserView.class)
	public String getLastNameLocalised() {
		return this.lastNameLocalised;
	}

	@JsonView(UnprivilegedUserView.class)
	public String getLastNameMain() {
		return this.lastNameMain;
	}

	@JsonView(UnprivilegedUserView.class)
	public String getMiddleNameLocalised() {
		return this.middleNameLocalised;
	}

	@JsonView(UnprivilegedUserView.class)
	public String getMiddleNameMain() {
		return this.middleNameMain;
	}

	@JsonView(PriviligedUserView.class)
	public String getMobile() {
		return this.mobile;
	}

	@JsonView(ServerUserView.class)
	@Override
	public String getPassword() {
		return this.password;
	}

	@JsonView(PriviligedUserView.class)
	public Set<Role> getRoles() {
		return this.roles;
	}

	@JsonView(PriviligedUserView.class)
	public String getSchool() {
		return this.school;
	}

	@JsonView(PriviligedUserView.class)
	public String getTeacherFirstName() {
		return this.teacherFirstName;
	}

	@JsonView(PriviligedUserView.class)
	public String getTeacherLastName() {
		return this.teacherLastName;
	}

	@JsonView(PriviligedUserView.class)
	public String getTeacherMiddleName() {
		return this.teacherMiddleName;
	}

	@JsonView(UnprivilegedUserView.class)
	@Override
	public String getUsername() {
		return this.username;
	}

	public boolean hasRole(final Role role) {
		return this.roles.contains(role);
	}

	@JsonView(PriviligedUserView.class)
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@JsonView(PriviligedUserView.class)
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@JsonView(PriviligedUserView.class)
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@JsonView(PriviligedUserView.class)
	@Override
	public boolean isEnabled() {
		return this.enabled;
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

	public void setBirthDate(final Date birthDate) {
		this.birthDate = birthDate;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public void setEmailConfirmationToken(String emailConfirmationToken) {
		this.emailConfirmationToken = emailConfirmationToken;
	}

	public void setFirstNameLocalised(final String firstNameLocalised) {
		this.firstNameLocalised = firstNameLocalised;
	}

	public void setFirstNameMain(final String firstNameMain) {
		this.firstNameMain = firstNameMain;
	}

	public void setId(final long id) {
		this.id = id;
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

	public void setPassword(final String password) {
		this.password = password;
	}

	public void setRoles(final Set<Role> roles) {
		this.roles = roles;
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

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String toString() {
		return String
				.format("User [id=%s, username=%s, firstNameMain=%s, middleNameMain=%s, lastNameMain=%s]",
						this.id, this.username, this.firstNameMain,
						this.middleNameMain, this.lastNameMain);
	}

}
