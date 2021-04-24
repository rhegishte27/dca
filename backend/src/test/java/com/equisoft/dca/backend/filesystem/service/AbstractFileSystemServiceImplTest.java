package com.equisoft.dca.backend.filesystem.service;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;

import com.equisoft.dca.backend.filesystem.model.FileData;

abstract class AbstractFileSystemServiceImplTest {
	private FileSystem fileSystem;
	private FileSystemService fileSystemService;

	@BeforeEach
	void setUp() {
		fileSystem = getFileSystem();
		fileSystemService = new FileSystemServiceImpl(fileSystem);
	}

	abstract FileSystem getFileSystem();

	abstract class AbstractIsPathValid {

		@BeforeEach
		void setUp() {
			fileSystemService = Mockito.spy(fileSystemService);
		}

		void givenGetPathThrowInvalidPathException_whenIsPathValid_returnFalse() {
			// given
			String input = "path";
			Mockito.doThrow(InvalidPathException.class).when(fileSystemService).getPath(input);

			// when
			boolean actual = fileSystemService.isPathValid(input);

			// then
			Assertions.assertThat(actual).isFalse();
		}

		void givenGetPathDoNotThrowInvalidPathException_whenIsPathValid_returnTrue() {
			// given
			String input = "path";
			Mockito.doReturn(Mockito.mock(Path.class)).when(fileSystemService).getPath(input);

			// when
			boolean actual = fileSystemService.isPathValid(input);

			// then
			Assertions.assertThat(actual).isTrue();
		}
	}

	abstract class AbstractGetDirectories {
		void givenPathNull_whenGetDirectories_thenThrowNullPointerException() {
			// given
			Path path = null;
			Class expected = NullPointerException.class;

			// when
			Throwable actual = Assertions.catchThrowable(() -> fileSystemService.getFilesDirectories(path));

			// then
			Assertions.assertThat(actual).isInstanceOf(expected);
		}

		void givenPathThatIsNotExisted_whenGetDirectories_thenThrowIOException() {
			// given
			Path path = fileSystem.getPath("test/test/test");
			Class expected = IOException.class;

			// when
			Throwable actual = Assertions.catchThrowable(() -> fileSystemService.getFilesDirectories(path));

			// then
			Assertions.assertThat(actual).isInstanceOf(expected);
		}

		void givenPathThatDoNotHaveSubDirectories_whenGetDirectories_thenReturnJustTheRoot() throws IOException {
			// given
			Path path = fileSystem.getPath("test/test/test");
			Files.createDirectories(path);
			Files.createDirectories(fileSystem.getPath("test/test/test2"));
			Files.createDirectories(fileSystem.getPath("test/test/test3"));
			List<FileData> expected = List.of(
					FileData.builder()
							.path(getPathString("test/test/test"))
							.name("test")
							.isDirectory(true)
							.parentPath(null)
							.childrenPathList(List.of())
							.build());

			// when
			List<FileData> actual = fileSystemService.getFilesDirectories(path);

			// then
			Assertions.assertThat(actual).isEqualTo(expected);
		}

		void givenPathThatHasSubDirectoriesAndFiles_whenGetDirectories_thenReturnRootAndAllFiles() throws IOException {
			// given
			Path root = fileSystem.getPath("test");
			Files.createDirectories(fileSystem.getPath("test/subFolder1/subFolder2"));
			Files.createDirectories(fileSystem.getPath("test/subFolder2"));

			Files.createFile(fileSystem.getPath("test/subFolder1/subFolder2/text.txt"));
			Files.createFile(fileSystem.getPath("test/textFile2.txt"));

			List<FileData> expected = Arrays.asList(
					FileData.builder()
							.path(getPathString("test"))
							.name("test")
							.isDirectory(true)
							.parentPath(null)
							.childrenPathList(List.of(getPathString("test/subFolder1"), getPathString("test/subFolder2"),
									getPathString("test/textFile2.txt")))
							.build(),
					FileData.builder()
							.path(getPathString("test/subFolder1"))
							.name("subFolder1")
							.isDirectory(true)
							.parentPath(getPathString("test"))
							.childrenPathList(List.of(getPathString("test/subFolder1/subFolder2")))
							.build(),
					FileData.builder()
							.path(getPathString("test/subFolder1/subFolder2"))
							.name("subFolder2")
							.isDirectory(true)
							.parentPath(getPathString("test/subFolder1"))
							.childrenPathList(List.of(getPathString("test/subFolder1/subFolder2/text.txt")))
							.build(),
					FileData.builder()
							.path(getPathString("test/subFolder1/subFolder2/text.txt"))
							.name("text.txt")
							.isDirectory(false)
							.parentPath(getPathString("test/subFolder1/subFolder2/"))
							.childrenPathList(List.of())
							.content("")
							.build(),
					FileData.builder()
							.path(getPathString("test/subFolder2"))
							.name("subFolder2")
							.isDirectory(true)
							.parentPath(getPathString("test"))
							.childrenPathList(List.of())
							.build(),
					FileData.builder()
							.path(getPathString("test/textFile2.txt"))
							.name("textFile2.txt")
							.isDirectory(false)
							.parentPath(getPathString("test"))
							.childrenPathList(List.of())
							.content("")
							.build()
			);

			// when
			List<FileData> actual = fileSystemService.getFilesDirectories(root);

			// then
			Assertions.assertThat(actual).isEqualTo(expected);
		}

		private String getPathString(String path) {
			return fileSystem.getPath(path).toString();
		}
	}

	abstract class AbstractGetPath {
		void givenPathNull_whenGetPath_thenThrowNullPointerException() {
			// given
			String path = null;
			Class expected = NullPointerException.class;

			// when
			Throwable actual = Assertions.catchThrowable(() -> fileSystemService.getPath(path));

			// then
			Assertions.assertThat(actual).isInstanceOf(expected);
		}

		void givenWrongPath_whenGetPath_thenThrowInvalidPathException(String path) {
			// given
			Class expected = InvalidPathException.class;

			// when
			Throwable actual = Assertions.catchThrowable(() -> fileSystemService.getPath(path));

			// then
			Assertions.assertThat(actual).isInstanceOf(expected);
		}

		void givenRightPath_whenGetPath_thenReturnRightPath(String path) {
			// given
			Path expected = fileSystem.getPath(path);

			// when
			Path actual = fileSystemService.getPath(path);

			// then
			Assertions.assertThat(actual).isEqualTo(expected);
		}
	}

	abstract class AbstractIsSubFolder {
		void givenPathToValidateNull_whenIsSubFolder_thenThrowNullPointerException() {
			// given
			Path pathToValidate = null;
			Path parentPath = fileSystem.getPath("test");
			Class expected = NullPointerException.class;

			// when
			Throwable actual = Assertions.catchThrowable(() -> fileSystemService.isPathSubFolder(pathToValidate, parentPath));

			// then
			Assertions.assertThat(actual).isInstanceOf(expected);
		}

		void givenPathParentNull_whenIsSubFolder_thenThrowNullPointerException() {
			// given
			Path pathToValidate = fileSystem.getPath("test");
			Path parentPath = null;
			Class expected = NullPointerException.class;

			// when
			Throwable actual = Assertions.catchThrowable(() -> fileSystemService.isPathSubFolder(pathToValidate, parentPath));

			// then
			Assertions.assertThat(actual).isInstanceOf(expected);
		}

		void givenBothPathNull_whenIsSubFolder_thenThrowNullPointerException() {
			// given
			Path pathToValidate = null;
			Path parentPath = null;
			Class expected = NullPointerException.class;

			// when
			Throwable actual = Assertions.catchThrowable(() -> fileSystemService.isPathSubFolder(pathToValidate, parentPath));

			// then
			Assertions.assertThat(actual).isInstanceOf(expected);
		}

		void given2EqualsPath_whenIsSubFolder_thenReturnFalse(String stringPathToValidate, String stringParentPath) {
			assertIsSubFolder(stringPathToValidate, stringParentPath, false);
		}

		void givenPathNotStartWithParentPath_whenIsSubFolder_thenReturnFalse(String stringPathToValidate, String stringParentPath) {
			assertIsSubFolder(stringPathToValidate, stringParentPath, false);
		}

		void givenPathNotEqualsAndStartWithParentPath_whenIsSubFolder_thenReturnTrue(String stringPathToValidate, String stringParentPath) {
			assertIsSubFolder(stringPathToValidate, stringParentPath, true);
		}

		private void assertIsSubFolder(String stringPathToValidate, String stringParentPath, boolean expected) {
			// given
			Path pathToValidate = fileSystem.getPath(stringPathToValidate);
			Path parentPath = fileSystem.getPath(stringParentPath);

			// when
			boolean actual = fileSystemService.isPathSubFolder(pathToValidate, parentPath);

			// then
			Assertions.assertThat(actual).isEqualTo(expected);
		}
	}

	abstract class AbstractCreateDirectories {
		void givenPathNull_whenCreateDirectories_thenThrowNullPointerException() {
			// given
			Path path = null;

			// when
			Throwable actual = Assertions.catchThrowable(() -> fileSystemService.createDirectories(path));

			// then
			Assertions.assertThat(actual).isInstanceOf(NullPointerException.class);
		}

		void givenPathSourceWithSubFolderNotExisted_whenCreateDirectories_thenSourceAndSubFoldersCreated() throws IOException {
			// given
			Path pathSource = fileSystem.getPath("source");
			Path pathSubFolder1 = pathSource.resolve("subFolder1");
			Path pathSubFolder2 = pathSubFolder1.resolve("subFolder2");

			Assertions.assertThat(Files.exists(pathSource)).isFalse();
			Assertions.assertThat(Files.exists(pathSubFolder1)).isFalse();
			Assertions.assertThat(Files.exists(pathSubFolder2)).isFalse();

			FileData expected = FileData.builder()
					.path(pathSubFolder2.toString())
					.name("subFolder2")
					.isDirectory(true)
					.parentPath(pathSubFolder1.toString())
					.childrenPathList(List.of())
					.build();

			// when
			FileData actual = fileSystemService.createDirectories(pathSubFolder2);

			// then
			Assertions.assertThat(Files.exists(pathSource)).isTrue();
			Assertions.assertThat(Files.exists(pathSubFolder1)).isTrue();
			Assertions.assertThat(Files.exists(pathSubFolder2)).isTrue();

			Assertions.assertThat(actual).isEqualTo(expected);
		}

		void givenPathSourceWithSubFolderAlreadyExisted_whenCreateDirectories_thenNothingWillBeChanged() throws IOException {
			// given
			Path pathSource = fileSystem.getPath("source");
			Path pathSubFolder1 = pathSource.resolve("subFolder1");
			Path pathSubFolder2 = pathSubFolder1.resolve("subFolder2");

			Files.createDirectories(pathSubFolder2);

			Assertions.assertThat(Files.exists(pathSource)).isTrue();
			Assertions.assertThat(Files.exists(pathSubFolder1)).isTrue();
			Assertions.assertThat(Files.exists(pathSubFolder2)).isTrue();

			FileData expected = FileData.builder()
					.path(pathSubFolder2.toString())
					.name("subFolder2")
					.isDirectory(true)
					.parentPath(pathSubFolder1.toString())
					.childrenPathList(List.of())
					.build();

			// when
			FileData actual = fileSystemService.createDirectories(pathSubFolder2);

			// then
			Assertions.assertThat(Files.exists(pathSource)).isTrue();
			Assertions.assertThat(Files.exists(pathSubFolder1)).isTrue();
			Assertions.assertThat(Files.exists(pathSubFolder2)).isTrue();

			Assertions.assertThat(actual).isEqualTo(expected);
		}
	}

	abstract class AbstractDeleteRecursively {
		void givenPathNull_whenDeleteRecursively_thenThrowNullPointerException() {
			// given
			Path path = null;

			// when
			Throwable actual = Assertions.catchThrowable(() -> fileSystemService.deleteRecursively(path));

			// then
			Assertions.assertThat(actual).isInstanceOf(NullPointerException.class);
		}

		void givenPathSource_whenDeleteRecursively_AllDirectoriesAllFilesShouldBeDeleted() throws IOException {
			// given
			Path pathSource = fileSystem.getPath("source");
			Path pathSubFolder1 = pathSource.resolve("subFolder1");
			Path pathSubFolder2 = pathSubFolder1.resolve("subFolder2");

			Files.createDirectories(pathSubFolder2);

			Assertions.assertThat(Files.exists(pathSource)).isTrue();
			Assertions.assertThat(Files.exists(pathSubFolder1)).isTrue();
			Assertions.assertThat(Files.exists(pathSubFolder2)).isTrue();

			// when
			fileSystemService.deleteRecursively(pathSource);

			// then
			Assertions.assertThat(Files.exists(pathSource)).isFalse();
			Assertions.assertThat(Files.exists(pathSubFolder1)).isFalse();
			Assertions.assertThat(Files.exists(pathSubFolder2)).isFalse();
		}
	}

	abstract class AbstractCreateDirectoryInTemporaryDirectory {
		void givenDirectoryPrefix_whenCreateDirectoryInTemporaryDirectory_thenDirectoryCreated(String directoryPrefix) throws IOException {
			// given

			// when
			Path actual = fileSystemService.createDirectoryInTemporaryDirectory(directoryPrefix);

			// then
			Assertions.assertThat(Files.exists(actual)).isTrue();
		}
	}

	abstract class AbstractCreateFileFromString {
		void givenContentAndFileNameAndInExistedDirectory_whenCreateFileFromString_throwNoSuchFileException(String content, String fileName, String directory)
				throws IOException {
			// given
			Path directoryPath = fileSystem.getPath(directory);
			Class expected = NoSuchFileException.class;

			// when
			Throwable actual = Assertions.catchThrowable(() -> fileSystemService.createFileFromString(content, fileName, directoryPath));

			// then
			Assertions.assertThat(actual).isInstanceOf(expected);
		}

		void givenContentAndFileNameAndExistedDirectory_whenCreateFileFromString_thenFileCreated(String content, String fileName, String directory)
				throws IOException {
			// given
			Path directoryPath = fileSystem.getPath(directory);
			Files.createDirectories(directoryPath);

			// when
			fileSystemService.createFileFromString(content, fileName, directoryPath);
			String expected = readFile(directoryPath.resolve(fileName).toAbsolutePath());

			// then
			Assertions.assertThat(Files.exists(directoryPath.resolve(fileName))).isTrue();
			Assertions.assertThat(expected).isEqualTo(content);
		}
	}

	abstract class AbstractCreateFileFromByte {
		void givenContentAndFileNameAndInExistedDirectory_whenCreateFileFromByte_throwNoSuchFileException(String content, String fileName, String directory)
				throws IOException {
			// given
			Path directoryPath = fileSystem.getPath(directory);
			byte[] contentByte = content.getBytes();
			Class expected = NoSuchFileException.class;

			// when
			Throwable actual = Assertions.catchThrowable(() -> fileSystemService.createFileFromBytes(contentByte, fileName, directoryPath));

			// then
			Assertions.assertThat(actual).isInstanceOf(expected);
		}

		void givenContentAndFileNameAndExistedDirectory_whenCreateFileFromByte_thenFileCreated(String content, String fileName, String directory)
				throws IOException {
			// given
			Path directoryPath = fileSystem.getPath(directory);
			byte[] contentByte = content.getBytes();
			Files.createDirectories(directoryPath);

			// when
			fileSystemService.createFileFromBytes(contentByte, fileName, directoryPath);
			String expected = readFile(directoryPath.resolve(fileName).toAbsolutePath());

			// then
			Assertions.assertThat(Files.exists(directoryPath.resolve(fileName))).isTrue();
			Assertions.assertThat(expected).isEqualTo(content);
		}
	}

	abstract class AbstractExtractFileName {
		void givenNullPath_whenExtractFileName_returnNull(String path) {
			// given

			// when
			String actual = fileSystemService.extractFilename(path);

			// then
			Assertions.assertThat(actual).isNull();
		}

		void givenPath_whenExtractFileName_returnFileName(String path, String expectedFileName) {
			// given

			// when
			String actual = fileSystemService.extractFilename(path);

			// then
			Assertions.assertThat(actual).isEqualTo(expectedFileName);
		}
	}

	abstract class AbstractReadFileToString {
		void givenInExistedFile_whenReadFileToString_throwNoSuchFileException() {
			// given
			String fileName = "test/fileName.csv";
			Path path = fileSystem.getPath(fileName);
			Class expected = NoSuchFileException.class;

			// when
			Throwable actual = Assertions.catchThrowable(() -> fileSystemService.readFileToString(path));

			// then
			Assertions.assertThat(actual).isInstanceOf(expected);
		}

		void givenExistedFile_whenReadFileToString_returnContent(String content) throws IOException {
			// given
			String fileName = "fileName.csv";
			Path path = fileSystem.getPath(fileName);
			Files.write(path, content.getBytes());

			// when
			String actual = fileSystemService.readFileToString(path);

			// then
			Assertions.assertThat(actual).isEqualTo(content);
		}
	}

	abstract class AbstractReadFileToBytes {
		void givenInExistedFile_whenReadFileToBytes_throwNoSuchFileException() {
			// given
			String fileName = "test/fileName.csv";
			Path path = fileSystem.getPath(fileName);
			Class expected = NoSuchFileException.class;

			// when
			Throwable actual = Assertions.catchThrowable(() -> fileSystemService.readFileToBytes(path));

			// then
			Assertions.assertThat(actual).isInstanceOf(expected);
		}

		void givenExistedFile_whenReadFileToBytes_returnContent(String content) throws IOException {
			// given
			String fileName = "fileName.csv";
			Path path = fileSystem.getPath(fileName);
			Files.write(path, content.getBytes());

			// when
			byte[] actual = fileSystemService.readFileToBytes(path);

			// then
			Assertions.assertThat(actual).isEqualTo(content.getBytes());
		}
	}

	abstract class AbstractFileExists {
		void givenInExistedFile_whenFileExists_returnFalse() {
			// given
			String fileName = "test/fileName.csv";
			Path path = fileSystem.getPath(fileName);

			// when
			boolean actual = fileSystemService.fileExists(path);

			// then
			Assertions.assertThat(actual).isFalse();
		}

		void givenExistedFile_whenReadFileExists_returnTrue() throws IOException {
			// given
			String fileName = "fileName.csv";
			Path path = fileSystem.getPath(fileName);
			Files.createDirectories(path);

			// when
			boolean actual = fileSystemService.fileExists(path);

			// then
			Assertions.assertThat(actual).isTrue();
		}
	}

	private String readFile(Path path) throws IOException {
		List<String> data = Files.readAllLines(path);
		return data.stream().map(i -> String.valueOf(i)).collect(Collectors.joining());
	}
}
