package org.ng200.openolympus;

import static org.ng200.openolympus.ToStringMatcher.compareBigDecimals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.math.BigDecimal;
import java.nio.charset.Charset;
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
	private MediaType contentType = new MediaType(
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

	@Before
	public void setup() throws Exception {
		this.mockMvc = webAppContextSetup(webApplicationContext).build();
		Application.setupContext(webApplicationContext);
		testingService.shutdownNow();

		testUtilities.logInAsAdmin();
		testUser1 = testUtilities.createTestUser("test1", "test1");
		testUser2 = testUtilities.createTestUser("test2", "test2");
		testUser3 = testUtilities.createTestUser("test3", "test3");
		testUser4 = testUtilities.createTestUser("test4", "test4");
		testUser5 = testUtilities.createTestUser("test5", "test5");
		testUserNotInContest = testUtilities.createTestUser("testNotInContest",
				"testNotInContest");
	}

	@Test
	public void checkContestNormalTimingsRatingAndTestingFinished()
			throws Exception {
		Contest contest = createContestUsingAPI(100);
		Task task1 = createDummyTask();
		Task task2 = createDummyTask();
		contest.setTasks(ImmutableSet.<Task> builder().add(task1).build());
		contestService.saveContest(contest);

		this.contestService.addContestParticipant(contest, testUser1);
		this.contestService.addContestParticipant(contest, testUser2);
		this.contestService.addContestParticipant(contest, testUser3);
		this.contestService.addContestParticipant(contest, testUser4);
		this.contestService.addContestParticipant(contest, testUser5);

		long time = System.currentTimeMillis();
		time = dummyData(task1, task2, time);

		mockMvc.perform(
				get("/api/contest/" + contest.getId() + "/completeResults"))
				.andDo(print()).andExpect(status().isOk())
				.andExpect(content().contentType(contentType))
				.andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$", Matchers.hasSize(5)))
				.andExpect(jsonPath("$[0].username").value("test2"))
				.andExpect(jsonPath("$[0].rank").value(1))
				.andExpect(jsonPath("$[1].username").value("test3"))
				.andExpect(jsonPath("$[1].rank").value(2))
				.andExpect(jsonPath("$[2].username").value("test4"))
				.andExpect(jsonPath("$[2].rank").value(2))
				.andExpect(jsonPath("$[3].username").value("test1"))
				.andExpect(jsonPath("$[3].rank").value(4))
				.andExpect(jsonPath("$[4].username").value("test5"))
				.andExpect(jsonPath("$[4].rank").value(5));

		mockMvc.perform(
				get("/api/contest/" + contest.getId() + "/testingFinished"))
				.andDo(print()).andExpect(status().isOk())
				.andExpect(content().string("true"));

		createDummySolution(time++, task1, testUser1, 20, 100, false);

		mockMvc.perform(
				get("/api/contest/" + contest.getId() + "/testingFinished"))
				.andDo(print()).andExpect(status().isOk())
				.andExpect(content().string("false"));
	}

	@Test
	public void checkContestTimeExtensions() throws Exception {
		Contest contest = createContestDirectly(0);
		Task task1 = createDummyTask();
		Task task2 = createDummyTask();
		contest.setTasks(ImmutableSet.<Task> builder().add(task1).build());
		contestService.saveContest(contest);

		this.contestService.addContestParticipant(contest, testUser1);
		this.contestService.addContestParticipant(contest, testUser2);
		this.contestService.addContestParticipant(contest, testUser3);
		this.contestService.addContestParticipant(contest, testUser4);
		this.contestService.extendTimeForUser(contest, testUser4, 1000);

		long time = System.currentTimeMillis();
		time = dummyData(task1, task2, time);

		mockMvc.perform(
				get("/api/contest/" + contest.getId() + "/completeResults"))
				.andDo(print()).andExpect(status().isOk())
				.andExpect(content().contentType(contentType))
				.andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$", Matchers.hasSize(4)))
				.andExpect(jsonPath("$[0].username").value("test4"))
				.andExpect(jsonPath("$[0].rank").value(1))
				.andExpect(jsonPath("$[0].score", compareBigDecimals("2.00")))
				.andExpect(jsonPath("$[1].username").value("test2"))
				.andExpect(jsonPath("$[1].rank").value(2))
				.andExpect(jsonPath("$[1].score", compareBigDecimals("0.00")))
				.andExpect(jsonPath("$[2].username").value("test3"))
				.andExpect(jsonPath("$[2].rank").value(2))
				.andExpect(jsonPath("$[3].username").value("test1"))
				.andExpect(jsonPath("$[3].rank").value(2));

		mockMvc.perform(
				get("/api/contest/" + contest.getId() + "/testingFinished"))
				.andDo(print()).andExpect(status().isOk())
				.andExpect(content().string("true"));

		createDummySolution(time++, task1, testUser4, 20, 100, false);

		mockMvc.perform(
				get("/api/contest/" + contest.getId() + "/testingFinished"))
				.andDo(print()).andExpect(status().isOk())
				.andExpect(content().string("false"));
	}

	private long dummyData(Task task1, Task task2, long time) {
		createDummySolution(time++, task1, testUser3, 0, 0, true);
		createDummySolution(time++, task1, testUser3, 1, 2, true);
		createDummySolution(time++, task1, testUser1, 1, 0, true);
		createDummySolution(time++, task1, testUser2, 0, 2, true);
		createDummySolution(time++, task1, testUser3, 0, 2, true);
		createDummySolution(time++, task1, testUser2, 0, 1, true);
		createDummySolution(time++, task1, testUser2, 3, 2, true);
		createDummySolution(time++, task1, testUser2, 0, 3, true);
		createDummySolution(time++, task1, testUser2, 20, 50, true);
		createDummySolution(time++, task1, testUser4, 0, 0, true);
		createDummySolution(time++, task1, testUser4, 1, 2, true);
		createDummySolution(time++, task1, testUser4, 0, 2, true);
		createDummySolution(time++, task1, testUserNotInContest, 20, 50, true);
		createDummySolution(time++, task2, testUserNotInContest, 20, 100, true);
		createDummySolution(time++, task1, testUserNotInContest, 20, 100, false);
		createDummySolution(time++, task2, testUser1, 20, 100, false);
		return time;
	}

	public void createDummySolution(long time, Task task, User user,
			int baseTestScore, int mainTestScore, boolean tested) {
		Solution solution = new Solution(task, user, "", new Date(time));
		if (baseTestScore == 0 && mainTestScore == 0)
			solution.setTested(tested);
		solution = solutionService.saveSolution(solution);
		for (int i = 0; i < baseTestScore; i++) {
			Verdict verdict = new Verdict(solution, BigDecimal.ONE, "a", true);
			verdict.setTested(tested);
			verdict.setScore(BigDecimal.ONE);
			verdict.setStatus(Result.OK);
			solutionService.saveVerdict(verdict);
		}
		for (int i = 0; i < mainTestScore; i++) {
			Verdict verdict = new Verdict(solution, BigDecimal.ONE, "a", false);
			verdict.setTested(tested);
			verdict.setScore(BigDecimal.ONE);
			verdict.setStatus(Result.OK);
			solutionService.saveVerdict(verdict);
		}

		Assert.assertEquals(solutionRepository.findOne(solution.getId())
				.isTested(), tested);
	}

	public Task createDummyTask() {
		Task task = new Task();
		task.setDescriptionFile(null);
		task.setPublished(true);
		task.setTaskLocation(null);
		task.setTimeAdded(Date.from(Instant.now()));
		task = taskService.saveTask(task);
		return task;
	}

	public Contest createContestUsingAPI(int duration) throws Exception {
		// @formatter:off
		ZonedDateTime now = ZonedDateTime.now();

		String result = mockMvc.perform(post("/api/contests/create")
										.param("name", "TestContest_" + id++)
										.param("startTime", now.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME))
										.param("duration", Integer.toString(duration)))
						.andDo(print())
						.andExpect(status().isOk())
				        .andExpect(content().contentType(contentType))
				        .andExpect(jsonPath("$.status").value("OK"))
				        .andExpect(jsonPath("$.data.id").exists())
				        .andReturn().getResponse().getContentAsString();
		return  contestRepository.findOne(Long.valueOf(JsonPath.read(result, "$.data.id").toString()));
		// @formatter:on
	}

	public Contest createContestDirectly(int duration) throws Exception {
		return contestService.saveContest(new Contest(Date.from(Instant.now()),
				duration, "TestContest_" + id++, new HashSet<Task>()));
	}
}
