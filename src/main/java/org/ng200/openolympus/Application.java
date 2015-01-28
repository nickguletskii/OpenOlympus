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
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import javax.servlet.MultipartConfigElement;
import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.ng200.openolympus.model.Role;
import org.ng200.openolympus.model.User;
import org.ng200.openolympus.resourceResolvers.OpenOlympusMessageSource;
import org.ng200.openolympus.services.RoleService;
import org.ng200.openolympus.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.ErrorPage;
import org.springframework.boot.context.embedded.MultipartConfigFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.datasource.init.ScriptException;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

@Configuration
@ComponentScan(basePackages = "org.ng200.openolympus")
@EnableAutoConfiguration
@PropertySource("classpath:openolympus.properties")
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class Application {
	public static void main(final String[] args) throws ScriptException,
			SQLException {
		final ConfigurableApplicationContext context = SpringApplication.run(
				Application.class, args);
		final UserService userService = context.getBean(UserService.class);
		final RoleService roleService = context.getBean(RoleService.class);
		final DataSource dataSource = context.getBean(DataSource.class);
		ScriptUtils
				.executeSqlScript(dataSource.getConnection(),
						new EncodedResource(new ClassPathResource(
								"/setupTriggers.sql")), false, false,
						ScriptUtils.DEFAULT_COMMENT_PREFIX,
						"^^^ NEW STATEMENT ^^^",
						ScriptUtils.DEFAULT_BLOCK_COMMENT_START_DELIMITER,
						ScriptUtils.DEFAULT_BLOCK_COMMENT_END_DELIMITER);

		if (userService.getUserByUsername("admin") == null) {
			Application.logger.info("Creating administrator account");
			final User admin = new User("admin",
					new BCryptPasswordEncoder().encode("admin"), "", "", "",
					"", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
					null, null);
			final Set<Role> roles = new HashSet<Role>();
			roles.add(roleService.getRoleByName(Role.USER));
			roles.add(roleService.getRoleByName(Role.SUPERUSER));
			admin.setRoles(roles);
			userService.saveUser(admin);
		}
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
	public HibernateJpaVendorAdapter jpaVendorAdapter() {
		final HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
		adapter.setShowSql(false);
		adapter.setGenerateDdl(true);
		adapter.setDatabasePlatform("org.ng200.openolympus.OpenOlympusPostgreDialect");
		return adapter;
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
		containerFactory.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND,
				"/errors/404"), new ErrorPage(HttpStatus.FORBIDDEN,
				"/errors/403"));

		containerFactory.setSessionTimeout(30);

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
}
