package com.equisoft.dca.backend.project.model;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.equisoft.dca.backend.common.model.BaseEnum;

class BackupKeepIntervalTest {
	@Nested
	class FromInteger {
		@ParameterizedTest
		@ValueSource(ints = {-2, -3, 6, 4, 0, 10000})
		void givenInvalidId_whenFrom_throwIllegalArgumentException(Integer id) {
			// given

			// when
			Throwable actual = Assertions.catchThrowable(() -> BaseEnum.valueOf(BackupKeepInterval.class, id));

			// then
			Assertions.assertThat(actual).isInstanceOf(IllegalArgumentException.class);
		}

		@ParameterizedTest
		@ValueSource(ints = {1, 2, 3})
		void givenValidId_whenFrom_throwReturnEnum(Integer id) {
			// given
			BackupKeepInterval expected = getBackupKeepInterval(id);

			// when
			BackupKeepInterval actual = BaseEnum.valueOf(BackupKeepInterval.class, id);

			// then
			Assertions.assertThat(actual).isEqualTo(expected);
		}
	}

	private BackupKeepInterval getBackupKeepInterval(int id) {
		switch (id) {
			case 1:
				return BackupKeepInterval.DAYS;
			case 2:
				return BackupKeepInterval.WEEKS;
			case 3:
				return BackupKeepInterval.MONTHS;
			default:
				throw new IllegalArgumentException();
		}
	}
}
