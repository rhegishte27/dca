package com.equisoft.dca.api.directory;

import java.io.IOException;
import java.net.URI;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.HandlerMapping;

import com.equisoft.dca.api.directory.dto.FileDataDto;
import com.equisoft.dca.api.directory.mapper.FileDataMapper;
import com.equisoft.dca.backend.filesystem.model.FileData;
import com.equisoft.dca.backend.filesystem.service.FileSystemService;

@ExtendWith(MockitoExtension.class)
class DirectoryResourceTest {

	@Mock
	private FileSystemService fileSystemService;
	@Mock
	private FileDataMapper mapper;

	private DirectoryResource directoryResource;

	@BeforeEach
	void setUp() {
		directoryResource = new DirectoryResource(fileSystemService, mapper);
	}

	@Nested
	class DeleteFile {
		@Mock
		private HttpServletRequest request;

		@Test
		void givenFileSystemServiceDeleteDirectoriesThrowIOException_whenDeleteFile_throwIOException() throws IOException {
			//given
			Class expected = IOException.class;
			String rootPath = "test";

			Path path = Mockito.mock(Path.class);
			Mockito.when(request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE)).thenReturn(DirectoryResource.PATH + "/" + rootPath);
			Mockito.when(fileSystemService.getPath(rootPath)).thenReturn(path);
			Mockito.doThrow(IOException.class).when(fileSystemService).deleteRecursively(path);

			//when
			Throwable actual = Assertions.catchThrowable(() -> directoryResource.deleteFile(request));

			//then
			Assertions.assertThat(actual).isInstanceOf(expected);
		}

		@Test
		void givenFileSystemServiceGetPathThrowInvalidPathException_whenCreateDirectories_throwInvalidPathException() {
			//given
			Class expected = InvalidPathException.class;
			String rootPath = "test";

			Mockito.when(request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE)).thenReturn(DirectoryResource.PATH + "/" + rootPath);
			Mockito.when(fileSystemService.getPath(rootPath)).thenThrow(InvalidPathException.class);

			//when
			Throwable actual = Assertions.catchThrowable(() -> directoryResource.deleteFile(request));

			//then
			Assertions.assertThat(actual).isInstanceOf(expected);
		}

		@Test
		void givenFileSystemDeleteRecursivelyThrowNoError_whenDeleteFile_thenReturnNoContent() throws IOException {
			// given
			String rootPath = "test";
			Path path = Mockito.mock(Path.class);
			Mockito.when(request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE)).thenReturn(DirectoryResource.PATH + "/" + rootPath);
			Mockito.when(fileSystemService.getPath(rootPath)).thenReturn(path);

			ResponseEntity expected = ResponseEntity.noContent().build();

			// when
			ResponseEntity<Void> actual = directoryResource.deleteFile(request);

			// then
			Assertions.assertThat(actual).isEqualTo(expected);
		}
	}

	@Nested
	class CreateDirectory {
		private FileDataDto requestParam;

		@BeforeEach
		void setUp() {
			requestParam = FileDataDto.builder().parentPath("folder").name("subFolder").build();
		}

		@Test
		void givenFileSystemServiceCreateDirectoriesThrowIOException_whenCreateDirectories_throwIOException() throws IOException {
			//given
			Class expected = IOException.class;

			Path path = Mockito.mock(Path.class);
			Mockito.when(fileSystemService.getPath(requestParam.getPath())).thenReturn(path);
			Mockito.when(path.resolve(requestParam.getName())).thenReturn(path);
			Mockito.when(fileSystemService.createDirectories(path)).thenThrow(IOException.class);

			//when
			Throwable actual = Assertions.catchThrowable(() -> directoryResource.createDirectory(requestParam));

			//then
			Assertions.assertThat(actual).isInstanceOf(expected);
		}

		@Test
		void givenFileSystemServiceGetPathThrowInvalidPathException_whenCreateDirectories_throwInvalidPathException() {
			//given
			Class expected = InvalidPathException.class;

			Mockito.when(fileSystemService.getPath(requestParam.getPath())).thenThrow(InvalidPathException.class);

			//when
			Throwable actual = Assertions.catchThrowable(() -> directoryResource.createDirectory(requestParam));

			//then
			Assertions.assertThat(actual).isInstanceOf(expected);
		}

		@Test
		void givenPathResoleFileNameThrowInvalidPathException_whenCreateDirectories_throwInvalidPathException() {
			//given
			Class expected = InvalidPathException.class;

			Path path = Mockito.mock(Path.class);
			Mockito.when(fileSystemService.getPath(requestParam.getPath())).thenReturn(path);
			Mockito.when(path.resolve(requestParam.getName())).thenThrow(InvalidPathException.class);

			//when
			Throwable actual = Assertions.catchThrowable(() -> directoryResource.createDirectory(requestParam));

			//then
			Assertions.assertThat(actual).isInstanceOf(expected);
		}

		@Test
		void givenValidPath_whenCreateDirectories_returnCreatedWithBody() throws IOException {
			//given
			FileDataDto fileDataDto = createFileDataDto();
			FileData fileData = createFileData();

			Path path = Mockito.mock(Path.class);
			Mockito.when(fileSystemService.getPath(requestParam.getPath())).thenReturn(path);
			Mockito.when(path.resolve(requestParam.getName())).thenReturn(path);
			Mockito.when(fileSystemService.createDirectories(path)).thenReturn(fileData);
			Mockito.when(mapper.toDto(fileData)).thenReturn(fileDataDto);

			ResponseEntity expected = ResponseEntity.created(URI.create(DirectoryResource.PATH + "/" + path.getFileName())).body(fileDataDto);

			//when
			ResponseEntity<FileDataDto> actual = directoryResource.createDirectory(requestParam);

			//then
			Assertions.assertThat(actual)
					.isNotNull()
					.extracting(ResponseEntity::getStatusCode, ResponseEntity::getBody)
					.containsExactly(expected.getStatusCode(), expected.getBody());
		}
	}

	@Nested
	class GetDirectories {
		@Mock
		private HttpServletRequest request;

		@Test
		void givenFileSystemServiceGetDirectoriesThrowIOException_whenGetDirectories_throwIOException() throws IOException {
			//given
			Class expected = IOException.class;
			String rootPath = "test";

			Path path = Mockito.mock(Path.class);
			Mockito.when(request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE)).thenReturn(DirectoryResource.PATH + "/" + rootPath);
			Mockito.when(fileSystemService.getPath(rootPath)).thenReturn(path);
			Mockito.when(fileSystemService.getFilesDirectories(path)).thenThrow(IOException.class);

			//when
			Throwable actual = Assertions.catchThrowable(() -> directoryResource.getDirectories(request));

			//then
			Assertions.assertThat(actual).isInstanceOf(expected);
		}

		@Test
		void givenFileSystemServiceGetPathThrowInvalidPathException_whenGetDirectories_throwInvalidPathException() {
			//given
			Class expected = InvalidPathException.class;
			String rootPath = "test";

			Mockito.when(request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE)).thenReturn(DirectoryResource.PATH + "/" + rootPath);
			Mockito.when(fileSystemService.getPath(rootPath)).thenThrow(InvalidPathException.class);

			//when
			Throwable actual = Assertions.catchThrowable(() -> directoryResource.getDirectories(request));

			//then
			Assertions.assertThat(actual).isInstanceOf(expected);
		}

		@Test
		void givenFileSystemServiceGetDirectoriesReturnLst_whenGetDirectories_thenReturnOkAndFileDataDto() throws IOException {
			//given
			List<FileData> lstFileData = createLstFileData();
			List<FileDataDto> lstFileDataDto = createLstFileDataDto();

			ResponseEntity expected = ResponseEntity.ok().body(lstFileDataDto);

			String root = "test/test/test";
			Path pathRoot = Mockito.mock(Path.class);

			Mockito.when(request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE)).thenReturn(DirectoryResource.PATH + "/" + root);
			Mockito.when(fileSystemService.getPath(root)).thenReturn(pathRoot);
			Mockito.when(fileSystemService.getFilesDirectories(pathRoot)).thenReturn(lstFileData);
			Mockito.when(mapper.toDtoList(lstFileData)).thenReturn(lstFileDataDto);

			//when
			ResponseEntity<List<FileDataDto>> actual = directoryResource.getDirectories(request);

			//then
			Assertions.assertThat(actual)
					.isNotNull()
					.extracting(ResponseEntity::getStatusCode, ResponseEntity::getBody)
					.containsExactly(expected.getStatusCode(), expected.getBody());
		}
	}

	private List<FileData> createLstFileData() {
		return List.of(createFileData(), createFileData(), createFileData());
	}

	private List<FileDataDto> createLstFileDataDto() {
		return List.of(createFileDataDto(), createFileDataDto(), createFileDataDto());
	}

	private FileDataDto createFileDataDto() {
		return FileDataDto.builder().build();
	}

	private FileData createFileData() {
		return FileData.builder().build();
	}
}
