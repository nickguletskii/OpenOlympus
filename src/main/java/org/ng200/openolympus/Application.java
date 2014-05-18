/**
 * The MIT License
 * Copyright (c) 2014 Nick Guletskii
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

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import javax.servlet.MultipartConfigElement;
import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.io.FileUtils;
import org.ng200.openolympus.model.Role;
import org.ng200.openolympus.model.User;
import org.ng200.openolympus.repositories.OlympusPersistentTokenRepositoryImpl;
import org.ng200.openolympus.repositories.UserRepository;
import org.ng200.openolympus.services.OlympUserDetailsService;
import org.ng200.openolympus.services.RoleService;
import org.ng200.openolympus.services.TestingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.ErrorPage;
import org.springframework.boot.context.embedded.MultiPartConfigFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

@Configuration
@ComponentScan
@EnableAutoConfiguration
public class Application {
	private static final Logger logger = LoggerFactory
			.getLogger(Application.class);

	public static void main(final String[] args) {
		final ConfigurableApplicationContext context = SpringApplication.run(
				Application.class, args);
		final UserRepository userRepository = context
				.getBean(UserRepository.class);
		final RoleService roleService = context.getBean(RoleService.class);
		if (userRepository.findByUsername("admin") == null) {
			Application.logger.info("Creating administrator account");
			final User admin = new User("admin",
					new BCryptPasswordEncoder().encode("admin"), "", "", "",
					"", "", "", "", "", "", "", "", "", "", "", "", "", "",
					null);
			final Set<Role> roles = new HashSet<Role>();
			roles.add(roleService.getRoleByName(Role.USER));
			roles.add(roleService.getRoleByName(Role.SUPERUSER));
			admin.setRoles(roles);
			userRepository.save(admin);
		}
		final ThreadPoolTaskExecutor taskExecutor = context
				.getBean(ThreadPoolTaskExecutor.class);
		taskExecutor.execute(context.getBean(TestingService.class));
	}

	@Value("${dbPassword}")
	private String postgresPassword;
	@Value("${dbAddress}")
	private String postgresAddress;

	@Value("${serverPort}")
	private int serverPort;

	@Bean
	public DataSource dataSource() {
		final BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName("org.postgresql.Driver");
		dataSource.setUsername("postgres");
		dataSource.setPassword(this.postgresPassword);
		dataSource.setUrl("jdbc:postgresql://" + this.postgresAddress
				+ "/openolympus");
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
	public ResourceBundleMessageSource messageSource() {
		final ResourceBundleMessageSource source = new ResourceBundleMessageSource() {

			@Override
			protected MessageFormat resolveCode(final String code,
					final Locale locale) {
				final MessageFormat format = super.resolveCode(code, locale);
				;
				if (format == null) {
					Application.this.reportMissingLocalisationKey(code);
				}
				return format;
			}

			@Override
			protected String resolveCodeWithoutArguments(final String code,
					final Locale locale) {
				final String string = super.resolveCodeWithoutArguments(code,
						locale);
				if (string == null) {
					Application.this.reportMissingLocalisationKey(code);
				}
				return string;
			}

		};
		source.setDefaultEncoding("UTF-8");
		source.setBasename("messages");
		return source;
	}

	@Bean
	public MultipartConfigElement multipartConfigElement() {
		final MultiPartConfigFactory factory = new MultiPartConfigFactory();
		factory.setMaxFileSize("1024MB");
		factory.setMaxRequestSize("1024MB");
		return factory.createMultipartConfig();
	}

	@Bean
	public StandardServletMultipartResolver multipartResolver() {
		return new StandardServletMultipartResolver();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		Application.logger.info("Creating password encoder");
		return new BCryptPasswordEncoder();
	}

	@Bean
	public PersistentTokenRepository persistentTokenRepository() {
		return new OlympusPersistentTokenRepositoryImpl();
	}

	private void reportMissingLocalisationKey(final String code) {
		try {
			if (code.isEmpty() || Character.isUpperCase(code.charAt(0))) {
				return;
			}
			final File file = new File(new File(StorageSpace.STORAGE_PREFIX),
					"missingLocalisation.txt");
			if (!file.exists()) {
				file.getParentFile().mkdirs();
				file.createNewFile();
			}
			final Set<String> s = new TreeSet<>(Arrays.asList(FileUtils
					.readFileToString(file).split("\n")));
			s.add(code);
			FileUtils.writeStringToFile(file,
					s.stream().collect(Collectors.joining("\n")));
		} catch (final IOException e) {
			Application.logger.error("Couldn't add to missing key repo: {}", e);
		}
	}

	public EmbeddedServletContainerFactory servletContainer() {
		final TomcatEmbeddedServletContainerFactory containerFactory = new TomcatEmbeddedServletContainerFactory(
				this.serverPort);
		containerFactory.getErrorPages().add(
				new ErrorPage(HttpStatus.NOT_FOUND, "/errors/404"));
		containerFactory.getErrorPages().add(
				new ErrorPage(HttpStatus.FORBIDDEN, "/errors/403"));

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

	@Bean
	public OlympUserDetailsService userDetailsService() {
		return new OlympUserDetailsService();
	}

}
