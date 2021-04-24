package com.equisoft.dca.backend.user.service;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import com.equisoft.dca.backend.user.model.Organization;

public interface OrganizationService {

	Optional<Organization> findById(int id);

	Optional<Organization> findByName(String name);

	Organization save(@Valid Organization organization);

	Organization update(@Valid Organization organization);

	void deleteById(Integer id);

	List<Organization> findAll();
}
