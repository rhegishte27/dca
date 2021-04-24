package com.equisoft.dca.backend.system.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.equisoft.dca.backend.system.model.System;

public interface SystemRepository extends JpaRepository<System, Integer> {

	Optional<System> findByIdentifier(String identifier);
}
