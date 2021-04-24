package com.equisoft.dca.backend.project.model;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.equisoft.dca.backend.common.model.BaseEnum;

class TypeProjectElementTest {
	@Nested
	class FromInteger {
		@ParameterizedTest
		@ValueSource(ints = {-2, -3, 10, 0, 10000})
		void givenInvalidId_whenFrom_throwIllegalArgumentException(Integer id) {
			// given

			// when
			Throwable actual = Assertions.catchThrowable(() -> BaseEnum.valueOf(TypeProjectElement.class, id));

			// then
			Assertions.assertThat(actual).isInstanceOf(IllegalArgumentException.class);
		}

		@ParameterizedTest
		@ValueSource(ints = {1, 2, 3})
		void givenValidId_whenFrom_returnEnum(Integer id) {
			// given
			TypeProjectElement expected = getTypeProjectElement(id);

			// when
			TypeProjectElement actual = BaseEnum.valueOf(TypeProjectElement.class, id);

			// then
			Assertions.assertThat(actual).isEqualTo(expected);
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
				throw new IllegalArgumentException();
		}
	}
}
