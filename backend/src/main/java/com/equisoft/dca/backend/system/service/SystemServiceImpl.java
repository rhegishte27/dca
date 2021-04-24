package com.equisoft.dca.backend.system.service;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.equisoft.dca.backend.exception.EntityAlreadyExistsException;
import com.equisoft.dca.backend.exception.EntityNotFoundException;
import com.equisoft.dca.backend.system.dao.SystemRepository;
import com.equisoft.dca.backend.system.model.System;
import com.equisoft.dca.backend.user.service.UserSettingService;

@Service
@Validated
class SystemServiceImpl implements SystemService {

	private final SystemRepository repository;

	private final UserSettingService userSettingService;

	@Inject
	SystemServiceImpl(SystemRepository repository, UserSettingService userSettingService) {
		this.repository = repository;
		this.userSettingService = userSettingService;
	}

	@Override
	public System save(System system) {
		prepareToSave(system);
		checkIfIdentifierDuplicated(system);
		return repository.save(system);
	}

	@Override
	public System update(System system) {
		System systemFound = repository.findById(system.getId())
				.orElseThrow(() -> new EntityNotFoundException(System.class, "id", system.getId().toString()));
		if (!systemFound.getIdentifier().equals(system.getIdentifier())) {
			throw new EntityNotFoundException(System.class, "identifier", system.getIdentifier());
		}
		prepareToSave(system);
		checkIfIdentifierDuplicated(system);
		return repository.save(system);
	}

	private void checkIfIdentifierDuplicated(System system) {
		repository.findByIdentifier(system.getIdentifier()).ifPresent(s -> {
			if (system.getId() == null || s.getId().intValue() != system.getId().intValue()) {
				throw new EntityAlreadyExistsException(System.class, "identifier", system.getIdentifier());
			}
		});
	}

	@Override
	public void deleteById(int id) {
		try {
			userSettingService.resetSystem(id);
			repository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new EntityNotFoundException(System.class, "id", String.valueOf(id));
		}
	}

	@Override
	public Optional<System> findById(int id) {
		return repository.findById(id);
	}

	@Override
	public List<System> findAll() {
		return repository.findAll();
	}

	private void prepareToSave(System system) {
		system.setIdentifier(system.getIdentifier().toUpperCase());
		system.setDescription(StringUtils.normalizeSpace(system.getDescription()));
	}
}
