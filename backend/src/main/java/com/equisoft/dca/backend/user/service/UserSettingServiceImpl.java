package com.equisoft.dca.backend.user.service;

import java.util.Optional;

import javax.inject.Inject;
import javax.validation.Valid;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.equisoft.dca.backend.exception.EntityNotFoundException;
import com.equisoft.dca.backend.user.dao.UserSettingRepository;
import com.equisoft.dca.backend.user.model.UserSetting;

@Service
@Validated
class UserSettingServiceImpl implements UserSettingService {

	private final UserSettingRepository repository;

	@Inject
	UserSettingServiceImpl(UserSettingRepository repository) {
		this.repository = repository;
	}

	@Override
	public Optional<UserSetting> findById(Integer id) {
		return repository.findById(id);
	}

	@Override
	public UserSetting save(@Valid UserSetting userSetting) {
		return repository.save(userSetting);
	}

	@Override
	public void deleteById(Integer id) {
		try {
			repository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new EntityNotFoundException(UserSetting.class, "id", String.valueOf(id));
		}
	}

	@Override
	public void resetProject(Integer projectId) {
		repository.resetProject(projectId);
	}

	@Override
	public void resetSystem(Integer systemId) {
		repository.resetSystem(systemId);
	}

	@Override
	public void resetOrganization(Integer organizationId) {
		repository.resetOrganization(organizationId);
	}

	@Override
	public void resetUser(Integer userId) {
		repository.resetUser(userId);
	}

	@Override
	public void resetLocation(Integer locationId) {
		repository.resetLocation(locationId);
	}

	@Override
	public void resetDataObject(Integer dataObjectId) {
		repository.resetDataObject(dataObjectId);
	}
}
