package com.equisoft.dca.backend.exception;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import lombok.AllArgsConstructor;
import lombok.Getter;

class EntityReferenceConflictedExceptionTest {

	@Nested
	class Message {
		@Test
		void givenAnObject_whenCreateEntityReferenceConflictedException_thenExceptionMessageContainsProperty() {
			//given

			//when
			EntityReferenceConflictedException ex =
					new EntityReferenceConflictedException(OneClass.class, OtherClass.class, "name", "test");

			//then
			Assertions.assertThat(ex)
					.hasMessage(String.format(EntityReferenceConflictedException.MESSAGE, "OneClass", "{name=test}", "OtherClass"));
		}

		@Test
		void givenAnObject_whenCreateEntityAlreadyExistsExceptionWithTwoProperties_thenExceptionMessageContainsTwoProperties() {
			//given
			OneClass oneClass = new OneClass(1, "value");

			//when
			EntityReferenceConflictedException ex =
					new EntityReferenceConflictedException(OneClass.class, OtherClass.class, "description", oneClass.getDescription(), "id",
							oneClass.getId().toString());

			//then
			Assertions.assertThat(ex)
					.hasMessage(String.format(EntityReferenceConflictedException.MESSAGE, "OneClass", "{description=value, id=1}", "OtherClass"));
		}
	}

	@Nested
	class ThrowException {

		@Test
		void givenAnObject_whenCreateEntityAlreadyExistsExceptionWithInvalidEntries_thenThrowIllegalArgumentException() {
			//given
			OneClass oneClass = new OneClass(1, "value");

			//when
			Throwable thrown = Assertions.catchThrowable(
					() -> new EntityReferenceConflictedException(OneClass.class, OtherClass.class, "id", "description", oneClass.getDescription()));

			//then
			Assertions.assertThat(thrown)
					.isInstanceOf(IllegalArgumentException.class);
		}
	}

	@Nested
	class GetArguments {
		@Test
		void givenAnObject_whenGetMessageArguments_thenReturnArray() {
			//given
			EntityReferenceConflictedException exception = new EntityReferenceConflictedException(OneClass.class, OtherClass.class, "id", "1");
			String[] expected = {"oneclass", "id", "1", "otherclass"};

			//when
			String[] actual = exception.getMessageArguments();

			//then
			Assertions.assertThat(actual).isNotNull().containsExactly(expected);
		}
	}

	@Nested
	class GetMessageCode {

		@Test
		void givenException_whenGetMessageCode_thenReturnCode() {
			//given
			EntityReferenceConflictedException exception = new EntityReferenceConflictedException(OneClass.class, OtherClass.class, "id", "1");
			String expected = "entity.referenceconflicted";

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

	@AllArgsConstructor
	@Getter
	private class OtherClass {
		private Integer id;
		private String description;
	}
}
