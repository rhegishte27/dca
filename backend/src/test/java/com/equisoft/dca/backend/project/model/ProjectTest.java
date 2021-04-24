package com.equisoft.dca.backend.project.model;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.equisoft.dca.backend.project.model.Project.ProjectBuilder;

class ProjectTest {

	private ProjectBuilder projectBuilder;

	@BeforeEach
	void setUp() {
		projectBuilder = Project.builder();
	}

	@Nested
	class IdentifierFormat {

		@ParameterizedTest
		@CsvSource({"tesT test , TEST TEST ", "Te123, TE123"})
		void givenUnformattedIdentifier_whenSet_thenUpperCase(String actual, String expected) {
			//given
			projectBuilder.identifier(actual);

			//when
			Project project = projectBuilder.build();

			//then
			Assertions.assertThat(project.getIdentifier()).isEqualTo(expected);
		}
	}

	@Nested
	class DescriptionFormat {

		@ParameterizedTest
		@CsvSource({"   test test     , test test", "  test                test, test test", "test\t\t\t\ttest, test test"})
		void givenUnformattedDescription_whenSet_thenNormalizeBlankSpaces(String actual, String expected) {
			//given
			projectBuilder.description(actual);

			//when
			Project project = projectBuilder.build();

			//then
			Assertions.assertThat(project.getDescription()).isEqualTo(expected);
		}
	}
}
