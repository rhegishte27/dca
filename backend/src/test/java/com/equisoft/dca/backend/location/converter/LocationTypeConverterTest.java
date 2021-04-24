package com.equisoft.dca.backend.location.converter;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.equisoft.dca.backend.location.model.LocationType;

class LocationTypeConverterTest {

	private LocationTypeConverter converter;

	@BeforeEach
	void setUp() {
		converter = new LocationTypeConverter();
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
			LocationType input = getLocationType(id);

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
			LocationType actual = converter.convertToEntityAttribute(null);

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
		@ValueSource(ints = {1, 2})
		void givenValidId_whenConvertToEntityAttribute_thenReturnLocationType(int id) {
			// given

			// when
			LocationType actual = converter.convertToEntityAttribute(id);

			// then
			Assertions.assertThat(actual).isEqualTo(getLocationType(id));
		}
	}

	private LocationType getLocationType(int id) {
		switch (id) {
			case 1:
				return LocationType.NETWORK;
			case 2:
				return LocationType.FTP;
			default:
				return null;
		}
	}
}
