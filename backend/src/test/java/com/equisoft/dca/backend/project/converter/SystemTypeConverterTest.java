package com.equisoft.dca.backend.project.converter;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.equisoft.dca.backend.project.model.SystemType;

class SystemTypeConverterTest {

	private SystemTypeConverter converter;

	@BeforeEach
	void setUp() {
		converter = new SystemTypeConverter();
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
			SystemType input = getSystemType(id);

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
			SystemType actual = converter.convertToEntityAttribute(null);

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
		void givenValidId_whenConvertToEntityAttribute_thenReturnGeneratedCodeLanguage(int id) {
			// given

			// when
			SystemType actual = converter.convertToEntityAttribute(id);

			// then
			Assertions.assertThat(actual).isEqualTo(getSystemType(id));
		}
	}

	private SystemType getSystemType(int id) {
		switch (id) {
			case 1:
				return SystemType.SOURCE;
			case 2:
				return SystemType.TARGET;
			case 3:
				return SystemType.UTILITY_SYSTEM;
			default:
				return null;
		}
	}
}
