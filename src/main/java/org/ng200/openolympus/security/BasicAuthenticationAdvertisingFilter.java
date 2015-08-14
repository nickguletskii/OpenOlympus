package org.ng200.openolympus.security;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

public class BasicAuthenticationAdvertisingFilter implements Filter {
	public void doFilter(final ServletRequest request,
			final ServletResponse response,
			final FilterChain chain)
					throws IOException, ServletException {
		HttpServletResponse res = (HttpServletResponse) response;
		res.setHeader("WWW-Authenticate",
				"Basic realm=\"openolympus-git-interface\"");
		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig filterConfig)
			throws ServletException {
	}

	@Override
	public void destroy() {
	}
}