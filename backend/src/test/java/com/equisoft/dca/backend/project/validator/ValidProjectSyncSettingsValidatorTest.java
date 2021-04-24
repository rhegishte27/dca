package com.equisoft.dca.backend.project.validator;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.equisoft.dca.backend.common.model.BaseEnum;
import com.equisoft.dca.backend.project.model.ProjectSyncSetting;
import com.equisoft.dca.backend.project.model.ProjectSyncSetting.ProjectSyncSettingId;
import com.equisoft.dca.backend.project.model.TypeProjectElement;

class ValidProjectSyncSettingsValidatorTest {

	private static ValidProjectSyncSettingsValidator validator;

	@BeforeAll
	static void setUpOnce() {
		validator = new ValidProjectSyncSettingsValidator();
	}

	@Nested
	class ProjectSyncSettingsIsNull {

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
	class ProjectSyncSettingsNotNull {
		private Set<ProjectSyncSetting> projectSyncSettings;

		@ParameterizedTest
		@ValueSource(strings = {"", "1-false", "2-false", "3-false", "1-true, 2-true", "1-false, 3-true", "2-true, 3-false"})
		void givenListSizeNotEqualsToTypeProjectElementSize_whenValidate_thenReturnFalse(String input) {
			assertValid(input, false);
		}

		@ParameterizedTest
		@ValueSource(strings = {"1-false, 1-true, 2-false", "2-true, 1-false, 2-true", "3-false, 1-true, 1-true", "1-false, 3-false, 3-false",
				"2-false, 3-true, 3-true", "2-false, 1-false, 1-false"})
		void givenListHavingMoreThan1SameTypeProjectElement_whenValidate_thenReturnFalse(String input) {
			assertValid(input, false);
		}

		@ParameterizedTest
		@ValueSource(
				strings = {"1-false, 2-false, 3-false, 4-false", "1-false, 4-true, 3-false ,2-false", "2-false, 4-true, 3-false, 1-false",
						"2-false, 1-false, 4-true, 3-false",
						"3-false,4-false, 1-false, 2-false", "3-false,4-false, 2-false, 1-false", "1-true, 2-false,4-false, 3-false",
						"1-false, 3-false ,2-true, 4-true", "2-false, 3-true, 4-true, 1-false",
						"2-false, 1-true, 3-true,4-false",
						"3-true, 1-true, 4-false,2-true", "3-false, 2-false, 1-true,4-false"})
		void givenListValid_whenValidate_thenReturnTrue(String input) {
			assertValid(input, true);
		}

		private void assertValid(String input, boolean expected) {
			//given
			projectSyncSettings = createListProjectSyncSettings(input);

			//when
			boolean actual = validator.isValid(projectSyncSettings, null);

			//then
			Assertions.assertThat(actual).isEqualTo(expected);
		}

		private Set<ProjectSyncSetting> createListProjectSyncSettings(String input) {
			if (input == null || input.isBlank()) {
				return Collections.emptySet();
			}

			String[] inputs = input.split(",");
			return Arrays.stream(inputs)
					.map(s -> {
						String[] data = s.split("-");
						TypeProjectElement typeProjectElement = BaseEnum.valueOf(TypeProjectElement.class, Integer.parseInt(data[0].trim()));
						Boolean isSyncActive = Boolean.valueOf(data[1].trim());
						return ProjectSyncSetting.builder()
								.id(new ProjectSyncSettingId(null, typeProjectElement))
								.isSyncEnabled(isSyncActive)
								.build();
					})
					.collect(Collectors.toSet());
		}
	}
}
