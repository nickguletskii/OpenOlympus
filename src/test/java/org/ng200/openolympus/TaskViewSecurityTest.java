package org.ng200.openolympus;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.ng200.openolympus.TaskViewSecurityTestCombination.ContestStatus;
import org.ng200.openolympus.TestContestFactory.TestContestBuilder;
import org.ng200.openolympus.jooq.enums.ContestPermissionType;
import org.ng200.openolympus.jooq.enums.GeneralPermissionType;
import org.ng200.openolympus.jooq.enums.TaskPermissionType;
import org.ng200.openolympus.jooq.tables.pojos.Contest;
import org.ng200.openolympus.jooq.tables.pojos.Task;
import org.ng200.openolympus.jooq.tables.pojos.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

@RunWith(Parameterized.class)
@WebIntegrationTest
@TestPropertySource(value = {
								"classpath:test-web.properties",
								"file:secret.properties"
})
@SpringApplicationConfiguration(Application.class)
@EnableTransactionManagement
@Transactional
@TransactionConfiguration(defaultRollback=true)

public class TaskViewSecurityTest {
	@ClassRule
	public static final SpringClassRule SPRING_CLASS_RULE = new SpringClassRule();
	@Rule
	public final SpringMethodRule springMethodRule = new SpringMethodRule();

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
	private TaskViewSecurityTestCombination combo;

	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders
				.webAppContextSetup(this.wac)
				.apply(SecurityMockMvcConfigurers.springSecurity())
				.build();
	}

	@Parameters(name="{index}: {0}")
	public static List<TaskViewSecurityTestCombination[]> data() {
		return TaskViewSecurityTestCombination.allCombinations()
				.map(x -> new TaskViewSecurityTestCombination[] {
																	x
		})
				.collect(Collectors.toList());
	}

	public TaskViewSecurityTest(TaskViewSecurityTestCombination combo) {
		this.combo = combo;
	}

	@Test
	@Rollback
	@Transactional
	public void testTaskViewAcl()
			throws Exception {
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
	}
}
