package org.ng200.openolympus;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jooq.DSLContext;
import org.ng200.openolympus.cerberus.util.Lists;
import org.ng200.openolympus.jooq.enums.ContestPermissionType;
import org.ng200.openolympus.jooq.tables.daos.ContestPermissionDao;
import org.ng200.openolympus.jooq.tables.pojos.Contest;
import org.ng200.openolympus.jooq.tables.pojos.ContestPermission;
import org.ng200.openolympus.jooq.tables.pojos.Task;
import org.ng200.openolympus.jooq.tables.pojos.User;
import org.ng200.openolympus.jooq.tables.records.ContestRecord;
import org.ng200.openolympus.jooq.tables.records.ContestTasksRecord;
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
		private List<Integer> tasks = new ArrayList<>();

		public List<Integer> getTasks() {
			return tasks;
		}

		public void setTasks(List<Integer> tasks) {
			this.tasks = tasks;
		}

		private OffsetDateTime startTime = null;
		private Duration duration = Duration.ofMinutes(1);

		public Duration getDuration() {
			return duration;
		}

		public void setDuration(Duration duration) {
			this.duration = duration;
		}

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
					.setDuration(duration)
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

			for (Integer task : tasks) {
				final ContestTasksRecord record = new ContestTasksRecord(
						contestRecord.getId(),
						task);
				record.attach(dslContext.configuration());
				record.insert();
			}

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

		public TestContestBuilder inProgress() {
			this.startTime = OffsetDateTime.now().minus(Duration.ofMinutes(1));
			this.duration = Duration.ofMinutes(2);
			return this;
		}

		public TestContestBuilder notStarted() {
			this.startTime = OffsetDateTime.now().plus(Duration.ofMinutes(1));
			this.duration = Duration.ofMinutes(2);
			return this;
		}

		public TestContestBuilder ended() {
			this.startTime = OffsetDateTime.now().minus(Duration.ofMinutes(5));
			this.duration = Duration.ofMinutes(4);
			return this;
		}

		public TestContestBuilder withTasks(Task... tasks) {
			this.tasks.addAll(Stream.of(tasks).map(t -> t.getId())
					.collect(Collectors.toList()));
			return this;
		}

		public TestContestBuilder withTasks(Integer... tasks) {
			this.tasks.addAll(Lists.from(tasks));
			return this;
		}
	}

	public TestContestBuilder contest() {
		return new TestContestBuilder();
	}
}
