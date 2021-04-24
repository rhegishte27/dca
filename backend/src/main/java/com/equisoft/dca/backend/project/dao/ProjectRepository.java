package com.equisoft.dca.backend.project.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.equisoft.dca.backend.project.model.Project;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer> {

	Optional<Project> findByIdentifier(String identifier);

	@Query(value = "select DISTINCT(p.Identifier) from projectsyncsettings pss join projects p on p.PK_Projects = pss.FK_Projects "
			+ "where pss.fk_locations = :locationId", nativeQuery = true)
	List<String> findLocationInProjectSyncSettings(@Param("locationId") int locationId);

	@Query(value = "select DISTINCT(p.Identifier) from projectsystems ps join projects p on p.PK_Projects = ps.FK_Projects "
			+ "where ps.fk_locations = :locationId", nativeQuery = true)
	List<String> findLocationInProjectSystems(@Param("locationId") int locationId);

}
