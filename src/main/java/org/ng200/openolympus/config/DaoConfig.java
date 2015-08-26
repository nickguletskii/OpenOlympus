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
package org.ng200.openolympus.config;

import org.jooq.DSLContext;
import org.ng200.openolympus.NameConstants;
import org.ng200.openolympus.jooq.tables.daos.ContestDao;
import org.ng200.openolympus.jooq.tables.daos.ContestParticipationDao;
import org.ng200.openolympus.jooq.tables.daos.ContestTasksDao;
import org.ng200.openolympus.jooq.tables.daos.GroupDao;
import org.ng200.openolympus.jooq.tables.daos.GroupUsersDao;
import org.ng200.openolympus.jooq.tables.daos.PropertyDao;
import org.ng200.openolympus.jooq.tables.daos.SolutionDao;
import org.ng200.openolympus.jooq.tables.daos.TaskDao;
import org.ng200.openolympus.jooq.tables.daos.TimeExtensionDao;
import org.ng200.openolympus.jooq.tables.daos.UserDao;
import org.ng200.openolympus.jooq.tables.daos.VerdictDao;
import org.ng200.openolympus.jooq.tables.pojos.Group;
import org.ng200.openolympus.jooq.tables.pojos.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DaoConfig {

	private static final Logger logger = LoggerFactory
	        .getLogger(DaoConfig.class);
	@Autowired
	private DSLContext dslContext;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Bean
	public ContestDao contestDao() {
		return new ContestDao(this.dslContext.configuration());
	}

	@Bean
	public ContestParticipationDao contestParticipationDao() {
		return new ContestParticipationDao(this.dslContext.configuration());
	}

	@Bean
	public ContestTasksDao contestTasksDao() {
		return new ContestTasksDao(this.dslContext.configuration());
	}

	@Bean
	public GroupDao groupDao() {
		return new GroupDao(this.dslContext.configuration());
	}

	@Bean
	public GroupUsersDao groupUsersDao() {
		return new GroupUsersDao(this.dslContext.configuration());
	}

	@Bean
	public PropertyDao propertyDao() {
		return new PropertyDao(this.dslContext.configuration());
	}

	@Bean
	public SolutionDao solutionDao() {
		return new SolutionDao(this.dslContext.configuration());
	}

	@Bean
	public TaskDao taskDao() {
		return new TaskDao(this.dslContext.configuration());
	}

	@Bean
	public TimeExtensionDao timeExtensionDao() {
		return new TimeExtensionDao(this.dslContext.configuration());
	}

	@Bean
	public UserDao userDao() {
		final UserDao userDao = new UserDao(this.dslContext.configuration());
		final GroupDao groupDao = new GroupDao(this.dslContext.configuration());

		if (groupDao
		        .fetchOneByName(NameConstants.ALL_USERS_GROUP_NAME) == null) {
			DaoConfig.logger
			        .info("The group of all users doesn't exist. Creating...");
			groupDao.insert(
			        new Group().setName(NameConstants.ALL_USERS_GROUP_NAME));
		}

		if (groupDao.fetchOneByName(
		        NameConstants.SUPERUSERS_GROUP_NAME) == null) {
			DaoConfig.logger
			        .info("The group of all users doesn't exist. Creating...");
			groupDao.insert(new Group()
			        .setName(NameConstants.SUPERUSERS_GROUP_NAME));
		}

		if (userDao.fetchOneByUsername(
		        NameConstants.SYSTEM_ACCOUNT_NAME) == null) {
			DaoConfig.logger
			        .info("The system account doesn't exist. Creating...");
			userDao.insert(
			        new User().setUsername(NameConstants.SYSTEM_ACCOUNT_NAME)
			                .setPassword(null)
			                .setSuperuser(true).setEnabled(true)
			                .setApproved(true)
			                .setApprovalEmailSent(false));
		}

		if (userDao.fetchOneByUsername(
		        NameConstants.SUPERUSER_ACCOUNT_NAME) == null) {
			DaoConfig.logger.info(
			        "The superuser account doesn't exist. Creating...");
			userDao.insert(new User()
			        .setUsername(NameConstants.SUPERUSER_ACCOUNT_NAME)
			        .setPassword(this.passwordEncoder
			                .encode(NameConstants.SUPERUSER_ACCOUNT_NAME))
			        .setSuperuser(true).setEnabled(true).setApproved(true)
			        .setApprovalEmailSent(false));
		}

		return userDao;
	}

	@Bean
	public VerdictDao verdictDao() {
		return new VerdictDao(this.dslContext.configuration());
	}
}
