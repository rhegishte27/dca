package com.equisoft.dca.api.project;

import java.net.URI;
import java.util.List;

import javax.inject.Inject;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.equisoft.dca.api.JsonRequestMapping;
import com.equisoft.dca.api.project.dto.ProjectDto;
import com.equisoft.dca.api.project.mapper.ProjectMapper;
import com.equisoft.dca.backend.exception.EntityNotFoundException;
import com.equisoft.dca.backend.project.model.Project;
import com.equisoft.dca.backend.project.service.ProjectService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@JsonRequestMapping(value = ProjectResource.PATH)
@Tag(name = "Project")
public class ProjectResource {

	static final String PATH = "/projects";

	private final ProjectService service;

	private final ProjectMapper mapper;

	@Inject
	public ProjectResource(ProjectService service, ProjectMapper mapper) {
		this.service = service;
		this.mapper = mapper;
	}

	@JsonRequestMapping(method = RequestMethod.POST)
	@Operation(summary = "Create a project")
	public ResponseEntity<ProjectDto> add(@RequestBody ProjectDto projectDto) {
		Project project = service.save(mapper.toEntity(projectDto));
		URI location = URI.create(String.format(PATH + "/%d", project.getId()));

		return ResponseEntity.created(location).body(mapper.toDto(project));
	}

	@JsonRequestMapping(value = "/{id}", method = RequestMethod.PUT)
	@Operation(summary = "Update a project")
	public ResponseEntity<Void> update(@PathVariable Integer id, @RequestBody ProjectDto projectDto) {
		projectDto.setId(id);
		service.update(mapper.toEntity(projectDto));

		return ResponseEntity.ok().build();
	}

	@JsonRequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@Operation(summary = "Delete a project")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		service.deleteById(id);

		return ResponseEntity.noContent().build();
	}

	@JsonRequestMapping(method = RequestMethod.GET)
	@Operation(summary = "Get all project")
	public ResponseEntity<List<ProjectDto>> findAll() {
		return ResponseEntity.ok().body(mapper.toDtoList(service.findAll()));
	}

	@JsonRequestMapping(value = "/{id}", method = RequestMethod.GET)
	@Operation(summary = "Get a project by identifier")
	public ResponseEntity<ProjectDto> findById(@PathVariable Integer id) {
		return service.findById(id)
				.map(o -> ResponseEntity.ok(mapper.toDto(o)))
				.orElseThrow(() -> new EntityNotFoundException(Project.class, "id", id.toString()));
	}
}
