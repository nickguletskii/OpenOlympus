/**
 * The MIT License
 * Copyright (c) 2014 Nick Guletskii
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
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "Users", indexes = { @Index(columnList = "username") })
public class User implements UserDetails {

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

	@OneToMany(fetch = FetchType.EAGER)
	private Set<Role> roles;

	private final boolean enabled = true;

	protected User() {
	}

	public User(final String username, final String password,
			final String firstNameMain, final String middleNameMain,
			final String lastNameMain, final String firstNameLocalised,
			final String middleNameLocalised, final String lastNameLocalised,
			final String teacherFirstName, final String teacherMiddleName,
			final String teacherLastName, final String addressLine1,
			final String addressLine2, final String addressCity,
			final String addressState, final String addressCountry,
			final String landline, final String mobile, final String school,
			final Date birthDate) {
		super();
		this.username = username;
		this.password = password;
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
	}

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

	public Date getBirthDate() {
		return this.birthDate;
	}

	public String getFirstNameLocalised() {
		return this.firstNameLocalised;
	}

	public String getFirstNameMain() {
		return this.firstNameMain;
	}

	public long getId() {
		return this.id;
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

	@Override
	public String getPassword() {
		return this.password;
	}

	public Set<Role> getRoles() {
		return this.roles;
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

	@Override
	public String getUsername() {
		return this.username;
	}

	public boolean hasRole(final Role role) {
		return this.roles.contains(role);
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

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

	@Override
	public String toString() {
		return String
				.format("User [id=%s, username=%s, password=%s, firstNameMain=%s, middleNameMain=%s, lastNameMain=%s, firstNameLocalised=%s, middleNameLocalised=%s, lastNameLocalised=%s, teacherFirstName=%s, teacherMiddleName=%s, teacherLastName=%s, addressLine1=%s, addressLine2=%s, addressCity=%s, addressState=%s, addressCountry=%s, school=%s, birthDate=%s]",
						this.id, this.username, this.password,
						this.firstNameMain, this.middleNameMain,
						this.lastNameMain, this.firstNameLocalised,
						this.middleNameLocalised, this.lastNameLocalised,
						this.teacherFirstName, this.teacherMiddleName,
						this.teacherLastName, this.addressLine1,
						this.addressLine2, this.addressCity, this.addressState,
						this.addressCountry, this.school, this.birthDate);
	}

}
