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

import java.io.ByteArrayOutputStream;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ng200.openolympus.annotations.SecurityClearanceRequired;
import org.ng200.openolympus.jooq.tables.pojos.User;
import org.ng200.openolympus.security.SecurityClearancePredicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@TestPropertySource(value = {
								"classpath:test-db.properties",
								"file:secret.properties"
})
@SpringApplicationConfiguration(classes = Application.class)
public class JacksonSecurityClearanceTests {

	@JsonFilter("simple-strict-security")
	public static class AnnotatedObject {
		private String anonymousField = "anonymousFieldValue";

		private String loggedInField = "loggedInFieldValue";

		private String roleField = "roleFieldValue";

		private String predicateField = "predicateFieldValue";

		@SecurityClearanceRequired(minimumClearance = SecurityClearanceType.ANONYMOUS)
		public String getanonymousField() {
			return this.anonymousField;
		}

		@SecurityClearanceRequired(minimumClearance = SecurityClearanceType.LOGGED_IN)
		public String getLoggedInField() {
			return this.loggedInField;
		}

		@SecurityClearanceRequired(minimumClearance = SecurityClearanceType.APPROVED_USER, predicates = TestSecurityClearancepredicatePredicate.class)
		public String getPredicateField() {
			return this.predicateField;
		}

		@SecurityClearanceRequired(minimumClearance = SecurityClearanceType.APPROVED_USER)
		public String getRoleField() {
			return this.roleField;
		}

		public void setanonymousField(String anonymousField) {
			this.anonymousField = anonymousField;
		}

		public void setLoggedInField(String loggedInField) {
			this.loggedInField = loggedInField;
		}

		public void setpredicateField(String predicateField) {
			this.predicateField = predicateField;
		}

		public void setRoleField(String roleField) {
			this.roleField = roleField;
		}

	}

	public static class TestSecurityClearancepredicatePredicate
			implements SecurityClearancePredicate {

		@Override
		public SecurityClearanceType getRequiredClearanceForObject(User user,
				Object obj) {
			if (user == null) {
				return SecurityClearanceType.DENIED;
			}
			return user.getUsername().equals("testPredicate")
					? SecurityClearanceType.SUPERUSER
					: SecurityClearanceType.APPROVED_USER;
		}

	}

	@Autowired
	private ObjectMapper jacksonObjectMapper;

	@Test
	public void checkAnonymous() throws Exception {
		final ByteArrayOutputStream bout = new ByteArrayOutputStream();
		final JsonFactory factory = new JsonFactory();
		final JsonGenerator generator = factory.createGenerator(bout);
		this.jacksonObjectMapper.writeValue(generator, new AnnotatedObject());

		Assert.assertThat(
				"Output should contain fields that are visible to anonymous users: ",
				bout.toString(),
				CoreMatchers.containsString("anonymousField"));
		Assert.assertThat(
				"Output should contain fields that are visible to anonymous users: ",
				bout.toString(),
				CoreMatchers.containsString("anonymousFieldValue"));

		Assert.assertThat(
				"Output should not contain fields that are visible to logged in users: ",
				bout.toString(),
				CoreMatchers.not(CoreMatchers.containsString("loggedInField")));
		Assert.assertThat(
				"Output should not contain fields that are visible to logged in users: ",
				bout.toString(),
				CoreMatchers.not(
						CoreMatchers.containsString("loggedInFieldValue")));

		Assert.assertThat(
				"Output shouldn't contain fields that are visible to user with username 'testPredicate': ",
				bout.toString(),
				CoreMatchers
						.not(CoreMatchers.containsString("predicateField")));
		Assert.assertThat(
				"Output shouldn't contain fields that are visible to user with username 'testPredicate': ",
				bout.toString(),
				CoreMatchers
						.not(CoreMatchers
								.containsString("predicateFieldValue")));
	}

	@Test
	@WithOpenOlympusMockUser(username = "test", approved = true, superuser = false)
	public void checkApproved() throws Exception {
		final ByteArrayOutputStream bout = new ByteArrayOutputStream();
		final JsonFactory factory = new JsonFactory();
		final JsonGenerator generator = factory.createGenerator(bout);
		this.jacksonObjectMapper.writeValue(generator, new AnnotatedObject());

		Assert.assertThat(
				"Output should contain fields that are visible to anonymous users: ",
				bout.toString(),
				CoreMatchers.containsString("anonymousField"));
		Assert.assertThat(
				"Output should contain fields that are visible to anonymous users: ",
				bout.toString(),
				CoreMatchers.containsString("anonymousFieldValue"));

		Assert.assertThat(
				"Output should contain fields that are visible to logged in users: ",
				bout.toString(),
				CoreMatchers.containsString("loggedInField"));
		Assert.assertThat(
				"Output should contain fields that are visible to logged in users: ",
				bout.toString(),
				CoreMatchers.containsString("loggedInFieldValue"));

		Assert.assertThat(
				"Output should contain fields that are visible to users with role: ",
				bout.toString(),
				CoreMatchers.containsString("roleField"));
		Assert.assertThat(
				"Output should contain fields that are visible to users with role: ",
				bout.toString(),
				CoreMatchers.containsString("roleFieldValue"));

		Assert.assertThat(
				"Output should contain fields that are visible to user with username 'testPredicate': ",
				bout.toString(), CoreMatchers.containsString("predicateField"));
		Assert.assertThat(
				"Output should contain fields that are visible to user with username 'testPredicate': ",
				bout.toString(), CoreMatchers
						.containsString("predicateFieldValue"));
	}

	@Test
	@WithOpenOlympusMockUser(username = "test", approved = false, superuser = false)
	public void checkLoggedIn() throws Exception {
		final ByteArrayOutputStream bout = new ByteArrayOutputStream();
		final JsonFactory factory = new JsonFactory();
		final JsonGenerator generator = factory.createGenerator(bout);
		this.jacksonObjectMapper.writeValue(generator, new AnnotatedObject());

		Assert.assertThat(
				"Output should contain fields that are visible to anonymous users: ",
				bout.toString(),
				CoreMatchers.containsString("anonymousField"));
		Assert.assertThat(
				"Output should contain fields that are visible to anonymous users: ",
				bout.toString(),
				CoreMatchers.containsString("anonymousFieldValue"));

		Assert.assertThat(
				"Output should contain fields that are visible to logged in users: ",
				bout.toString(),
				CoreMatchers.containsString("loggedInField"));
		Assert.assertThat(
				"Output should contain fields that are visible to logged in users: ",
				bout.toString(),
				CoreMatchers.containsString("loggedInFieldValue"));

		Assert.assertThat(
				"Output shouldn't contain fields that are visible to users with role: ",
				bout.toString(),
				CoreMatchers
						.not(CoreMatchers.containsString("roleField")));
		Assert.assertThat(
				"Output shouldn't contain fields that are visible to users with role: ",
				bout.toString(),
				CoreMatchers
						.not(CoreMatchers.containsString("roleFieldValue")));

		Assert.assertThat(
				"Output shouldn't contain fields that are visible to user with username 'testPredicate': ",
				bout.toString(),
				CoreMatchers
						.not(CoreMatchers.containsString("predicateField")));
		Assert.assertThat(
				"Output shouldn't contain fields that are visible to user with username 'testPredicate': ",
				bout.toString(),
				CoreMatchers
						.not(CoreMatchers
								.containsString("predicateFieldValue")));
	}

	@Test
	@WithOpenOlympusMockUser(username = "testPredicate", approved = true, superuser = false)
	public void checkPredicate() throws Exception {
		final ByteArrayOutputStream bout = new ByteArrayOutputStream();
		final JsonFactory factory = new JsonFactory();
		final JsonGenerator generator = factory.createGenerator(bout);
		this.jacksonObjectMapper.writeValue(generator, new AnnotatedObject());

		Assert.assertThat(
				"Output should contain fields that are visible to anonymous users: ",
				bout.toString(),
				CoreMatchers.containsString("anonymousField"));
		Assert.assertThat(
				"Output should contain fields that are visible to anonymous users: ",
				bout.toString(),
				CoreMatchers.containsString("anonymousFieldValue"));

		Assert.assertThat(
				"Output should contain fields that are visible to logged in users: ",
				bout.toString(),
				CoreMatchers.containsString("loggedInField"));
		Assert.assertThat(
				"Output should contain fields that are visible to logged in users: ",
				bout.toString(),
				CoreMatchers.containsString("loggedInFieldValue"));

		Assert.assertThat(
				"Output should contain fields that are visible to users with role: ",
				bout.toString(),
				CoreMatchers.containsString("roleField"));
		Assert.assertThat(
				"Output should contain fields that are visible to users with role: ",
				bout.toString(),
				CoreMatchers.containsString("roleFieldValue"));

		Assert.assertThat(
				"Output shouldn't contain fields that are visible to user with username 'testPredicate': ",
				bout.toString(),
				CoreMatchers
						.not(CoreMatchers.containsString("predicateField")));
		Assert.assertThat(
				"Output shouldn't contain fields that are visible to user with username 'testPredicate': ",
				bout.toString(),
				CoreMatchers.not(
						CoreMatchers.containsString("predicateFieldValue")));
	}
}
