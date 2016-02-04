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
package org.ng200.openolympus;

import java.util.stream.Stream;

import org.jooq.DSLContext;
import org.ng200.openolympus.jooq.enums.GeneralPermissionType;
import org.ng200.openolympus.jooq.tables.daos.PrincipalDao;
import org.ng200.openolympus.jooq.tables.pojos.Principal;
import org.ng200.openolympus.jooq.tables.pojos.User;
import org.ng200.openolympus.jooq.tables.records.UserRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class TestUserFactory {

	public class TestUserBuilder {

		private String prefix = "testUser";
		private boolean approved = true;
		private boolean superuser = false;
		private GeneralPermissionType[] permissions = {};

		public User build()
				throws Exception {
			final UserRecord userRecord = new UserRecord();
			userRecord.attach(TestUserFactory.this.dslContext.configuration());
			final String username = this.prefix + "_" + TestUtils.generateId();
			userRecord
					.setUsername(username)
					.setPassword(TestUserFactory.this.passwordEncoder
							.encode("password"))
					.setApproved(this.approved)
					.setSuperuser(this.superuser)
					.setApprovalEmailSent(false)
					.setEnabled(true);
			userRecord.insert();

			userRecord.refresh();

			final Principal principal = TestUserFactory.this.principalDao
					.findById(userRecord.getId());

			principal.setPermissions(this.permissions);

			TestUserFactory.this.principalDao.update(principal);

			return userRecord.into(User.class);
		}

		public GeneralPermissionType[] getPermissions() {
			return this.permissions;
		}

		public String getPrefix() {
			return this.prefix;
		}

		public boolean isApproved() {
			return this.approved;
		}

		public boolean isSuperuser() {
			return this.superuser;
		}

		public TestUserBuilder setApproved(boolean approved) {
			this.approved = approved;
			return this;
		}

		public TestUserBuilder setPrefix(String prefix) {
			this.prefix = prefix;
			return this;
		}

		public TestUserBuilder setSuperuser(boolean superuser) {
			this.superuser = superuser;
			return this;
		}

		public TestUserBuilder withoutPermissions(
				GeneralPermissionType... generalPermissionTypes) {
			this.permissions = Stream.of(GeneralPermissionType.values())
					.filter(x -> !Stream.of(generalPermissionTypes)
							.anyMatch(y -> x == y))
					.toArray(n -> new GeneralPermissionType[n]);
			return this;
		}

		public TestUserBuilder withPermissions(
				GeneralPermissionType... generalPermissionTypes) {
			this.permissions = generalPermissionTypes;
			return this;
		}
	}

	@Autowired
	private DSLContext dslContext;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private PrincipalDao principalDao;

	public TestUserBuilder user() {
		return new TestUserBuilder();
	}
}
