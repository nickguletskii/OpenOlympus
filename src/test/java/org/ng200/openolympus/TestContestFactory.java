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

	public class TestContestBuilder {

		private String prefix = "testContest";
		private boolean showFullTestsDuringContest = false;
		private List<Pair<ContestPermissionType, Long>> permissions = new ArrayList<>();
		private List<Integer> tasks = new ArrayList<>();

		private OffsetDateTime startTime = null;

		private Duration duration = Duration.ofMinutes(1);

		public Contest build()
				throws Exception {
			final ContestRecord contestRecord = new ContestRecord();
			contestRecord
					.attach(TestContestFactory.this.dslContext.configuration());
			final String name = this.prefix + "_" + TestUtils.generateId();
			contestRecord.setName(name)
					.setDuration(this.duration)
					.setStartTime(this.startTime == null
							? OffsetDateTime.now()
							: this.startTime)
					.setShowFullTestsDuringContest(
							this.showFullTestsDuringContest);
			contestRecord.insert();

			contestRecord.refresh();

			TestContestFactory.this.contestPermissionDao
					.insert(this.permissions.stream()
							.map(perm -> new ContestPermission()
									.setPrincipalId(perm.getSecond())
									.setPermission(perm.getFirst())
									.setContestId(contestRecord.getId()))
							.collect(Collectors.toList()));

			for (final Integer task : this.tasks) {
				final ContestTasksRecord record = new ContestTasksRecord(
						contestRecord.getId(),
						task);
				record.attach(
						TestContestFactory.this.dslContext.configuration());
				record.insert();
			}

			return contestRecord.into(Contest.class);
		}

		public TestContestBuilder ended() {
			this.startTime = OffsetDateTime.now().minus(Duration.ofMinutes(5));
			this.duration = Duration.ofMinutes(4);
			return this;
		}

		public Duration getDuration() {
			return this.duration;
		}

		public List<Pair<ContestPermissionType, Long>> getPermissions() {
			return this.permissions;
		}

		public String getPrefix() {
			return this.prefix;
		}

		public OffsetDateTime getStartTime() {
			return this.startTime;
		}

		public List<Integer> getTasks() {
			return this.tasks;
		}

		public TestContestBuilder inProgress() {
			this.startTime = OffsetDateTime.now().minus(Duration.ofMinutes(1));
			this.duration = Duration.ofMinutes(2);
			return this;
		}

		public boolean isShowFullTestsDuringContest() {
			return this.showFullTestsDuringContest;
		}

		public TestContestBuilder notStarted() {
			this.startTime = OffsetDateTime.now().plus(Duration.ofMinutes(1));
			this.duration = Duration.ofMinutes(2);
			return this;
		}

		public TestContestBuilder permit(Long user,
				ContestPermissionType perm) {
			this.permissions
					.add(new Pair<ContestPermissionType, Long>(perm, user));
			return this;
		}

		public TestContestBuilder permit(User user,
				ContestPermissionType perm) {
			return this.permit(user.getId(), perm);
		}

		public void setDuration(Duration duration) {
			this.duration = duration;
		}

		public TestContestBuilder setPermissions(
				List<Pair<ContestPermissionType, Long>> permissions) {
			this.permissions = permissions;
			return this;
		}

		public TestContestBuilder setPrefix(String prefix) {
			this.prefix = prefix;
			return this;
		}

		public TestContestBuilder setShowFullTestsDuringContest(
				boolean showFullTestsDuringContest) {
			this.showFullTestsDuringContest = showFullTestsDuringContest;
			return this;

		}

		public TestContestBuilder setStartTime(OffsetDateTime startTime) {
			this.startTime = startTime;
			return this;
		}

		public void setTasks(List<Integer> tasks) {
			this.tasks = tasks;
		}

		public TestContestBuilder withTasks(Integer... tasks) {
			this.tasks.addAll(Lists.from(tasks));
			return this;
		}

		public TestContestBuilder withTasks(Task... tasks) {
			this.tasks.addAll(Stream.of(tasks).map(t -> t.getId())
					.collect(Collectors.toList()));
			return this;
		}
	}

	@Autowired
	private DSLContext dslContext;

	@Autowired
	private ContestPermissionDao contestPermissionDao;

	public TestContestBuilder contest() {
		return new TestContestBuilder();
	}
}
