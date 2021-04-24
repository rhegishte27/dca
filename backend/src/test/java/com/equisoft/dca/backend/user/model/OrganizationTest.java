package com.equisoft.dca.backend.user.model;

import java.lang.annotation.Annotation;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import javax.validation.metadata.ConstraintDescriptor;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

class OrganizationTest {
	private static final int NAME_SIZE_MIN = 1;
	private static final int NAME_SIZE_MAX = 50;

	private static final int DESCRIPTION_SIZE_MIN = 1;
	private static final int DESCRIPTION_SIZE_MAX = 255;

	private static Validator validator;

	private Organization organization;

	@BeforeAll
	static void setUpOnce() {
		ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
		validator = validatorFactory.getValidator();
	}

	@BeforeEach
	void setUp() {
		organization = Organization.builder()
				.id(1)
				.name(getARandomStringValidForName())
				.description(getARandomStringValidForDescription())
				.build();
	}

	@Nested
	class Name {
		@ParameterizedTest
		@EmptySource
		void givenNameBlank_whenValidateObject_thenReturnSizeAndEmptyConstraint(String name) {
			//given
			organization.setName(name);

			//when
			Set<ConstraintViolation<Organization>> violations = validator.validate(organization);

			//then
			assertConstraints(violations, Size.class, NotBlank.class);
		}

		@ParameterizedTest
		@ValueSource(strings = {"  ", "     ", "\t"})
		void givenNameBlankMoreThan1Char_whenValidateObject_thenReturnEmptyConstraint(String name) {
			//given
			organization.setName(name);

			//when
			Set<ConstraintViolation<Organization>> violations = validator.validate(organization);

			//then
			assertConstraints(violations, NotBlank.class);
		}

		@ParameterizedTest
		@NullSource
		void givenNameNull_whenValidateObject_thenReturnEmptyConstraint(String name) {
			//given
			organization.setName(name);

			//when
			Set<ConstraintViolation<Organization>> violations = validator.validate(organization);

			//then
			assertConstraints(violations, NotBlank.class);
		}

		@Test
		void givenNameSizeAboveMaxLimit_whenValidateObject_thenReturnSizeConstraint() {
			//given
			organization.setName(RandomStringUtils.random(NAME_SIZE_MAX + 1));

			//when
			Set<ConstraintViolation<Organization>> violations = validator.validate(organization);

			//then
			assertConstraints(violations, Size.class);
		}

		@Test
		void givenNameSizeBelowMinLimit_whenValidateObject_thenReturnSizeAndEmptyConstraint() {
			//given
			organization.setName(RandomStringUtils.random(NAME_SIZE_MIN - 1));

			//when
			Set<ConstraintViolation<Organization>> violations = validator.validate(organization);

			//then
			assertConstraints(violations, Size.class, NotBlank.class);
		}

		@ParameterizedTest
		@ValueSource(strings = {"NAME", "testt", "abcdef", "123 345 56", "@#$!asd@#%^"})
		void givenNameValid_whenValidateObject_thenReturnNoViolation(String name) {
			//given
			organization.setName(name);

			//when
			Set<ConstraintViolation<Organization>> violations = validator.validate(organization);

			//then
			Assertions.assertThat(violations).isEmpty();
		}
	}

	@Nested
	class Description {
		@ParameterizedTest
		@EmptySource
		void givenDescriptionBlank_whenValidateObject_thenReturnSizeAndEmptyConstraint(String description) {
			//given
			organization.setDescription(description);

			//when
			Set<ConstraintViolation<Organization>> violations = validator.validate(organization);

			//then
			assertConstraints(violations, Size.class, NotBlank.class);
		}

		@ParameterizedTest
		@ValueSource(strings = {" ", "    ", "\t"})
		void givenDescriptionBlankMoreThan1Char_whenValidateObject_thenReturnSizeAndEmptyConstraint(String description) {
			//given
			organization.setDescription(description);

			//when
			Set<ConstraintViolation<Organization>> violations = validator.validate(organization);

			//then
			assertConstraints(violations, NotBlank.class);
		}

		@ParameterizedTest
		@NullSource
		void givenDescriptionNull_whenValidateObject_thenReturnEmptyConstraint(String description) {
			//given
			organization.setDescription(description);

			//when
			Set<ConstraintViolation<Organization>> violations = validator.validate(organization);

			//then
			assertConstraints(violations, NotBlank.class);
		}

		@Test
		void givenDescriptionSizeAboveMaxLimit_whenValidateObject_thenReturnSizeConstraint() {
			//given
			organization.setDescription(RandomStringUtils.random(DESCRIPTION_SIZE_MAX + 1));

			//when
			Set<ConstraintViolation<Organization>> violations = validator.validate(organization);

			//then
			assertConstraints(violations, Size.class);
		}

		@Test
		void givenDescriptionSizeBelowMinLimit_whenValidateObject_thenReturnSizeConstraint() {
			//given
			organization.setDescription(RandomStringUtils.random(DESCRIPTION_SIZE_MIN - 1));

			//when
			Set<ConstraintViolation<Organization>> violations = validator.validate(organization);

			//then
			assertConstraints(violations, Size.class, NotBlank.class);
		}

		@ParameterizedTest
		@ValueSource(strings = {"DESCRIPTION", "testt", "abcdef", "123 345 56", "@#$!asd@#%^"})
		void givenDescriptionValid_whenValidateObject_thenReturnNoViolation(String description) {
			//given
			organization.setDescription(description);

			//when
			Set<ConstraintViolation<Organization>> violations = validator.validate(organization);

			//then
			Assertions.assertThat(violations).isEmpty();
		}
	}

	private void assertConstraints(Set<ConstraintViolation<Organization>> violations, Class... constraints) {
		Assertions.assertThat(violations)
				.hasSize(constraints.length)
				.extracting(ConstraintViolation::getConstraintDescriptor)
				.extracting(ConstraintDescriptor::getAnnotation)
				.flatExtracting(Annotation::annotationType)
				.contains(constraints);
	}

	private String getARandomStringValidForName() {
		return RandomStringUtils.random(RandomUtils.nextInt(NAME_SIZE_MIN, NAME_SIZE_MAX));
	}

	private String getARandomStringValidForDescription() {
		return RandomStringUtils.random(RandomUtils.nextInt(DESCRIPTION_SIZE_MIN, DESCRIPTION_SIZE_MAX));
	}
}
