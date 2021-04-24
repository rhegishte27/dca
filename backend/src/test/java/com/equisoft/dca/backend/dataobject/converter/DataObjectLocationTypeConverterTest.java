package com.equisoft.dca.backend.dataobject.converter;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.equisoft.dca.backend.common.model.BaseEnum;
import com.equisoft.dca.backend.dataobject.model.DataObjectLocationType;

class DataObjectLocationTypeConverterTest {
	private DataObjectLocationTypeConverter converter;

	@BeforeEach
	void setUp() {
		converter = new DataObjectLocationTypeConverter();
	}

	@Nested
	class ConvertToDatabaseColumn {

		@Test
		void givenEnumNull_whenConvertToDatabaseColumn_thenReturnNull() {
			// given

			// when
			Integer actual = converter.convertToDatabaseColumn(null);

			// then
			Assertions.assertThat(actual).isNull();
		}

		@ParameterizedTest
		@ValueSource(ints = {1})
		void givenEnum_whenConvertToDatabaseColumn_thenReturnEnumId(int id) {
			// given
			DataObjectLocationType input = BaseEnum.valueOf(DataObjectLocationType.class, id);

			// when
			Integer actual = converter.convertToDatabaseColumn(input);

			// then
			Assertions.assertThat(actual).isEqualTo(id);
		}
	}

	@Nested
	class ConvertToEntityAttribute {

		@Test
		void givenNullId_whenConvertToEntityAttribute_thenReturnNull() {
			// given

			// when
			DataObjectLocationType actual = converter.convertToEntityAttribute(null);

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
		@ValueSource(ints = {1})
		void givenValidId_whenConvertToEntityAttribute_thenReturnLanguage(int id) {
			// given

			// when
			DataObjectLocationType actual = converter.convertToEntityAttribute(id);

			// then
			Assertions.assertThat(actual).isEqualTo(BaseEnum.valueOf(DataObjectLocationType.class, id));
		}
	}
}
