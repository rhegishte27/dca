package com.equisoft.dca.backend.user.model;

import java.util.Arrays;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class RoleTest {

	private Role role;

	@BeforeEach
	void setUp() {
		role = createRole(2);
	}

	@Nested
	class HigherRoleLevel {

		@ParameterizedTest
		@ValueSource(ints = {2, 3, 4})
		void givenUserWithHigherOrEqualsRoleLevel_whenHasHigherRoleLevel_thenReturnTrue(Integer roleLevel) {
			//given
			Role otherRole = createRole(roleLevel);

			//when
			boolean actual = role.hasHigherOrEqualsLevel(otherRole);

			//then
			Assertions.assertThat(actual).isTrue();
		}

		@ParameterizedTest
		@ValueSource(ints = {1})
		void givenUserWithLesserRoleLevel_whenHasHigherRoleLevel_thenReturnFalse(Integer roleLevel) {
			//given
			Role otherRole = createRole(roleLevel);

			//when
			boolean actual = role.hasHigherOrEqualsLevel(otherRole);

			//then
			Assertions.assertThat(actual).isFalse();
		}
	}

	private Role createRole(Integer level) {
		return Arrays.stream(Role.values()).filter(s -> level == s.getLevel()).findFirst().orElse(null);
	}
}
