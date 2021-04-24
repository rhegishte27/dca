package com.equisoft.dca.backend.util;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

class StringUtilTest {

	@Nested
	class IsAllUpperCase {
		@ParameterizedTest
		@EmptySource
		@NullSource
		@ValueSource(strings = {" ", "      ", "\t"})
		void givenBlankOrNullString_whenIsAllUpperCase_returnFalse(String input) {
			// given

			// when
			boolean actual = StringUtil.isAllUpperCase(input);

			// then
			Assertions.assertThat(actual).isEqualTo(false);
		}

		@ParameterizedTest
		@ValueSource(strings = {"test", "TESt", "TeSt", "TESt    ", "This is a String", "This_is_a_String"})
		void givenStringNotAllUpperCase_whenIsAllUpperCase_returnFalse(String input) {
			// given

			// when
			boolean actual = StringUtil.isAllUpperCase(input);

			// then
			Assertions.assertThat(actual).isEqualTo(false);
		}

		@ParameterizedTest
		@ValueSource(strings = {"TEST", "TEST  ", "TEST\t", "THIS IS A STRING", "THIS_IS_A_STRING"})
		void givenStringAllUpperCase_whenIsAllUpperCase_returnTrue(String input) {
			// given

			// when
			boolean actual = StringUtil.isAllUpperCase(input);

			// then
			Assertions.assertThat(actual).isEqualTo(true);
		}
	}

	@Nested
	class IsAllLowerCase {
		@ParameterizedTest
		@EmptySource
		@NullSource
		@ValueSource(strings = {" ", "      ", "\t"})
		void givenBlankOrNullString_whenIsAllLowerCase_returnFalse(String input) {
			// given

			// when
			boolean actual = StringUtil.isAllLowerCase(input);

			// then
			Assertions.assertThat(actual).isEqualTo(false);
		}

		@ParameterizedTest
		@ValueSource(strings = {"TEST", "TESt", "TeSt", "TESt    ", "This is a String", "This_is_a_String"})
		void givenStringNotAllLowerCase_whenIsAllLowerCase_returnFalse(String input) {
			// given

			// when
			boolean actual = StringUtil.isAllLowerCase(input);

			// then
			Assertions.assertThat(actual).isEqualTo(false);
		}

		@ParameterizedTest
		@ValueSource(strings = {"test", "test  ", "test\t", "this is a string", "this_is_a_string"})
		void givenStringAllLowerCase_whenIsAllLowerCase_returnTrue(String input) {
			// given

			// when
			boolean actual = StringUtil.isAllLowerCase(input);

			// then
			Assertions.assertThat(actual).isEqualTo(true);
		}
	}

	@Nested
	class ToTitleCaseSpaceDelimiter {
		@ParameterizedTest
		@NullSource
		void givenNullString_whenToTitleCaseSpaceDelimiter_returnNull(String input) {
			// given

			// when
			String actual = StringUtil.toTitleCaseSpaceDelimiter(input);

			// then
			Assertions.assertThat(actual).isEqualTo(input);
		}

		@ParameterizedTest
		@EmptySource
		@ValueSource(strings = {" ", "      ", "\t"})
		void givenBlankString_whenToTitleCaseSpaceDelimiter_returnBlank(String input) {
			// given

			// when
			String actual = StringUtil.toTitleCaseSpaceDelimiter(input);

			// then
			Assertions.assertThat(actual).isEqualTo(input);
		}

		@ParameterizedTest
		@ValueSource(strings = {"a", "$", "!", "1", "T"})
		void givenStringWith1Char_whenToTitleCaseSpaceDelimiter_returnThisCharUpperCase(String input) {
			// given

			// when
			String actual = StringUtil.toTitleCaseSpaceDelimiter(input);

			// then
			String expected = input.toUpperCase();
			Assertions.assertThat(actual).isEqualTo(expected);
		}

		@ParameterizedTest
		@CsvSource({"test1, Test1", "tEST, Test", "test  ,Test  ", "test\t, Test\t", "this is a string, This Is A String", "this_is_a_string and This, "
				+ "This_is_a_string And This",
				"t@s1, T@s1", "TH1S 1s @ cOmPl3X sTring, Th1s 1s @ Compl3x String"})
		void givenString_whenToTitleCaseSpaceDelimiter_returnTitleCaseInput(String input, String expected) {
			// given

			// when
			String actual = StringUtil.toTitleCaseSpaceDelimiter(input);

			// then
			Assertions.assertThat(actual).isEqualTo(expected);
		}
	}

	@Nested
	class ToTitleCaseIfNotMixedCase {
		@ParameterizedTest
		@NullSource
		void givenNullString_whenToTitleCaseIfNotMixedCase_returnNull(String input) {
			// given

			// when
			String actual = StringUtil.toTitleCaseIfNotMixedCase(input);

			// then
			Assertions.assertThat(actual).isEqualTo(input);
		}

		@ParameterizedTest
		@EmptySource
		@ValueSource(strings = {" ", "      ", "\t"})
		void givenBlankString_whenToTitleCaseIfNotMixedCase_returnBlank(String input) {
			// given

			// when
			String actual = StringUtil.toTitleCaseIfNotMixedCase(input);

			// then
			Assertions.assertThat(actual).isEqualTo(input);
		}

		@ParameterizedTest
		@ValueSource(strings = {"a", "$", "!", "1", "T"})
		void givenStringWith1Char_whenToTitleCaseIfNotMixedCase_returnThisCharUpperCase(String input) {
			// given

			// when
			String actual = StringUtil.toTitleCaseIfNotMixedCase(input);

			// then
			String expected = input.toUpperCase();
			Assertions.assertThat(actual).isEqualTo(expected);
		}

		@ParameterizedTest
		@CsvSource({"test1, Test1", "test, Test", "test  ,Test  ", "test\t, Test\t", "this is a string, This Is A String", "this_is_a_string and this, "
				+ "This_is_a_string And This",
				"t@s1, T@s1", "th1s 1s @ compl3x string, Th1s 1s @ Compl3x String"})
		void givenLowerCaseString_whenToTitleCaseIfNotMixedCase_returnToTileString(String input, String expected) {
			// given

			// when
			String actual = StringUtil.toTitleCaseIfNotMixedCase(input);

			// then
			Assertions.assertThat(actual).isEqualTo(expected);
		}

		@ParameterizedTest
		@CsvSource({"TEST1, Test1", "TEST, Test", "TEST  ,Test  ", "TEST\t, Test\t", "THIS IS A STRING, This Is A String", "THIS_IS_A_STRING AND THIS, "
				+ "This_is_a_string And This",
				"T@S1, T@s1", "TH1S 1S @ COMP13X STRING, Th1s 1s @ Comp13x String"})
		void givenUpperCaseString_whenToTitleCaseIfNotMixedCase_returnToTileString(String input, String expected) {
			// given

			// when
			String actual = StringUtil.toTitleCaseIfNotMixedCase(input);

			// then
			Assertions.assertThat(actual).isEqualTo(expected);
		}

		@ParameterizedTest
		@ValueSource(strings = {"TeST1", "TEsT", "TesT  ", "tESt\t", "THiS iS A sTRINg", "THIS_IS_A_STRING ANd THIS",
				"T@s1", "tH1S 1S @ cOMP13X STRING"})
		void givenMixedCaseString_whenToTitleCaseIfNotMixedCase_returnMixedCase(String input) {
			// given

			// when
			String actual = StringUtil.toTitleCaseIfNotMixedCase(input);

			// then
			Assertions.assertThat(actual).isEqualTo(input);
		}
	}
}
