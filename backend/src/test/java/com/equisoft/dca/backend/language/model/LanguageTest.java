package com.equisoft.dca.backend.language.model;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.equisoft.dca.backend.common.model.BaseEnum;

class LanguageTest {
	@Nested
	class FromInteger {
		@ParameterizedTest
		@ValueSource(ints = {-2, -3, 3, 4, 0, 10000})
		void givenInvalidId_whenFrom_throwIllegalArgumentException(Integer id) {
			// given

			// when
			Throwable actual = Assertions.catchThrowable(() -> BaseEnum.valueOf(Language.class, id));

			// then
			Assertions.assertThat(actual).isInstanceOf(IllegalArgumentException.class);
		}

		@ParameterizedTest
		@ValueSource(ints = {1, 2})
		void givenValidId_whenFrom_throwReturnEnum(Integer id) {
			// given
			Language expected = getLanguage(id);

			// when
			Language actual = BaseEnum.valueOf(Language.class, id);

			// then
			Assertions.assertThat(actual).isEqualTo(expected);
		}
	}

	private Language getLanguage(int id) {
		switch (id) {
			case 1:
				return Language.ENGLISH;
			case 2:
				return Language.SPANISH;
			default:
				throw new IllegalArgumentException();
		}
	}
}
