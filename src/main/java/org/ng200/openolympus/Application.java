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

import java.sql.SQLException;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.servlet.MultipartConfigElement;
import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultConfiguration;
import org.ng200.openolympus.cerberus.util.Lists;
import org.ng200.openolympus.jooq.tables.daos.ContestDao;
import org.ng200.openolympus.jooq.tables.daos.ContestParticipationDao;
import org.ng200.openolympus.jooq.tables.daos.ContestTasksDao;
import org.ng200.openolympus.jooq.tables.daos.GroupDao;
import org.ng200.openolympus.jooq.tables.daos.PropertyDao;
import org.ng200.openolympus.jooq.tables.daos.SolutionDao;
import org.ng200.openolympus.jooq.tables.daos.TaskDao;
import org.ng200.openolympus.jooq.tables.daos.TimeExtensionDao;
import org.ng200.openolympus.jooq.tables.daos.UserDao;
import org.ng200.openolympus.jooq.tables.pojos.User;
import org.ng200.openolympus.resourceResolvers.OpenOlympusMessageSource;
import org.ng200.openolympus.services.StorageService;
import org.ng200.openolympus.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.MultipartConfigFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@ComponentScan(basePackages = "org.ng200.openolympus")
@EnableAutoConfiguration
@PropertySource("classpath:openolympus.properties")
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableCaching
public class Application {
	public static void main(final String[] args) throws SQLException {
		final ConfigurableApplicationContext context = SpringApplication.run(
				Application.class, args);
		Application.setupContext(context);
	}

	public static void setupContext(
			final ApplicationContext webApplicationContext) throws SQLException {
		final UserService userService = webApplicationContext
				.getBean(UserService.class);

	}

	static final Logger logger = LoggerFactory.getLogger(Application.class);

	@Value("${dbPassword}")
	private String postgresPassword;
	@Value("${dbAddress}")
	private String postgresAddress;

	@Value("${serverPort}")
	private int serverPort;

	@Value("${storagePath}")
	private String storagePath;

	@Bean
	public DataSource dataSource() {
		final BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName("org.postgresql.Driver");
		dataSource.setUsername("postgres");
		dataSource.setPassword(this.postgresPassword);
		dataSource.setUrl("jdbc:postgresql://" + this.postgresAddress
				+ "/openolympus");
		Application.logger.info("Connecting: {}", dataSource.getUrl());
		return dataSource;
	}

	@Bean
	public SolutionDao solutionDao() {
		return new SolutionDao(dslContext().configuration());
	}

	@Bean
	public UserDao userDao() {
		UserDao userDao = new UserDao(dslContext().configuration());
		if (userDao.fetchOneByUsername("admin") == null) {
			logger.info("The administrative account doesn't exist. Creating...");
			userDao.insert(new User().setUsername("admin")
					.setPassword(passwordEncoder().encode("admin"))
					.setSuperuser(true).setEnabled(true).setApproved(true)
					.setApprovalEmailSent(false));
		}
		if (userDao.fetchOneByUsername("system") == null) {
			logger.info("The system account doesn't exist. Creating...");
			userDao.insert(new User().setUsername("system").setPassword(null)
					.setSuperuser(true).setEnabled(true).setApproved(true)
					.setApprovalEmailSent(false));
		}

		return userDao;
	}

	@Bean
	public TaskDao taskDao() {
		return new TaskDao(dslContext().configuration());
	}

	@Bean
	public ContestDao contestDao() {
		return new ContestDao(dslContext().configuration());
	}

	@Bean
	public ContestParticipationDao contestParticipationDao() {
		return new ContestParticipationDao(dslContext().configuration());
	}

	@Bean
	public ContestTasksDao contestTasksDao() {
		return new ContestTasksDao(dslContext().configuration());
	}

	@Bean
	public TimeExtensionDao timeExtensionDao() {
		return new TimeExtensionDao(dslContext().configuration());
	}

	@Bean
	public GroupDao groupDao() {
		return new GroupDao(dslContext().configuration());
	}

	@Bean
	public PropertyDao propertyDao() {
		return new PropertyDao(dslContext().configuration());
	}

	@Bean
	public DSLContext dslContext() {
		return DSL.using(new DefaultConfiguration().set(dataSource()).set(
				SQLDialect.POSTGRES));
	}

	@Bean
	public StorageService storageService() {
		return new StorageService();
	}

	@Bean
	public SessionLocaleResolver localeResolver() {
		final SessionLocaleResolver resolver = new SessionLocaleResolver();
		resolver.setDefaultLocale(Locale.forLanguageTag("ru"));
		return resolver;
	}

	@Bean
	public OpenOlympusMessageSource messageSource() {
		final OpenOlympusMessageSource source = new OpenOlympusMessageSource();
		source.setDefaultEncoding("UTF-8");
		source.setBasename("classpath:/messages");
		return source;
	}

	@Bean
	public MultipartConfigElement multipartConfigElement() {
		final MultipartConfigFactory factory = new MultipartConfigFactory();
		factory.setMaxFileSize("1024MB");
		factory.setMaxRequestSize("1024MB");
		return factory.createMultipartConfig();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		Application.logger.info("Creating password encoder");
		return new BCryptPasswordEncoder();
	}

	@Bean
	public EmbeddedServletContainerFactory servletContainer() {
		final TomcatEmbeddedServletContainerFactory containerFactory = new TomcatEmbeddedServletContainerFactory(
				this.serverPort);
		containerFactory.setSessionTimeout(30, TimeUnit.DAYS);
		return containerFactory;
	}

	@Bean
	public ThreadPoolTaskExecutor taskExecutor() {
		final ThreadPoolTaskExecutor pool = new ThreadPoolTaskExecutor();
		pool.setCorePoolSize(5);
		pool.setMaxPoolSize(10);
		pool.setWaitForTasksToCompleteOnShutdown(true);
		return pool;
	}

	@Bean
	public MappingJackson2HttpMessageConverter jacksonMessageConverter() {
		MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();

		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new DurationJacksonModule());
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
				false);
		messageConverter.setObjectMapper(mapper);
		return messageConverter;
	}

	@Bean
	public SimpleCacheManager cacheManager() {
		SimpleCacheManager cacheManager = new SimpleCacheManager();

		cacheManager.setCaches(Lists.from(new ConcurrentMapCache("solutions"),
				new ConcurrentMapCache("contests")));
		return cacheManager;
	}

	@Bean
	public PersistentTokenRepository persistentTokenRepository() {
		JdbcTokenRepositoryImpl db = new JdbcTokenRepositoryImpl() {

			@Override
			protected void initDao() {
				getJdbcTemplate()
						.execute(
								"create table if not exists persistent_logins (username varchar(64) not null, series varchar(64) primary key, "
										+ "token varchar(64) not null, last_used timestamp not null)");
			}

		};
		db.setDataSource(dataSource());
		return db;
	}
}
