package com.equisoft.dca.backend.location.service;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.equisoft.dca.backend.exception.EntityAlreadyExistsException;
import com.equisoft.dca.backend.exception.EntityNotFoundException;
import com.equisoft.dca.backend.location.exception.LocationReferenceConflictedException;
import com.equisoft.dca.backend.filesystem.model.FileData;
import com.equisoft.dca.backend.filesystem.service.FileSystemService;
import com.equisoft.dca.backend.ftp.service.FtpService;
import com.equisoft.dca.backend.location.dao.LocationRepository;
import com.equisoft.dca.backend.location.exception.InvalidLocationPathException;
import com.equisoft.dca.backend.location.model.Location;
import com.equisoft.dca.backend.location.model.LocationType;
import com.equisoft.dca.backend.project.service.ProjectService;
import com.equisoft.dca.backend.user.service.UserSettingService;
import com.equisoft.dca.backend.util.StringUtil;

@Service
@Validated
class LocationServiceImpl implements LocationService {

	private final LocationRepository locationRepository;

	private final UserSettingService userSettingService;

	private final FtpService ftpService;

	private final ProjectService projectService;

	private final FileSystemService fileSystemService;

	@Inject
	LocationServiceImpl(LocationRepository locationRepository, UserSettingService userSettingService,
			FtpService ftpService, ProjectService projectService, FileSystemService fileSystemService) {
		this.locationRepository = locationRepository;
		this.userSettingService = userSettingService;
		this.ftpService = ftpService;
		this.projectService = projectService;
		this.fileSystemService = fileSystemService;
	}

	@Override
	public Optional<Location> findById(Integer id) {
		return locationRepository.findById(id);
	}

	@Override
	public List<Location> findAll() {
		return locationRepository.findAll();
	}

	@Override
	public Location save(@Valid Location location) {
		return saveLocation(location);
	}

	@Override
	public Location update(@Valid Location location) {
		Location locationFound = locationRepository.findById(location.getId())
				.orElseThrow(() -> new EntityNotFoundException(Location.class, "id", location.getId().toString()));
		if (!locationFound.getIdentifier().equals(location.getIdentifier())) {
			throw new EntityNotFoundException(Location.class, "identifier", location.getIdentifier());
		}
		return saveLocation(location);
	}

	private Location saveLocation(Location location) {
		if (location.getLocationType().equals(LocationType.NETWORK)) {
			if (!fileSystemService.isValidDirectory(location.getPath())) {
				throw new InvalidLocationPathException();
			}
		}
		prepareToSave(location);
		checkIfIdentifierIsDuplicated(location);
		return locationRepository.save(location);
	}

	private void prepareToSave(Location location) {
		location.setIdentifier(StringUtil.toTitleCaseIfNotMixedCase(StringUtils.normalizeSpace(location.getIdentifier())));
	}

	private void checkIfIdentifierIsDuplicated(Location location) {
		locationRepository.findByIdentifier(location.getIdentifier()).ifPresent(s -> {
			if (location.getId() == null || !location.getId().equals(s.getId())) {
				throw new EntityAlreadyExistsException(Location.class, "identifier", location.getIdentifier());
			}
		});
	}

	@Override
	public void deleteById(Integer id) {
		checkReferenceConflict(id);
		try {
			userSettingService.resetLocation(id);
			locationRepository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new EntityNotFoundException(Location.class, "id", String.valueOf(id));
		}
	}

	private void checkReferenceConflict(Integer id) {
		List projects = projectService.getProjectsWithLocation(id);
		if (!projects.isEmpty()) {
			throw new LocationReferenceConflictedException(locationRepository.findById(id).get().getIdentifier(),projects);
		}
	}

	private Location findByIdOrElseThrow(Integer id) {
		return locationRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException(Location.class, "id", String.valueOf(id)));
	}

	@Override
	public List<FileData> getFiles(Integer id) {
		return locationRepository.findById(id)
				.filter(l -> l.getLocationType().equals(LocationType.FTP))
				.map(ftpService::listFiles)
				.orElseThrow(() -> new IllegalArgumentException("Location type must be FTP"));
	}

}
