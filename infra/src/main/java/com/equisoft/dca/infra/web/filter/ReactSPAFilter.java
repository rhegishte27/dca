package com.equisoft.dca.infra.web.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.springframework.web.filter.GenericFilterBean;

@WebFilter(asyncSupported = true, urlPatterns = "/*")
public class ReactSPAFilter extends GenericFilterBean {

	private static final String INDEX_HTML = "/index.html";

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		final HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		final String uri = httpServletRequest.getRequestURI().substring(httpServletRequest.getContextPath().length());
		final String withoutParams = uri.split("\\?")[0]; //ignores query parameters

		// Should let it through if the URI contains a "." (file extension) or "api/v1" (API)
		if (withoutParams.contains(".") || withoutParams.contains("api/v1")) {
			chain.doFilter(request, response);
		} else {
			chain.doFilter(new HttpServletRequestWrapper(httpServletRequest) {
				@Override
				public String getServletPath() {
					return INDEX_HTML;
				}
			}, response);
		}
	}
}
