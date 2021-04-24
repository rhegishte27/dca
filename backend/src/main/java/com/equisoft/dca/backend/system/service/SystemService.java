package com.equisoft.dca.backend.system.service;

import java.util.List;
import java.util.Optional;

import com.equisoft.dca.backend.system.model.System;

public interface SystemService {

	System save(System system);

	System update(System system);

	void deleteById(int id);

	Optional<System> findById(int id);

	List<System> findAll();
}
