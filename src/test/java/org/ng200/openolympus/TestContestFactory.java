package org.ng200.openolympus;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.jooq.DSLContext;
import org.ng200.openolympus.jooq.enums.ContestPermissionType;
import org.ng200.openolympus.jooq.tables.daos.ContestPermissionDao;
import org.ng200.openolympus.jooq.tables.pojos.Contest;
import org.ng200.openolympus.jooq.tables.pojos.ContestPermission;
import org.ng200.openolympus.jooq.tables.pojos.User;
import org.ng200.openolympus.jooq.tables.records.ContestRecord;
import org.ng200.openolympus.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TestContestFactory {

	@Autowired
	private DSLContext dslContext;

	@Autowired
	private ContestPermissionDao contestPermissionDao;

	public class TestContestBuilder {

		private String prefix = "testContest";
		private boolean showFullTestsDuringContest = false;
		private List<Pair<ContestPermissionType, Long>> permissions = new ArrayList<>();
		private OffsetDateTime startTime = null;

		public OffsetDateTime getStartTime() {
			return startTime;
		}

		public TestContestBuilder setStartTime(OffsetDateTime startTime) {
			this.startTime = startTime;
			return this;
		}

		public String getPrefix() {
			return prefix;
		}

		public TestContestBuilder setPrefix(String prefix) {
			this.prefix = prefix;
			return this;
		}

		public boolean isShowFullTestsDuringContest() {
			return showFullTestsDuringContest;
		}

		public TestContestBuilder setShowFullTestsDuringContest(
				boolean showFullTestsDuringContest) {
			this.showFullTestsDuringContest = showFullTestsDuringContest;
			return this;

		}

		public List<Pair<ContestPermissionType, Long>> getPermissions() {
			return permissions;
		}

		public TestContestBuilder setPermissions(
				List<Pair<ContestPermissionType, Long>> permissions) {
			this.permissions = permissions;
			return this;
		}

		public Contest build()
				throws Exception {
			ContestRecord contestRecord = new ContestRecord();
			contestRecord.attach(dslContext.configuration());
			String name = prefix + "_" + TestUtils.generateId();
			contestRecord.setName(name)
					.setDuration(Duration.ofMinutes(1))
					.setStartTime(startTime == null
							? OffsetDateTime.now()
							: startTime)
					.setShowFullTestsDuringContest(showFullTestsDuringContest);
			contestRecord.insert();

			contestRecord.refresh();

			contestPermissionDao.insert(permissions.stream()
					.map(perm -> new ContestPermission()
							.setPrincipalId(perm.getSecond())
							.setPermission(perm.getFirst())
							.setContestId(contestRecord.getId()))
					.collect(Collectors.toList()));

			return contestRecord.into(Contest.class);
		}

		public TestContestBuilder permit(User user,
				ContestPermissionType perm) {
			return permit(user.getId(), perm);
		}

		public TestContestBuilder permit(Long user,
				ContestPermissionType perm) {
			permissions.add(new Pair<ContestPermissionType, Long>(perm, user));
			return this;
		}
	}

	public TestContestBuilder contest() {
		return new TestContestBuilder();
	}
}
