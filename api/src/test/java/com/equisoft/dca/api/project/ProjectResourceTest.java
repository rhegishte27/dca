package com.equisoft.dca.api.project;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import com.equisoft.dca.api.project.dto.ProjectDto;
import com.equisoft.dca.api.project.mapper.ProjectMapper;
import com.equisoft.dca.backend.exception.EntityAlreadyExistsException;
import com.equisoft.dca.backend.exception.EntityNotFoundException;
import com.equisoft.dca.backend.project.model.Project;
import com.equisoft.dca.backend.project.service.ProjectService;

@ExtendWith(MockitoExtension.class)
class ProjectResourceTest {

	@Mock
	private ProjectService service;
	@Mock
	private ProjectMapper mapper;

	private ProjectResource projectResource;
	private Project project;
	private ProjectDto projectDto;

	@BeforeEach
	void setUp() {
		this.projectResource = new ProjectResource(service, mapper);
		this.project = createProject(1, "test", "description");
		this.projectDto = createProjectDto(project);
	}

	@Nested
	class Add {

		@Test
		void givenProjectWithDuplicateIdentifier_whenAdd_thenThrowEntityAlreadyExistsException() {
			//given
			Class expected = EntityAlreadyExistsException.class;

			Mockito.when(mapper.toEntity(projectDto)).thenReturn(project);
			Mockito.when(service.save(ArgumentMatchers.any(Project.class))).thenThrow(EntityAlreadyExistsException.class);

			//when
			Throwable actual = Assertions.catchThrowable(() -> projectResource.add(projectDto));

			//then
			Assertions.assertThat(actual).isInstanceOf(expected);
		}

		@Test
		void givenReallyNewProject_whenAdd_thenReturnStatusCodeCreatedAndLocationAndBodyWithProject() {
			//given
			ResponseEntity expected = ResponseEntity.created(URI.create("/projects/" + project.getId())).body(projectDto);

			Mockito.when(mapper.toEntity(projectDto)).thenReturn(project);
			Mockito.when(service.save(ArgumentMatchers.any(Project.class))).thenReturn(project);
			Mockito.when(mapper.toDto(project)).thenReturn(projectDto);

			//when
			ResponseEntity<ProjectDto> actual = projectResource.add(projectDto);

			//then
			Assertions.assertThat(actual)
					.isNotNull()
					.extracting(ResponseEntity::getStatusCode, ResponseEntity::getHeaders, ResponseEntity::getBody)
					.containsExactly(expected.getStatusCode(), expected.getHeaders(), expected.getBody());
		}
	}

	@Nested
	class Update {

		@Test
		void givenNonExistentProject_whenUpdate_thenThrowEntityNotFoundException() {
			//given
			Class expected = EntityNotFoundException.class;

			Mockito.when(mapper.toEntity(projectDto)).thenReturn(project);
			Mockito.when(service.update(ArgumentMatchers.any(Project.class))).thenThrow(EntityNotFoundException.class);

			//when
			Throwable actual = Assertions.catchThrowable(() -> projectResource.update(projectDto.getId(), projectDto));

			//then
			Assertions.assertThat(actual).isInstanceOf(expected);
		}

		@Test
		void givenExistingProject_whenUpdate_thenReturnStatusCodeOkAndBodyNull() {
			//given
			ResponseEntity expected = ResponseEntity.ok().build();

			Mockito.when(mapper.toEntity(projectDto)).thenReturn(project);
			Mockito.when(service.update(ArgumentMatchers.any(Project.class))).thenReturn(project);

			//when
			ResponseEntity<Void> actual = projectResource.update(projectDto.getId(), projectDto);

			//then
			Assertions.assertThat(actual)
					.isNotNull()
					.extracting(ResponseEntity::getStatusCode, ResponseEntity::getBody)
					.containsExactly(expected.getStatusCode(), expected.getBody());
		}
	}

	@Nested
	class Delete {

		@Test
		void givenExistingProjectId_whenDelete_thenReturnStatusCodeNoContentAndBodyNull() {
			//given
			ResponseEntity expected = ResponseEntity.noContent().build();

			//when
			ResponseEntity<Void> actual = projectResource.delete(ArgumentMatchers.anyInt());

			//then
			Assertions.assertThat(actual)
					.isNotNull()
					.extracting(ResponseEntity::getStatusCode, ResponseEntity::getBody)
					.containsExactly(expected.getStatusCode(), expected.getBody());
		}

		@Test
		void givenNonExistentProjectId_whenDelete_thenThrowEntityNotFoundException() {
			//given
			Class expected = EntityNotFoundException.class;

			Mockito.doThrow(EntityNotFoundException.class).when(service).deleteById(ArgumentMatchers.anyInt());

			//when
			Throwable actual = Assertions.catchThrowable(() -> projectResource.delete(ArgumentMatchers.anyInt()));

			//then
			Assertions.assertThat(actual).isInstanceOf(expected);
		}
	}

	@Nested
	class FindById {

		@Test
		void givenExistingProjectId_whenFindById_thenReturnStatusCodeOkAndBodyWithProject() {
			//given
			ResponseEntity expected = ResponseEntity.ok().body(projectDto);

			Mockito.when(service.findById(ArgumentMatchers.anyInt())).thenReturn(Optional.of(project));
			Mockito.when(mapper.toDto(project)).thenReturn(projectDto);

			//when
			ResponseEntity<ProjectDto> actual = projectResource.findById(ArgumentMatchers.anyInt());

			//then
			Assertions.assertThat(actual)
					.isNotNull()
					.extracting(ResponseEntity::getStatusCode, ResponseEntity::getBody)
					.containsExactly(expected.getStatusCode(), expected.getBody());
		}

		@Test
		void givenNonExistentProjectId_whenFindById_thenThrowEntityNotFoundException() {
			//given
			Class expected = EntityNotFoundException.class;

			Mockito.when(service.findById(ArgumentMatchers.anyInt())).thenReturn(Optional.empty());

			//when
			Throwable actual = Assertions.catchThrowable(() -> projectResource.findById(ArgumentMatchers.anyInt()));

			//then
			Assertions.assertThat(actual).isInstanceOf(expected);
		}
	}

	@Nested
	class FindAll {

		@Test
		void givenExistingProjects_whenFindAll_thenReturnStatusCodeOkAndBodyWithProjectList() {
			//given
			List<Project> projects = createProjectList();
			List<ProjectDto> projectDtos = createProjectDtoList(projects);

			ResponseEntity expected = ResponseEntity.ok().body(projectDtos);

			Mockito.when(service.findAll()).thenReturn(projects);
			Mockito.when(mapper.toDtoList(projects)).thenReturn(projectDtos);

			//when
			ResponseEntity<List<ProjectDto>> actual = projectResource.findAll();

			//then
			Assertions.assertThat(actual)
					.isNotNull()
					.extracting(ResponseEntity::getStatusCode, ResponseEntity::getBody)
					.containsExactly(expected.getStatusCode(), expected.getBody());
		}

		@Test
		void givenNonExistentProjects_whenFindAll_thenReturnStatusCodeOkAndBodyEmpty() {
			//given
			ResponseEntity expected = ResponseEntity.ok().body(Collections.emptyList());

			Mockito.when(service.findAll()).thenReturn(Collections.emptyList());

			//when
			ResponseEntity<List<ProjectDto>> actual = projectResource.findAll();

			//then
			Assertions.assertThat(actual)
					.isNotNull()
					.extracting(ResponseEntity::getStatusCode, ResponseEntity::getBody)
					.containsExactly(expected.getStatusCode(), expected.getBody());
		}
	}

	private Project createProject(Integer id, String identifier, String description) {
		return Project.builder()
				.id(id)
				.identifier(identifier)
				.description(description)
				.build();
	}

	private ProjectDto createProjectDto(Project project) {
		return ProjectDto.builder()
				.id(project.getId())
				.identifier(project.getIdentifier())
				.description(project.getDescription())
				.build();
	}

	private List<Project> createProjectList() {
		return List.of(createProject(1, "identi1", "Description 1"),
				createProject(2, "identi2", "Description 2"),
				createProject(3, "identi3", "Description 3"));
	}

	private List<ProjectDto> createProjectDtoList(List<Project> projects) {
		return projects
				.stream()
				.map(this::createProjectDto)
				.collect(Collectors.toList());
	}
}
