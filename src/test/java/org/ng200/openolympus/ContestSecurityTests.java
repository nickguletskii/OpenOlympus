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

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import java.time.Duration;
import java.time.OffsetDateTime;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ng200.openolympus.cerberus.util.Lists;
import org.ng200.openolympus.jooq.enums.ContestPermissionType;
import org.ng200.openolympus.jooq.enums.GeneralPermissionType;
import org.ng200.openolympus.jooq.enums.TaskPermissionType;
import org.ng200.openolympus.jooq.tables.pojos.Contest;
import org.ng200.openolympus.jooq.tables.pojos.Task;
import org.ng200.openolympus.jooq.tables.pojos.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
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
@WebIntegrationTest
@TestPropertySource(value = {
								"classpath:test-web.properties",
								"file:secret.properties"
})
@SpringApplicationConfiguration(Application.class)
@EnableTransactionManagement
@Transactional
@Rollback
public class ContestSecurityTests {

	@Autowired
	private WebApplicationContext wac;
	private MockMvc mockMvc;

	@Autowired
	private TestUserFactory testUserFactory;

	@Autowired
	private TestContestFactory testContestFactory;

	@Autowired
	private TestTaskFactory testTaskFactory;

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

						.with(csrf())
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

						.with(csrf())
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

						.with(csrf())
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
		this.mockMvc
				.perform(
						MockMvcRequestBuilders
								.fileUpload("/api/contests/create")
								.param("name",
										"testContest" + TestUtils.generateId())
								.param("duration",
										Duration.ofMinutes(5).toMillis() + "")
								.param("startTime", time.toString())
								.param("showFullTestsDuringContest", "false")
								.accept(MediaType.APPLICATION_JSON)

								.with(csrf())
								.with(SecurityMockMvcRequestPostProcessors
										.user(user)))
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

						.with(csrf())
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

						.with(csrf())
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

						.with(csrf())
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

						.with(csrf())
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

						.with(csrf())
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

						.with(csrf())
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

						.with(csrf())
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

						.with(csrf())
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

						.with(csrf())
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

						.with(csrf())
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

						.with(csrf())
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

						.with(csrf())
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

						.with(csrf())
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

						.with(csrf())
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

						.with(csrf())
						.with(SecurityMockMvcRequestPostProcessors.user(user)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
	}

	@Test
	@Rollback
	@Transactional
	public void testContestTaskAdditionWithoutPermissionWithoutTaskPermission()
			throws Exception {

		final User user = this.testUserFactory.user()
				.build();

		final Contest contest = this.testContestFactory.contest()
				.build();

		final Task task = this.testTaskFactory.task()
				.build();

		this.mockMvc.perform(
				MockMvcRequestBuilders
						.post("/api/contest/{contest}/addTask",
								contest.getId())
						.param("name", task.getName())
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON)

						.with(csrf())
						.with(SecurityMockMvcRequestPostProcessors.user(user)))
				.andExpect(MockMvcResultMatchers.status().isForbidden())
				.andReturn();
	}

	@Test
	@Rollback
	@Transactional
	public void testContestTaskAdditionWithoutPermissionWithTaskPermission()
			throws Exception {

		final User user = this.testUserFactory.user()
				.build();

		final Contest contest = this.testContestFactory.contest()
				.build();

		final Task task = this.testTaskFactory.task()
				.permit(user, TaskPermissionType.add_to_contest)
				.build();

		this.mockMvc.perform(
				MockMvcRequestBuilders
						.post("/api/contest/{contest}/addTask",
								contest.getId())
						.param("name", task.getName())
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON)

						.with(csrf())
						.with(SecurityMockMvcRequestPostProcessors.user(user)))
				.andExpect(MockMvcResultMatchers.status().isForbidden())
				.andReturn();
	}

	@Test
	@Rollback
	@Transactional
	public void testContestTaskAdditionWithPermissionWithoutTaskPermission()
			throws Exception {
		final User user = this.testUserFactory.user()
				.build();

		final Contest contest = this.testContestFactory.contest()
				.permit(user, ContestPermissionType.add_task)
				.build();

		final Task task = this.testTaskFactory.task()
				.build();

		this.mockMvc.perform(
				MockMvcRequestBuilders
						.post("/api/contest/{contest}/addTask",
								contest.getId())
						.param("name", task.getName())
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON)

						.with(csrf())
						.with(SecurityMockMvcRequestPostProcessors.user(user)))
				.andExpect(MockMvcResultMatchers.status().isForbidden())
				.andReturn();
	}

	@Test
	@Rollback
	@Transactional
	public void testContestTaskAdditionWithPermissionWithTaskPermission()
			throws Exception {
		final User user = this.testUserFactory.user()
				.build();

		final Contest contest = this.testContestFactory.contest()
				.permit(user, ContestPermissionType.add_task)
				.build();

		final Task task = this.testTaskFactory.task()
				.permit(user, TaskPermissionType.add_to_contest)
				.build();

		this.mockMvc.perform(
				MockMvcRequestBuilders
						.post("/api/contest/{contest}/addTask",
								contest.getId())
						.param("name", task.getName())
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON)

						.with(csrf())
						.with(SecurityMockMvcRequestPostProcessors.user(user)))
				.andExpect(MockMvcResultMatchers.status().isForbidden())
				.andReturn();
	}

	@Test
	@Rollback
	@Transactional
	public void testContestTaskRemovalWithoutPermission()
			throws Exception {

		final User user = this.testUserFactory.user()
				.build();

		final Task task = this.testTaskFactory.task()
				.build();

		final Contest contest = this.testContestFactory.contest()
				.withTasks(task)
				.build();

		this.mockMvc.perform(
				MockMvcRequestBuilders
						.delete("/api/contest/{contest}/removeTask",
								contest.getId())
						.param("task", task.getId().toString())
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON)

						.with(csrf())
						.with(SecurityMockMvcRequestPostProcessors.user(user)))
				.andExpect(MockMvcResultMatchers.status().isForbidden())
				.andReturn();
	}

	@Test
	@Rollback
	@Transactional
	public void testContestTaskRemovalWithPermission()
			throws Exception {
		final User user = this.testUserFactory.user()
				.build();

		final Task task = this.testTaskFactory.task()
				.build();

		final Contest contest = this.testContestFactory.contest()
				.withTasks(task)
				.permit(user, ContestPermissionType.remove_task)
				.build();

		this.mockMvc.perform(
				MockMvcRequestBuilders
						.delete("/api/contest/{contest}/removeTask",
								contest.getId())
						.param("task", task.getId().toString())
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON)

						.with(csrf())
						.with(SecurityMockMvcRequestPostProcessors.user(user)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
	}

}
