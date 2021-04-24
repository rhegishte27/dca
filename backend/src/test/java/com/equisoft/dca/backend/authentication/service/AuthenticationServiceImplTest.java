package com.equisoft.dca.backend.authentication.service;

import java.util.Collections;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.equisoft.dca.backend.user.model.User;
import com.equisoft.dca.backend.user.service.UserService;

import static org.mockito.BDDMockito.willReturn;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceImplTest {
	private static final String IDENTIFIER = "identifier";

	@Mock
	private UserService userService;
	@Mock
	private PasswordEncoder passwordEncoder;

	private AuthenticationService authenticationService;

	@BeforeEach
	void setUp() {
		authenticationService = new AuthenticationServiceImpl(userService, passwordEncoder);
	}

	@Nested
	class Authenticate {
		@Test
		void givenIdentifierNotExistedInBd_whenAuthenticate_throwBadCredentialsException() {
			// given
			willReturn(Optional.empty()).given(userService).findByIdentifier(IDENTIFIER);

			// then
			Throwable actual =
					Assertions.catchThrowable(() -> authenticationService.authenticate(IDENTIFIER, "password"));

			// then
			Assertions.assertThat(actual).isInstanceOf(BadCredentialsException.class);
		}

		@Test
		void givenIdentifierExistedAndPasswordDoesNotMatch_whenAuthenticate_throwBadCredentialsException() {
			// given
			String passwordInput = "passwordInput";

			User user = createUser();
			willReturn(Optional.of(user)).given(userService).findByIdentifier(IDENTIFIER);
			willReturn(false).given(passwordEncoder).matches(passwordInput, user.getPasswordHash());

			// when
			Throwable actual =
					Assertions.catchThrowable(() -> authenticationService.authenticate(IDENTIFIER, passwordInput));

			// then
			Assertions.assertThat(actual).isInstanceOf(BadCredentialsException.class);
		}

		@Test
		void givenIdentifierExistedAndPasswordDoesMatch_whenAuthenticate_returnAuthenticate() {
			// given
			String passwordInput = "passwordInput";

			User user = createUser();
			willReturn(Optional.of(user)).given(userService).findByIdentifier(IDENTIFIER);
			willReturn(true).given(passwordEncoder).matches(passwordInput, user.getPasswordHash());

			// when
			Authentication actual = authenticationService.authenticate(IDENTIFIER, passwordInput);

			// then
			UsernamePasswordAuthenticationToken expected = new UsernamePasswordAuthenticationToken(user.getIdentifier(), user.getPasswordHash(),
					Collections.emptyList());
			Assertions.assertThat(actual).isEqualTo(expected);
		}
	}

	private User createUser() {
		return User.builder()
				.identifier(IDENTIFIER)
				.password("password")
				.passwordHash("passwordHash")
				.build();
	}
}
