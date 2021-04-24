package com.equisoft.dca.backend.exception.setting;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ApplicationSettingCommonFolderExceptionTest {
	@Nested
	class Message {
		@Test
		void givenFolderName_whenCreateException_thenExceptionContainsRightMessage() {
			// given
			String expected = "Common folder is invalid";

			// when
			ApplicationSettingCommonFolderException actual = new ApplicationSettingCommonFolderException();

			// then
			Assertions.assertThat(actual).hasMessage(expected);
		}
	}

	@Nested
	class Arguments {
		@Test
		void givenFolderNameCode_whenCreateException_thenExceptionContainsEmptyArgs() {
			// given

			// when
			ApplicationSettingCommonFolderException actual = new ApplicationSettingCommonFolderException();

			// then
			Assertions.assertThat(actual.getMessageArguments()).isNotNull().isEmpty();
		}
	}

	@Nested
	class MessageCode {
		@Test
		void givenNewException_whenGetMessageCode_returnRightMessageCode() {
			// given
			ApplicationSettingCommonFolderException ex = new ApplicationSettingCommonFolderException();
			String expected = "setting.commonfolder.valid";

			// when
			String actual = ex.getMessageCode();

			// then
			Assertions.assertThat(actual).isEqualTo(expected);
		}
	}
}
