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

import javax.servlet.MultipartConfigElement;

import org.ng200.openolympus.resourceResolvers.OpenOlympusMessageSource;
import org.ng200.openolympus.services.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.MultipartConfigFactory;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

@Configuration
@ComponentScan(basePackages = "org.ng200.openolympus")
@EnableAutoConfiguration
@PropertySource(value = {
							"classpath:openolympus.properties",
							"file:openolympus.properties",
							"classpath:application.properties",
							"file:application.properties",
							"classpath:secret.properties",
							"file:secret.properties"
}, ignoreResourceNotFound = true)
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableCaching
@EnableTransactionManagement
public class Application {
	static final Logger logger = LoggerFactory.getLogger(Application.class);

	public static void main(final String[] args) throws SQLException {
		SpringApplication.run(
				Application.class, args);
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
	public StorageService storageService() {
		return new StorageService();
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
