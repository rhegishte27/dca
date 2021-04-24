package com.equisoft.dca.backend.user.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.equisoft.dca.backend.user.model.Organization;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Integer> {
	Optional<Organization> findByName(String name);
}
