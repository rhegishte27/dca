package com.equisoft.dca.backend.location.model;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.equisoft.dca.backend.common.model.BaseEnum;

class LocationTypeTest {

	@Nested
	class ValueOf {

		@ParameterizedTest
		@ValueSource(ints = {-2, -3, 0, 10000})
		void givenInvalidId_whenValueOf_thenThrowIllegalArgumentException(Integer id) {
			// given

			// when
			Throwable actual = Assertions.catchThrowable(() -> BaseEnum.valueOf(LocationType.class, id));

			// then
			Assertions.assertThat(actual).isInstanceOf(IllegalArgumentException.class);
		}

		@ParameterizedTest
		@ValueSource(ints = {1, 2})
		void givenValidId_whenValueOf_thenReturnEnum(Integer id) {
			// given
			LocationType expected = getLocationType(id);

			// when
			LocationType actual = BaseEnum.valueOf(LocationType.class, id);

			// then
			Assertions.assertThat(actual).isEqualTo(expected);
		}
	}

	private LocationType getLocationType(int id) {
		switch (id) {
			case 1:
				return LocationType.NETWORK;
			case 2:
				return LocationType.FTP;
			default:
				throw new IllegalArgumentException();
		}
	}
}
