package com.equisoft.dca.backend.user.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.equisoft.dca.backend.user.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
	Optional<User> findByIdentifier(String identifier);

	List<User> findByOrganizationId(int organizationId);
}
