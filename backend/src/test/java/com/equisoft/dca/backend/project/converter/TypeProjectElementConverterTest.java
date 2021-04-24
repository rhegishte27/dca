package com.equisoft.dca.backend.project.converter;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.equisoft.dca.backend.project.model.TypeProjectElement;

class TypeProjectElementConverterTest {
	private TypeProjectElementConverter converter;

	@BeforeEach
	void setUp() {
		converter = new TypeProjectElementConverter();
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
		@ValueSource(ints = {1, 2})
		void givenEnum_whenConvertToDatabaseColumn_returnEnumId(int id) {
			// given
			TypeProjectElement input = getTypeProjectElement(id);

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
			TypeProjectElement actual = converter.convertToEntityAttribute(null);

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
		@ValueSource(ints = {1, 2})
		void givenValidId_whenConvertToEntityAttribute_thenReturnTypeProjectElement(int id) {
			// given

			// when
			TypeProjectElement actual = converter.convertToEntityAttribute(id);

			// then
			Assertions.assertThat(actual).isEqualTo(getTypeProjectElement(id));
		}
	}

	private TypeProjectElement getTypeProjectElement(int id) {
		switch (id) {
			case 1:
				return TypeProjectElement.SOURCE_CODE;
			case 2:
				return TypeProjectElement.MAPS;
			case 3:
				return TypeProjectElement.TABLE_DATA_DICTIONARY;
			case 4:
				return TypeProjectElement.BACKUPS;
			default:
				return null;
		}
	}
}
