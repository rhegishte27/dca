package com.equisoft.dca.backend.exception;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import lombok.AllArgsConstructor;
import lombok.Getter;

class EntityNotFoundExceptionTest {

	@Nested
	class Message {

		@Test
		void givenAnObject_whenCreateEntityNotFoundExceptionWithOneProperty_thenExceptionMessageContainsProperty() {
			//given
			String messageExpected = "OneClass {description=value} was not found";
			String[] argumentsExpected = new String[]{"oneclass", "description", "value"};
			OneClass oneClass = new OneClass(1, "value");

			//when
			EntityNotFoundException ex = new EntityNotFoundException(OneClass.class, "description", oneClass.getDescription());

			//then
			Assertions.assertThat(ex).hasMessage(messageExpected);
			Assertions.assertThat(ex.getMessageArguments()).isEqualTo(argumentsExpected);
		}

		@Test
		void givenAnObject_whenCreateEntityNotFoundExceptionWithTwoProperties_thenExceptionMessageContainsTwoPropertiesSameOrder() {
			//given
			String messageExpected = "OneClass {id=1, description=value} was not found";
			String[] argumentsExpected = new String[]{"oneclass", "id", "1", "description", "value"};
			OneClass oneClass = new OneClass(1, "value");

			//when
			EntityNotFoundException ex =
					new EntityNotFoundException(OneClass.class, "id", oneClass.getId().toString(), "description", oneClass.getDescription());

			//then
			Assertions.assertThat(ex).hasMessage(messageExpected);
			Assertions.assertThat(ex.getMessageArguments()).isEqualTo(argumentsExpected);
		}
	}

	@Nested
	class ThrowException {

		@Test
		void givenAnObject_whenCreateEntityNotFoundExceptionWithInvalidEntries_thenThrowIllegalArgumentException() {
			//given
			OneClass oneClass = new OneClass(1, "value");

			//when
			Throwable thrown = Assertions.catchThrowable(() -> new EntityNotFoundException(OneClass.class, "id", "description", oneClass.getDescription()));

			//then
			Assertions.assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
		}
	}

	@Nested
	class GetArguments {

		@Test
		void givenAnObject_whenGetMessageArguments_thenReturnArray() {
			//given
			OneClass oneClass = new OneClass(1, "value");
			EntityNotFoundException exception = new EntityNotFoundException(OneClass.class, "description", oneClass.getDescription());
			String[] expected = {"oneclass", "description", oneClass.getDescription()};

			//when
			String[] actual = exception.getMessageArguments();

			//then
			Assertions.assertThat(actual).isNotNull().isEqualTo(expected);
		}
	}

	@Nested
	class GetMessageCode {

		@Test
		void givenException_whenGetMessageCode_thenReturnCode() {
			//given
			OneClass oneClass = new OneClass(1, "value");
			EntityNotFoundException exception = new EntityNotFoundException(OneClass.class, "description", oneClass.getDescription());
			String expected = "entity.notfound";

			//when
			String actual = exception.getMessageCode();

			//then
			Assertions.assertThat(actual).isNotNull().isEqualTo(expected);
		}
	}

	@AllArgsConstructor
	@Getter
	private class OneClass {
		private Integer id;
		private String description;
	}
}
