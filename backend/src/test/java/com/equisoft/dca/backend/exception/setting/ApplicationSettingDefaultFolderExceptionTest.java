package com.equisoft.dca.backend.exception.setting;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

class ApplicationSettingDefaultFolderExceptionTest {
	@Nested
	class Message {
		@ParameterizedTest
		@NullSource
		@EmptySource
		@ValueSource(strings = {"test"})
		void givenFolderName_whenCreateException_thenExceptionContainsRightMessage(String folderName) {
			// given
			String expected = folderName + " must be valid and must be sub folder of Common Folder";

			// when
			ApplicationSettingDefaultFolderException actual = new ApplicationSettingDefaultFolderException(folderName, "test");

			// then
			Assertions.assertThat(actual).hasMessage(expected);
		}
	}

	@Nested
	class Arguments {
		@ParameterizedTest
		@NullSource
		@EmptySource
		@ValueSource(strings = {"test"})
		void givenFolderNameCode_whenCreateException_thenExceptionContainsArgs(String folderNameCode) {
			// given
			String[] expected = {folderNameCode};

			// when
			ApplicationSettingDefaultFolderException actual = new ApplicationSettingDefaultFolderException("test", folderNameCode);

			// then
			Assertions.assertThat(actual.getMessageArguments()).isNotNull().isEqualTo(expected);
		}
	}

	@Nested
	class MessageCode {
		@Test
		void givenNewException_whenGetMessageCode_returnRightMessageCode() {
			// given
			ApplicationSettingDefaultFolderException ex = new ApplicationSettingDefaultFolderException("test", "test");
			String expected = "setting.defaultfolder.valid";

			// when
			String actual = ex.getMessageCode();

			// then
			Assertions.assertThat(actual).isEqualTo(expected);
		}
	}
}
