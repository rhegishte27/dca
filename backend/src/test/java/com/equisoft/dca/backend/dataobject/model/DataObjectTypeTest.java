package com.equisoft.dca.backend.dataobject.model;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.equisoft.dca.backend.common.model.BaseEnum;

class DataObjectTypeTest {

	@Nested
	class ValueOf {

		@ParameterizedTest
		@ValueSource(ints = {-2, -3, 30, 40, 0, 10000})
		void givenInvalidId_whenValueOf_throwIllegalArgumentException(Integer id) {
			// given

			// when
			Throwable actual = Assertions.catchThrowable(() -> BaseEnum.valueOf(DataObjectType.class, id));

			// then
			Assertions.assertThat(actual).isInstanceOf(IllegalArgumentException.class);
		}

		@ParameterizedTest
		@ValueSource(ints = {1, 2, 3, 4, 5})
		void givenValidId_whenValueOf_throwReturnEnum(Integer id) {
			// given
			DataObjectType expected = BaseEnum.valueOf(DataObjectType.class, id);

			// when
			DataObjectType actual = BaseEnum.valueOf(DataObjectType.class, id);

			// then
			Assertions.assertThat(actual).isEqualTo(expected);
		}
	}
}
