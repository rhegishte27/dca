package com.equisoft.dca.backend.dataobject.validator;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class CobolDataObjectContentValidatorTest {

	private static CobolDataObjectContentValidator validator;

	@BeforeAll
	static void setUpOnce() {
		validator = new CobolDataObjectContentValidator();
	}

	@Nested
	class ValidContent {

		@Test
		void givenValidContent_whenValidate_thenReturnTrue() {
			//given
			String content = createValidContent();

			//when
			boolean actual = validator.isValid(content);

			//then
			Assertions.assertThat(actual).isTrue();
		}

		private String createValidContent() {
			StringBuilder content = new StringBuilder();
			content.append("      *************************************************************");
			content.append(System.lineSeparator());
			content.append("      * Copyright 2015 Universal Conversion Technologies");
			content.append(System.lineSeparator());
			content.append("xxxxxx        ");
			content.append(System.lineSeparator());
			content.append("xxxxxx");
			content.append(System.lineSeparator());
			content.append("xxxxxx                                                                      xxxxxxx");
			content.append(System.lineSeparator());
			content.append("      -");
			content.append(System.lineSeparator());
			content.append(System.lineSeparator());
			content.append("       01 TRCNVTRX.");
			return content.toString();
		}
	}

	@Nested
	class NullContent {
		@Test
		void givenNullContent_whenValidate_thenReturnFalse() {
			//given
			String content = null;

			//when
			boolean actual = validator.isValid(content);

			//then
			Assertions.assertThat(actual).isFalse();
		}
	}

	@Nested
	class InvalidContent {

		@Test
		void givenInvalidLine_whenValidate_thenReturnFalse() {
			//given
			String content = createInvalidContentWhereFirstLineHasXXXX();

			//when
			boolean actual = validator.isValid(content);

			//then
			Assertions.assertThat(actual).isFalse();
		}

		@Test
		void givenInvalidWord_whenValidate_thenReturnFalse() {
			//given
			String content = createInvalidContentWhereFirstLineHasX1();

			//when
			boolean actual = validator.isValid(content);

			//then
			Assertions.assertThat(actual).isFalse();
		}

		private String createInvalidContentWhereFirstLineHasXXXX() {
			StringBuilder content = new StringBuilder();
			content.append("      *************************************************************");
			content.append(System.lineSeparator());
			content.append("      * Copyright 2015 Universal Conversion Technologies");
			content.append(System.lineSeparator());
			content.append("xxxxxx        ");
			content.append(System.lineSeparator());
			content.append("xxxxxx                                                               xxxxxxx");
			content.append(System.lineSeparator());
			content.append("      -");
			content.append(System.lineSeparator());
			content.append(System.lineSeparator());
			content.append("       01 TRCNVTRX.");
			return content.toString();
		}

		private String createInvalidContentWhereFirstLineHasX1() {
			StringBuilder content = new StringBuilder();
			content.append("      *************************************************************");
			content.append(System.lineSeparator());
			content.append("      * Copyright 2015 Universal Conversion Technologies");
			content.append(System.lineSeparator());
			content.append("xxxxxx        ");
			content.append(System.lineSeparator());
			content.append("xxxxxx                                                                      xxxxxxx");
			content.append(System.lineSeparator());
			content.append("      -");
			content.append(System.lineSeparator());
			content.append(System.lineSeparator());
			content.append("       X1 TRCNVTRX.");
			return content.toString();
		}
	}
}
