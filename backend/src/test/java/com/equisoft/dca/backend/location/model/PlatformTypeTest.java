package com.equisoft.dca.backend.location.model;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.equisoft.dca.backend.common.model.BaseEnum;

class PlatformTypeTest {

	@Nested
	class ValueOf {

		@ParameterizedTest
		@ValueSource(ints = {-2, -3, 0, 10000})
		void givenInvalidId_whenValueOf_thenThrowIllegalArgumentException(Integer id) {
			// given

			// when
			Throwable actual = Assertions.catchThrowable(() -> BaseEnum.valueOf(PlatformType.class, id));

			// then
			Assertions.assertThat(actual).isInstanceOf(IllegalArgumentException.class);
		}

		@ParameterizedTest
		@ValueSource(ints = {1, 2, 3, 4, 5})
		void givenValidId_whenValueOf_thenReturnEnum(Integer id) {
			// given
			PlatformType expected = getPlatformType(id);

			// when
			PlatformType actual = BaseEnum.valueOf(PlatformType.class, id);

			// then
			Assertions.assertThat(actual).isEqualTo(expected);
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
				throw new IllegalArgumentException();
		}
	}
}
