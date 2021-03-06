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
package org.ng200.openolympus.config;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.ng200.openolympus.CsrfHeaderFilter;
import org.ng200.openolympus.controller.auth.OpenOlympusAuthenticationFailureHandler;
import org.ng200.openolympus.controller.auth.OpenOlympusAuthenticationSuccessHandler;
import org.ng200.openolympus.controller.auth.RecaptchaAuthenticationFilter;
import org.ng200.openolympus.services.UserSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.RememberMeAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@Profile("web")
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	private static final String REMEMBERME_COOKIE_NAME = "openolympus-rememberme";

	@Autowired
	private UserSecurityService userSecurityService;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private PersistentTokenConfiguration persistentTokenConfiguration;
	@Autowired
	private DataSource dataSource;

	@Bean
	public OpenOlympusAuthenticationFailureHandler authenticationFailureHandler() {
		return new OpenOlympusAuthenticationFailureHandler();
	}

	@Bean
	public OpenOlympusAuthenticationSuccessHandler authenticationSuccessHandler() {
		return new OpenOlympusAuthenticationSuccessHandler();
	}

	@Override
	protected void configure(final AuthenticationManagerBuilder auth)
			throws Exception {
		auth.userDetailsService(this.userSecurityService).passwordEncoder(
				this.passwordEncoder);
	}

	@Override
	protected void configure(final HttpSecurity http) throws Exception {

		http
				.csrf()
				.csrfTokenRepository(this.csrfTokenRepository())
				.ignoringAntMatchers("/git/**")
				.and()
				.headers()
				.xssProtection()
				.and()
				.and()
				.exceptionHandling()
				.accessDeniedHandler(new AccessDeniedHandlerImpl())
				.authenticationEntryPoint(new Http403ForbiddenEntryPoint())
				.and()
				.formLogin()
				.loginPage("/login")
				.failureHandler(this.authenticationFailureHandler())
				.successHandler(this.authenticationSuccessHandler())
				.loginProcessingUrl("/api/login")
				.usernameParameter("username")
				.passwordParameter("password")
				.permitAll().and()
				.logout()
				.logoutRequestMatcher(new AntPathRequestMatcher("/api/logout"))
				.logoutSuccessHandler(this.logoutSuccessHandler())
				.permitAll().and()
				.rememberMe()
				.rememberMeServices(this.rememberMeServices())
				.tokenRepository(this.persistentTokenRepository())
				.key(this.persistentTokenConfiguration.getPersistentTokenKey())
				.tokenValiditySeconds(60 * 60 * 24 * 14)
				.userDetailsService(this.userSecurityService).and()
				.authenticationProvider(this.rememberMeAuthenticationProvider())
				.authorizeRequests()
				.anyRequest().permitAll().and()
				.addFilterBefore(this.reCaptchaAuthenticationFilter(),
						UsernamePasswordAuthenticationFilter.class)
				.addFilterAfter(new CsrfHeaderFilter(), CsrfFilter.class)
				.httpBasic();
	}

	@Bean
	public CsrfTokenRepository csrfTokenRepository() {
		final HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
		repository.setHeaderName("X-CSRF-TOKEN");
		return repository;
	}

	public BasicAuthenticationFilter getBasicAuthenticationFilter()
			throws Exception {

		final BasicAuthenticationFilter filter = new BasicAuthenticationFilter(
				this.authenticationManager());
		return filter;
	}

	@Bean
	public LogoutSuccessHandler logoutSuccessHandler() {
		return (request, response, authentication) -> response
				.setStatus(HttpStatus.OK.value());
	}

	@Bean
	public PersistentTokenRepository persistentTokenRepository() {
		final JdbcTokenRepositoryImpl db = new JdbcTokenRepositoryImpl() {

			@Override
			protected void initDao() {
				this.getJdbcTemplate()
						.execute(
								"create table if not exists persistent_logins (username varchar(64) not null, series varchar(64) primary key, "
										+ "token varchar(64) not null, last_used timestamp not null)");
			}

		};
		db.setDataSource(this.dataSource);
		return db;
	}

	@Bean
	public RecaptchaAuthenticationFilter reCaptchaAuthenticationFilter() {
		return new RecaptchaAuthenticationFilter();
	}

	@Bean
	public RememberMeAuthenticationProvider rememberMeAuthenticationProvider() {
		final RememberMeAuthenticationProvider rememberMeAuthenticationProvider = new RememberMeAuthenticationProvider(
				this.persistentTokenConfiguration.getPersistentTokenKey());
		return rememberMeAuthenticationProvider;
	}

	@Bean
	public RememberMeServices rememberMeServices() {
		final PersistentTokenBasedRememberMeServices rememberMeServices = new PersistentTokenBasedRememberMeServices(
				this.persistentTokenConfiguration.getPersistentTokenKey(),
				this.userSecurityService,
				this.persistentTokenRepository()) {

			@Override
			protected String extractRememberMeCookie(
					HttpServletRequest request) {
				if (request.getHeader("X-Auth-Token") != null) {
					return request.getHeader("X-Auth-Token");
				}
				final Cookie[] cookies = request.getCookies();

				if ((cookies == null) || (cookies.length == 0)) {
					return null;
				}

				for (final Cookie cookie : cookies) {
					if (WebSecurityConfig.REMEMBERME_COOKIE_NAME.equals(cookie
							.getName())) {
						return cookie.getValue();
					}
				}

				return null;
			}

		};
		rememberMeServices
				.setCookieName(WebSecurityConfig.REMEMBERME_COOKIE_NAME);
		rememberMeServices.setAlwaysRemember(true);
		return rememberMeServices;
	}
}