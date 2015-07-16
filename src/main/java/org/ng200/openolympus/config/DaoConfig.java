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
import org.ng200.openolympus.jooq.tables.pojos.Group;
import org.ng200.openolympus.jooq.tables.pojos.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
	public SolutionDao solutionDao() {
		return new SolutionDao(dslContext.configuration());
	}

	@Bean
	public UserDao userDao() {
		UserDao userDao = new UserDao(dslContext.configuration());
		GroupDao groupDao = new GroupDao(dslContext.configuration());

		if (groupDao
				.fetchOneByName(NameConstants.ALL_USERS_GROUP_NAME) == null) {
			logger.info("The group of all users doesn't exist. Creating...");
			groupDao.insert(
					new Group().setName(NameConstants.ALL_USERS_GROUP_NAME));
		}

		if (groupDao.fetchOneByName(
				NameConstants.SUPERUSERS_GROUP_NAME) == null) {
			logger.info("The group of all users doesn't exist. Creating...");
			groupDao.insert(new Group()
					.setName(NameConstants.SUPERUSERS_GROUP_NAME));
		}

		if (userDao.fetchOneByUsername(
				NameConstants.SYSTEM_ACCOUNT_NAME) == null) {
			logger.info("The system account doesn't exist. Creating...");
			userDao.insert(
					new User().setUsername(NameConstants.SYSTEM_ACCOUNT_NAME)
							.setPassword(null)
							.setSuperuser(true).setEnabled(true)
							.setApproved(true)
							.setApprovalEmailSent(false));
		}

		if (userDao.fetchOneByUsername(
				NameConstants.SUPERUSER_ACCOUNT_NAME) == null) {
			logger.info(
					"The superuser account doesn't exist. Creating...");
			userDao.insert(new User()
					.setUsername(NameConstants.SUPERUSER_ACCOUNT_NAME)
					.setPassword(passwordEncoder
							.encode(NameConstants.SUPERUSER_ACCOUNT_NAME))
					.setSuperuser(true).setEnabled(true).setApproved(true)
					.setApprovalEmailSent(false));
		}

		return userDao;
	}

	@Bean
	public TaskDao taskDao() {
		return new TaskDao(dslContext.configuration());
	}

	@Bean
	public ContestDao contestDao() {
		return new ContestDao(dslContext.configuration());
	}

	@Bean
	public ContestParticipationDao contestParticipationDao() {
		return new ContestParticipationDao(dslContext.configuration());
	}

	@Bean
	public ContestTasksDao contestTasksDao() {
		return new ContestTasksDao(dslContext.configuration());
	}

	@Bean
	public TimeExtensionDao timeExtensionDao() {
		return new TimeExtensionDao(dslContext.configuration());
	}

	@Bean
	public GroupDao groupDao() {
		return new GroupDao(dslContext.configuration());
	}

	@Bean
	public PropertyDao propertyDao() {
		return new PropertyDao(dslContext.configuration());
	}

	@Bean
	public GroupUsersDao groupUsersDao() {
		return new GroupUsersDao(dslContext.configuration());
	}

}
