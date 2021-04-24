package com.equisoft.dca.infra.security.util;

import java.util.concurrent.TimeUnit;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public final class CookieUtils {

	public static final String AUTH_COOKIE_NAME = "DCA_Authorization";

	private static final String JSESSIONID = "JSESSIONID";

	private CookieUtils() {
	}

	public static void buildAuthCookie(HttpServletRequest request, HttpServletResponse response, String token, int tokenDuration) {
		String basePath = request.getContextPath();
		Cookie cookie = new Cookie(AUTH_COOKIE_NAME, token);
		cookie.setPath(basePath);
		cookie.setHttpOnly(true);
		cookie.setMaxAge((int) TimeUnit.MINUTES.toMillis(tokenDuration) / 1000);
		response.addCookie(cookie);
	}

	public static void destroyAuthCookie(HttpServletRequest request, HttpServletResponse response) {
		String basePath = request.getContextPath();
		Cookie cookie = new Cookie(AUTH_COOKIE_NAME, "");
		cookie.setPath(basePath);
		cookie.setHttpOnly(true);
		cookie.setMaxAge(0);
		response.addCookie(cookie);
	}

	public static void destroySessionCookie(HttpServletRequest request, HttpServletResponse response) {
		String basePath = request.getContextPath();
		Cookie cookie = new Cookie(JSESSIONID, "");
		cookie.setHttpOnly(true);
		cookie.setMaxAge(0);
		cookie.setPath(basePath);
		response.addCookie(cookie);
	}
}
