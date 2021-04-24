package com.equisoft.dca.backend.filesystem.service;

import java.io.IOException;
import java.nio.file.FileSystem;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;

class FileSystemServiceImplUnixTest extends AbstractFileSystemServiceImplTest {

	private FileSystem fileSystem = Jimfs.newFileSystem(Configuration.unix());

	@Override
	FileSystem getFileSystem() {
		return fileSystem;
	}

	@Nested
	class IsPathValid extends AbstractIsPathValid {

		@Test
		void givenGetPathThrowInvalidPathException_whenIsPathValid_returnFalse() {
			super.givenGetPathThrowInvalidPathException_whenIsPathValid_returnFalse();
		}

		@Test
		void givenGetPathDoNotThrowInvalidPathException_whenIsPathValid_returnTrue() {
			super.givenGetPathDoNotThrowInvalidPathException_whenIsPathValid_returnTrue();
		}
	}

	@Nested
	class GetDirectories extends AbstractGetDirectories {
		@Test
		void givenPathNull_whenGetDirectories_thenThrowNullPointerException() {
			super.givenPathNull_whenGetDirectories_thenThrowNullPointerException();
		}

		@Test
		void givenPathThatDoNotHaveSubDirectories_whenGetDirectories_thenReturnJustTheRoot() throws IOException {
			super.givenPathThatDoNotHaveSubDirectories_whenGetDirectories_thenReturnJustTheRoot();
		}

		@Test
		void givenPathThatIsNotExisted_whenGetDirectories_thenThrowIOException() {
			super.givenPathThatIsNotExisted_whenGetDirectories_thenThrowIOException();
		}

		@Test
		void givenPathThatHasSubDirectoriesAndFiles_whenGetDirectories_thenReturnRootAndAllFiles() throws IOException {
			super.givenPathThatHasSubDirectoriesAndFiles_whenGetDirectories_thenReturnRootAndAllFiles();
		}
	}

	@Nested
	class GetPath extends AbstractGetPath {
		@Test
		void givenPathNull_whenGetPath_thenThrowNullPointerException() {
			super.givenPathNull_whenGetPath_thenThrowNullPointerException();
		}

		@ParameterizedTest
		@ValueSource(strings = {"src/test/java", "/test", "test\\test", "/src/test/java", "\\test\\test", "/", "\\"})
		void givenRightPath_whenGetPath_thenReturnRightPath(String path) {
			super.givenRightPath_whenGetPath_thenReturnRightPath(path);
		}
	}

	@Nested
	class IsSubFolder extends AbstractIsSubFolder {
		@ParameterizedTest
		@CsvSource(value = {"test, test", "test, test/", "test/, test/", "test/, test\\", "test/test, test/test/", "test/test, test/test////"})
		void given2EqualsPath_whenIsSubFolder_thenReturnFalse(String stringPathToValidate, String stringParentPath) {
			super.given2EqualsPath_whenIsSubFolder_thenReturnFalse(stringPathToValidate, stringParentPath);
		}

		@ParameterizedTest
		@CsvSource(value = {"test, foo", "test, test/fo", "testTest, test"})
		void givenPathNotStartWithParentPath_whenIsSubFolder_thenReturnFalse(String stringPathToValidate, String stringParentPath) {
			super.givenPathNotStartWithParentPath_whenIsSubFolder_thenReturnFalse(stringPathToValidate, stringParentPath);
		}

		@ParameterizedTest
		@CsvSource(value = {"test/test, test", "test/test/test, test", "test/test, test/", "test/test/test, test/test"})
		void givenPathNotEqualsAndStartWithParentPath_whenIsSubFolder_thenReturnTrue(String stringPathToValidate, String stringParentPath) {
			super.givenPathNotEqualsAndStartWithParentPath_whenIsSubFolder_thenReturnTrue(stringPathToValidate, stringParentPath);
		}

		@Test
		void givenPathToValidateNull_whenIsSubFolder_thenThrowNullPointerException() {
			super.givenPathToValidateNull_whenIsSubFolder_thenThrowNullPointerException();
		}

		@Test
		void givenPathParentNull_whenIsSubFolder_thenThrowNullPointerException() {
			super.givenPathParentNull_whenIsSubFolder_thenThrowNullPointerException();
		}

		@Test
		void givenBothPathNull_whenIsSubFolder_thenThrowNullPointerException() {
			super.givenBothPathNull_whenIsSubFolder_thenThrowNullPointerException();
		}
	}

	@Nested
	class CreateDirectories extends AbstractCreateDirectories {
		@Test
		void givenPathNull_whenCreateDirectories_thenThrowNullPointerException() {
			super.givenPathNull_whenCreateDirectories_thenThrowNullPointerException();
		}

		@Test
		void givenPathSourceWithSubFolderNotExisted_whenCreateDirectories_thenSourceAndSubFoldersCreated() throws IOException {
			super.givenPathSourceWithSubFolderNotExisted_whenCreateDirectories_thenSourceAndSubFoldersCreated();
		}

		@Test
		void givenPathSourceWithSubFolderAlreadyExisted_whenCreateDirectories_thenNothingWillBeChanged() throws IOException {
			super.givenPathSourceWithSubFolderAlreadyExisted_whenCreateDirectories_thenNothingWillBeChanged();
		}
	}

	@Nested
	class DeleteRecursively extends AbstractDeleteRecursively {
		@Test
		void givenPathNull_whenDeleteRecursively_thenThrowNullPointerException() {
			super.givenPathNull_whenDeleteRecursively_thenThrowNullPointerException();
		}

		@Test
		void givenPathSource_whenDeleteRecursively_AllDirectoriesAllFilesShouldBeDeleted() throws IOException {
			super.givenPathSource_whenDeleteRecursively_AllDirectoriesAllFilesShouldBeDeleted();
		}
	}

	@Nested
	class CreateDirectoryInTemporaryDirectory extends AbstractCreateDirectoryInTemporaryDirectory {
		@ParameterizedTest
		@NullSource
		@EmptySource
		@ValueSource(strings = {"test", "abcdef"})
		void givenDirectoryPrefix_whenCreateDirectoryInTemporaryDirectory_thenDirectoryCreated(String directoryPrefix) throws IOException {
			super.givenDirectoryPrefix_whenCreateDirectoryInTemporaryDirectory_thenDirectoryCreated(directoryPrefix);
		}
	}

	@Nested
	class CreateFileFromString extends AbstractCreateFileFromString {
		@ParameterizedTest
		@CsvSource(value = {"aaaaaa, ttestst, asfasdfasdf", "abcad, test, test"})
		void givenContentAndFileNameAndInExistedDirectory_whenCreateFileFromString_throwNoSuchFileException(String content, String fileName, String directory)
				throws IOException {
			super.givenContentAndFileNameAndInExistedDirectory_whenCreateFileFromString_throwNoSuchFileException(content, fileName, directory);
		}

		@ParameterizedTest
		@CsvSource(value = {"aaaaaa, ttestst, asfasdfasdf", "abcad, test, test"})
		void givenContentAndFileNameAndExistedDirectory_whenCreateFileFromString_thenFileCreated(String content, String fileName, String directory)
				throws IOException {
			super.givenContentAndFileNameAndExistedDirectory_whenCreateFileFromString_thenFileCreated(content, fileName, directory);
		}
	}

	@Nested
	class CreateFileFromByte extends AbstractCreateFileFromByte {
		@ParameterizedTest
		@CsvSource(value = {"aaaaaa, ttestst, asfasdfasdf", "abcad, test, test"})
		void givenContentAndFileNameAndInExistedDirectory_whenCreateFileFromByte_throwNoSuchFileException(String content, String fileName, String directory)
				throws IOException {
			super.givenContentAndFileNameAndInExistedDirectory_whenCreateFileFromByte_throwNoSuchFileException(content, fileName, directory);
		}

		@ParameterizedTest
		@CsvSource(value = {"aaaaaa, ttestst, asfasdfasdf", "abcad, test, test"})
		void givenContentAndFileNameAndExistedDirectory_whenCreateFileFromByte_thenFileCreated(String content, String fileName, String directory)
				throws IOException {
			super.givenContentAndFileNameAndExistedDirectory_whenCreateFileFromByte_thenFileCreated(content, fileName, directory);
		}
	}

	@Nested
	class ExtractFileName extends AbstractExtractFileName {
		@ParameterizedTest
		@NullSource
		void givenNullPath_whenExtractFileName_returnNull(String path) {
			super.givenNullPath_whenExtractFileName_returnNull(path);
		}

		@ParameterizedTest
		@CsvSource(value = {"test/test.csv, test.csv", "test1/test2/test3, test3"})
		void givenPath_whenExtractFileName_returnFileName(String path, String expectedFileName) {
			super.givenPath_whenExtractFileName_returnFileName(path, expectedFileName);
		}
	}

	@Nested
	class ReadFileToString extends AbstractReadFileToString {
		@Test
		void givenInExistedFile_whenReadFileToString_throwNoSuchFileException() {
			super.givenInExistedFile_whenReadFileToString_throwNoSuchFileException();
		}

		@ParameterizedTest
		@ValueSource(strings = {"aaaaa", "bbbbb", "abc\ndef"})
		void givenExistedFile_whenReadFileToString_returnContent(String content) throws IOException {
			super.givenExistedFile_whenReadFileToString_returnContent(content);
		}
	}

	@Nested
	class ReadFileToBytes extends AbstractReadFileToBytes {
		@Test
		void givenInExistedFile_whenReadFileToBytes_throwNoSuchFileException() {
			super.givenInExistedFile_whenReadFileToBytes_throwNoSuchFileException();
		}

		@ParameterizedTest
		@ValueSource(strings = {"aaaaa", "bbbbb", "abc\ndef"})
		void givenExistedFile_whenReadFileToBytes_returnContent(String content) throws IOException {
			super.givenExistedFile_whenReadFileToBytes_returnContent(content);
		}
	}

	@Nested
	class FileExists extends AbstractFileExists {
		@Test
		void givenInExistedFile_whenFileExists_returnFalse() {
			super.givenInExistedFile_whenFileExists_returnFalse();
		}

		@Test
		void givenExistedFile_whenReadFileExists_returnTrue() throws IOException {
			super.givenExistedFile_whenReadFileExists_returnTrue();
		}
	}
}
