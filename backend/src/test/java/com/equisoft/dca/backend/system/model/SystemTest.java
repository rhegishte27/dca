package com.equisoft.dca.backend.system.model;

import java.lang.annotation.Annotation;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.validation.metadata.ConstraintDescriptor;

import org.apache.commons.lang3.RandomStringUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class SystemTest {
	private static final int IDENTIFIER_MIN_SIZE = 3;
	private static final int IDENTIFIER_MAX_SIZE = 8;

	private static final int DESCRIPTION_MAX_SIZE = 255;

	private static Validator validator;

	@BeforeAll
	static void setUpOnce() {
		ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
		validator = validatorFactory.getValidator();
	}

	@Nested
	class Identifier {
		@ParameterizedTest
		@EmptySource
		@ValueSource(strings = {"  ", "\t"})
		void givenEmptyIdentifier_whenValidateSystem_thenReturnNotBlankAndSizeandPatternConstraints(String identifier) {
			//given
			System system = createSystem(1, identifier, "description");

			//when
			Set<ConstraintViolation<System>> violations = validator.validate(system);

			//then
			assertConstraints(violations, NotBlank.class, Size.class, Pattern.class);
		}

		@Test
		void givenNullIdentifier_whenValidateSystem_thenReturnNotBlankAndSizeAndPatternAndAsciiConstraints() {
			//given
			System system = createSystem(1, null, "description");

			//when
			Set<ConstraintViolation<System>> violations = validator.validate(system);

			//then
			assertConstraints(violations, NotBlank.class);
		}

		@Test
		void givenIdentifierLengthBelowMinSize_whenValidateSystem_thenReturnSizeConstraint() {
			//given
			System system = createSystem(1, RandomStringUtils.randomAlphabetic(IDENTIFIER_MIN_SIZE - 1), "description");

			//when
			Set<ConstraintViolation<System>> violations = validator.validate(system);

			//then
			assertConstraints(violations, Size.class);
		}

		@Test
		void givenIdentifierLengthAboveMaxSize_whenValidateSystem_thenReturnSizeContraint() {
			//given
			System system = createSystem(1, RandomStringUtils.randomAlphabetic(IDENTIFIER_MAX_SIZE + 1), "description");

			//when
			Set<ConstraintViolation<System>> violations = validator.validate(system);

			//then
			assertConstraints(violations, Size.class);
		}

		@ParameterizedTest
		@ValueSource(strings = {"1dsfdffs", "we1ss@dd", "$e1ssDdd"})
		void givenIdentifierInvalidPattern_whenValidateSystem_thenReturnPatternConstraint(String identifier) {
			//given
			System system = createSystem(1, identifier, "description");

			//when
			Set<ConstraintViolation<System>> violations = validator.validate(system);

			//then
			assertConstraints(violations, Pattern.class);
		}
	}

	@Nested
	class Description {
		@Test
		void givenEmptyDescription_whenValidateSystem_thenReturnNotBlankAndSizeConstraints() {
			//given
			System system = createSystem(1, "identifi", "");

			//when
			Set<ConstraintViolation<System>> violations = validator.validate(system);

			//then
			assertConstraints(violations, NotBlank.class, Size.class);
		}

		@ParameterizedTest
		@ValueSource(strings = {"  ", "\t"})
		void givenBlankDescription_whenValidateSystem_thenReturnNotBlankConstraint(String description) {
			//given
			System system = createSystem(1, "identifi", description);

			//when
			Set<ConstraintViolation<System>> violations = validator.validate(system);

			//then
			assertConstraints(violations, NotBlank.class);
		}

		@Test
		void givenEmptyDescription_whenValidateSystem_thenReturnNotBlankAndSizeConstraint() {
			//given
			System system = createSystem(1, "identifi", "");

			//when
			Set<ConstraintViolation<System>> violations = validator.validate(system);

			//then
			assertConstraints(violations, NotBlank.class, Size.class);
		}

		@Test
		void givenNullDescription_whenValidateSystem_thenReturnNotBlankConstraint() {
			//given
			System system = createSystem(1, "identifi", null);

			//when
			Set<ConstraintViolation<System>> violations = validator.validate(system);

			//then
			assertConstraints(violations, NotBlank.class);
		}

		@Test
		void givenDescriptionLengthAboveMaxSize_whenValidateSystem_thenReturnSizeConstraint() {
			//given
			System system = createSystem(1, "identifi", RandomStringUtils.random(DESCRIPTION_MAX_SIZE + 1));

			//when
			Set<ConstraintViolation<System>> violations = validator.validate(system);

			//then
			assertConstraints(violations, Size.class);
		}
	}

	private System createSystem(Integer id, String identifier, String description) {
		return System.builder().id(id).identifier(identifier).description(description).build();
	}

	private void assertConstraints(Set<ConstraintViolation<System>> violations, Class... constraints) {
		Assertions.assertThat(violations)
				.hasSize(constraints.length)
				.extracting(ConstraintViolation::getConstraintDescriptor)
				.extracting(ConstraintDescriptor::getAnnotation)
				.flatExtracting(Annotation::annotationType)
				.contains((Object[]) constraints);
	}
}
