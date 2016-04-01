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
package org.ng200.openolympus.gitsupport;

import java.util.Enumeration;

import javax.servlet.FilterConfig;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.eclipse.jgit.http.server.GitFilter;
import org.eclipse.jgit.http.server.glue.MetaServlet;
import org.eclipse.jgit.http.server.glue.ServletBinder;
import org.eclipse.jgit.transport.resolver.RepositoryResolver;
import org.ng200.openolympus.security.BasicAuthenticationAdvertisingFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

public class OpenOlympusGitServlet extends MetaServlet {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private final GitFilter gitFilter;

	/**
	 * New servlet that will load its base directory from {@code web.xml}.
	 * <p>
	 * The required parameter {@code base-path} must be configured to point to
	 * the local filesystem directory where all served Git repositories reside.
	 */
	public OpenOlympusGitServlet(
			BasicAuthenticationFilter authenticationFilter) {
		super(new GitFilter() {

			@Override
			protected ServletBinder register(ServletBinder binder) {
				binder = binder.through(
						new BasicAuthenticationAdvertisingFilter());
				binder = binder.through(authenticationFilter);
				return super.register(binder);
			}

		});

		this.gitFilter = (GitFilter) this.getDelegateFilter();
	}

	@Override
	public void init(final ServletConfig config) throws ServletException {
		this.gitFilter.init(new FilterConfig() {
			@Override
			public String getFilterName() {
				return OpenOlympusGitServlet.this.gitFilter.getClass()
						.getName();
			}

			@Override
			public String getInitParameter(String name) {
				return config.getInitParameter(name);
			}

			@Override
			public Enumeration<String> getInitParameterNames() {
				return config.getInitParameterNames();
			}

			@Override
			public ServletContext getServletContext() {
				return config.getServletContext();
			}
		});
	}

	public void setRepositoryResolver(
			RepositoryResolver<HttpServletRequest> resolver) {
		this.gitFilter.setRepositoryResolver(resolver);
	}
}