package com.equisoft.dca.infra.authentication;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import com.equisoft.dca.backend.userfolder.service.UserFolderService;
import com.equisoft.dca.infra.security.context.AuthenticationFacade;
import com.equisoft.dca.infra.security.util.CookieUtils;

@Service
class DcaLogoutHandler implements LogoutHandler {

	private final UserFolderService userFolderService;

	private final AuthenticationFacade authenticationFacade;

	@Inject
	DcaLogoutHandler(UserFolderService userFolderService, AuthenticationFacade authenticationFacade) {
		this.userFolderService = userFolderService;
		this.authenticationFacade = authenticationFacade;
	}

	@Override
	public void logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) {
		httpServletRequest.getSession().invalidate();
		CookieUtils.destroyAuthCookie(httpServletRequest, httpServletResponse);
		CookieUtils.destroySessionCookie(httpServletRequest, httpServletResponse);

		userFolderService.deleteUserFolder(authenticationFacade.getUserIdentifier().orElse(null));
	}
}
