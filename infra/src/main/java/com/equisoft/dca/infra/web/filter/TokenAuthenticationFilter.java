package com.equisoft.dca.infra.web.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import com.equisoft.dca.infra.ErrorInformation;
import com.equisoft.dca.infra.security.context.AuthenticationFacade;
import com.equisoft.dca.infra.security.token.Token;
import com.equisoft.dca.infra.security.token.TokenProvider;
import com.equisoft.dca.infra.security.token.UnpackedToken;
import com.equisoft.dca.infra.security.util.CookieUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import static java.util.stream.Collectors.toList;

@Component
public class TokenAuthenticationFilter extends GenericFilterBean {

	private final ObjectMapper objectMapper;

	private final TokenProvider tokenProvider;

	private final AuthenticationFacade authenticationFacade;

	@Inject
	TokenAuthenticationFilter(ObjectMapper objectMapper, TokenProvider tokenProvider, AuthenticationFacade authenticationFacade) {
		this.objectMapper = objectMapper;
		this.tokenProvider = tokenProvider;
		this.authenticationFacade = authenticationFacade;
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
		HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

		try {
			getToken(httpServletRequest).ifPresentOrElse(
					token -> validateToken(token, httpServletRequest).ifPresent(t -> regenToken(httpServletRequest, t, httpServletResponse)),
					authenticationFacade::clearContext
			);
		} catch (Exception ex) {
			makeUnauthorized(httpServletRequest, ex, httpServletResponse);
			ex.printStackTrace();
			return;
		}

		filterChain.doFilter(httpServletRequest, httpServletResponse);
	}

	private Optional<UnpackedToken> validateToken(String token, HttpServletRequest request) {
		if (StringUtils.isBlank(token)) {
			return Optional.empty();
		}

		UnpackedToken unpackedToken = tokenProvider.validateToken(token);
		UsernamePasswordAuthenticationToken authentication =
				new UsernamePasswordAuthenticationToken(unpackedToken.getIdentifier(), null, createAuthorities(unpackedToken));
		authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		authenticationFacade.setAuthentication(authentication);
		return Optional.of(unpackedToken);
	}

	private Optional<String> getToken(HttpServletRequest request) {
		return getTokenFromCookie(request).map(Optional::of).orElse(getTokenFromHeader(request));
	}

	private Optional<String> getTokenFromHeader(HttpServletRequest request) {
		return Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION));
	}

	private Optional<String> getTokenFromCookie(HttpServletRequest request) {
		return request.getCookies() == null ? Optional.empty() :
				Arrays.stream(request.getCookies())
						.filter(c -> c.getName().equals(CookieUtils.AUTH_COOKIE_NAME))
						.map(Cookie::getValue)
						.findFirst();
	}

	private List<SimpleGrantedAuthority> createAuthorities(UnpackedToken unpackedToken) {
		return unpackedToken.getRoles().stream().map(SimpleGrantedAuthority::new).collect(toList());
	}

	private void regenToken(HttpServletRequest servletRequest, UnpackedToken token, HttpServletResponse servletResponse) {
		Token dcaToken = tokenProvider.generateToken(token.getIdentifier(), new HashSet<>(token.getRoles()));
		CookieUtils.buildAuthCookie(servletRequest, servletResponse, dcaToken.getValue(), dcaToken.getDuration());
	}

	private void makeUnauthorized(HttpServletRequest servletRequest, Exception ex, HttpServletResponse servletResponse) {
		try {
			ErrorInformation error = ErrorInformation.builder().message("api.errors.unauthorizedJWT").extraInformation(ex.getMessage()).build();
			servletResponse.getWriter().write(objectMapper.writeValueAsString(error));
			servletResponse.setContentType(MediaType.APPLICATION_JSON.toString());
			CookieUtils.destroyAuthCookie(servletRequest, servletResponse);
		} catch (IOException ignored) {
		}

		servletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
	}
}
