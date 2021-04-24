package com.equisoft.dca.backend.dataobject.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ImportDataObjectServiceImplTest {

	/*
	@Mock
	private DataObjectResultRepository repository;

	@Mock
	private SystemService systemService;

	@Mock
	private FileSystemService fileSystemService;

	@Mock
	private ProcessService processService;

	@Mock
	private DataObjectFieldService dataObjectFieldService;

	private ImportDataObjectService service;

	@BeforeEach
	void init() {
		service = new ImportDataObjectServiceImpl(repository, systemService, fileSystemService, processService, dataObjectFieldService);
	}

	@Nested
	class ExceptionWhenCreateWorkingDirectory {

		@Test
		void givenIOExceptionCreatingWorkingDirectory_whenExecuteProcess_thenThrowImportDataObjectExecutionFailedException() throws IOException {
			//given
			Class expected = ImportDataObjectExecutionFailedException.class;

			Mockito.when(fileSystemService.createDirectoryInTemporaryDirectory(ArgumentMatchers.anyString())).thenThrow(IOException.class);

			//when
			Throwable actual = Assertions.catchThrowable(() -> service.execute(ArgumentMatchers.any(DataObject.class)));

			//then
			Assertions.assertThat(actual).isInstanceOf(expected);
		}
	}

	@Nested
	class ExceptionWhenCreateDataObjectFile {

		@Test
		void givenIOExceptionCreatingDataObjectFile_whenExecuteProcess_thenThrowImportDataObjectExecutionFailedException() throws IOException {
			//given
			Class expected = ImportDataObjectExecutionFailedException.class;

			Mockito.when(fileSystemService.createDirectoryInTemporaryDirectory(ArgumentMatchers.anyString())).thenReturn(Path.of("test"));
			Mockito.when(fileSystemService.extractFilename(ArgumentMatchers.anyString())).thenReturn("filename");
			Mockito.doThrow(IOException.class).when(fileSystemService)
					.createFileFromString(ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.any(Path.class));

			//when
			Throwable actual = Assertions.catchThrowable(() -> service.execute(createDataObject(1, "dataobj")));

			//then
			Assertions.assertThat(actual).isInstanceOf(expected);
		}
	}

	@Nested
	class ExceptionWhenCreateDataDictionaryFile {

		@Test
		void givenIOExceptionCreatingDataDictionaryFile_whenExecuteProcess_thenThrowImportDataObjectExecutionFailedException() throws IOException {
			//given
			Class expected = ImportDataObjectExecutionFailedException.class;

			Mockito.when(fileSystemService.createDirectoryInTemporaryDirectory(ArgumentMatchers.anyString())).thenReturn(Path.of("test"));
			Mockito.when(fileSystemService.extractFilename(ArgumentMatchers.anyString())).thenReturn("filename");
			Mockito.when(systemService.findById(ArgumentMatchers.anyInt())).thenReturn(Optional.ofNullable(createSystem(1, "system", "data")));
			Mockito.doThrow(IOException.class).when(fileSystemService)
					.createFileFromBytes(ArgumentMatchers.any(), ArgumentMatchers.anyString(), ArgumentMatchers.any(Path.class));

			//when
			Throwable actual = Assertions.catchThrowable(() -> service.execute(createDataObject(1, "dataobj")));

			//then
			Assertions.assertThat(actual).isInstanceOf(expected);
		}
	}

	@Nested
	class ExceptionWhenImporting {

		@Test
		void givenIOExceptionWhenImporting_whenExecuteProcess_thenThrowImportDataObjectExecutionFailedException() throws IOException, InterruptedException {
			//given
			Class expected = ImportDataObjectExecutionFailedException.class;

			Mockito.when(fileSystemService.createDirectoryInTemporaryDirectory(ArgumentMatchers.anyString())).thenReturn(Path.of("test"));
			Mockito.when(fileSystemService.extractFilename(ArgumentMatchers.anyString())).thenReturn("filename");
			Mockito.when(systemService.findById(ArgumentMatchers.anyInt())).thenReturn(Optional.ofNullable(createSystem(1, "system", "data")));
			Mockito.doThrow(IOException.class).when(processService)
					.execute(ArgumentMatchers.any(Process.class), ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString(),
							ArgumentMatchers.anyString());

			//when
			Throwable actual = Assertions.catchThrowable(() -> service.execute(createDataObject(1, "dataobj")));

			//then
			Assertions.assertThat(actual).isInstanceOf(expected);
		}
	}

	@Nested
	class ExceptionResultFlagNotCreated {

		@Test
		void givenResultFlagFileNotCreated_whenExecuteProcess_thenThrowImportDataObjectTimeoutException() throws IOException, InterruptedException {
			//given
			Class expected = ImportDataObjectTimeoutException.class;

			Mockito.when(fileSystemService.createDirectoryInTemporaryDirectory(ArgumentMatchers.anyString())).thenReturn(Path.of("test"));
			Mockito.when(fileSystemService.extractFilename(ArgumentMatchers.anyString())).thenReturn("filename");
			Mockito.when(systemService.findById(ArgumentMatchers.anyInt())).thenReturn(Optional.ofNullable(createSystem(1, "system", "data")));
			Mockito.when(fileSystemService.fileExists(ArgumentMatchers.any(Path.class))).thenReturn(false);

			//when
			Throwable actual = Assertions.catchThrowable(() -> service.execute(createDataObject(1, "dataobj")));

			//then
			Assertions.assertThat(actual).isInstanceOf(expected);

			Mockito.verify(fileSystemService, Mockito.times(1)).createDirectoryInTemporaryDirectory(ArgumentMatchers.anyString());
			Mockito.verify(fileSystemService, Mockito.times(2)).extractFilename(ArgumentMatchers.anyString());
			Mockito.verify(systemService, Mockito.times(1)).findById(ArgumentMatchers.anyInt());
			Mockito.verify(processService, Mockito.times(1))
					.execute(ArgumentMatchers.any(Process.class), ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString(),
							ArgumentMatchers.anyString());
			Mockito.verify(fileSystemService, Mockito.times(1)).fileExists(ArgumentMatchers.any(Path.class));
			Mockito.verify(fileSystemService, Mockito.times(0)).readFileToString(ArgumentMatchers.any(Path.class));
			Mockito.verify(repository, Mockito.times(0)).save(ArgumentMatchers.any(DataObjectFile.class));
			Mockito.verify(fileSystemService, Mockito.times(0)).readFileToBytes(ArgumentMatchers.any(Path.class));
			Mockito.verify(systemService, Mockito.times(0)).save(ArgumentMatchers.any(System.class));
			Mockito.verify(fileSystemService, Mockito.times(1)).deleteRecursively(ArgumentMatchers.any(Path.class));
		}
	}

	@Nested
	class ExceptionReadResultFile {

		@Test
		void givenIOExceptionWhenReadResultFile_whenExecuteProcess_thenThrowImportDataObjectExecutionFailedException() throws IOException,
				InterruptedException {
			//given
			Class expected = ImportDataObjectExecutionFailedException.class;

			Mockito.when(fileSystemService.createDirectoryInTemporaryDirectory(ArgumentMatchers.anyString())).thenReturn(Path.of("test"));
			Mockito.when(fileSystemService.extractFilename(ArgumentMatchers.anyString())).thenReturn("filename");
			Mockito.when(systemService.findById(ArgumentMatchers.anyInt())).thenReturn(Optional.ofNullable(createSystem(1, "system", "data")));
			Mockito.when(fileSystemService.fileExists(ArgumentMatchers.any(Path.class))).thenReturn(true);
			Mockito.when(fileSystemService.readFileToString(ArgumentMatchers.any(Path.class))).thenThrow(IOException.class);

			//when
			Throwable actual = Assertions.catchThrowable(() -> service.execute(createDataObject(1, "dataobj")));

			//then
			Assertions.assertThat(actual).isInstanceOf(expected);

			Mockito.verify(fileSystemService, Mockito.times(1)).createDirectoryInTemporaryDirectory(ArgumentMatchers.anyString());
			Mockito.verify(fileSystemService, Mockito.times(2)).extractFilename(ArgumentMatchers.anyString());
			Mockito.verify(systemService, Mockito.times(1)).findById(ArgumentMatchers.anyInt());
			Mockito.verify(processService, Mockito.times(1))
					.execute(ArgumentMatchers.any(Process.class), ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString(),
							ArgumentMatchers.anyString());
			Mockito.verify(fileSystemService, Mockito.times(1)).fileExists(ArgumentMatchers.any(Path.class));
			Mockito.verify(fileSystemService, Mockito.times(1)).readFileToString(ArgumentMatchers.any(Path.class));
			Mockito.verify(repository, Mockito.times(0)).save(ArgumentMatchers.any(DataObjectFile.class));
			Mockito.verify(fileSystemService, Mockito.times(0)).readFileToBytes(ArgumentMatchers.any(Path.class));
			Mockito.verify(systemService, Mockito.times(0)).save(ArgumentMatchers.any(System.class));
			Mockito.verify(fileSystemService, Mockito.times(1)).deleteRecursively(ArgumentMatchers.any(Path.class));
		}
	}

	@Nested
	class ExceptionReadDataDictionary {

		@Test
		void givenIOExceptionWhenReadDataDictionary_whenExecuteProcess_thenThrowImportDataObjectExecutionFailedException()
				throws IOException, InterruptedException {
			//given
			Class expected = ImportDataObjectExecutionFailedException.class;
			String resultFileContent = "result file content";
			DataObjectFile dataObjectFileSaved = createImportDataObjectResult(1, resultFileContent, true);

			Mockito.when(fileSystemService.createDirectoryInTemporaryDirectory(ArgumentMatchers.anyString())).thenReturn(Path.of("test"));
			Mockito.when(fileSystemService.extractFilename(ArgumentMatchers.anyString())).thenReturn("filename");
			Mockito.when(systemService.findById(ArgumentMatchers.anyInt())).thenReturn(Optional.ofNullable(createSystem(1, "system", "data")));
			Mockito.when(fileSystemService.fileExists(ArgumentMatchers.any(Path.class))).thenReturn(true);
			Mockito.when(fileSystemService.readFileToString(ArgumentMatchers.any(Path.class))).thenReturn(resultFileContent);
			Mockito.when(repository.save(ArgumentMatchers.any(DataObjectFile.class))).thenReturn(dataObjectFileSaved);
			Mockito.when(fileSystemService.readFileToBytes(ArgumentMatchers.any(Path.class))).thenThrow(IOException.class);

			//when
			Throwable actual = Assertions.catchThrowable(() -> service.execute(createDataObject(1, "dataobj")));

			//then
			Assertions.assertThat(actual).isInstanceOf(expected);

			Mockito.verify(fileSystemService, Mockito.times(1)).createDirectoryInTemporaryDirectory(ArgumentMatchers.anyString());
			Mockito.verify(fileSystemService, Mockito.times(2)).extractFilename(ArgumentMatchers.anyString());
			Mockito.verify(systemService, Mockito.times(2)).findById(ArgumentMatchers.anyInt());
			Mockito.verify(processService, Mockito.times(1))
					.execute(ArgumentMatchers.any(Process.class), ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString(),
							ArgumentMatchers.anyString());
			Mockito.verify(fileSystemService, Mockito.times(1)).fileExists(ArgumentMatchers.any(Path.class));
			Mockito.verify(fileSystemService, Mockito.times(1)).readFileToString(ArgumentMatchers.any(Path.class));
			Mockito.verify(repository, Mockito.times(1)).save(ArgumentMatchers.any(DataObjectFile.class));
			Mockito.verify(fileSystemService, Mockito.times(1)).readFileToBytes(ArgumentMatchers.any(Path.class));
			Mockito.verify(systemService, Mockito.times(0)).save(ArgumentMatchers.any(System.class));
			Mockito.verify(fileSystemService, Mockito.times(1)).deleteRecursively(ArgumentMatchers.any(Path.class));
		}
	}

	@Nested
	class ResultFile {

		@Test
		void givenResultFileWithoutError_whenExecuteProcess_thenReturnValidImportDataObjectResult() throws IOException, InterruptedException {
			//given
			String resultFileContent = "result file content";
			DataObjectFile expected = createImportDataObjectResult(1, resultFileContent, true);

			DataObjectFile dataObjectFileSaved = createImportDataObjectResult(1, resultFileContent, true);

			DataObject dataObjectToExecute = createDataObject(1, "dataobj");

			Mockito.when(fileSystemService.createDirectoryInTemporaryDirectory(ArgumentMatchers.anyString())).thenReturn(Path.of("test"));
			Mockito.when(fileSystemService.extractFilename(ArgumentMatchers.anyString())).thenReturn("filename");
			Mockito.when(systemService.findById(ArgumentMatchers.anyInt())).thenReturn(Optional.ofNullable(createSystem(1, "system", "data")));
			Mockito.when(fileSystemService.fileExists(ArgumentMatchers.any(Path.class))).thenReturn(true);
			Mockito.when(fileSystemService.readFileToString(ArgumentMatchers.any(Path.class))).thenReturn(resultFileContent);
			Mockito.when(repository.save(ArgumentMatchers.any(DataObjectFile.class))).thenReturn(dataObjectFileSaved);

			//when
			DataObjectFile actual = service.execute(dataObjectToExecute);

			//then
			Assertions.assertThat(actual).isEqualTo(expected);

			Mockito.verify(fileSystemService, Mockito.times(1)).createDirectoryInTemporaryDirectory(ArgumentMatchers.anyString());
			Mockito.verify(fileSystemService, Mockito.times(2)).extractFilename(ArgumentMatchers.anyString());
			Mockito.verify(systemService, Mockito.times(2)).findById(ArgumentMatchers.anyInt());
			Mockito.verify(processService, Mockito.times(1))
					.execute(ArgumentMatchers.any(Process.class), ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString(),
							ArgumentMatchers.anyString());
			Mockito.verify(fileSystemService, Mockito.times(1)).fileExists(ArgumentMatchers.any(Path.class));
			Mockito.verify(fileSystemService, Mockito.times(1)).readFileToString(ArgumentMatchers.any(Path.class));
			Mockito.verify(repository, Mockito.times(1)).save(ArgumentMatchers.any(DataObjectFile.class));
			Mockito.verify(fileSystemService, Mockito.times(1)).readFileToBytes(ArgumentMatchers.any(Path.class));
			Mockito.verify(systemService, Mockito.times(1)).save(ArgumentMatchers.any(System.class));
			Mockito.verify(fileSystemService, Mockito.times(1)).deleteRecursively(ArgumentMatchers.any(Path.class));
		}

		@Test
		void givenResultFileWithError_whenExecuteProcess_thenReturnValidImportDataObjectResult() throws IOException, InterruptedException {
			//given
			String resultFileContent = "result ERROR file content";
			DataObjectFile expected = createImportDataObjectResult(1, resultFileContent, false);

			DataObjectFile dataObjectFileSaved = createImportDataObjectResult(1, resultFileContent, false);

			DataObject dataObjectToExecute = createDataObject(1, "dataobj");

			Mockito.when(fileSystemService.createDirectoryInTemporaryDirectory(ArgumentMatchers.anyString())).thenReturn(Path.of("test"));
			Mockito.when(fileSystemService.extractFilename(ArgumentMatchers.anyString())).thenReturn("filename");
			Mockito.when(systemService.findById(ArgumentMatchers.anyInt())).thenReturn(Optional.ofNullable(createSystem(1, "system", "data")));
			Mockito.when(fileSystemService.fileExists(ArgumentMatchers.any(Path.class))).thenReturn(true);
			Mockito.when(fileSystemService.readFileToString(ArgumentMatchers.any(Path.class))).thenReturn(resultFileContent);
			Mockito.when(repository.save(ArgumentMatchers.any(DataObjectFile.class))).thenReturn(dataObjectFileSaved);

			//when
			DataObjectFile actual = service.execute(dataObjectToExecute);

			//then
			Assertions.assertThat(actual).isEqualTo(expected);

			Mockito.verify(fileSystemService, Mockito.times(1)).createDirectoryInTemporaryDirectory(ArgumentMatchers.anyString());
			Mockito.verify(fileSystemService, Mockito.times(2)).extractFilename(ArgumentMatchers.anyString());
			Mockito.verify(systemService, Mockito.times(1)).findById(ArgumentMatchers.anyInt());
			Mockito.verify(processService, Mockito.times(1))
					.execute(ArgumentMatchers.any(Process.class), ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString(),
							ArgumentMatchers.anyString());
			Mockito.verify(fileSystemService, Mockito.times(1)).fileExists(ArgumentMatchers.any(Path.class));
			Mockito.verify(fileSystemService, Mockito.times(1)).readFileToString(ArgumentMatchers.any(Path.class));
			Mockito.verify(repository, Mockito.times(1)).save(ArgumentMatchers.any(DataObjectFile.class));
			Mockito.verify(fileSystemService, Mockito.times(0)).readFileToBytes(ArgumentMatchers.any(Path.class));
			Mockito.verify(systemService, Mockito.times(0)).save(ArgumentMatchers.any(System.class));
			Mockito.verify(fileSystemService, Mockito.times(1)).deleteRecursively(ArgumentMatchers.any(Path.class));
		}
	}

	private DataObjectFile createImportDataObjectResult(Integer id, String resultFileContent, boolean valid) {
		return DataObjectFile.builder()
				.id(id)
				.content(resultFileContent)
				.valid(valid)
				.build();
	}

	private DataObject createDataObject(Integer id, String identifier) {
		return DataObject.builder()
				.id(id)
				.identifier(identifier)
				.content("       01 TRCNVTRX.")
				.type(DataObjectType.COBOL_COPYBOOK)
				.originalFileName("filename")
				.system(System.builder().id(1).build())
				.build();
	}

	private System createSystem(Integer id, String identifier, String dataDictionary) {
		return System.builder()
				.id(id)
				.identifier(identifier)
				.dataDictionary(Optional.ofNullable(dataDictionary).map(String::getBytes).orElse(null))
				.build();
	}
	*/
}
