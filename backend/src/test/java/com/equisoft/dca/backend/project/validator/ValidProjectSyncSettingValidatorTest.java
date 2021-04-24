package com.equisoft.dca.backend.project.validator;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.equisoft.dca.backend.location.model.Location;
import com.equisoft.dca.backend.project.model.ProjectSyncSetting;

class ValidProjectSyncSettingValidatorTest {

	private static ValidProjectSyncSettingValidator validator;

	@BeforeAll
	static void setUpOnce() {
		validator = new ValidProjectSyncSettingValidator();
	}

	@Nested
	class ProjectSyncSettingIsNull {

		@Test
		void givenNullProjectSyncSetting_whenValidate_thenReturnFalse() {
			//given

			//when
			boolean actual = validator.isValid(null, null);

			//then
			Assertions.assertThat(actual).isFalse();
		}
	}

	@Nested
	class ProjectSyncSettingIsNotNull {
		private ProjectSyncSetting.ProjectSyncSettingBuilder projectSyncSetting;

		private void assertValidProjectSyncSetting(boolean expected) {
			// given

			// when
			boolean actual = validator.isValid(projectSyncSetting.build(), null);

			// then
			Assertions.assertThat(actual).isEqualTo(expected);
		}

		@BeforeEach
		void setUp() {
			projectSyncSetting = ProjectSyncSetting.builder();
		}

		@Nested
		class SyncProjectElementEnabled {
			@BeforeEach
			void setUp() {
				projectSyncSetting.isSyncEnabled(true);
			}

			@Nested
			class LocationIsNull {
				@BeforeEach
				void setUp() {
					projectSyncSetting.location(null);
				}

				@Test
				void whenValid_returnFalse() {
					assertValidProjectSyncSetting(false);
				}
			}

			@Nested
			class LocationIsNotNull {
				@BeforeEach
				void setUp() {
					projectSyncSetting.location(Location.builder().build());
				}

				@Test
				void whenValid_returnTrue() {
					assertValidProjectSyncSetting(true);
				}
			}
		}

		@Nested
		class SyncProjectElementDisabled {
			@BeforeEach
			void setUp() {
				projectSyncSetting.isSyncEnabled(false);
			}

			@Nested
			class LocationIsNull {
				@BeforeEach
				void setUp() {
					projectSyncSetting.location(null);
				}

				@Test
				void whenValid_returnTrue() {
					assertValidProjectSyncSetting(true);
				}
			}

			@Nested
			class LocationIsNotNull {
				@BeforeEach
				void setUp() {
					projectSyncSetting.location(Location.builder().build());
				}

				@Test
				void whenValid_returnTrue() {
					assertValidProjectSyncSetting(true);
				}
			}
		}
	}
}
