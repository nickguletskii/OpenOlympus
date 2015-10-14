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

	@Autowired
	private DSLContext dslContext;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private PrincipalDao principalDao;

	public class TestUserBuilder {

		private String prefix = "testUser";
		private boolean approved = true;
		private boolean superuser = false;
		private GeneralPermissionType[] permissions = {};

		public GeneralPermissionType[] getPermissions() {
			return permissions;
		}

		public String getPrefix() {
			return prefix;
		}

		public TestUserBuilder setPrefix(String prefix) {
			this.prefix = prefix;
			return this;
		}

		public boolean isApproved() {
			return approved;
		}

		public TestUserBuilder setApproved(boolean approved) {
			this.approved = approved;
			return this;
		}

		public boolean isSuperuser() {
			return superuser;
		}

		public TestUserBuilder setSuperuser(boolean superuser) {
			this.superuser = superuser;
			return this;
		}

		public TestUserBuilder withPermissions(
				GeneralPermissionType... generalPermissionTypes) {
			this.permissions = generalPermissionTypes;
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

		public User build()
				throws Exception {
			UserRecord userRecord = new UserRecord();
			userRecord.attach(dslContext.configuration());
			String username = prefix + "_" + TestUtils.generateId();
			userRecord
					.setUsername(username)
					.setPassword(passwordEncoder.encode("password"))
					.setApproved(approved)
					.setSuperuser(superuser)
					.setApprovalEmailSent(false)
					.setEnabled(true);
			userRecord.insert();

			userRecord.refresh();

			Principal principal = principalDao.findById(userRecord.getId());

			principal.setPermissions(permissions);

			principalDao.update(principal);

			return userRecord.into(User.class);
		}
	}

	public TestUserBuilder user() {
		return new TestUserBuilder();
	}
}
