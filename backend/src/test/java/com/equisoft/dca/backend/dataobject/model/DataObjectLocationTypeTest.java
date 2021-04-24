package com.equisoft.dca.backend.dataobject.model;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.equisoft.dca.backend.common.model.BaseEnum;

class DataObjectLocationTypeTest {
	@Nested
	class ValueOf {

		@ParameterizedTest
		@ValueSource(ints = {-2, -3, 30, 40, 0, 10000})
		void givenInvalidId_whenValueOf_throwIllegalArgumentException(Integer id) {
			// given

			// when
			Throwable actual = Assertions.catchThrowable(() -> BaseEnum.valueOf(DataObjectLocationType.class, id));

			// then
			Assertions.assertThat(actual).isInstanceOf(IllegalArgumentException.class);
		}

		@ParameterizedTest
		@ValueSource(ints = {1, 2, 3})
		void givenValidId_whenValueOf_throwReturnEnum(Integer id) {
			// given
			DataObjectLocationType expected = BaseEnum.valueOf(DataObjectLocationType.class, id);

			// when
			DataObjectLocationType actual = BaseEnum.valueOf(DataObjectLocationType.class, id);

			// then
			Assertions.assertThat(actual).isEqualTo(expected);
		}
	}
}
