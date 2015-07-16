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

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.ng200.openolympus.Authorities;
import org.ng200.openolympus.controller.auth.OpenOlympusAuthenticationFailureHandler;
import org.ng200.openolympus.controller.auth.OpenOlympusAuthenticationSuccessHandler;
import org.ng200.openolympus.controller.auth.RecaptchaAuthenticationFilter;
import org.ng200.openolympus.services.UserSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.RememberMeAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@EnableWebSecurity
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

	private static String[] permittedAny = {
												"/",
												"/translation",
												"/resources/**",
												"/api/user/register/",
												"/errors/**"
	};

	private static String[] authorisedAny = {
												"/api/archive/rank",
												"/api/archive/rankCount",
												"/api/archive/tasks",
												"/api/archive/taskCount",
												"/api/contests",
												"/api/contestsCount",
												"/api/contest/*",
												"/api/contest/*/results",
												"/api/verdict",
												"/api/user/solutions",
												"/api/user/solutionsCount",
												"/api/user/solutionsCount",
												"/api/task/*",
												"/api/task/*/name"
	};

	private static String[] authorisedPost = {
												"/user/**",
												"/api/task/*/submitSolution"
	};
	private static String[] authorisedGet = {};
	private static String[] permittedGet = {};
	private static String[] administrativeAny = {
													"/api/admin/**",
													"/api/task/create",
													"/api/taskSourcecode",
													"/api/task/*/edit",
													"/api/task/*/rejudgeTask",
													"/api/contests/create",
													"/api/contest/*/edit",
													"/api/contest/*/participants",
													"/api/contest/*/remove",
													"/api/contest/*/completeResults",
													"/api/contest/*/addTask",
													"/api/contest/*/addUser",
													"/api/contest/*/addUserTime",
													"/api/contest/*/removeTask/*",
													"/api/taskSourcecode"
	};

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
				.csrf().disable()
				.headers()
				.xssProtection().disable()
				.and()
				.exceptionHandling()
				.accessDeniedHandler(new AccessDeniedHandlerImpl())
				.authenticationEntryPoint(new Http403ForbiddenEntryPoint())
				.and()
				.formLogin()
				.loginPage("/login")
				.failureHandler(this.authenticationFailureHandler())
				.successHandler(this.authenticationSuccessHandler())
				.loginProcessingUrl("/login")
				.usernameParameter("username")
				.passwordParameter("password")
				.permitAll().and()
				.logout()
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				.logoutSuccessUrl("/")
				.permitAll().and()
				.rememberMe()
				.rememberMeServices(this.rememberMeServices())
				.tokenRepository(persistentTokenRepository())
				.key(this.persistentTokenConfiguration.getPersistentTokenKey())
				.tokenValiditySeconds(60 * 60 * 24 * 14)
				.userDetailsService(this.userSecurityService).and()
				.authenticationProvider(this.rememberMeAuthenticationProvider())
				.authorizeRequests()
				.antMatchers(WebSecurityConfig.permittedAny).permitAll().and()
				.authorizeRequests()
				.antMatchers(WebSecurityConfig.authorisedAny)
				.hasAuthority(Authorities.USER.getAuthority()).and()
				.authorizeRequests()
				.antMatchers(HttpMethod.POST, WebSecurityConfig.authorisedPost)
				.hasAuthority(Authorities.USER.getAuthority()).and()
				.authorizeRequests()
				.antMatchers(HttpMethod.GET, WebSecurityConfig.authorisedGet)
				.hasAuthority(Authorities.USER.getAuthority()).and()
				.authorizeRequests()
				.antMatchers(HttpMethod.GET, WebSecurityConfig.permittedGet)
				.permitAll().and()
				.authorizeRequests()
				.antMatchers(WebSecurityConfig.administrativeAny)
				.hasAuthority(Authorities.SUPERUSER.getAuthority()).and()
				.addFilterBefore(this.reCaptchaAuthenticationFilter(),
						UsernamePasswordAuthenticationFilter.class)
				.httpBasic();
		//@formatter:on
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
		db.setDataSource(dataSource);
		return db;
	}
}