package com.equisoft.dca.infra.authentication;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import com.equisoft.dca.backend.userfolder.service.UserFolderService;
import com.equisoft.dca.infra.security.token.Token;
import com.equisoft.dca.infra.security.token.TokenProvider;

@ExtendWith(MockitoExtension.class)
class DcaLoginHandlerTest {
	@Mock
	private UserFolderService userFolderService;
	@Mock
	private TokenProvider tokenProvider;
	@Mock
	private HttpServletResponse response;
	@Mock
	private HttpServletRequest request;
	@Mock
	private Authentication authentication;

	private DcaLoginHandler loginHandler;

	@BeforeEach
	void setUp() {
		loginHandler = new DcaLoginHandler(tokenProvider, userFolderService);
	}

	@Test
	void givenAuthentication_whenOnSuccessfulAuthentication_shouldCallGenerateToken() {
		// given
		givenAuthentication();

		// when
		loginHandler.onAuthenticationSuccess(request, response, authentication);

		// then
		Mockito.verify(tokenProvider, Mockito.times(1))
				.generateToken(ArgumentMatchers.anyString(), ArgumentMatchers.anySet());
	}

	@Test
	void givenAuthentication_whenOnSuccessfulAuthentication_shouldCallCreateFolderUser() {
		// given
		givenAuthentication();

		// when
		loginHandler.onAuthenticationSuccess(request, response, authentication);

		// then
		Mockito.verify(userFolderService, Mockito.times(1))
				.createUserFolder(ArgumentMatchers.anyString());
	}

	private void givenAuthentication() {
		Principal principal = Mockito.mock(Principal.class);
		Mockito.doReturn(principal).when(authentication).getPrincipal();
		Mockito.doReturn("identifier").when(principal).toString();

		Mockito.doReturn(Token.builder().build())
				.when(tokenProvider).generateToken(ArgumentMatchers.anyString(), ArgumentMatchers.anySet());
	}
}
