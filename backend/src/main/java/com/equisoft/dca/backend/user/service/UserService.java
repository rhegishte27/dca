package com.equisoft.dca.backend.user.service;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.validation.annotation.Validated;

import com.equisoft.dca.backend.common.OnCreate;
import com.equisoft.dca.backend.common.OnUpdate;
import com.equisoft.dca.backend.user.model.User;

public interface UserService {
	Optional<User> findById(int id);

	Optional<User> findByIdentifier(String identifier);

	@Validated(OnCreate.class)
	User save(String currentUserIdentifier, @Valid User user);

	@Validated(OnUpdate.class)
	User update(String currentUserIdentifier, @Valid User user);

	void deleteById(String currentUserIdentifier, Integer id);

	List<User> findAll();

	List<User> findByOrganizationId(int organizationId);
}
