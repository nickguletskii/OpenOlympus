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

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.time.Duration;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashSet;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.ng200.openolympus.cerberus.SolutionResult.Result;
import org.ng200.openolympus.model.Contest;
import org.ng200.openolympus.model.Solution;
import org.ng200.openolympus.model.Task;
import org.ng200.openolympus.model.User;
import org.ng200.openolympus.model.Verdict;
import org.ng200.openolympus.repositories.ContestRepository;
import org.ng200.openolympus.repositories.SolutionRepository;
import org.ng200.openolympus.services.ContestService;
import org.ng200.openolympus.services.SolutionService;
import org.ng200.openolympus.services.TaskService;
import org.ng200.openolympus.services.TestingService;
import org.ng200.openolympus.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.google.common.collect.ImmutableSet;
import com.jayway.jsonpath.JsonPath;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@TestPropertySource(properties = {
		"spring.jpa.hibernate.ddl-auto:create",
		"enableCaptcha:false",
		"emailHost:",
		"emailConfirmationEnabled:false",
		"hibernate.show_sql:true",
		"hibernate.format_sql:true",
		"spring.jpa.hibernate.show_sql:true",
		"spring.jpa.hibernate.format_sql:true",
		"log4j.logger.org.hibernate.SQL=DEBUG,C1,R1"
})
@WebAppConfiguration
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ContestTest {
	private final MediaType contentType = new MediaType(
			MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

	private static int id = 0;

	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Value("${spring.jpa.hibernate.ddl-auto:''}")
	private String test;

	@Autowired
	private TestUtilities testUtilities;

	@Autowired
	private UserService userService;

	@Autowired
	private TaskService taskService;

	@Autowired
	private TestingService testingService;

	@Autowired
	private ContestService contestService;

	@Autowired
	private ContestRepository contestRepository;

	@Autowired
	private SolutionService solutionService;

	@Autowired
	private SolutionRepository solutionRepository;

	private User testUser1;

	private User testUser2;

	private User testUser3;

	private User testUserNotInContest;

	private User testUser4;

	private User testUser5;

	@Test
	public void checkContestNormalTimingsRatingAndTestingFinished()
			throws Exception {
		final Contest contest = this.createContestUsingAPI(1000000);
		final Task task1 = this.createDummyTask();
		final Task task2 = this.createDummyTask();
		contest.setTasks(ImmutableSet.<Task> builder().add(task1).build());
		this.contestService.saveContest(contest);

		this.contestService.addContestParticipant(contest, this.testUser1);
		this.contestService.addContestParticipant(contest, this.testUser2);
		this.contestService.addContestParticipant(contest, this.testUser3);
		this.contestService.addContestParticipant(contest, this.testUser4);
		this.contestService.addContestParticipant(contest, this.testUser5);

		long time = System.currentTimeMillis();
		time = this.dummyData(task1, task2, time);

		this.mockMvc
				.perform(
						MockMvcRequestBuilders.get("/api/contest/"
								+ contest.getId() + "/completeResults"))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(
						MockMvcResultMatchers.content().contentType(
								this.contentType))
				.andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
				.andExpect(
						MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(5)))
				.andExpect(
						MockMvcResultMatchers.jsonPath("$[0].username").value(
								"test2"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].rank").value(1))
				.andExpect(
						MockMvcResultMatchers.jsonPath("$[1].username").value(
								"test3"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[1].rank").value(2))
				.andExpect(
						MockMvcResultMatchers.jsonPath("$[2].username").value(
								"test4"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[2].rank").value(2))
				.andExpect(
						MockMvcResultMatchers.jsonPath("$[3].username").value(
								"test1"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[3].rank").value(4))
				.andExpect(
						MockMvcResultMatchers.jsonPath("$[4].username").value(
								"test5"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[4].rank").value(5));

		this.mockMvc
				.perform(
						MockMvcRequestBuilders.get("/api/contest/"
								+ contest.getId() + "/testingFinished"))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().string("true"));

		this.createDummySolution(time++, task1, this.testUser1, 20, 100, false);

		this.mockMvc
				.perform(
						MockMvcRequestBuilders.get("/api/contest/"
								+ contest.getId() + "/testingFinished"))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().string("false"));
	}

	@Test
	public void checkContestTimeExtensions() throws Exception {
		final Contest contest = this.createContestDirectly(Duration
				.ofSeconds(0));
		final Task task1 = this.createDummyTask();
		final Task task2 = this.createDummyTask();
		contest.setTasks(ImmutableSet.<Task> builder().add(task1).build());
		this.contestService.saveContest(contest);

		this.contestService.addContestParticipant(contest, this.testUser1);
		this.contestService.addContestParticipant(contest, this.testUser2);
		this.contestService.addContestParticipant(contest, this.testUser3);
		this.contestService.addContestParticipant(contest, this.testUser4);
		this.contestService.extendTimeForUser(contest, this.testUser4,
				Duration.ofSeconds(1000));

		long time = System.currentTimeMillis();
		time = this.dummyData(task1, task2, time);

		this.mockMvc
				.perform(
						MockMvcRequestBuilders.get("/api/contest/"
								+ contest.getId() + "/completeResults"))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(
						MockMvcResultMatchers.content().contentType(
								this.contentType))
				.andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
				.andExpect(
						MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(4)))
				.andExpect(
						MockMvcResultMatchers.jsonPath("$[0].username").value(
								"test4"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].rank").value(1))
				.andExpect(
						MockMvcResultMatchers.jsonPath("$[0].score",
								ToStringMatcher.compareBigDecimals("2.00")))
				.andExpect(
						MockMvcResultMatchers.jsonPath("$[1].username").value(
								"test1"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[1].rank").value(2))
				.andExpect(
						MockMvcResultMatchers.jsonPath("$[1].score",
								ToStringMatcher.compareBigDecimals("0.00")))
				.andExpect(
						MockMvcResultMatchers.jsonPath("$[2].username").value(
								"test2"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[2].rank").value(2))
				.andExpect(
						MockMvcResultMatchers.jsonPath("$[3].username").value(
								"test3"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[3].rank").value(2));

		this.mockMvc
				.perform(
						MockMvcRequestBuilders.get("/api/contest/"
								+ contest.getId() + "/testingFinished"))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().string("true"));

		this.createDummySolution(time++, task1, this.testUser4, 20, 100, false);

		this.mockMvc
				.perform(
						MockMvcRequestBuilders.get("/api/contest/"
								+ contest.getId() + "/testingFinished"))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().string("false"));
	}

	public Contest createContestDirectly(Duration duration) throws Exception {
		return this.contestService.saveContest(new Contest(Date.from(Instant
				.now()), duration, "TestContest_" + ContestTest.id++,
				new HashSet<Task>(), false));
	}

	public Contest createContestUsingAPI(int duration) throws Exception {
		// @formatter:off
		final ZonedDateTime now = ZonedDateTime.now();

		final String result = this.mockMvc.perform(MockMvcRequestBuilders.post("/api/contests/create")
				.param("name", "TestContest_" + ContestTest.id++)
				.param("startTime", now.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME))
				.param("duration", Integer.toString(duration)))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(this.contentType))
				.andExpect(MockMvcResultMatchers.jsonPath("$.status").value("OK"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.data.id").exists())
				.andReturn().getResponse().getContentAsString();
		return  this.contestRepository.findOne(Long.valueOf(JsonPath.read(result, "$.data.id").toString()));
		// @formatter:on
	}

	public void createDummySolution(long time, Task task, User user,
			int baseTestScore, int mainTestScore, boolean tested) {
		Solution solution = new Solution(task, user, "", new Date(time));
		if (baseTestScore == 0 && mainTestScore == 0) {
			solution.setTested(tested);
		}
		solution = this.solutionService.saveSolution(solution);
		for (int i = 0; i < baseTestScore; i++) {
			final Verdict verdict = new Verdict(solution, BigDecimal.ONE, "a",
					true);
			verdict.setTested(tested);
			verdict.setScore(BigDecimal.ONE);
			verdict.setStatus(Result.OK);
			this.solutionService.saveVerdict(verdict);
		}
		for (int i = 0; i < mainTestScore; i++) {
			final Verdict verdict = new Verdict(solution, BigDecimal.ONE, "a",
					false);
			verdict.setTested(tested);
			verdict.setScore(BigDecimal.ONE);
			verdict.setStatus(Result.OK);
			this.solutionService.saveVerdict(verdict);
		}

		Assert.assertEquals(this.solutionRepository.findOne(solution.getId())
				.isTested(), tested);
	}

	public Task createDummyTask() {
		Task task = new Task();
		task.setDescriptionFile(null);
		task.setPublished(true);
		task.setTaskLocation(null);
		task.setTimeAdded(Date.from(Instant.now()));
		task = this.taskService.saveTask(task);
		return task;
	}

	private long dummyData(Task task1, Task task2, long time) {
		this.createDummySolution(time++, task1, this.testUser3, 0, 0, true);
		this.createDummySolution(time++, task1, this.testUser3, 1, 2, true);
		this.createDummySolution(time++, task1, this.testUser1, 1, 0, true);
		this.createDummySolution(time++, task1, this.testUser2, 0, 2, true);
		this.createDummySolution(time++, task1, this.testUser3, 0, 2, true);
		this.createDummySolution(time++, task1, this.testUser2, 0, 1, true);
		this.createDummySolution(time++, task1, this.testUser2, 3, 2, true);
		this.createDummySolution(time++, task1, this.testUser2, 0, 3, true);
		this.createDummySolution(time++, task1, this.testUser2, 20, 50, true);
		this.createDummySolution(time++, task1, this.testUser4, 0, 0, true);
		this.createDummySolution(time++, task1, this.testUser4, 1, 2, true);
		this.createDummySolution(time++, task1, this.testUser4, 0, 2, true);
		this.createDummySolution(time++, task1, this.testUserNotInContest, 20,
				50, true);
		this.createDummySolution(time++, task2, this.testUserNotInContest, 20,
				100, true);
		this.createDummySolution(time++, task1, this.testUserNotInContest, 20,
				100, false);
		this.createDummySolution(time++, task2, this.testUser1, 20, 100, false);
		return time;
	}

	@Before
	public void setup() throws Exception {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(
				this.webApplicationContext).build();
		Application.setupContext(this.webApplicationContext);
		this.testingService.shutdownNow();

		this.testUtilities.logInAsAdmin();
		this.testUser1 = this.testUtilities.createTestUser("test1", "test1");
		this.testUser2 = this.testUtilities.createTestUser("test2", "test2");
		this.testUser3 = this.testUtilities.createTestUser("test3", "test3");
		this.testUser4 = this.testUtilities.createTestUser("test4", "test4");
		this.testUser5 = this.testUtilities.createTestUser("test5", "test5");
		this.testUserNotInContest = this.testUtilities.createTestUser(
				"testNotInContest", "testNotInContest");
	}
}
