package com.equisoft.dca.infra.authentication;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.core.Authentication;

import com.equisoft.dca.backend.userfolder.service.UserFolderService;
import com.equisoft.dca.infra.security.context.AuthenticationFacade;

@ExtendWith(MockitoExtension.class)
class DcaLogoutHandlerTest {

	@Mock
	private UserFolderService userFolderService;
	@Mock
	private HttpServletResponse response;
	@Mock
	private HttpServletRequest request;
	@Mock
	private Authentication authentication;
	@Mock
	private AuthenticationFacade authenticationFacade;

	private DcaLogoutHandler logoutHandler;

	@BeforeEach
	void setUp() {
		logoutHandler = new DcaLogoutHandler(userFolderService, authenticationFacade);
	}

	@Test
	void givenUserDataBen_whenLogout_shouldCallAuthenticationServiceDeleteUserFolder() {
		// given
		String identifier = "IDENTIFIER";

		Mockito.doReturn(new MockHttpSession()).when(request).getSession();
		Mockito.when(authenticationFacade.getUserIdentifier()).thenReturn(Optional.of(identifier));

		// when
		logoutHandler.logout(request, response, authentication);

		// then
		Mockito.verify(userFolderService, Mockito.times(1)).deleteUserFolder(identifier);
	}
}
