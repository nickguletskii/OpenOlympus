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

import java.util.Collection;

import org.ng200.openolympus.Authorities;
import org.ng200.openolympus.jooq.tables.interfaces.IUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;

public interface UserDetailsImpl extends IUser, UserDetails {

	static final Logger logger = LoggerFactory.getLogger(UserDetailsImpl.class);

	@Override
	public default Collection<? extends GrantedAuthority> getAuthorities() {
		Builder<GrantedAuthority> builder = ImmutableList
				.<GrantedAuthority> builder();
		if (getApproved())
			builder.add(Authorities.USER);
		if (getSuperuser())
			builder.add(Authorities.SUPERUSER);
		return builder.build();
	}

	@Override
	public default boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public default boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public default boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public default boolean isEnabled() {
		return getEnabled();
	}
}
