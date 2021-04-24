package com.equisoft.dca.backend.project.model;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.equisoft.dca.backend.common.model.BaseEnum;

class CompilerTest {
	@Nested
	class FromInteger {
		@ParameterizedTest
		@ValueSource(ints = {-2, -3, 7, 8, 0, 10000})
		void givenInvalidId_whenFrom_throwIllegalArgumentException(Integer id) {
			// given

			// when
			Throwable actual = Assertions.catchThrowable(() -> BaseEnum.valueOf(Compiler.class, id));

			// then
			Assertions.assertThat(actual).isInstanceOf(IllegalArgumentException.class);
		}

		@ParameterizedTest
		@ValueSource(ints = {1, 2, 3, 4, 5, 6})
		void givenValidId_whenFrom_throwReturnEnum(Integer id) {
			// given
			Compiler expected = getCompiler(id);

			// when
			Compiler actual = BaseEnum.valueOf(Compiler.class, id);

			// then
			Assertions.assertThat(actual).isEqualTo(expected);
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
				throw new IllegalArgumentException();
		}
	}
}
