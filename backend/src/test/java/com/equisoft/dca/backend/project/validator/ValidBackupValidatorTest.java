package com.equisoft.dca.backend.project.validator;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.provider.CsvSource;

import com.equisoft.dca.backend.project.model.BackupInterval;
import com.equisoft.dca.backend.project.model.BackupKeepInterval;
import com.equisoft.dca.backend.project.model.Project;

class ValidBackupValidatorTest {

	private static ValidBackupValidator validator;

	private Project project;

	@BeforeAll
	static void setUpOnce() {
		validator = new ValidBackupValidator();
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
	class BackupIsActive {

		@ParameterizedTest
		@CsvSource({",,,", "DAYS, 1,,", "WEEKS, 1, DAYS,", "MONTHS, 1, DAYS, 0"})
		void givenBackupIsActiveAndBackupPropertiesAreNull_whenValidate_thenReturnFalse(ArgumentsAccessor argumentsAccessor) {
			//given
			project = Project.builder()
					.isBackupEnabled(true)
					.backupInterval(argumentsAccessor.get(0, BackupInterval.class))
					.numberOfBackupIntervals(argumentsAccessor.getInteger(1))
					.backupKeepInterval(argumentsAccessor.get(2, BackupKeepInterval.class))
					.numberOfBackupKeepIntervals(argumentsAccessor.getInteger(3))
					.build();

			//when
			boolean actual = validator.isValid(project, null);

			//then
			Assertions.assertThat(actual).isFalse();
		}

		@Test
		void givenBackupIsActiveAndBackupPropertiesAreAllNotNull_whenValidate_thenReturnTrue() {
			//given
			project = Project.builder()
					.isBackupEnabled(true)
					.backupInterval(BackupInterval.MONTHS)
					.numberOfBackupIntervals(1)
					.backupKeepInterval(BackupKeepInterval.DAYS)
					.numberOfBackupKeepIntervals(1)
					.build();

			//when
			boolean actual = validator.isValid(project, null);

			//then
			Assertions.assertThat(actual).isTrue();
		}
	}

	@Nested
	class BackupIsNotActive {

		@ParameterizedTest
		@CsvSource({"DAYS, 1, DAYS, 1", ",,, 0", ",, MONTHS,", "WEEKS,,,", ",1,,"})
		void givenBackupIsNotActiveAndBackupPropertiesAreNotNull_whenValidate_thenReturnFalse(ArgumentsAccessor argumentsAccessor) {
			//given
			project = Project.builder()
					.isBackupEnabled(false)
					.backupInterval(argumentsAccessor.get(0, BackupInterval.class))
					.numberOfBackupIntervals(argumentsAccessor.getInteger(1))
					.backupKeepInterval(argumentsAccessor.get(2, BackupKeepInterval.class))
					.numberOfBackupKeepIntervals(argumentsAccessor.getInteger(3))
					.build();

			//when
			boolean actual = validator.isValid(project, null);

			//then
			Assertions.assertThat(actual).isFalse();
		}

		@Test
		void givenBackupIsNotActiveAndBackupPropertiesAreAllNull_whenValidate_thenReturnTrue() {
			//given
			project = Project.builder()
					.isBackupEnabled(false)
					.backupInterval(null)
					.numberOfBackupIntervals(null)
					.backupKeepInterval(null)
					.numberOfBackupKeepIntervals(null)
					.build();

			//when
			boolean actual = validator.isValid(project, null);

			//then
			Assertions.assertThat(actual).isTrue();
		}
	}
}
