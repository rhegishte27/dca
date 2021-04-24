package com.equisoft.dca.backend.user.converter;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.equisoft.dca.backend.common.model.BaseEnum;
import com.equisoft.dca.backend.user.model.Feature;

class FeatureConverterTest {
	private FeatureConverter converter;

	@BeforeEach
	void setUp() {
		converter = new FeatureConverter();
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
			Feature input = getFeature(id);

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
			Feature actual = converter.convertToEntityAttribute(null);

			// then
			Assertions.assertThat(actual).isNull();
		}

		@ParameterizedTest
		@ValueSource(ints = {10, -1, 0, -3, 999999})
		void givenInvalidId_whenConvertToEntityAttribute_throwIllegalArgumentException(int id) {
			// given

			// when
			Throwable actual = Assertions.catchThrowable(() -> converter.convertToEntityAttribute(id));

			// then
			Assertions.assertThat(actual).isInstanceOf(IllegalArgumentException.class);
		}

		@ParameterizedTest
		@ValueSource(ints = {1, 2, 3})
		void givenValidId_whenConvertToEntityAttribute_thenReturnFeature(int id) {
			// given

			// when
			Feature actual = converter.convertToEntityAttribute(id);

			// then
			Assertions.assertThat(actual).isEqualTo(getFeature(id));
		}
	}

	private Feature getFeature(int id) {
		return BaseEnum.valueOf(Feature.class, id);
	}
}
