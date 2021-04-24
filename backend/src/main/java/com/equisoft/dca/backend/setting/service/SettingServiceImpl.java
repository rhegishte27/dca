package com.equisoft.dca.backend.setting.service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import javax.inject.Inject;
import javax.validation.Valid;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.equisoft.dca.backend.exception.EntityAlreadyExistsException;
import com.equisoft.dca.backend.exception.EntityNotFoundException;
import com.equisoft.dca.backend.exception.setting.ApplicationSettingCommonFolderException;
import com.equisoft.dca.backend.exception.setting.ApplicationSettingDefaultFolderException;
import com.equisoft.dca.backend.filesystem.service.FileSystemService;
import com.equisoft.dca.backend.setting.dao.SettingRepository;
import com.equisoft.dca.backend.setting.model.Setting;

@Service
@Validated
class SettingServiceImpl implements SettingService {
	private final SettingRepository repository;

	private final FileSystemService fileSystemService;

	@Inject
	SettingServiceImpl(SettingRepository repository, FileSystemService fileSystemService) {
		this.repository = repository;
		this.fileSystemService = fileSystemService;
	}

	@Override
	public Setting save(@Valid Setting setting) {
		get().map(i -> {
			throw new EntityAlreadyExistsException(System.class);
		});
		createFolders(setting);
		return repository.save(setting);
	}

	@Override
	public Setting update(@Valid Setting setting) {
		Setting s = get().orElseThrow(() -> new EntityNotFoundException(Setting.class));

		if (setting.getId() == null || setting.getId().intValue() != s.getId().intValue()) {
			throw new EntityNotFoundException(Setting.class);
		}

		createFolders(setting);
		return repository.save(setting);
	}

	@Override
	public Optional<Setting> get() {
		return repository.findAll()
				.stream()
				.findFirst();
	}

	private void createFolders(Setting setting) {
		validateFolders(setting);

		createFolders(setting.getCommonFolder());
		createFolders(setting.getDefaultDownloadFolder());
		createFolders(setting.getDefaultExportFolder());
		createFolders(setting.getDefaultImportFolder());
	}

	private void validateFolders(Setting setting) {
		if (!fileSystemService.isPathValid(setting.getCommonFolder())) {
			throw new ApplicationSettingCommonFolderException();
		}

		Path commonFolder = fileSystemService.getPath(setting.getCommonFolder());
		validateDefaultFolders(setting.getDefaultImportFolder(), commonFolder, "Default import folder", "setting.defaultimportfolder");
		validateDefaultFolders(setting.getDefaultExportFolder(), commonFolder, "Default export folder", "setting.defaultexportfolder");
		validateDefaultFolders(setting.getDefaultDownloadFolder(), commonFolder, "Default download folder", "setting.defaultdownloadfolder");
	}

	private void validateDefaultFolders(String path, Path commonFolder, String folderName, String folderNameCode) {
		if (!fileSystemService.isPathValid(path)
				|| !fileSystemService.isPathSubFolder(fileSystemService.getPath(path), commonFolder)) {
			throw new ApplicationSettingDefaultFolderException(folderName, folderNameCode);
		}
	}

	private void createFolders(String path) {
		try {
			fileSystemService.createDirectories(fileSystemService.getPath(path));
		} catch (IOException e) {
			throw new RuntimeException("Unknown error " + e.getMessage(), e);
		}
	}
}
