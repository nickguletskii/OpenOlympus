package org.ng200.openolympus;

import org.springframework.security.core.GrantedAuthority;

public class Authorities {

	protected static class Authority implements GrantedAuthority {
		private final String authority;

		public Authority(String authority) {
			this.authority = authority;
		}

		@Override
		public String getAuthority() {
			return authority;
		}

	}

	public static GrantedAuthority USER = new Authority("USER");
	public static GrantedAuthority SUPERUSER = new Authority(
			"SUPERUSER");

}
