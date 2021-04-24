package com.equisoft.dca.backend.project.validator;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.EnumSource.Mode;

import com.equisoft.dca.backend.project.model.Compiler;
import com.equisoft.dca.backend.project.model.GeneratedCodeLanguage;
import com.equisoft.dca.backend.project.model.Project;

class ValidCompilerValidatorTest {

	private static ValidCompilerValidator validator;

	private Project project;

	@BeforeAll
	static void setUpOnce() {
		validator = new ValidCompilerValidator();
	}

	@Nested
	class ProjectIsNull {

		@Test
		void givenNullProject_whenValidate_thenReturnFalse() {
			//given

			//when
			boolean actual = validator.isValid(null, null);

			//then
			Assertions.assertThat(actual).isFalse();
		}
	}

	@Nested
	class ValidateGeneratedCodeLanguage {

		@ParameterizedTest
		@EnumSource(Compiler.class)
		void givenGeneratedCodeLanguageNull_whenValidate_thenReturnFalse(Compiler compiler) {
			//given
			project = Project.builder().generatedCodeLanguage(null).compiler(compiler).build();

			//when
			boolean actual = validator.isValid(project, null);

			//then
			Assertions.assertThat(actual).isFalse();
		}
	}

	@Nested
	class ValidateCompiler {

		@ParameterizedTest
		@EnumSource(GeneratedCodeLanguage.class)
		void givenCompilerNull_whenIsValid_thenReturnFalse(GeneratedCodeLanguage generatedCodeLanguage) {
			//given
			project = Project.builder().generatedCodeLanguage(generatedCodeLanguage).compiler(null).build();

			//when
			boolean actual = validator.isValid(project, null);

			//then
			Assertions.assertThat(actual).isFalse();
		}
	}

	@Nested
	class ValidateGeneratedCodeLanguageAndCompiler {

		@ParameterizedTest
		@EnumSource(value = Compiler.class, names = {"GENERIC"}, mode = Mode.EXCLUDE)
		void givenGeneratedCodeLanguageEqualsJavaAndCompilerInvalid_whenIsValid_thenReturnFalse(Compiler compiler) {
			//given
			project = Project.builder().generatedCodeLanguage(GeneratedCodeLanguage.JAVA).compiler(compiler).build();

			//when
			boolean actual = validator.isValid(project, null);

			//then
			Assertions.assertThat(actual).isFalse();
		}

		@ParameterizedTest
		@EnumSource(value = Compiler.class, names = {"GENERIC"})
		void givenGeneratedCodeLanguageEqualsJavaAndValidCompiler_whenIsValid_thenReturnTrue(Compiler compiler) {
			//given
			project = Project.builder().generatedCodeLanguage(GeneratedCodeLanguage.JAVA).compiler(compiler).build();

			//when
			boolean actual = validator.isValid(project, null);

			//then
			Assertions.assertThat(actual).isTrue();
		}

		@ParameterizedTest
		@EnumSource(Compiler.class)
		void givenGeneratedCodeLanguageEqualsCobolAndCompilerValid_whenIsValid_thenReturnTrue(Compiler compiler) {
			//given
			project = Project.builder().generatedCodeLanguage(GeneratedCodeLanguage.COBOL).compiler(compiler).build();

			//when
			boolean actual = validator.isValid(project, null);

			//then
			Assertions.assertThat(actual).isTrue();
		}
	}
}
