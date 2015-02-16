package org.ng200.openolympus.services;

import org.ng200.openolympus.model.User;
import org.ng200.openolympus.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserSecurityService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(final String username)
			throws UsernameNotFoundException {
		final User user = this.userRepository.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException(
					"No user with that username exists.");
		}
		return user;
	}

}
