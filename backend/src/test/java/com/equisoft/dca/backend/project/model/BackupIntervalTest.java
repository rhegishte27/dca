package com.equisoft.dca.backend.project.model;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.equisoft.dca.backend.common.model.BaseEnum;

class BackupIntervalTest {

	@Nested
	class FromInteger {

		@ParameterizedTest
		@ValueSource(ints = {-2, -3, 6, 4, 0, 10000})
		void givenInvalidId_whenFrom_throwIllegalArgumentException(Integer id) {
			// given

			// when
			Throwable actual = Assertions.catchThrowable(() -> BaseEnum.valueOf(BackupInterval.class, id));

			// then
			Assertions.assertThat(actual).isInstanceOf(IllegalArgumentException.class);
		}

		@ParameterizedTest
		@ValueSource(ints = {1, 2, 3})
		void givenValidId_whenFrom_throwReturnEnum(Integer id) {
			// given
			BackupInterval expected = getBackupInterval(id);

			// when
			BackupInterval actual = BaseEnum.valueOf(BackupInterval.class, id);

			// then
			Assertions.assertThat(actual).isEqualTo(expected);
		}
	}

	private BackupInterval getBackupInterval(int id) {
		switch (id) {
			case 1:
				return BackupInterval.DAYS;
			case 2:
				return BackupInterval.WEEKS;
			case 3:
				return BackupInterval.MONTHS;
			default:
				throw new IllegalArgumentException();
		}
	}
}
