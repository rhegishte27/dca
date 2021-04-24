package com.equisoft.dca.backend.common.util;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class StringCompressionTest {

	private StringCompression stringCompression;

	@BeforeEach
	void setUp() {
		stringCompression = new StringCompression(List.of("ACCEPT", "ACCESS", "ADD", "ADVANCING", "AFTER", "ALL", "ALPHABETIC", "ALSO"));
	}

	@Nested
	class TextIsNull {

		@Test
		void givenTextIsNull_whenCompress_thenReturnEmpty() {
			//given

			//when
			Optional<String> actual = stringCompression.compress(null, 3, List.of("THSIALTN", "123456", "123"), false, true);

			//then
			Assertions.assertThat(actual).isEmpty();
		}
	}

	@Nested
	class TextLengthIsLessThanOrEqualToMaxLength {

		@Nested
		class DoNotCheckCobolList {

			@Test
			void givenTextIsNotInExcludeWords_whenCompress_thenReturnTheSameText() {
				//given
				String text = "accepte";
				Optional<String> expected = Optional.of(text);

				//when
				Optional<String> actual = stringCompression.compress(text, 8, List.of("THSIALTN", "123456", "123"), false, false);

				//then
				Assertions.assertThat(actual).isPresent().isEqualTo(expected);
			}

			@Test
			void givenTextIsInExcludedWords_whenCompress_thenReturnAnotherText() {
				//given
				String text = "Acc3pTe";
				Optional<String> expected = Optional.of("Acc3pT");

				//when
				Optional<String> actual = stringCompression.compress(text, 8, List.of(text, "THSIALTN", "123456", "123"), false, false);

				//then
				Assertions.assertThat(actual).isPresent().isEqualTo(expected);
			}
		}

		@Nested
		class CheckCobolList {

			@Test
			void givenTextIsNotInCobolList_whenCompress_thenReturnTheSameText() {
				//given
				String text = "AccepTe";
				Optional<String> expected = Optional.of(text);

				//when
				Optional<String> actual = stringCompression.compress(text, 8, List.of("THSIALTN", "123456", "123"), true, false);

				//then
				Assertions.assertThat(actual).isPresent().isEqualTo(expected);
			}

			@Test
			void givenTextIsInCobolList_whenCompress_thenReturnAnotherText() {
				//given
				String text = "AFTER";
				Optional<String> expected = Optional.of("AFTR");

				//when
				Optional<String> actual = stringCompression.compress(text, 8, List.of("THSIALTN", "123456", "123"), true, false);

				//then
				Assertions.assertThat(actual).isPresent().isEqualTo(expected);
			}
		}

		@Nested
		class Capitalize {

			@Test
			void givenCapitalizeText_whenCompress_thenReturnSameTextCapitalized() {
				//given
				String text = "AccepTe";
				Optional<String> expected = Optional.of("ACCEPTE");

				//when
				Optional<String> actual = stringCompression.compress(text, 8, List.of("THSIALTN", "123456", "123"), true, true);

				//then
				Assertions.assertThat(actual).isPresent().isEqualTo(expected);
			}

			@Test
			void givenCapitalizeText_whenCompress_thenReturnAnotherTextCapitalized() {
				//given
				String text = "AccepTe";
				Optional<String> expected = Optional.of("ACCEPTE");

				//when
				Optional<String> actual = stringCompression.compress(text, 7, List.of("THSIALTN", "123456", "123"), true, true);

				//then
				Assertions.assertThat(actual).isPresent().isEqualTo(expected);
			}
		}
	}

	@Nested
	class TextLengthIsGreaterThanMaxLength {

		@Nested
		class DoNotCheckCobolList {

			@Test
			void givenTextIsNotInExcludeWords_whenCompress_thenReturnTheSameText() {
				//given
				String text = "THIS_IS_A_LONG_TABLE-NAME";
				Optional<String> expected = Optional.of("THIALTN");

				//when
				Optional<String> actual = stringCompression.compress(text, 8, List.of("THSIALTN", "123456", "123"), true, false);

				//then
				Assertions.assertThat(actual).isPresent().isEqualTo(expected);
			}
		}
	}
}
