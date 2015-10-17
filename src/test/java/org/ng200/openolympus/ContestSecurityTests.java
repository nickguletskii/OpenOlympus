package org.ng200.openolympus;

import java.time.Duration;
import java.time.OffsetDateTime;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ng200.openolympus.cerberus.util.Lists;
import org.ng200.openolympus.jooq.enums.ContestPermissionType;
import org.ng200.openolympus.jooq.enums.GeneralPermissionType;
import org.ng200.openolympus.jooq.tables.pojos.Contest;
import org.ng200.openolympus.jooq.tables.pojos.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;

@RunWith(SpringJUnit4ClassRunner.class)
@TestPropertySource(value = {
								"classpath:test-web.properties",
								"file:secret.properties"
})
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@EnableTransactionManagement
@Transactional
@TransactionConfiguration
public class ContestSecurityTests {
	@Autowired
	private WebApplicationContext wac;
	private MockMvc mockMvc;

	@Autowired
	private TestUserFactory testUserFactory;

	@Autowired
	private TestContestFactory testContestFactory;

	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders
				.webAppContextSetup(this.wac)
				.apply(SecurityMockMvcConfigurers.springSecurity())
				.build();
	}

	@Test
	@Rollback
	@Transactional
	public void testContestAclManagementWithoutPermission() throws Exception {

		final User user = this.testUserFactory.user()
				.build();

		final Contest contest = this.testContestFactory.contest()
				.build();

		final Gson gson = new Gson();

		this.mockMvc.perform(
				MockMvcRequestBuilders
						.put("/api/contest/{contest}/acl", contest.getId())
						.content(gson.toJson(ImmutableMap
								.builder()
								.put("manage_acl", Lists.from(user.getId()))
								.put("add_task", Lists.from(user.getId()))
								.build()))
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON)
						.with(SecurityMockMvcRequestPostProcessors.user(user)))
				.andExpect(MockMvcResultMatchers.status().isForbidden())
				.andReturn();
	}

	@Test
	@Rollback
	@Transactional
	public void testContestAclManagementWithPermission() throws Exception {

		final User user = this.testUserFactory.user()
				.build();

		final Contest contest = this.testContestFactory.contest()
				.permit(user, ContestPermissionType.manage_acl)
				.build();

		final Gson gson = new Gson();

		this.mockMvc.perform(
				MockMvcRequestBuilders
						.put("/api/contest/{contest}/acl", contest.getId())
						.content(gson.toJson(ImmutableMap
								.builder()
								.put("manage_acl", Lists.from(user.getId()))
								.put("add_task", Lists.from(user.getId()))
								.build()))
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON)
						.with(SecurityMockMvcRequestPostProcessors.user(user)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
	}

	@Test
	@Rollback
	@Transactional
	public void testContestCreationWithNeededPriviliges() throws Exception {
		final User user = this.testUserFactory.user()
				.withPermissions(GeneralPermissionType.create_contests)
				.build();

		final OffsetDateTime time = OffsetDateTime.now();
		this.mockMvc.perform(
				MockMvcRequestBuilders.fileUpload("/api/contests/create")
						.param("name", "testContest" + TestUtils.generateId())
						.param("duration",
								Duration.ofMinutes(5).toMillis() + "")
						.param("startTime", time.toString())
						.param("showFullTestsDuringContest", "false")
						.accept(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON)
						.with(SecurityMockMvcRequestPostProcessors.user(user)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(
						MockMvcResultMatchers.jsonPath("$.status").value("OK"))
				.andReturn();
	}

	@Test
	@Rollback
	@Transactional
	public void testContestCreationWithoutNeededPriviliges() throws Exception {

		final User user = this.testUserFactory.user()
				.withoutPermissions(GeneralPermissionType.create_contests)
				.build();

		final OffsetDateTime time = OffsetDateTime.now();
		this.mockMvc.perform(
				MockMvcRequestBuilders.fileUpload("/api/contests/create")
						.param("name", "testContest" + TestUtils.generateId())
						.param("duration",
								Duration.ofMinutes(5).toMillis() + "")
						.param("startTime", time.toString())
						.param("showFullTestsDuringContest", "false")
						.accept(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON)
						.with(SecurityMockMvcRequestPostProcessors.user(user)))
				.andExpect(MockMvcResultMatchers.status().isForbidden())
				.andReturn();
	}

	@Test
	@Rollback
	@Transactional
	public void testContestModificationWithoutPermission() throws Exception {

		final User user = this.testUserFactory.user()
				.build();

		final Contest contest = this.testContestFactory.contest()
				.build();

		this.mockMvc.perform(
				MockMvcRequestBuilders.fileUpload("/api/contest/{contest}/edit",
						contest.getId())
						.param("name", "test")
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON)
						.with(SecurityMockMvcRequestPostProcessors.user(user)))
				.andExpect(MockMvcResultMatchers.status().isForbidden())
				.andReturn();
	}

	@Test
	@Rollback
	@Transactional
	public void testContestModificationWithPermission() throws Exception {

		final User user = this.testUserFactory.user()
				.build();

		final Contest contest = this.testContestFactory.contest()
				.permit(user, ContestPermissionType.edit)
				.build();

		this.mockMvc.perform(
				MockMvcRequestBuilders.fileUpload("/api/contest/{contest}/edit",
						contest.getId())
						.param("name", "test")
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON)
						.with(SecurityMockMvcRequestPostProcessors.user(user)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
	}

	@Test
	@Rollback
	@Transactional
	public void testContestParticipantListWithoutPermission() throws Exception {

		final User user = this.testUserFactory.user()
				.build();

		final Contest contest = this.testContestFactory.contest()
				.build();

		this.mockMvc.perform(
				MockMvcRequestBuilders
						.get("/api/contest/{contest}/participants",
								contest.getId())
						.param("page", "1")
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON)
						.with(SecurityMockMvcRequestPostProcessors.user(user)))
				.andExpect(MockMvcResultMatchers.status().isForbidden())
				.andReturn();
	}

	@Test
	@Rollback
	@Transactional
	public void testContestParticipantListWithPermission() throws Exception {

		final User user = this.testUserFactory.user()
				.build();

		final Contest contest = this.testContestFactory.contest()
				.permit(user, ContestPermissionType.view_participants)
				.build();

		this.mockMvc.perform(
				MockMvcRequestBuilders
						.get("/api/contest/{contest}/participants",
								contest.getId())
						.param("page", "1")
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON)
						.with(SecurityMockMvcRequestPostProcessors.user(user)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
	}

	@Test
	@Rollback
	@Transactional
	public void testContestRemovalWithoutPermission() throws Exception {

		final User user = this.testUserFactory.user()
				.build();

		final Contest contest = this.testContestFactory.contest()
				.build();

		this.mockMvc.perform(
				MockMvcRequestBuilders
						.post("/api/contest/{contest}/remove", contest.getId())
						.with(SecurityMockMvcRequestPostProcessors.user(user))
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isForbidden())
				.andReturn();
	}

	@Test
	@Rollback
	@Transactional
	public void testContestRemovalWithPermission() throws Exception {

		final User user = this.testUserFactory.user()
				.build();

		final Contest contest = this.testContestFactory.contest()
				.permit(user, ContestPermissionType.delete)
				.build();

		this.mockMvc.perform(
				MockMvcRequestBuilders
						.post("/api/contest/{contest}/remove", contest.getId())
						.accept(MediaType.APPLICATION_JSON)
						.with(SecurityMockMvcRequestPostProcessors.user(user)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
	}

	@Test
	@Rollback
	@Transactional
	public void testContestResultsWithAfterPermissionAfterContest()
			throws Exception {

		final User user = this.testUserFactory.user()
				.build();

		final Contest contest = this.testContestFactory.contest()
				.ended()
				.permit(user, ContestPermissionType.view_results_after_contest)
				.build();

		this.mockMvc.perform(
				MockMvcRequestBuilders
						.get("/api/contest/{contest}/results", contest.getId())
						.param("page", "1")
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON)
						.with(SecurityMockMvcRequestPostProcessors.user(user)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
	}

	@Test
	@Rollback
	@Transactional
	public void testContestResultsWithAfterPermissionBeforeContest()
			throws Exception {

		final User user = this.testUserFactory.user()
				.build();

		final Contest contest = this.testContestFactory.contest()
				.notStarted()
				.permit(user, ContestPermissionType.view_results_after_contest)
				.build();

		this.mockMvc.perform(
				MockMvcRequestBuilders
						.get("/api/contest/{contest}/results", contest.getId())
						.param("page", "1")
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON)
						.with(SecurityMockMvcRequestPostProcessors.user(user)))
				.andExpect(MockMvcResultMatchers.status().isForbidden())
				.andReturn();
	}

	@Test
	@Rollback
	@Transactional
	public void testContestResultsWithAfterPermissionDuringContest()
			throws Exception {

		final User user = this.testUserFactory.user()
				.build();

		final Contest contest = this.testContestFactory.contest()
				.inProgress()
				.permit(user, ContestPermissionType.view_results_after_contest)
				.build();

		this.mockMvc.perform(
				MockMvcRequestBuilders
						.get("/api/contest/{contest}/results", contest.getId())
						.param("page", "1")
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON)
						.with(SecurityMockMvcRequestPostProcessors.user(user)))
				.andExpect(MockMvcResultMatchers.status().isForbidden())
				.andReturn();
	}

	@Test
	@Rollback
	@Transactional
	public void testContestResultsWithDuringPermissionAfterContest()
			throws Exception {

		final User user = this.testUserFactory.user()
				.build();

		final Contest contest = this.testContestFactory.contest()
				.ended()
				.permit(user, ContestPermissionType.view_results_during_contest)
				.build();

		this.mockMvc.perform(
				MockMvcRequestBuilders
						.get("/api/contest/{contest}/results", contest.getId())
						.param("page", "1")
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON)
						.with(SecurityMockMvcRequestPostProcessors.user(user)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
	}

	@Test
	@Rollback
	@Transactional
	public void testContestResultsWithDuringPermissionBeforeContest()
			throws Exception {

		final User user = this.testUserFactory.user()
				.build();

		final Contest contest = this.testContestFactory.contest()
				.notStarted()
				.permit(user, ContestPermissionType.view_results_during_contest)
				.build();

		this.mockMvc.perform(
				MockMvcRequestBuilders
						.get("/api/contest/{contest}/results", contest.getId())
						.param("page", "1")
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON)
						.with(SecurityMockMvcRequestPostProcessors.user(user)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
	}

	@Test
	@Rollback
	@Transactional
	public void testContestResultsWithoutPermissionAfterContest()
			throws Exception {

		final User user = this.testUserFactory.user()
				.build();

		final Contest contest = this.testContestFactory.contest()
				.ended()
				.build();

		this.mockMvc.perform(
				MockMvcRequestBuilders
						.get("/api/contest/{contest}/results", contest.getId())
						.param("page", "1")
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON)
						.with(SecurityMockMvcRequestPostProcessors.user(user)))
				.andExpect(MockMvcResultMatchers.status().isForbidden())
				.andReturn();
	}

	@Test
	@Rollback
	@Transactional
	public void testContestResultsWithoutPermissionBeforeContest()
			throws Exception {

		final User user = this.testUserFactory.user()
				.build();

		final Contest contest = this.testContestFactory.contest()
				.notStarted()
				.build();

		this.mockMvc.perform(
				MockMvcRequestBuilders
						.get("/api/contest/{contest}/results", contest.getId())
						.param("page", "1")
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON)
						.with(SecurityMockMvcRequestPostProcessors.user(user)))
				.andExpect(MockMvcResultMatchers.status().isForbidden())
				.andReturn();
	}

	@Test
	@Rollback
	@Transactional
	public void testContestResultsWithoutPermissionDuringContest()
			throws Exception {

		final User user = this.testUserFactory.user()
				.build();

		final Contest contest = this.testContestFactory.contest()
				.inProgress()
				.build();

		this.mockMvc.perform(
				MockMvcRequestBuilders
						.get("/api/contest/{contest}/results", contest.getId())
						.param("page", "1")
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON)
						.with(SecurityMockMvcRequestPostProcessors.user(user)))
				.andExpect(MockMvcResultMatchers.status().isForbidden())
				.andReturn();
	}

	@Test
	@Rollback
	@Transactional
	public void testContestResultsWithPermissionDuringContest()
			throws Exception {

		final User user = this.testUserFactory.user()
				.build();

		final Contest contest = this.testContestFactory.contest()
				.inProgress()
				.permit(user, ContestPermissionType.view_results_during_contest)
				.build();

		this.mockMvc.perform(
				MockMvcRequestBuilders
						.get("/api/contest/{contest}/results", contest.getId())
						.param("page", "1")
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON)
						.with(SecurityMockMvcRequestPostProcessors.user(user)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
	}

	@Test
	@Rollback
	@Transactional
	public void testContestTaskAdditionWithoutPermission() throws Exception {

		final User user = this.testUserFactory.user()
				.build();

		final Contest contest = this.testContestFactory.contest()
				.permit(user, ContestPermissionType.participate)
				.build();

		this.mockMvc.perform(
				MockMvcRequestBuilders
						.post("/api/contest/{contest}/addUserTime",
								contest.getId())
						.param("time", "" + Duration.ofHours(1).toMillis())
						.param("user", user.getId().toString())
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON)
						.with(SecurityMockMvcRequestPostProcessors.user(user)))
				.andExpect(MockMvcResultMatchers.status().isForbidden())
				.andReturn();
	}

	@Test
	@Rollback
	@Transactional
	public void testContestTaskAdditionWithPermission() throws Exception {

		final User user = this.testUserFactory.user()
				.build();

		final Contest contest = this.testContestFactory.contest()
				.permit(user, ContestPermissionType.extend_time)
				.permit(user, ContestPermissionType.participate)
				.build();

		this.mockMvc.perform(
				MockMvcRequestBuilders
						.post("/api/contest/{contest}/addUserTime",
								contest.getId())
						.param("time", "" + Duration.ofHours(1).toMillis())
						.param("user", user.getId().toString())
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON)
						.with(SecurityMockMvcRequestPostProcessors.user(user)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
	}

}
