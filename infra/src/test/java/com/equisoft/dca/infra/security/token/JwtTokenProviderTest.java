package com.equisoft.dca.infra.security.token;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.equisoft.dca.backend.setting.model.Setting;
import com.equisoft.dca.backend.setting.service.SettingService;
import com.equisoft.dca.infra.security.rsa.UnsafeRSAKeyProvider;

@ExtendWith(MockitoExtension.class)
class JwtTokenProviderTest {
	private static final String ALGO = "RSA";
	private static final String IDENTIFIER = "identifier";
	private static final String ROLE = "ROLE_ONE";

	private JwtTokenProvider tokenProvider;

	@Mock
	private SettingService settingService;

	@BeforeEach
	void setUp() {
		tokenProvider = new JwtTokenProvider(new UnsafeRSAKeyProvider(ALGO), settingService);
	}

	@Nested
	class ValidateToken {
		@Test
		void givenAGeneratedToken_whenValidating_thenSignedUsernameIsValid() {
			Set<String> roles = Collections.singleton(ROLE);
			Token dcaToken = tokenProvider.generateToken(IDENTIFIER, roles);

			UnpackedToken token = tokenProvider.validateToken(dcaToken.getValue());

			Assertions.assertThat(token.getIdentifier()).isEqualTo(IDENTIFIER);
			Assertions.assertThat(token.getRoles()).containsExactly(ROLE);
		}
	}

	@Nested
	class GenerateToken {

		@Nested
		class TokenDuration {
			private static final int DEFAULT_TOKEN_DURATION = 90;

			@Test
			void givenSettingReturnEmpty_whenGenerateToken_thenReturnDefaultTokenDuration() {
				// given
				Mockito.doReturn(Optional.empty()).when(settingService).get();

				// when
				Token actual = tokenProvider.generateToken(IDENTIFIER, Collections.singleton(ROLE));

				// then
				Assertions.assertThat(actual.getDuration()).isEqualTo(DEFAULT_TOKEN_DURATION);
			}

			@Test
			void givenSettingReturnSettingWithNullTokenDuration_whenGenerateToken_thenReturnDefaultTokenDuration() {
				// given
				Setting setting = Setting.builder().tokenDuration(null).build();
				Mockito.doReturn(Optional.of(setting)).when(settingService).get();

				// when
				Token actual = tokenProvider.generateToken(IDENTIFIER, Collections.singleton(ROLE));

				// then
				Assertions.assertThat(actual.getDuration()).isEqualTo(DEFAULT_TOKEN_DURATION);
			}

			@ParameterizedTest
			@ValueSource(ints = {1, 2, 30, 1000})
			void givenSettingReturnSettingWithTokenDuration_whenGenerateToken_thenReturnDefaultTokenDuration(Integer tokenDuration) {
				// given
				Setting setting = Setting.builder().tokenDuration(tokenDuration).build();
				Mockito.doReturn(Optional.of(setting)).when(settingService).get();

				// when
				Token actual = tokenProvider.generateToken(IDENTIFIER, Collections.singleton(ROLE));

				// then
				Assertions.assertThat(actual.getDuration()).isEqualTo(tokenDuration);
			}
		}
	}
}
