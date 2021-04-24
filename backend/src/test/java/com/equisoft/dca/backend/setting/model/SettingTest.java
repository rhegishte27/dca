package com.equisoft.dca.backend.setting.model;

import java.lang.annotation.Annotation;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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

import com.equisoft.dca.backend.common.model.BaseEnum;
import com.equisoft.dca.backend.language.model.Language;

class SettingTest {

	private static Validator validator;

	private Setting.SettingBuilder setting;

	@BeforeAll
	static void setUpOnce() {
		ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
		validator = validatorFactory.getValidator();
	}

	@BeforeEach
	void setUp() {
		setting = Setting.builder();
		setting.language(Language.SPANISH);
		setting.tokenDuration(20);
		setting.commonFolder("common");
		setting.defaultDownloadFolder("download");
		setting.defaultImportFolder("import");
		setting.defaultExportFolder("export");
	}

	@Nested
	class TokenDuration {

		@ParameterizedTest
		@NullSource
		void givenTokenDurationNull_whenValidateObject_thenReturnNullConstraint(Integer duration) {
			// given
			setting.tokenDuration(duration);

			// when
			Set<ConstraintViolation<Setting>> violations = validator.validate(setting.build());

			// then
			assertConstraints(violations, NotNull.class);
		}

		@ParameterizedTest
		@ValueSource(ints = {1, 0, -1, 19, -999999})
		void givenTokenDurationLessThanMinValue_whenValidateObject_thenReturnMinConstraint(Integer duration) {
			// given
			setting.tokenDuration(duration);

			// when
			Set<ConstraintViolation<Setting>> violations = validator.validate(setting.build());

			// then
			assertConstraints(violations, Min.class);
		}

		@ParameterizedTest
		@ValueSource(ints = {121, 400, 9999})
		void givenTokenDurationGreaterThanMaxValue_whenValidateObject_thenReturnMaxConstraint(Integer duration) {
			// given
			setting.tokenDuration(duration);

			// when
			Set<ConstraintViolation<Setting>> violations = validator.validate(setting.build());

			// then
			assertConstraints(violations, Max.class);
		}

		@ParameterizedTest
		@ValueSource(ints = {20, 21, 119, 120, 25, 30, 60, 100})
		void givenValidTokenDuration_whenValidateObject_thenReturnNoViolation(Integer duration) {
			// given
			setting.tokenDuration(duration);

			// when
			Set<ConstraintViolation<Setting>> violations = validator.validate(setting.build());

			// then
			Assertions.assertThat(violations).isEmpty();
		}
	}

	@Nested
	class LanguageTest {

		@Test
		void givenLanguageNull_whenValidateObject_thenReturnNullConstraint() {
			// given
			setting.language(null);

			// when
			Set<ConstraintViolation<Setting>> violations = validator.validate(setting.build());

			// then
			assertConstraints(violations, NotNull.class);
		}

		@ParameterizedTest
		@ValueSource(ints = {1, 2})
		void givenValidLanguage_whenValidateObject_thenReturnNoViolation(Integer idLanguage) {
			// given
			setting.language(BaseEnum.valueOf(Language.class, idLanguage));

			// when
			Set<ConstraintViolation<Setting>> violations = validator.validate(setting.build());

			// then
			Assertions.assertThat(violations).isEmpty();
		}
	}

	@Nested
	class CommonFolder {
		@ParameterizedTest
		@EmptySource
		@NullSource
		void givenBlank_whenValidateObject_thenReturnValidPathAndValidDefaultFolderViolation(String input) {
			// given
			setting.commonFolder(input);

			// when
			Set<ConstraintViolation<Setting>> violations = validator.validate(setting.build());

			// then
			assertConstraints(violations, NotBlank.class);
		}

		@Test
		void givenInputMoreThanMaxSize_whenValidateObject_thenReturnValidPathAndValidDefaultFolderViolation() {
			// given
			setting.commonFolder(getRandomStringMoreThanMaxSize());

			// when
			Set<ConstraintViolation<Setting>> violations = validator.validate(setting.build());

			// then
			assertConstraints(violations, Size.class);
		}

		@ParameterizedTest
		@ValueSource(strings = {"test", "testTest", "pathValid"})
		void givenValidPathCommonFolder_whenValidatedObject_thenReturnNoViolation(String path) {
			// given
			setting.commonFolder(path);

			// when
			Set<ConstraintViolation<Setting>> violations = validator.validate(setting.build());

			// then
			Assertions.assertThat(violations).isEmpty();
		}
	}

	@Nested
	class DefaultImportFolder {

		@ParameterizedTest
		@EmptySource
		@NullSource
		void givenBlank_whenValidateObject_thenReturnValidPathAndValidDefaultFolderViolation(String input) {
			// given
			setting.defaultImportFolder(input);

			// when
			Set<ConstraintViolation<Setting>> violations = validator.validate(setting.build());

			// then
			assertConstraints(violations, NotBlank.class);
		}

		@Test
		void givenInputMoreThanMaxSize_whenValidateObject_thenReturnValidPathViolation() {
			// given
			setting.defaultImportFolder(getRandomStringMoreThanMaxSize());

			// when
			Set<ConstraintViolation<Setting>> violations = validator.validate(setting.build());

			// then
			assertConstraints(violations, Size.class);
		}

		@ParameterizedTest
		@ValueSource(strings = {"test", "testTest", "pathValid"})
		void givenValidDefaultImportFolderPath_whenValidateObject_thenReturnNoViolation(String path) {
			// given
			setting.defaultImportFolder(path);

			// when
			Set<ConstraintViolation<Setting>> violations = validator.validate(setting.build());

			// then
			Assertions.assertThat(violations).isEmpty();
		}
	}

	@Nested
	class DefaultExportFolder {
		@ParameterizedTest
		@EmptySource
		@NullSource
		void givenBlank_whenValidateObject_thenReturnValidPathAndValidDefaultFolderViolation(String input) {
			// given
			setting.defaultExportFolder(input);

			// when
			Set<ConstraintViolation<Setting>> violations = validator.validate(setting.build());

			// then
			assertConstraints(violations, NotBlank.class);
		}

		@Test
		void givenInputMoreThanMaxSize_whenValidateObject_thenReturnValidPathViolation() {
			// given
			setting.defaultExportFolder(getRandomStringMoreThanMaxSize());

			// when
			Set<ConstraintViolation<Setting>> violations = validator.validate(setting.build());

			// then
			assertConstraints(violations, Size.class);
		}

		@ParameterizedTest
		@ValueSource(strings = {"test", "testTest", "pathValid"})
		void givenValidDefaultExportFolderPath_whenValidateObject_thenReturnNoViolation(String path) {
			// given
			setting.defaultExportFolder(path);

			// when
			Set<ConstraintViolation<Setting>> violations = validator.validate(setting.build());

			// then
			Assertions.assertThat(violations).isEmpty();
		}
	}

	@Nested
	class DefaultDownloadFolder {
		@ParameterizedTest
		@EmptySource
		@NullSource
		void givenBlank_whenValidateObject_thenReturnValidPathAndValidDefaultFolderViolation(String input) {
			// given
			setting.defaultDownloadFolder(input);

			// when
			Set<ConstraintViolation<Setting>> violations = validator.validate(setting.build());

			// then
			assertConstraints(violations, NotBlank.class);
		}

		@Test
		void givenInputMoreThanMaxSize_whenValidateObject_thenReturnValidPathViolation() {
			// given
			setting.defaultDownloadFolder(getRandomStringMoreThanMaxSize());

			// when
			Set<ConstraintViolation<Setting>> violations = validator.validate(setting.build());

			// then
			assertConstraints(violations, Size.class);
		}

		@ParameterizedTest
		@ValueSource(strings = {"test", "testTest", "pathValid"})
		void givenValidDefaultDownloadFolderPath_whenValidateObject_thenReturnNoViolation(String path) {
			// given
			setting.defaultDownloadFolder(path);

			// when
			Set<ConstraintViolation<Setting>> violations = validator.validate(setting.build());

			// then
			Assertions.assertThat(violations).isEmpty();
		}
	}

	private void assertConstraints(Set<ConstraintViolation<Setting>> violations, Class... constraints) {
		Assertions.assertThat(violations)
				.hasSize(constraints.length)
				.extracting(ConstraintViolation::getConstraintDescriptor)
				.extracting(ConstraintDescriptor::getAnnotation)
				.flatExtracting(Annotation::annotationType)
				.contains((Object[]) constraints);
	}

	private String getRandomStringMoreThanMaxSize() {
		int maxSize = 256;
		return RandomStringUtils.randomAlphabetic(maxSize + RandomUtils.nextInt(1, 50));
	}
}
