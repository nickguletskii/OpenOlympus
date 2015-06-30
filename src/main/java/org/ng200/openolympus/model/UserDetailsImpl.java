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
			builder.add(Authorities.ADMINISTRATOR);
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
