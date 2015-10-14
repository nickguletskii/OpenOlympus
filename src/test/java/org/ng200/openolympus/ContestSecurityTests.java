package org.ng200.openolympus;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
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
				.apply(springSecurity())
				.build();
	}

	@Test
	@Rollback
	@Transactional
	public void testContestCreationWithNeededPriviliges() throws Exception {
		User user = testUserFactory.user()
				.withPermissions(GeneralPermissionType.create_contests)
				.build();

		OffsetDateTime time = OffsetDateTime.now();
		mockMvc.perform(
				fileUpload("/api/contests/create")
						.param("name", "testContest" + TestUtils.generateId())
						.param("duration",
								Duration.ofMinutes(5).toMillis() + "")
						.param("startTime", time.toString())
						.param("showFullTestsDuringContest", "false")
						.accept(MediaType.APPLICATION_JSON)
						.with(user(user)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status").value("OK"))
				.andReturn();
	}

	@Test
	@Rollback
	@Transactional
	public void testContestCreationWithoutNeededPriviliges() throws Exception {

		User user = testUserFactory.user()
				.withoutPermissions(GeneralPermissionType.create_contests)
				.build();

		OffsetDateTime time = OffsetDateTime.now();
		mockMvc.perform(
				fileUpload("/api/contests/create")
						.param("name", "testContest" + TestUtils.generateId())
						.param("duration",
								Duration.ofMinutes(5).toMillis() + "")
						.param("startTime", time.toString())
						.param("showFullTestsDuringContest", "false")
						.accept(MediaType.APPLICATION_JSON)
						.with(user(user)))
				.andExpect(status().isForbidden())
				.andReturn();
	}

	@Test
	@Rollback
	@Transactional
	public void testContestAclManagementWithPermission() throws Exception {

		User user = testUserFactory.user()
				.build();

		Contest contest = testContestFactory.contest()
				.permit(user, ContestPermissionType.manage_acl)
				.build();

		Gson gson = new Gson();

		mockMvc.perform(
				put("/api/contest/{contest}/acl", contest.getId())
						.content(gson.toJson(ImmutableMap
								.builder()
								.put("manage_acl", Lists.from(user.getId()))
								.put("add_task", Lists.from(user.getId()))
								.build()))
						.contentType(MediaType.APPLICATION_JSON)
						.with(user(user)))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();
	}

	@Test
	@Rollback
	@Transactional
	public void testContestAclManagementWithoutPermission() throws Exception {

		User user = testUserFactory.user()
				.build();

		Contest contest = testContestFactory.contest()
				.build();

		Gson gson = new Gson();

		mockMvc.perform(
				put("/api/contest/{contest}/acl", contest.getId())
						.content(gson.toJson(ImmutableMap
								.builder()
								.put("manage_acl", Lists.from(user.getId()))
								.put("add_task", Lists.from(user.getId()))
								.build()))
						.contentType(MediaType.APPLICATION_JSON)
						.with(user(user)))
				.andDo(print())
				.andExpect(status().isForbidden())
				.andReturn();
	}

}
