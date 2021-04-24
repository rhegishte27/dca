package com.equisoft.dca.backend.location.converter;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.equisoft.dca.backend.location.model.PlatformType;

class PlatformTypeConverterTest {

	private PlatformTypeConverter converter;

	@BeforeEach
	void setUp() {
		converter = new PlatformTypeConverter();
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
		@ValueSource(ints = {1, 2, 3, 4, 5})
		void givenEnum_whenConvertToDatabaseColumn_returnEnumId(int id) {
			// given
			PlatformType input = getPlatformType(id);

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
			PlatformType actual = converter.convertToEntityAttribute(null);

			// then
			Assertions.assertThat(actual).isNull();
		}

		@ParameterizedTest
		@ValueSource(ints = {9, 10, -1, 0, -3, 999999})
		void givenInvalidId_whenConvertToEntityAttribute_thenThrowIllegalArgumentException(int id) {
			// given

			// when
			Throwable actual = Assertions.catchThrowable(() -> converter.convertToEntityAttribute(id));

			// then
			Assertions.assertThat(actual).isInstanceOf(IllegalArgumentException.class);
		}

		@ParameterizedTest
		@ValueSource(ints = {1, 2, 3, 4, 5})
		void givenValidId_whenConvertToEntityAttribute_thenReturnPlatformType(int id) {
			// given

			// when
			PlatformType actual = converter.convertToEntityAttribute(id);

			// then
			Assertions.assertThat(actual).isEqualTo(getPlatformType(id));
		}
	}

	private PlatformType getPlatformType(int id) {
		switch (id) {
			case 1:
				return PlatformType.WINDOWS_UNIX;
			case 2:
				return PlatformType.MAINFRAME;
			case 3:
				return PlatformType.AS400;
			case 4:
				return PlatformType.AS400_2;
			case 5:
				return PlatformType.VSE_BIM;
			default:
				return null;
		}
	}

}
