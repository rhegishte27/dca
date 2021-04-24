package com.equisoft.dca.infra.authentication;

import java.util.Collections;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.equisoft.dca.backend.userfolder.service.UserFolderService;
import com.equisoft.dca.infra.security.token.Token;
import com.equisoft.dca.infra.security.token.TokenProvider;
import com.equisoft.dca.infra.security.util.CookieUtils;

@Service
class DcaLoginHandler implements LoginHandler {

	private final TokenProvider tokenProvider;

	private final UserFolderService userFolderService;

	@Inject
	DcaLoginHandler(TokenProvider tokenProvider, UserFolderService userFolderService) {
		this.tokenProvider = tokenProvider;
		this.userFolderService = userFolderService;
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) {
		String identifier = authentication.getPrincipal().toString();
		Token token = tokenProvider.generateToken(identifier, Collections.emptySet());
		CookieUtils.buildAuthCookie(httpServletRequest, httpServletResponse, token.getValue(), token.getDuration());
		userFolderService.createUserFolder(identifier);
	}
}
