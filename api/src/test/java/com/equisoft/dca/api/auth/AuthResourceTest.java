package com.equisoft.dca.api.auth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import com.equisoft.dca.api.auth.dto.LoginDto;
import com.equisoft.dca.api.configuration.dto.ConfigurationDto;
import com.equisoft.dca.api.configuration.mapper.ConfigurationMapper;
import com.equisoft.dca.backend.authentication.service.AuthenticationService;
import com.equisoft.dca.backend.configuration.model.Configuration;
import com.equisoft.dca.backend.configuration.service.ConfigurationService;
import com.equisoft.dca.infra.authentication.LoginHandler;

import static org.mockito.BDDMockito.willReturn;
import static org.mockito.BDDMockito.willThrow;

@ExtendWith(MockitoExtension.class)
class AuthResourceTest {
	private static final String IDENTIFIER = "IDENTIFIER";
	private static final String PASSWORD = "pass";

	@Mock
	private AuthenticationService authenticationService;
	@Mock
	private ConfigurationService configurationService;
	@Mock
	private LogoutHandler logoutHandler;
	@Mock
	private LoginHandler loginHandler;
	@Mock
	private ConfigurationMapper configurationMapper;
	@Mock
	private HttpServletResponse response;
	@Mock
	private HttpServletRequest request;
	@Mock
	private Authentication authentication;

	private AuthResource authResource;

	@BeforeEach
	void setUp() {
		authResource = new AuthResource(authenticationService, configurationService, loginHandler, logoutHandler, configurationMapper);
	}

	@Nested
	class Login {
		@Nested
		class SuccessfulAuthentication {

			@Test
			void givenSuccessfulAuthentication_whenLogin_returnOkAndLoginDtoWithAuthenticationPrincipalAndUserLanguage() {
				// given
				willReturn(authentication).given(authenticationService).authenticate(IDENTIFIER, PASSWORD);

				Configuration configuration = Configuration.builder().build();
				ConfigurationDto configurationDto = ConfigurationDto.builder().build();

				willReturn(configuration).given(configurationService).get(IDENTIFIER);
				willReturn(configurationDto).given(configurationMapper).toDto(configuration);

				ResponseEntity expected = ResponseEntity.ok().body(configurationDto);

				// when
				ResponseEntity<ConfigurationDto> actual =
						authResource.login(request, LoginDto.builder().identifier(IDENTIFIER).password(PASSWORD).build(), response);

				// then
				Mockito.verify(loginHandler, Mockito.times(1)).onAuthenticationSuccess(request, response, authentication);
				Assertions.assertThat(actual)
						.isNotNull()
						.extracting(ResponseEntity::getStatusCode, ResponseEntity::getBody)
						.containsExactly(expected.getStatusCode(), expected.getBody());
			}
		}

		@Nested
		class UnsuccessfulAuthentication {
			@Test
			void givenAuthServiceThrowBadCredentialException_whenLogin_thenThrowBadCredentialsException() {
				// given
				willThrow(BadCredentialsException.class).given(authenticationService).authenticate(IDENTIFIER, PASSWORD);
				LoginDto loginDto = LoginDto.builder().identifier(IDENTIFIER).password(PASSWORD).build();

				// when
				Throwable actual = Assertions.catchThrowable(() -> authResource.login(request, loginDto, response));

				// then
				Assertions.assertThat(actual).isInstanceOf(BadCredentialsException.class);
			}
		}
	}

	@Nested
	class Logout {
		@Test
		void givenSuccessfulAuthentication_whenLogout_returnOkNoBody() {
			// given

			// when
			ResponseEntity<Void> actualResponse = authResource.logout(request, response);

			// then
			Mockito.verify(logoutHandler, Mockito.times(1)).logout(request, response, null);

			Assertions.assertThat(actualResponse)
					.isNotNull()
					.extracting(ResponseEntity::getStatusCode)
					.isEqualTo(HttpStatus.OK);
			Assertions.assertThat(actualResponse.getBody())
					.isNull();
		}
	}
}
