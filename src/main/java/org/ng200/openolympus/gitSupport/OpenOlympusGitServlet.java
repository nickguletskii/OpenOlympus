package org.ng200.openolympus.gitSupport;

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
	 * The required parameter {@code base-path} must be configured to point
	 * to the local filesystem directory where all served Git repositories
	 * reside.
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

		gitFilter = (GitFilter) getDelegateFilter();
	}

	public void setRepositoryResolver(
			RepositoryResolver<HttpServletRequest> resolver) {
		gitFilter.setRepositoryResolver(resolver);
	}

	@Override
	public void init(final ServletConfig config) throws ServletException {
		gitFilter.init(new FilterConfig() {
			public String getFilterName() {
				return gitFilter.getClass().getName();
			}

			public String getInitParameter(String name) {
				return config.getInitParameter(name);
			}

			public Enumeration<String> getInitParameterNames() {
				return config.getInitParameterNames();
			}

			public ServletContext getServletContext() {
				return config.getServletContext();
			}
		});
	}
}