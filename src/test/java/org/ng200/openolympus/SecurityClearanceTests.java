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

import static org.junit.Assert.assertThat;

import java.io.ByteArrayOutputStream;

import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ng200.openolympus.annotations.SecurityClearanceRequired;
import org.ng200.openolympus.config.JacksonConfiguration;
import org.ng200.openolympus.jooq.tables.pojos.User;
import org.ng200.openolympus.security.SecurityClearanceUnlessPredicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = JacksonConfiguration.class)
@PropertySource(value = {
							"classpath:openolympus.properties",
							"file:openolympus.properties",
							"classpath:application.properties",
							"file:application.properties",
							"classpath:secret.properties",
							"file:secret.properties"
}, ignoreResourceNotFound = true)
public class SecurityClearanceTests {
	private static final Logger logger = LoggerFactory
			.getLogger(SecurityClearanceTests.class);

	@JsonFilter("simple-strict-security")
	public static class AnnotatedObject {
		private String annonymousField = "annonymousFieldValue";

		@SecurityClearanceRequired(value = SecurityClearanceType.ANNONYMOUS)
		public String getAnnonymousField() {
			return annonymousField;
		}

		public void setAnnonymousField(String annonymousField) {
			this.annonymousField = annonymousField;
		}

		private String loggedInField = "loggedInFieldValue";

		@SecurityClearanceRequired(value = SecurityClearanceType.LOGGED_IN)
		public String getLoggedInField() {
			return loggedInField;
		}

		public void setLoggedInField(String loggedInField) {
			this.loggedInField = loggedInField;
		}

		private String roleField = "roleFieldValue";

		@SecurityClearanceRequired(value = SecurityClearanceType.APPROVED_USER)
		public String getRoleField() {
			return roleField;
		}

		public void setRoleField(String roleField) {
			this.roleField = roleField;
		}

		private String unlessField = "unlessFieldValue";

		@SecurityClearanceRequired(value = SecurityClearanceType.SUPERUSER, unless = TestSecurityClearanceUnlessPredicate.class)
		public String getUnlessField() {
			return unlessField;
		}

		public void setUnlessField(String unlessField) {
			this.unlessField = unlessField;
		}

	}

	public static class TestSecurityClearanceUnlessPredicate
			implements SecurityClearanceUnlessPredicate {

		@Override
		public boolean objectMatches(User user, Object obj) {
			if (user == null)
				return false;
			return user.getUsername().equals("testPredicate");
		}

	}

	@Autowired
	private ObjectMapper jacksonObjectMapper;

	@Test
	public void checkAnnonymous() throws Exception {
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		JsonFactory factory = new JsonFactory();
		JsonGenerator generator = factory.createGenerator(bout);
		jacksonObjectMapper.writeValue(generator, new AnnotatedObject());

		assertThat(
				"Output should contain fields that are visible to annonymous users: ",
				bout.toString(),
				CoreMatchers.containsString("annonymousField"));
		assertThat(
				"Output should contain fields that are visible to annonymous users: ",
				bout.toString(),
				CoreMatchers.containsString("annonymousFieldValue"));

		assertThat(
				"Output should not contain fields that are visible to logged in users: ",
				bout.toString(),
				CoreMatchers.not(CoreMatchers.containsString("loggedInField")));
		assertThat(
				"Output should not contain fields that are visible to logged in users: ",
				bout.toString(),
				CoreMatchers.not(
						CoreMatchers.containsString("loggedInFieldValue")));

		assertThat(
				"Output shouldn't contain fields that are visible to user with username 'testPredicate': ",
				bout.toString(),
				CoreMatchers.not(CoreMatchers.containsString("unlessField")));
		assertThat(
				"Output shouldn't contain fields that are visible to user with username 'testPredicate': ",
				bout.toString(),
				CoreMatchers
						.not(CoreMatchers.containsString("unlessFieldValue")));
	}

	@Test
	@WithOpenOlympusMockUser(username = "test", approved = false, superuser = false)
	public void checkLoggedIn() throws Exception {
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		JsonFactory factory = new JsonFactory();
		JsonGenerator generator = factory.createGenerator(bout);
		jacksonObjectMapper.writeValue(generator, new AnnotatedObject());

		assertThat(
				"Output should contain fields that are visible to annonymous users: ",
				bout.toString(),
				CoreMatchers.containsString("annonymousField"));
		assertThat(
				"Output should contain fields that are visible to annonymous users: ",
				bout.toString(),
				CoreMatchers.containsString("annonymousFieldValue"));

		assertThat(
				"Output should contain fields that are visible to logged in users: ",
				bout.toString(),
				CoreMatchers.containsString("loggedInField"));
		assertThat(
				"Output should contain fields that are visible to logged in users: ",
				bout.toString(),
				CoreMatchers.containsString("loggedInFieldValue"));

		assertThat(
				"Output shouldn't contain fields that are visible to users with role: ",
				bout.toString(),
				CoreMatchers
						.not(CoreMatchers.containsString("roleField")));
		assertThat(
				"Output shouldn't contain fields that are visible to users with role: ",
				bout.toString(),
				CoreMatchers
						.not(CoreMatchers.containsString("roleFieldValue")));

		assertThat(
				"Output shouldn't contain fields that are visible to user with username 'testPredicate': ",
				bout.toString(),
				CoreMatchers.not(CoreMatchers.containsString("unlessField")));
		assertThat(
				"Output shouldn't contain fields that are visible to user with username 'testPredicate': ",
				bout.toString(),
				CoreMatchers
						.not(CoreMatchers.containsString("unlessFieldValue")));
	}

	@Test
	@WithOpenOlympusMockUser(username = "test", approved = true, superuser = false)
	public void checkApproved() throws Exception {
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		JsonFactory factory = new JsonFactory();
		JsonGenerator generator = factory.createGenerator(bout);
		jacksonObjectMapper.writeValue(generator, new AnnotatedObject());

		assertThat(
				"Output should contain fields that are visible to annonymous users: ",
				bout.toString(),
				CoreMatchers.containsString("annonymousField"));
		assertThat(
				"Output should contain fields that are visible to annonymous users: ",
				bout.toString(),
				CoreMatchers.containsString("annonymousFieldValue"));

		assertThat(
				"Output should contain fields that are visible to logged in users: ",
				bout.toString(),
				CoreMatchers.containsString("loggedInField"));
		assertThat(
				"Output should contain fields that are visible to logged in users: ",
				bout.toString(),
				CoreMatchers.containsString("loggedInFieldValue"));

		assertThat(
				"Output should contain fields that are visible to users with role: ",
				bout.toString(),
				CoreMatchers.containsString("roleField"));
		assertThat(
				"Output should contain fields that are visible to users with role: ",
				bout.toString(),
				CoreMatchers.containsString("roleFieldValue"));

		assertThat(
				"Output shouldn't contain fields that are visible to user with username 'testPredicate': ",
				bout.toString(),
				CoreMatchers.not(CoreMatchers.containsString("unlessField")));
		assertThat(
				"Output shouldn't contain fields that are visible to user with username 'testPredicate': ",
				bout.toString(),
				CoreMatchers
						.not(CoreMatchers.containsString("unlessFieldValue")));
	}

	@Test
	@WithOpenOlympusMockUser(username = "testPredicate", approved = true, superuser = false)
	public void checkUnless() throws Exception {
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		JsonFactory factory = new JsonFactory();
		JsonGenerator generator = factory.createGenerator(bout);
		jacksonObjectMapper.writeValue(generator, new AnnotatedObject());

		assertThat(
				"Output should contain fields that are visible to annonymous users: ",
				bout.toString(),
				CoreMatchers.containsString("annonymousField"));
		assertThat(
				"Output should contain fields that are visible to annonymous users: ",
				bout.toString(),
				CoreMatchers.containsString("annonymousFieldValue"));

		assertThat(
				"Output should contain fields that are visible to logged in users: ",
				bout.toString(),
				CoreMatchers.containsString("loggedInField"));
		assertThat(
				"Output should contain fields that are visible to logged in users: ",
				bout.toString(),
				CoreMatchers.containsString("loggedInFieldValue"));

		assertThat(
				"Output should contain fields that are visible to users with role: ",
				bout.toString(),
				CoreMatchers.containsString("roleField"));
		assertThat(
				"Output should contain fields that are visible to users with role: ",
				bout.toString(),
				CoreMatchers.containsString("unlessFieldValue"));

		assertThat(
				"Output should contain fields that are visible to user with username 'testPredicate': ",
				bout.toString(),
				CoreMatchers.containsString("unlessField"));
		assertThat(
				"Output should contain fields that are visible to user with username 'testPredicate': ",
				bout.toString(),
				CoreMatchers.containsString("unlessFieldValue"));
	}
}
