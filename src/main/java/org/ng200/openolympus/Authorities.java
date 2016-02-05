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
package org.ng200.openolympus;

import org.springframework.security.core.GrantedAuthority;

public class Authorities {

	public static class OlympusAuthority implements GrantedAuthority {
		/**
		 *
		 */
		private static final long serialVersionUID = 6009653253064200656L;
		private final String authority;
		private final SecurityClearanceType clearanceType;

		public OlympusAuthority(String authority,
				SecurityClearanceType clearanceType) {
			this.authority = authority;
			this.clearanceType = clearanceType;
		}

		@Override
		public String getAuthority() {
			return this.authority;
		}

		public SecurityClearanceType getClearanceType() {
			return this.clearanceType;
		}

	}

	public static GrantedAuthority USER = new OlympusAuthority("USER",
			SecurityClearanceType.APPROVED_USER);
	public static GrantedAuthority ADMINISTRATOR = new OlympusAuthority(
			"ADMINISTRATOR", SecurityClearanceType.ADMINISTRATIVE_USER);
	public static GrantedAuthority SUPERUSER = new OlympusAuthority(
			"SUPERUSER", SecurityClearanceType.SUPERUSER);

}
