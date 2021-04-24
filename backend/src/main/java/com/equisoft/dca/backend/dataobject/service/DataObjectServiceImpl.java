package com.equisoft.dca.backend.dataobject.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.validation.Valid;

import org.apache.commons.io.FilenameUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.equisoft.dca.backend.common.util.StringCompression;
import com.equisoft.dca.backend.dataobject.dao.DataObjectRepository;
import com.equisoft.dca.backend.dataobject.model.DataObject;
import com.equisoft.dca.backend.dataobject.model.DataObjectFile;
import com.equisoft.dca.backend.dataobject.model.DataObjectStatus;
import com.equisoft.dca.backend.dataobject.model.DataObjectType;
import com.equisoft.dca.backend.dataobject.validator.DataObjectContentValidator;
import com.equisoft.dca.backend.dataobject.validator.DataObjectContentValidatorFactory;
import com.equisoft.dca.backend.exception.EntityAlreadyExistsException;
import com.equisoft.dca.backend.exception.EntityNotFoundException;
import com.equisoft.dca.backend.ftp.service.FtpService;
import com.equisoft.dca.backend.location.model.Location;
import com.equisoft.dca.backend.location.model.LocationType;
import com.equisoft.dca.backend.system.model.System;
import com.equisoft.dca.backend.user.model.User;
import com.equisoft.dca.backend.user.service.UserService;
import com.equisoft.dca.backend.user.service.UserSettingService;

@Service
@Validated
class DataObjectServiceImpl implements DataObjectService {

	private static final int IDENTIFIER_MAX_SIZE = 8;

	private final DataObjectRepository repository;

	private final UserService userService;

	private final UserSettingService userSettingService;

	private final ImportDataObjectService importDataObjectService;

	private final DataObjectFieldService dataObjectFieldService;

	private final DataObjectContentValidatorFactory validatorFactory;

	private final FtpService ftpService;

	private final StringCompression stringCompression;

	@Inject
	DataObjectServiceImpl(DataObjectRepository repository, UserService userService, UserSettingService userSettingService,
			ImportDataObjectService importDataObjectService,
			DataObjectFieldService dataObjectFieldService, DataObjectContentValidatorFactory validatorFactory,
			FtpService ftpService, StringCompression stringCompression) {
		this.repository = repository;
		this.userService = userService;
		this.userSettingService = userSettingService;
		this.importDataObjectService = importDataObjectService;
		this.dataObjectFieldService = dataObjectFieldService;
		this.validatorFactory = validatorFactory;
		this.ftpService = ftpService;
		this.stringCompression = stringCompression;
	}

	@Override
	public DataObject save(@Valid DataObject dataObject) {
		findUserByIdentifierOrElseThrowException(dataObject.getDataObjectFiles().get(0).getUser().getIdentifier());
		checkIfIdentifierIsDuplicated(dataObject, false);
		return saveDataObject(dataObject);
	}

	@Override
	public List<DataObject> create(System system, DataObjectType dataObjectType, List<DataObjectFile> dataObjectFiles) {
		return dataObjectFiles.stream()
				.map(d -> {
					DataObject lastDataObjectSaved =
							repository.findBySystemIdAndDataObjectFilesOriginalFileNameOrderByDataTimeUploadedDesc(system.getId(), d.getOriginalFileName())
									.stream()
									.findFirst()
									.orElse(null);
					DataObject dataObject = DataObject.builder()
							.system(system)
							.type(dataObjectType)
							.identifier(getDefaultIdentifier(d, lastDataObjectSaved))
							.description(getDefaultDescription(d, lastDataObjectSaved))
							.dataObjectFiles(List.of(d))
							.build();
					return dataObject;
				})
				.collect(Collectors.toList());
	}

	private String getDefaultDescription(DataObjectFile dataObjectFile, DataObject lastDataObjectSaved) {
		if (lastDataObjectSaved != null) {
			return lastDataObjectSaved.getDescription();
		}
		return dataObjectFile.getOriginalFileName();
	}

	private String getDefaultIdentifier(DataObjectFile dataObjectFile, DataObject lastDataObjectSaved) {
		if (lastDataObjectSaved != null) {
			return lastDataObjectSaved.getIdentifier();
		}
		return stringCompression.compress(
				FilenameUtils.removeExtension(dataObjectFile.getOriginalFileName()),
				IDENTIFIER_MAX_SIZE,
				Collections.emptyList(),
				true
				, true)
				.orElse(null);
	}

	@Override
	public List<DataObject> save(String userIdentifier, List<DataObject> dataObjects) {
		return dataObjects.stream()
				.map(d -> {
					DataObjectFile dataObjectFile = d.getDataObjectFiles().get(0);
					dataObjectFile.setUser(findUserByIdentifierOrElseThrowException(userIdentifier));
					dataObjectFile.setResultContent("");
					dataObjectFile.setDataObject(d);
					return save(d);
				})
				.collect(Collectors.toList());
	}

	/*@Override
	public DataObject update(@Valid DataObject dataObject) {
		dataObject.setUser(findUserByIdentifierOrElseThrowException(dataObject.getUser().getIdentifier()));

		DataObject dataObjectFound = repository.findById(dataObject.getId())
				.orElseThrow(() -> new EntityNotFoundException(DataObject.class, "id", dataObject.getId().toString()));
		if (!dataObjectFound.getIdentifier().equals(dataObject.getIdentifier())) {
			throw new EntityNotFoundException(DataObject.class, "identifier", dataObject.getIdentifier());
		}

		checkIfIdentifierIsDuplicated(dataObject, true);

		return saveDataObject(dataObject);
	}*/

	private DataObject saveDataObject(@Valid DataObject dataObject) {
		dataObject.setStatus(DataObjectStatus.ERROR);
		Optional<DataObject> dataObjectExists = repository.findByIdentifierAndSystemId(dataObject.getIdentifier(), dataObject.getSystem().getId());
		if (!dataObjectExists.isPresent()) {
			repository.save(dataObject);
		}
		DataObject dataObjectProcessed = importDataObjectService.execute(dataObject);
		repository.updateDataObjectStatus(dataObject.getId(), dataObjectProcessed.getStatus().getId());
		return dataObjectProcessed;
	}

	@Override
	public void deleteById(Integer id) {
		try {
			userSettingService.resetDataObject(id);
			dataObjectFieldService.deleteByDataObjectId(id);
			repository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new EntityNotFoundException(DataObject.class, "id", id.toString());
		}
	}

	@Override
	public Optional<DataObject> findById(Integer id) {
		return repository.findById(id);
	}

	@Override
	public List<DataObject> findAll() {
		return repository.findAll();
	}

	@Override
	public List<DataObjectFile> validateDataObjects(DataObjectType dataObjectType, Location location, List<DataObjectFile> dataObjectFiles) {

		if (location != null && LocationType.FTP.equals(location.getLocationType())) {
			ftpService.downloadFiles(location, dataObjectFiles.stream().map(DataObjectFile::getOriginalFileName).collect(Collectors.toList()))
					.forEach((key, value) -> {
						for (DataObjectFile dataObjectFile : dataObjectFiles) {
							if (dataObjectFile.getOriginalFileName().equals(key)) {
								dataObjectFile.setDataObjectContent(value);
							}
						}
					});
		}

		for (DataObjectFile dataObjectFile : dataObjectFiles) {
			DataObjectContentValidator validator = validatorFactory.createDataObjectContentValidator(dataObjectType);
			dataObjectFile.setStatus(validator.isValid(dataObjectFile.getDataObjectContent()) ? DataObjectStatus.SUCCESS : DataObjectStatus.ERROR);
		}
		return dataObjectFiles;
	}

	private User findUserByIdentifierOrElseThrowException(String userIdentifier) {
		return userService.findByIdentifier(userIdentifier)
				.orElseThrow(() -> new EntityNotFoundException(User.class, "identifier", userIdentifier));
	}

	private void checkIfIdentifierIsDuplicated(DataObject dataObject, boolean isUpdate) {
		repository.findByIdentifierAndSystemId(dataObject.getIdentifier(), dataObject.getSystem().getId())
				.ifPresent(o -> {
					if (isUpdate) {
						if (dataObject.getId() == null || o.getId().intValue() != dataObject.getId().intValue()) {
							throw new EntityAlreadyExistsException(DataObject.class, "identifier", dataObject.getIdentifier());
						}
					} else {
						dataObject.setId(o.getId());
					}
				});
	}
}
