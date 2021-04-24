package com.equisoft.dca.backend.project.service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.inject.Inject;
import javax.validation.Valid;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.equisoft.dca.backend.exception.EntityAlreadyExistsException;
import com.equisoft.dca.backend.exception.EntityNotFoundException;
import com.equisoft.dca.backend.project.dao.ProjectRepository;
import com.equisoft.dca.backend.project.model.Project;
import com.equisoft.dca.backend.user.service.UserSettingService;

@Service
@Validated
class ProjectServiceImpl implements ProjectService {

	private final ProjectRepository repository;

	private final UserSettingService userSettingService;

	@Inject
	ProjectServiceImpl(ProjectRepository repository, UserSettingService userSettingService) {
		this.repository = repository;
		this.userSettingService = userSettingService;
	}

	@Override
	public Project save(@Valid Project project) {
		checkIfIdentifierDuplicated(project);
		return saveEntity(project);
	}

	@Override
	public Project update(@Valid Project project) {
		Project projectFound = repository.findById(project.getId())
				.orElseThrow(() -> new EntityNotFoundException(Project.class, "id", project.getId().toString()));
		if (!projectFound.getIdentifier().equals(project.getIdentifier())) {
			throw new EntityNotFoundException(Project.class, "identifier", String.valueOf(project.getIdentifier()));
		}
		checkIfIdentifierDuplicated(project);
		return saveEntity(project);
	}

	private Project saveEntity(Project project) {
		project.getProjectSyncSettings().forEach(p -> p.getId().setProject(project));
		project.getSystems().forEach(p -> p.getId().setProject(project));
		return repository.save(project);
	}

	private void checkIfIdentifierDuplicated(Project project) {
		repository.findByIdentifier(project.getIdentifier()).ifPresent(p -> {
			if (project.getId() == null || p.getId().intValue() != project.getId().intValue()) {
				throw new EntityAlreadyExistsException(Project.class, "identifier", project.getIdentifier());
			}
		});
	}

	@Override
	public void deleteById(int id) {
		try {
			userSettingService.resetProject(id);
			repository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new EntityNotFoundException(Project.class, "id", String.valueOf(id));
		}
	}

	public List<String> getProjectsWithLocation(int locationId) {
		return Stream.of(repository.findLocationInProjectSyncSettings(locationId), repository.findLocationInProjectSystems(locationId))
				.flatMap(Collection::stream)
				.distinct()
				.collect(Collectors.toList());
	}

	@Override
	public Optional<Project> findById(int id) {
		return repository.findById(id);
	}

	@Override
	public List<Project> findAll() {
		return repository.findAll();
	}

}
