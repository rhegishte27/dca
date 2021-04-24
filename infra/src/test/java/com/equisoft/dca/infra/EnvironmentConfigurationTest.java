package com.equisoft.dca.infra;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EnvironmentConfigurationTest {

	private EnvironmentConfiguration environmentConfiguration;

	@BeforeEach
	void setUp() {
		environmentConfiguration = new EnvironmentConfiguration();
	}

	@Nested
	class HasReactDevModeUrl {
		@Test
		void givenUrlNotEmpty_whenHasReactDevMode_returnTrue() {
			// given
			environmentConfiguration.setReactDevModeUrl("not empty");

			// when
			boolean actual = environmentConfiguration.hasReactDevModeUrl();

			// then
			Assertions.assertThat(actual).isTrue();
		}

		@Test
		void givenUrlEmpty_whenHasReactDevMode_returnFalse() {
			// given
			environmentConfiguration.setReactDevModeUrl("");

			// when
			boolean actual = environmentConfiguration.hasReactDevModeUrl();

			// then
			Assertions.assertThat(actual).isFalse();
		}
	}
}
