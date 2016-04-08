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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Random;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ng200.openolympus.TaskViewSecurityTestCombination.ContestStatus;
import org.ng200.openolympus.TestContestFactory.TestContestBuilder;
import org.ng200.openolympus.TestTaskFactory.TestTaskBuilder;
import org.ng200.openolympus.TestUserFactory.TestUserBuilder;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
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
public class TaskSecurityTests {

	@Autowired
	private WebApplicationContext wac;
	private MockMvc mockMvc;

	@Autowired
	private TestUserFactory testUserFactory;

	@Autowired
	private TestContestFactory testContestFactory;

	@Autowired
	private TestTaskFactory testTaskFactory;

	private Random random = new Random();

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
	public void testTaskCreationWithPermission() throws Exception {

		final User user = this.testUserFactory.user()
				.withPermissions(GeneralPermissionType.task_supervisor)
				.build();

		this.mockMvc.perform(
				MockMvcRequestBuilders
						.fileUpload("/api/task/create")
						.file(createJudgeFile())
						.param("name", "testTask" + TestUtils.generateId())
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.MULTIPART_FORM_DATA)

						.with(csrf())
						.with(SecurityMockMvcRequestPostProcessors.user(user)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
	}

	@Test
	@Rollback
	@Transactional
	public void testTaskCreationWithoutPermission() throws Exception {

		final User user = this.testUserFactory.user()
				.withoutPermissions(GeneralPermissionType.task_supervisor)
				.build();

		this.mockMvc.perform(
				MockMvcRequestBuilders
						.fileUpload("/api/task/create")
						.file(createJudgeFile())
						.param("name", "testTask" + TestUtils.generateId())
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.MULTIPART_FORM_DATA)

						.with(csrf())
						.with(SecurityMockMvcRequestPostProcessors.user(user)))
				.andExpect(MockMvcResultMatchers.status().isForbidden())
				.andReturn();
	}

	@Test
	@Rollback
	@Transactional
	public void testTaskModificationWithPermission() throws Exception {

		final User user = this.testUserFactory.user()
				.build();

		final Task task = this.testTaskFactory.task()
				.permit(user, TaskPermissionType.modify)
				.build();

		this.mockMvc.perform(
				MockMvcRequestBuilders
						.get("/api/task/{task}/edit", task.getId())
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON)

						.with(csrf())
						.with(SecurityMockMvcRequestPostProcessors.user(user)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();

		this.mockMvc.perform(
				MockMvcRequestBuilders
						.fileUpload("/api/task/{task}/edit", task.getId())
						.file(createJudgeFile())
						.param("name", "testTask" + TestUtils.generateId())
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.MULTIPART_FORM_DATA)

						.with(csrf())
						.with(SecurityMockMvcRequestPostProcessors.user(user)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
	}

	@Test
	@Rollback
	@Transactional
	public void testTaskModificationWithoutPermission() throws Exception {

		final User user = this.testUserFactory.user()
				.build();

		final Task task = this.testTaskFactory.task()
				.deny(user, TaskPermissionType.modify)
				.build();

		this.mockMvc.perform(
				MockMvcRequestBuilders
						.get("/api/task/{task}/edit", task.getId())
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON)

						.with(csrf())
						.with(SecurityMockMvcRequestPostProcessors.user(user)))
				.andExpect(MockMvcResultMatchers.status().isForbidden())
				.andReturn();

		this.mockMvc.perform(
				MockMvcRequestBuilders
						.fileUpload("/api/task/{task}/edit", task.getId())
						.file(createJudgeFile())
						.param("name", "testTask" + TestUtils.generateId())
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.MULTIPART_FORM_DATA)

						.with(csrf())
						.with(SecurityMockMvcRequestPostProcessors.user(user)))
				.andExpect(MockMvcResultMatchers.status().isForbidden())
				.andReturn();
	}

	@Test
	@Rollback
	@Transactional
	public void testTaskRedjudgementWithPermission() throws Exception {

		final User user = this.testUserFactory.user()
				.build();

		final Task task = this.testTaskFactory.task()
				.permit(user, TaskPermissionType.rejudge)
				.build();

		this.mockMvc.perform(
				MockMvcRequestBuilders
						.post("/api/task/{task}/rejudgeTask", task.getId())

						.with(csrf())
						.with(SecurityMockMvcRequestPostProcessors.user(user)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();
	}

	@Test
	@Rollback
	@Transactional
	public void testTaskRedjudgementWithoutPermission() throws Exception {

		final User user = this.testUserFactory.user()
				.build();

		final Task task = this.testTaskFactory.task()
				.deny(user, TaskPermissionType.rejudge)
				.build();

		this.mockMvc.perform(
				MockMvcRequestBuilders
						.post("/api/task/{task}/rejudgeTask", task.getId())
						.with(csrf())
						.with(SecurityMockMvcRequestPostProcessors.user(user)))
				.andExpect(MockMvcResultMatchers.status().isForbidden())
				.andReturn();
	}

	@Test
	@Rollback
	@Transactional
	public void testTaskAclManagementWithPermission() throws Exception {

		final User user = this.testUserFactory.user()
				.build();

		final Task task = this.testTaskFactory.task()
				.permit(user, TaskPermissionType.manage_acl)
				.build();

		final Gson gson = new Gson();

		this.mockMvc.perform(
				MockMvcRequestBuilders
						.put("/api/task/{task}/acl", task.getId())
						.content(gson.toJson(ImmutableMap
								.builder()
								.put("modify", Lists.from(user.getId()))
								.put("rejudge", Lists.from(user.getId()))
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
	public void testTaskAclManagementWithoutPermission() throws Exception {

		final User user = this.testUserFactory.user()
				.build();

		final Task task = this.testTaskFactory.task()
				.deny(user, TaskPermissionType.manage_acl)
				.build();

		final Gson gson = new Gson();

		this.mockMvc.perform(
				MockMvcRequestBuilders
						.put("/api/task/{task}/acl", task.getId())
						.content(gson.toJson(ImmutableMap
								.builder()
								.put("manage_acl", Lists.from(user.getId()))
								.put("rejudge", Lists.from(user.getId()))
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
	public void testTaskViewAcl() throws Exception {
		TaskViewSecurityTestCombination.allCombinations().forEach(combo -> {
			try {
				final User user = this.testUserFactory.user()
						.withPermissions(combo.getUserGeneralPermissions()
								.toArray(new GeneralPermissionType[0]))
						.build();

				final Task task = this.testTaskFactory.task()
						.permit(user, combo.getTaskPermissions()
								.toArray(new TaskPermissionType[0]))
						.build();

				if (combo.getContestStatus() != ContestStatus.NONE) {
					TestContestBuilder contestBuilder = this.testContestFactory
							.contest()
							.permit(user, combo.getContestPermissions()
									.toArray(new ContestPermissionType[0]));

					switch (combo.getContestStatus()) {
					case FINISHED:
						contestBuilder.ended();
						break;
					case HASNT_STARTED:
						contestBuilder.notStarted();
						break;
					case IN_PROGRESS:
						contestBuilder.inProgress();
						break;
					default:
						throw new UnsupportedOperationException();
					}
					if (combo.isTaskInContest()) {
						contestBuilder.withTasks(task);
					}
					final Contest contest = contestBuilder
							.build();
				}
				this.mockMvc.perform(
						MockMvcRequestBuilders
								.get("/api/task/{task}/name", task.getId())

								.with(csrf())
								.with(SecurityMockMvcRequestPostProcessors
										.user(user)))
						.andExpect(combo.isForbidden()
								? MockMvcResultMatchers.status().isForbidden()
								: MockMvcResultMatchers.status().isOk())
						.andReturn();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		});
	}

	private MockMultipartFile createJudgeFile()
			throws IOException, ArchiveException {
		MockMultipartFile multipartFile;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try (
				ArchiveOutputStream aos = new ArchiveStreamFactory()
						.createArchiveOutputStream("zip", bos);) {
			ZipArchiveEntry entry = new ZipArchiveEntry("test");
			int len = 1024;
			byte[] bytes = new byte[len];
			random.nextBytes(bytes);
			entry.setSize(1024);
			aos.putArchiveEntry(entry);
			aos.write(bytes);
			aos.closeArchiveEntry();
		} finally {
			bos.close();
			multipartFile = new MockMultipartFile("judgeFile",
					bos.toByteArray());
		}
		return multipartFile;
	}

}
