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

import javax.servlet.Filter;

import org.ng200.openolympus.model.Role;
import org.ng200.openolympus.repositories.OlympusPersistentTokenRepositoryImpl;
import org.ng200.openolympus.services.ContestSecurityService;
import org.ng200.openolympus.services.OlympUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.CharacterEncodingFilter;

@Configuration
@EnableWebMvcSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	public OlympUserDetailsService userDetailsService;

	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private OlympusPersistentTokenRepositoryImpl persistentTokenRepository;

	@Autowired
	private ContestSecurityService contestSecurityService;

	@Value("${persistentTokenKey}")
	private String persistentTokenKey;

	private static String[] permittedAny = { "/", "/api/taskCompletion/",
		"/archive/", "/contests/", "/contest/*", "/errors/**", "/home/",
		"/locale/", "/register/", "/resources/**", "/contest/*/results",
		"/api/taskCompletion/", "/api/verdict", "/api/solution" };

	private static String[] authorisedPost = { "/task/*", "/user/**" };
	private static String[] authorisedGet = { "/task/*", "/solution",
		"/user/solutions*", "/user/**" };
	private static String[] permittedGet = { "/task/*" };
	private static String[] administrativeAny = { "/admin/**", "/archive/add",
		"/contest/*/addTask", "/contest/*/edit", "/task/*/edit",
		"/contest/*/addUser", "/contest/*/addUserTime", "/contests/add",
		"/contest/*/removeTask/*", "/contests/remove/*",
		"/admin/diagnostics", "/contest/*/completeResults" };

	@Bean
	public Filter characterEncodingFilter() {
		final CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
		characterEncodingFilter.setEncoding("UTF-8");
		characterEncodingFilter.setForceEncoding(true);
		return characterEncodingFilter;
	}

	@Override
	protected void configure(final AuthenticationManagerBuilder auth)
			throws Exception {
		auth.userDetailsService(this.userDetailsService).passwordEncoder(
				this.passwordEncoder);
	}

	@Override
	protected void configure(final HttpSecurity http) throws Exception {
		http.addFilterBefore(this.characterEncodingFilter(),
				ChannelProcessingFilter.class).csrf().disable().headers()
				.xssProtection().and().formLogin().loginPage("/login")
				.failureUrl("/login-failure").loginProcessingUrl("/login")
				.usernameParameter("username").passwordParameter("password")
				.permitAll().and().logout()
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				.logoutSuccessUrl("/").permitAll().and().rememberMe()
				.rememberMeServices(this.rememberMeServices())
				.tokenRepository(this.persistentTokenRepository)
				.key(this.persistentTokenKey).and().authorizeRequests()
				.antMatchers(WebSecurityConfig.permittedAny).permitAll().and()
				.authorizeRequests()
				.antMatchers(HttpMethod.POST, WebSecurityConfig.authorisedPost)
				.authenticated().and().authorizeRequests()
				.antMatchers(HttpMethod.GET, WebSecurityConfig.authorisedGet)
				.authenticated().and().authorizeRequests()
				.antMatchers(HttpMethod.GET, WebSecurityConfig.permittedGet)
				.permitAll().and().authorizeRequests()
				.antMatchers(WebSecurityConfig.administrativeAny)
				.hasAuthority(Role.SUPERUSER).and().httpBasic();
	}

	@Bean
	public RememberMeServices rememberMeServices() {
		final PersistentTokenBasedRememberMeServices rememberMeServices = new PersistentTokenBasedRememberMeServices(
				"password", this.userDetailsService,
				this.persistentTokenRepository);
		rememberMeServices.setCookieName("openolympus-rememberme");
		rememberMeServices.setParameter("rememberme");
		return rememberMeServices;
	}

}