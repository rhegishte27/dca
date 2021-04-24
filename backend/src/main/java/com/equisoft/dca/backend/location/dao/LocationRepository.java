package com.equisoft.dca.backend.location.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.equisoft.dca.backend.location.model.Location;

@Repository
public interface LocationRepository extends JpaRepository<Location, Integer> {

	Optional<Location> findByIdentifier(String identifier);
}
