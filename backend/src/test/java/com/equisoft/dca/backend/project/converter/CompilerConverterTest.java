package com.equisoft.dca.backend.project.converter;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.equisoft.dca.backend.project.model.Compiler;

class CompilerConverterTest {
	private CompilerConverter converter;

	@BeforeEach
	void setUp() {
		converter = new CompilerConverter();
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
		@ValueSource(ints = {1, 2, 3, 4, 5, 6})
		void givenEnum_whenConvertToDatabaseColumn_returnEnumId(int id) {
			// given
			Compiler input = getCompiler(id);

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
			Compiler actual = converter.convertToEntityAttribute(null);

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
		@ValueSource(ints = {1, 2, 3, 4, 5, 6})
		void givenValidId_whenConvertToEntityAttribute_thenReturnCompiler(int id) {
			// given

			// when
			Compiler actual = converter.convertToEntityAttribute(id);

			// then
			Assertions.assertThat(actual).isEqualTo(getCompiler(id));
		}
	}

	private Compiler getCompiler(int id) {
		switch (id) {
			case 1:
				return Compiler.GENERIC;
			case 2:
				return Compiler.MICROFOCUS;
			case 3:
				return Compiler.FUJITSU;
			case 4:
				return Compiler.VISUAL_AGE;
			case 5:
				return Compiler.COBOL_II;
			case 6:
				return Compiler.DOUBLE_BYTE;
			default:
				return null;
		}
	}
}
