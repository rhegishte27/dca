package com.equisoft.dca.backend.user.validator;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import com.equisoft.dca.backend.common.model.BaseEnum;
import com.equisoft.dca.backend.user.model.Feature;
import com.equisoft.dca.backend.user.model.Role;
import com.equisoft.dca.backend.user.model.User;

class ValidRoleFeatureValidatorTest {

	private static ValidRoleFeatureValidator validator;

	@BeforeAll
	static void setUpOnce() {
		validator = new ValidRoleFeatureValidator();
	}

	@Nested
	class UserIsNull {
		@Test
		void givenNullUser_whenValidate_thenReturnFalse() {
			//given

			//when
			boolean actual = validator.isValid(null, null);

			//then
			Assertions.assertThat(actual).isFalse();
		}
	}

	@Nested
	class UserIsNotNull {
		private User.UserBuilder userBuilder;

		@BeforeEach
		void setUp() {
			userBuilder = User.builder();
		}

		@ParameterizedTest
		@NullAndEmptySource
		@ValueSource(strings = {"1-2-3", "5-6-4-7-8", "5", "1", "2-7-4"})
		void givenRoleNull_whenValidate_thenReturnFalse(String featuresId) {
			assertValidRoleFeatures(null, getFeaturesInput(featuresId), false);
		}

		@ParameterizedTest
		@NullSource
		@EnumSource(Role.class)
		void givenFeatureNull_whenValidate_thenReturnFalse(Role role) {
			assertValidRoleFeatures(role, null, false);
		}

		@ParameterizedTest
		@CsvSource(value = {"1, 1-2-3-4", "1, ", "2, 4-5-6", "3, 1", "3, 1-2-3", "4, 1", "4, 1-2-3", "5, 1", "5, 1-2-3"})
		void givenRoleFeaturesNotValid_whenValidate_thenReturnFalse(String roleId, String featuresId) {
			assertValidRoleFeatures(getRoleInput(roleId), getFeaturesInput(featuresId), false);
		}

		@ParameterizedTest
		@CsvSource(value = {"1, 1-2-3-4-5-6-7-8-9", "2, 3-4-5-6-7-8-9",
				"3, ", "3, 2", "3, 2-3-4",
				"4, ", "4, 3", "4, 5-6-7-8-9",
				"5, ", "5, 5-6-7-8", "5, 2-3"})
		void givenRoleFeaturesValid_whenValidate_thenReturnTrue(String roleId, String featuresId) {
			assertValidRoleFeatures(getRoleInput(roleId), getFeaturesInput(featuresId), true);
		}

		private void assertValidRoleFeatures(Role role, Set<Feature> features, boolean expected) {
			// given
			userBuilder.role(role)
					.features(features)
					.build();

			// when
			boolean actual = validator.isValid(userBuilder.build(), null);

			// then
			Assertions.assertThat(actual).isEqualTo(expected);
		}

		private Role getRoleInput(String input) {
			return BaseEnum.valueOf(Role.class, Integer.parseInt(input));
		}

		private Set<Feature> getFeaturesInput(String input) {
			if (input == null || input.isBlank()) {
				return Collections.emptySet();
			}
			String[] featureId = input.split("-");
			return Arrays.stream(featureId).map(f -> BaseEnum.valueOf(Feature.class, Integer.parseInt(f))).collect(Collectors.toSet());
		}
	}
}
