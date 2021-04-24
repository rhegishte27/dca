package com.equisoft.dca.backend.user.exception;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class UserRoleLevelExceptionTest {

	@Nested
	class Message {

		@Test
		void givenArguments_whenCreateUserRoleLevelException_thenExceptionMessageContainsArguments() {
			//given
			String expected = "User {description:test test test} is not allowed to modify User {id:1}";
			String[] arguments = new String[]{"description", "test test test", "id", "1"};
			UserRoleLevelException ex = new UserRoleLevelException(arguments);

			//when
			String actual = ex.getMessage();

			//then
			Assertions.assertThat(actual).isNotNull().isEqualTo(expected);
		}
	}

	@Nested
	class GetArguments {

		@Test
		void givenException_whenGetArguments_thenReturnArgumentsSameOrder() {
			//given
			String[] expected = new String[]{"description", "test test test", "id", "1"};
			UserRoleLevelException ex = new UserRoleLevelException(expected);

			//when
			String[] actual = ex.getMessageArguments();

			//then
			Assertions.assertThat(actual).isNotNull().containsExactly(expected);
		}
	}

	@Nested
	class GetMessageCode {

		@Test
		void givenException_whenGetMessageCode_thenReturnCode() {
			//given
			String[] arguments = new String[]{"description", "test test test", "id", "1"};
			UserRoleLevelException exception = new UserRoleLevelException(arguments);
			String expected = "userrolelevel.exception";

			//when
			String actual = exception.getMessageCode();

			//then
			Assertions.assertThat(actual).isNotNull().isEqualTo(expected);
		}
	}
}
