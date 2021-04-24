package com.equisoft.dca.backend.project.converter;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.equisoft.dca.backend.project.model.BackupKeepInterval;

class BackupKeepIntervalConverterTest {
	private BackupKeepIntervalConverter converter;

	@BeforeEach
	void setUp() {
		converter = new BackupKeepIntervalConverter();
	}

	@Nested
	class ConvertToDatabaseColumn {
		@Test
		void givenEnumNull_whenConvertToDatabaseColumn_returnNull() {
			// given

			// when
			Integer actual = converter.convertToDatabaseColumn(null);

			// then
			Assertions.assertThat(actual).isNull();
		}

		@ParameterizedTest
		@ValueSource(ints = {1, 2, 3})
		void givenEnum_whenConvertToDatabaseColumn_returnEnumId(int id) {
			// given
			BackupKeepInterval input = getBackupKeepInterval(id);

			// when
			Integer actual = converter.convertToDatabaseColumn(input);

			// then
			Assertions.assertThat(actual).isEqualTo(id);
		}
	}

	@Nested
	class ConvertToEntityAttribute {
		@Test
		void givenNullId_whenConvertToEntityAttribute_returnNull() {
			// given

			// when
			BackupKeepInterval actual = converter.convertToEntityAttribute(null);

			// then
			Assertions.assertThat(actual).isNull();
		}

		@ParameterizedTest
		@ValueSource(ints = {9, 10, -1, 0, -3, 999999})
		void givenInvalidId_whenConvertToEntityAttribute_throwIllegalArgumentException(int id) {
			// given

			// when
			Throwable actual = Assertions.catchThrowable(() -> converter.convertToEntityAttribute(id));

			// then
			Assertions.assertThat(actual).isInstanceOf(IllegalArgumentException.class);
		}

		@ParameterizedTest
		@ValueSource(ints = {1, 2, 3})
		void givenValidId_whenConvertToEntityAttribute_thenReturnBackupKeepInterval(int id) {
			// given

			// when
			BackupKeepInterval actual = converter.convertToEntityAttribute(id);

			// then
			Assertions.assertThat(actual).isEqualTo(getBackupKeepInterval(id));
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
				return null;
		}
	}
}
