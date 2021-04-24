package com.equisoft.dca.backend.user.model;

import java.lang.annotation.Annotation;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.NotNull;
import javax.validation.metadata.ConstraintDescriptor;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class UserSettingTest {

	private static Validator validator;

	private UserSetting userSetting;

	@BeforeAll
	static void setUpOnce() {
		ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
		validator = validatorFactory.getValidator();
	}

	@BeforeEach
	void setUp() {
		this.userSetting = createUserSetting(1);
	}

	@Nested
	class ValidateUser {

		@Test
		void given_when_then() {
			//given

			//when
			Set<ConstraintViolation<UserSetting>> violations = validator.validate(userSetting);

			//then
			assertConstraints(violations, NotNull.class);
		}
	}

	private UserSetting createUserSetting(Integer id) {
		return UserSetting.builder().id(id).build();
	}

	private void assertConstraints(Set<ConstraintViolation<UserSetting>> violations, Class... constraints) {
		Assertions.assertThat(violations)
				.hasSize(constraints.length)
				.extracting(ConstraintViolation::getConstraintDescriptor)
				.extracting(ConstraintDescriptor::getAnnotation)
				.flatExtracting(Annotation::annotationType)
				.contains(constraints);
	}
}
