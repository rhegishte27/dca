package com.equisoft.dca.backend.userfolder.service;

import java.io.IOException;
import java.nio.file.Path;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.equisoft.dca.backend.filesystem.service.FileSystemService;

@ExtendWith(MockitoExtension.class)
class UserFolderServiceImplTest {
	private static final String SOURCE_FOLDER = "usersFolder";

	@Mock
	private FileSystemService fileSystemService;

	private UserFolderService userFolderService;

	@BeforeEach
	void setUp() {
		userFolderService = new UserFolderServiceImpl(fileSystemService);
	}

	@Nested
	class CreateUserFolder {
		@Test
		void givenIdentifierNull_whenCreateUserFolder_thenNullPointerException() {
			// given
			String identifier = null;
			Class expected = NullPointerException.class;

			// when
			Throwable actual = Assertions.catchThrowable(() -> userFolderService.createUserFolder(identifier));

			// then
			Assertions.assertThat(actual).isInstanceOf(expected);
		}

		@Test
		void givenFileSystemServiceThrowIOException_whenCreateUserFolder_thenThrowRuntimeException() throws IOException {
			// given
			String identifier = "test";

			Path pathFolderUser = getPathSourceFolderResolveIdentifier(identifier);

			Mockito.doThrow(IOException.class).when(fileSystemService).createDirectories(pathFolderUser);
			Class expected = RuntimeException.class;

			// when
			Throwable actual = Assertions.catchThrowable(() -> userFolderService.createUserFolder(identifier));

			// then
			Assertions.assertThat(actual).isInstanceOf(expected);
		}

		@Test
		void givenRightIdentifier_whenCreateUserFolder_thenShouldCallFileSystemServiceCreateDirectories() throws IOException {
			// given
			String identifier = "test";

			Path pathFolderUser = getPathSourceFolderResolveIdentifier(identifier);

			// when
			userFolderService.createUserFolder(identifier);

			// then
			Mockito.verify(fileSystemService, Mockito.times(1)).createDirectories(pathFolderUser);
		}
	}

	@Nested
	class DeleteUserFolder {
		@Test
		void givenIdentifierNull_whenDeleteUserFolder_thenThrowNullPointerException() {
			// given
			String identifier = null;
			Class expected = NullPointerException.class;

			// when
			Throwable actual = Assertions.catchThrowable(() -> userFolderService.deleteUserFolder(identifier));

			// then
			Assertions.assertThat(actual).isInstanceOf(expected);
		}

		@Test
		void givenFileSystemServiceThrowIOException_whenDeleteUserFolder_thenThrowRuntimeException() throws IOException {
			// given
			String identifier = "test";

			Path pathFolderUser = getPathSourceFolderResolveIdentifier(identifier);

			Mockito.doThrow(IOException.class).when(fileSystemService).deleteRecursively(pathFolderUser);
			Class expected = RuntimeException.class;

			// when
			Throwable actual = Assertions.catchThrowable(() -> userFolderService.deleteUserFolder(identifier));

			// then
			Assertions.assertThat(actual).isInstanceOf(expected);
		}

		@Test
		void givenRightIdentifier_whenDeleteUserFolder_thenShouldCallFileSystemServiceDeleteRecursively() throws IOException {
			// given
			String identifier = "test";

			Path pathFolderUser = getPathSourceFolderResolveIdentifier(identifier);

			// when
			userFolderService.deleteUserFolder(identifier);

			// then
			Mockito.verify(fileSystemService, Mockito.times(1)).deleteRecursively(pathFolderUser);
		}
	}

	private Path getPathSourceFolderResolveIdentifier(String identifier) {
		Path pathSourceFolder = Mockito.mock(Path.class);
		Mockito.doReturn(pathSourceFolder).when(fileSystemService).getPath(SOURCE_FOLDER);

		Path pathFolderUser = Mockito.mock(Path.class);
		Mockito.doReturn(pathFolderUser).when(pathSourceFolder).resolve(identifier);

		return pathFolderUser;
	}
}
