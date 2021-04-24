package com.equisoft.dca.infra.authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;

public interface LoginHandler {
	void onAuthenticationSuccess(HttpServletRequest req, HttpServletResponse res, Authentication authentication);
}
