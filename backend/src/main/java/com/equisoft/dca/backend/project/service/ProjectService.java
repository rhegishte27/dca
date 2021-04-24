package com.equisoft.dca.backend.project.service;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import com.equisoft.dca.backend.project.model.Project;

public interface ProjectService {

	Project save(@Valid Project project);

	Project update(@Valid Project project);

	void deleteById(int id);

	Optional<Project> findById(int id);

	List<String> getProjectsWithLocation(int locationId);

	List<Project> findAll();
}
