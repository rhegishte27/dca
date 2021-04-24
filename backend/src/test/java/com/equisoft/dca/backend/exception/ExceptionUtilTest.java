package com.equisoft.dca.backend.exception;

import java.util.LinkedHashMap;
import java.util.Map;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ExceptionUtilTest {

	@Nested
	class ToMap {

		@Nested
		class ThrowException {

			@Test
			void givenNullKeyType_whenToMap_thenThrowIllegalArgumentException() {
				// given
				Object[] entries = new Object[]{"key", "value"};
				Class expected = IllegalArgumentException.class;

				// when
				Throwable thrown = Assertions.catchThrowable(
						() -> ExceptionUtil.toMap(null, String.class, entries));

				//then
				Assertions.assertThat(thrown).isInstanceOf(expected);
			}

			@Test
			void givenNullValueType_whenToMap_thenThrowIllegalArgumentException() {
				// given
				Object[] entries = new Object[]{"key", "value"};
				Class expected = IllegalArgumentException.class;

				// when
				Throwable thrown = Assertions.catchThrowable(
						() -> ExceptionUtil.toMap(String.class, null, entries));

				//then
				Assertions.assertThat(thrown).isInstanceOf(expected);
			}

			@Test
			void givenInvalidEntries_whenToMap_throwIllegalArgumentException() {
				// given

				// when
				Throwable thrown = Assertions.catchThrowable(
						() -> ExceptionUtil.toMap(Object.class, Object.class, "test"));

				//then
				Assertions.assertThat(thrown)
						.isInstanceOf(IllegalArgumentException.class);
			}

			@Test
			void givenEntriesWithKeyDoesNotMatchKeyType_whenToMap_thenThrowIllegalArgumentException() {
				//given
				Object[] entries = new Object[]{1, "test"};

				//when
				Throwable thrown = Assertions.catchThrowable(
						() -> ExceptionUtil.toMap(String.class, String.class, entries));

				//then
				Assertions.assertThat(thrown)
						.isInstanceOf(IllegalArgumentException.class);
			}

			@Test
			void givenEntriesWithKeyDoesNotMatchValueType_whenToMap_thenThrowIllegalArgumentException() {
				//given
				Object[] entries = new Object[]{"String", 2};

				//when
				Throwable thrown = Assertions.catchThrowable(
						() -> ExceptionUtil.toMap(String.class, String.class, entries));

				//then
				Assertions.assertThat(thrown)
						.isInstanceOf(IllegalArgumentException.class);
			}
		}

		@Test
		void givenValidEntries_whenToMap_thenReturnRightMap() {
			//given
			String key = "String";
			int value = 2;
			Object[] entries = new Object[]{key, value};

			//when
			Map<String, Integer> actual = ExceptionUtil.toMap(String.class, Integer.class, entries);

			//then
			Map<String, Integer> expected = new LinkedHashMap<>();
			expected.put(key, value);
			Assertions.assertThat(actual).isEqualTo(expected);
		}
	}
}
