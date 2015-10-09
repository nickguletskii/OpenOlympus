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

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.time.OffsetDateTime;

import org.jooq.DSLContext;
import org.jooq.DeleteConditionStep;
import org.jooq.Result;
import org.jooq.exception.DataAccessException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ng200.openolympus.jooq.Tables;
import org.ng200.openolympus.jooq.enums.ContestPermissionType;
import org.ng200.openolympus.jooq.tables.pojos.Contest;
import org.ng200.openolympus.jooq.tables.pojos.Group;
import org.ng200.openolympus.jooq.tables.pojos.User;
import org.ng200.openolympus.jooq.tables.records.ContestParticipationRecord;
import org.ng200.openolympus.jooq.tables.records.ContestPermissionRecord;
import org.ng200.openolympus.services.ContestService;
import org.ng200.openolympus.services.GroupService;
import org.ng200.openolympus.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@TestPropertySource(value = {
								"classpath:test-db.properties",
								"file:secret.properties"
})
@SpringApplicationConfiguration(classes = Application.class)

@EnableTransactionManagement
@Transactional
@TransactionConfiguration
public class ContestParticipantDatabaseTest {

	@Autowired
	private DSLContext dslContext;

	@Autowired
	private ContestService contestService;

	@Autowired
	private GroupService groupService;

	@Autowired
	private UserService userService;

	private void addContestPermission(Contest contest, long principalId,
			ContestPermissionType contestPermsissionType) {
		this.dslContext.insertInto(Tables.CONTEST_PERMISSION)
				.columns(Tables.CONTEST_PERMISSION.CONTEST_ID,
						Tables.CONTEST_PERMISSION.PRINCIPAL_ID,
						Tables.CONTEST_PERMISSION.PERMISSION)
				.values(contest.getId(), principalId, contestPermsissionType)
				.execute();
	}

	private Contest createContest() {
		final Contest contest = this.contestService
				.insertContest(new Contest()
						.setDuration(Duration.ofHours(1))
						.setName("Test")
						.setShowFullTestsDuringContest(false)
						.setStartTime(OffsetDateTime.now()));

		Assert.assertNotNull("Contest must have an ID after insertion!",
				contest.getId());
		Assert.assertNotNull("Contest must exist after insertion!",
				this.contestService.getContestByName("Test"));
		return contest;
	}

	private DeleteConditionStep<ContestPermissionRecord> deleteContestPermission(
			Contest contest, long principalId,
			ContestPermissionType permissionType) {
		return this.dslContext.deleteFrom(Tables.CONTEST_PERMISSION)
				.where(Tables.CONTEST_PERMISSION.CONTEST_ID
						.eq(contest.getId())
						.and(Tables.CONTEST_PERMISSION.PRINCIPAL_ID
								.eq(principalId)
								.and(Tables.CONTEST_PERMISSION.PERMISSION.eq(
										permissionType))));
	}

	private Group getAllUsersGroup() {
		final Group group = this.groupService
				.getGroupByName(NameConstants.ALL_USERS_GROUP_NAME);

		Assert.assertNotNull(group);

		return group;
	}

	private Result<ContestParticipationRecord> getContestParticipations(
			Contest contest,
			User user) {
		return this.dslContext.selectFrom(Tables.CONTEST_PARTICIPATION)
				.where(Tables.CONTEST_PARTICIPATION.CONTEST_ID
						.eq(contest.getId()))
				.and(Tables.CONTEST_PARTICIPATION.USER_ID
						.eq(user.getId()))
				.fetch();
	}

	private User getRootUser() {
		final User user = this.userService
				.getUserByUsername(NameConstants.SUPERUSER_ACCOUNT_NAME);

		Assert.assertNotNull("Superuser must exist!", user);
		return user;
	}

	@Test
	@Rollback
	@Transactional
	public void testGivingGroupOtherPermissionDoesntAddAsParticipant()
			throws DataAccessException, IOException {
		Assert.assertNotNull(this.dslContext);
		final Contest contest = this.createContest();

		final User user = this.getRootUser();

		final Group allUsersGroup = this.getAllUsersGroup();

		this.addContestPermission(contest, allUsersGroup.getId(),
				ContestPermissionType.know_about);

		Assert.assertTrue(
				"Granting permission 'know_about' shouldn't add user as participant!",
				this.getContestParticipations(contest, user).isEmpty());

		this.addContestPermission(contest, allUsersGroup.getId(),
				ContestPermissionType.participate);
	}

	@Test
	@Rollback
	@Transactional
	public void testGivingGroupParticipatePermissionAddsAsParticipant()
			throws DataAccessException, IOException {
		Assert.assertNotNull(this.dslContext);
		final Contest contest = this.createContest();

		final User user = this.getRootUser();

		final Group allUsersGroup = this.getAllUsersGroup();

		this.addContestPermission(contest, allUsersGroup.getId(),
				ContestPermissionType.participate);

		Assert.assertTrue(
				"Granting permission 'participant' should add one and one contest participation table row per user only!",
				this.getContestParticipations(contest, user).size() == 1);

		this.deleteContestPermission(contest, allUsersGroup.getId(),
				ContestPermissionType.participate)
				.execute();

		Assert.assertTrue(
				"Removing permission 'participant' remove the contest participation table row!",
				this.getContestParticipations(contest, user).size() == 0);
	}

	@Test
	@Rollback
	@Transactional
	public void testGivingUserOtherPermissionDoesntAddAsParticipant()
			throws DataAccessException, IOException {
		Assert.assertNotNull(this.dslContext);
		final Contest contest = this.createContest();

		final User user = this.getRootUser();

		this.addContestPermission(contest, user.getId(),
				ContestPermissionType.know_about);

		Assert.assertTrue(
				"Granting permission 'know_about' shouldn't add user as participant!",
				this.getContestParticipations(contest, user).isEmpty());
	}

	@Test
	@Rollback
	@Transactional
	public void testGivingUserParticipatePermissionAddsAsParticipant()
			throws DataAccessException, IOException {
		Assert.assertNotNull(this.dslContext);
		final Contest contest = this.createContest();

		final User user = this.getRootUser();

		this.addContestPermission(contest, user.getId(),
				ContestPermissionType.participate);

		Assert.assertTrue(
				"Granting permission 'participant' should add one and one contest participation table row only!",
				this.getContestParticipations(contest, user).size() == 1);

		Assert.assertNotNull("User score in contest must not be null",
				this.getContestParticipations(contest, user)
						.get(0).getScore());

		Assert.assertTrue(this.deleteContestPermission(contest, user.getId(),
				ContestPermissionType.participate)
				.execute() == 1);

		Assert.assertTrue(
				"Removing permission 'participant' remove the contest participation table row!",
				this.getContestParticipations(contest, user).size() == 0);
	}

	@Test
	@Rollback
	@Transactional
	public void testRevokingUserPermissionDoesntRemoveParticipantWhenDuplicate()
			throws DataAccessException, IOException {
		Assert.assertNotNull(this.dslContext);
		final Contest contest = this.createContest();

		final User user = this.getRootUser();

		final Group allUsersGroup = this.getAllUsersGroup();

		this.addContestPermission(contest, allUsersGroup.getId(),
				ContestPermissionType.participate);
		this.addContestPermission(contest, user.getId(),
				ContestPermissionType.participate);

		Assert.assertTrue(
				"Granting permission 'participant' should add one and one contest participation table row per user only!",
				this.getContestParticipations(contest, user).size() == 1);

		this.deleteContestPermission(contest, user.getId(),
				ContestPermissionType.participate)
				.execute();

		Assert.assertTrue(
				"Removing one 'participant' permission shouldn't remove contest participation if another remains!",
				this.getContestParticipations(contest, user).size() == 1);
	}

	@Test
	@Rollback
	@Transactional
	public void testRevokingGroupPermissionDoesntRemoveParticipantWhenDuplicate()
			throws DataAccessException, IOException {
		Assert.assertNotNull(this.dslContext);
		final Contest contest = this.createContest();

		final User user = this.getRootUser();

		final Group allUsersGroup = this.getAllUsersGroup();

		this.addContestPermission(contest, allUsersGroup.getId(),
				ContestPermissionType.participate);
		this.addContestPermission(contest, user.getId(),
				ContestPermissionType.participate);

		Assert.assertTrue(
				"Granting permission 'participant' should add one and one contest participation table row per user only!",
				this.getContestParticipations(contest, user).size() == 1);

		this.deleteContestPermission(contest, allUsersGroup.getId(),
				ContestPermissionType.participate)
				.execute();

		Assert.assertTrue(
				"Removing one 'participant' permission shouldn't remove contest participation if another remains!",
				this.getContestParticipations(contest, user).size() == 1);
	}

}
