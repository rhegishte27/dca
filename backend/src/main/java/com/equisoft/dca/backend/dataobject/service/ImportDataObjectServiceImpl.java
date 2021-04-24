package com.equisoft.dca.backend.dataobject.service;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Optional;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.equisoft.dca.backend.dataobject.dao.DataObjectFileRepository;
import com.equisoft.dca.backend.dataobject.exception.ImportDataObjectExecutionFailedException;
import com.equisoft.dca.backend.dataobject.exception.ImportDataObjectTimeoutException;
import com.equisoft.dca.backend.dataobject.model.DataObject;
import com.equisoft.dca.backend.dataobject.model.DataObjectFile;
import com.equisoft.dca.backend.dataobject.model.DataObjectStatus;
import com.equisoft.dca.backend.dataobject.model.DataObjectType;
import com.equisoft.dca.backend.filesystem.service.FileSystemService;
import com.equisoft.dca.backend.process.model.Process;
import com.equisoft.dca.backend.process.model.ProcessResult;
import com.equisoft.dca.backend.process.service.ProcessService;
import com.equisoft.dca.backend.system.model.System;
import com.equisoft.dca.backend.system.service.SystemService;

@Service
class ImportDataObjectServiceImpl implements ImportDataObjectService {

	private static final String WORKING_DIRECTORY_PREFIX = "import_data_object_";

	private static final String DCA_ARGUMENT = "DCA";

	private final DataObjectFileRepository dataObjectFileRepository;

	private final SystemService systemService;

	private final FileSystemService fileSystemService;

	private final ProcessService processService;

	private final DataObjectFieldService dataObjectFieldService;

	@Inject
	ImportDataObjectServiceImpl(DataObjectFileRepository dataObjectFileRepository,
			SystemService systemService, FileSystemService fileSystemService,
			ProcessService processService, DataObjectFieldService dataObjectFieldService) {
		this.dataObjectFileRepository = dataObjectFileRepository;
		this.systemService = systemService;
		this.fileSystemService = fileSystemService;
		this.processService = processService;
		this.dataObjectFieldService = dataObjectFieldService;
	}

	@Transactional
	public DataObject execute(DataObject dataObject) {
		Path workingDirectory = null;

		try {
			workingDirectory = prepareWorkingDirectory(dataObject);
			processDataObject(workingDirectory, dataObject);
			return saveProcessResult(workingDirectory, dataObject);

		} catch (IOException | InterruptedException e) {
			throw new ImportDataObjectExecutionFailedException(e);

		} finally {
			Optional.ofNullable(workingDirectory).ifPresent(this::deleteWorkingDirectory);
		}
	}

	private Path prepareWorkingDirectory(DataObject dataObject) throws IOException {
		Path workingDirectory = createWorkingDirectory();
		createDataObjectContentFile(dataObject.getDataObjectFiles().get(0), workingDirectory);
		createSystemDataDictionaryFile(dataObject.getSystem(), workingDirectory);
		return workingDirectory;
	}

	private void createSystemDataDictionaryFile(System system, Path workingDirectory) {
		systemService.findById(system.getId())
				.map(System::getDataDictionary)
				.ifPresent(d -> {
					try {
						fileSystemService.createFileFromBytes(d, ProcessResult.DATA_DICTIONARY.getFilename(), workingDirectory);
					} catch (IOException e) {
						throw new ImportDataObjectExecutionFailedException(e);
					}
				});
	}

	private Path createWorkingDirectory() throws IOException {
		return fileSystemService.createDirectoryInTemporaryDirectory(WORKING_DIRECTORY_PREFIX);
	}

	private void createDataObjectContentFile(DataObjectFile dataObjectFile, Path workingDirectory) throws IOException {
		fileSystemService.createFileFromString(dataObjectFile.getDataObjectContent(), fileSystemService.extractFilename(dataObjectFile.getOriginalFileName()),
				workingDirectory);
	}

	private DataObject saveProcessResult(Path workingDirectory, DataObject dataObject) throws IOException {
		dataObject.getDataObjectFiles().set(0 ,saveDataObjectFile(workingDirectory, dataObject));
		dataObject.setStatus(dataObject.getDataObjectFiles().get(0).getStatus());
		dataObjectFieldService.save(dataObject);
		if (!DataObjectStatus.ERROR.equals(dataObject.getStatus())) {
			saveSystemDataDictionary(workingDirectory, dataObject.getSystem());
		}
		return dataObject;
	}

	private DataObjectFile saveDataObjectFile(Path workingDirectory, DataObject dataObject) throws IOException {
		String resultFileContent = readResultFile(workingDirectory);
		return dataObjectFileRepository.save(DataObjectFile.builder()
				.dataObject(dataObject)
				.user(dataObject.getDataObjectFiles().get(0).getUser())
				.originalFileName(dataObject.getDataObjectFiles().get(0).getOriginalFileName())
				.status(getDataObjectStatus(resultFileContent))
				.dataObjectContent(dataObject.getDataObjectFiles().get(0).getDataObjectContent())
				.resultContent(resultFileContent)
				.build());
	}

	private void saveSystemDataDictionary(Path workingDirectory, System system) {
		Optional<System> sys = systemService.findById(system.getId());

		if (sys.isPresent()) {
			System system1 = sys.get();
			system1.setDataDictionary(readDataDictionaryFile(workingDirectory));
			system1.setDataDictionaryDateTimeUpdated(LocalDateTime.now());
			systemService.update(system1);
		}

		systemService.findById(system.getId()).ifPresent(s -> {
			s.setDataDictionary(readDataDictionaryFile(workingDirectory));
			s.setDataDictionaryDateTimeUpdated(LocalDateTime.now());
			systemService.update(s);
		});
	}

	private byte[] readDataDictionaryFile(Path workingDirectory) {
		try {
			return fileSystemService.readFileToBytes(workingDirectory.resolve(ProcessResult.DATA_DICTIONARY.getFilename()));
		} catch (IOException e) {
			throw new ImportDataObjectExecutionFailedException(e);
		}
	}

	private DataObjectStatus getDataObjectStatus(String content) {
		if (content.contains("ERROR")) {
			return DataObjectStatus.ERROR;
		}
		if (content.contains("WARNING")) {
			return DataObjectStatus.WARNING;
		}
		return DataObjectStatus.SUCCESS;
	}

	private String readResultFile(Path workingDirectory) throws IOException {
		return fileSystemService.readFileToString(workingDirectory.resolve(ProcessResult.RESULT.getFilename()));
	}

	private void processDataObject(Path workingDirectory, DataObject dataObject) throws IOException, InterruptedException {
		processService.execute(getProcess(dataObject.getType()),
				workingDirectory.toString(),
				fileSystemService.extractFilename(dataObject.getDataObjectFiles().get(0).getOriginalFileName()),
				dataObject.getIdentifier(),
				DCA_ARGUMENT);
		if (!hasProcessFinished(workingDirectory)) {
			throw new ImportDataObjectTimeoutException();
		}
	}

	private boolean hasProcessFinished(Path workingDirectory) {
		return fileSystemService.fileExists(workingDirectory.resolve(ProcessResult.FLAG.getFilename()));
	}

	private void deleteWorkingDirectory(Path path) {
		try {
			fileSystemService.deleteRecursively(path);
		} catch (IOException e) {
			throw new ImportDataObjectExecutionFailedException(e);
		}
	}

	private Process getProcess(DataObjectType type) {
		switch (type) {
			case COBOL_COPYBOOK: return Process.IMPORT_COBOL_COPYBOOK;
			default: return null;
		}
	}
}
