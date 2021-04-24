package com.equisoft.dca.backend.user.service;

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
import com.equisoft.dca.backend.exception.EntityReferenceConflictedException;
import com.equisoft.dca.backend.user.dao.OrganizationRepository;
import com.equisoft.dca.backend.user.model.Organization;
import com.equisoft.dca.backend.user.model.User;
import com.equisoft.dca.backend.util.StringUtil;

@Service
@Validated
class OrganizationServiceImpl implements OrganizationService {
	private final OrganizationRepository organizationRepository;

	private final UserService userService;

	private final UserSettingService userSettingService;

	@Inject
	OrganizationServiceImpl(OrganizationRepository organizationRepository, UserService userService, UserSettingService userSettingService) {
		this.organizationRepository = organizationRepository;
		this.userService = userService;
		this.userSettingService = userSettingService;
	}

	@Override
	public Optional<Organization> findById(int id) {
		return organizationRepository.findById(id);
	}

	@Override
	public Optional<Organization> findByName(String name) {
		return organizationRepository.findByName(name);
	}

	@Override
	public Organization save(@Valid Organization organization) {
		prepareForSave(organization);
		checkIfNameDuplicated(organization);
		return organizationRepository.save(organization);
	}

	@Override
	public Organization update(@Valid Organization organization) {
		findByIdOrElseThrow(organization.getId());
		prepareForSave(organization);
		checkIfNameDuplicated(organization);
		return organizationRepository.save(organization);
	}

	private void checkIfNameDuplicated(Organization organization) {
		organizationRepository.findByName(organization.getName()).ifPresent(s -> {
			if (organization.getId() == null || organization.getId().intValue() != s.getId().intValue()) {
				throw new EntityAlreadyExistsException(Organization.class, "name", organization.getName());
			}
		});
	}

	private void prepareForSave(Organization organization) {
		organization.setName(StringUtil.toTitleCaseIfNotMixedCase(StringUtils.normalizeSpace(organization.getName())));
		organization.setDescription(StringUtils.normalizeSpace(organization.getDescription()));
	}

	@Override
	public void deleteById(Integer id) {
		checkReferenceConflict(id);
		try {
			userSettingService.resetOrganization(id);
			organizationRepository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new EntityNotFoundException(Organization.class, "id", String.valueOf(id));
		}
	}

	private void checkReferenceConflict(Integer id) {
		if (!userService.findByOrganizationId(id).isEmpty()) {
			throw new EntityReferenceConflictedException(Organization.class, User.class, "name", findByIdOrElseThrow(id).getName());
		}
	}

	private Organization findByIdOrElseThrow(Integer id) {
		return organizationRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException(Organization.class, "id", String.valueOf(id)));
	}

	@Override
	public List<Organization> findAll() {
		return organizationRepository.findAll();
	}
}
