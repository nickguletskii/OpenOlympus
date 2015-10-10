package org.ng200.openolympus;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

import java.time.Duration;
import java.time.OffsetDateTime;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ng200.openolympus.jooq.enums.GeneralPermissionType;
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

}
