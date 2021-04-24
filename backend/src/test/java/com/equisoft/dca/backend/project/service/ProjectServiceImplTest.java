package com.equisoft.dca.backend.project.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;

import com.equisoft.dca.backend.exception.EntityAlreadyExistsException;
import com.equisoft.dca.backend.exception.EntityNotFoundException;
import com.equisoft.dca.backend.location.model.Location;
import com.equisoft.dca.backend.project.dao.ProjectRepository;
import com.equisoft.dca.backend.project.model.Project;
import com.equisoft.dca.backend.project.model.ProjectSyncSetting;
import com.equisoft.dca.backend.project.model.ProjectSyncSetting.ProjectSyncSettingId;
import com.equisoft.dca.backend.project.model.ProjectSystem;
import com.equisoft.dca.backend.project.model.ProjectSystem.ProjectSystemId;
import com.equisoft.dca.backend.project.model.TypeProjectElement;
import com.equisoft.dca.backend.system.model.System;
import com.equisoft.dca.backend.user.service.UserSettingService;

@ExtendWith(MockitoExtension.class)
class ProjectServiceImplTest {

	@Mock
	private ProjectRepository repository;
	@Mock
	private UserSettingService userSettingService;

	private ProjectServiceImpl service;

	@BeforeEach
	void setUp() {
		service = new ProjectServiceImpl(repository, userSettingService);
	}

	@Nested
	class Save {
		@Test
		void givenNonExistentProjectIdentifier_whenSave_thenReturnNewProject() {
			//given
			Project projectToSave = createProject(null, "test", "description");
			Project expected = createProject(1, "TEST", "description");

			Mockito.when(repository.findByIdentifier(expected.getIdentifier())).thenReturn(Optional.empty());
			Mockito.when(repository.save(projectToSave)).thenReturn(expected);

			//when
			Project actual = service.save(projectToSave);

			//then
			Mockito.verify(repository, Mockito.times(1)).findByIdentifier(ArgumentMatchers.anyString());
			Mockito.verify(repository, Mockito.times(1)).save(ArgumentMatchers.any(Project.class));
			Assertions.assertThat(actual)
					.isNotNull()
					.isEqualTo(expected);
		}

		@ParameterizedTest
		@CsvSource({"TEST, TEST", "TEST, test", "NEW ENTITY 1, 'new entity 1'"})
		void givenExistingProjectIdentifier_whenSave_thenThrowEntityAlreadyExistsException(String identifierExisted, String identifierToSave) {
			//given
			Class expected = EntityAlreadyExistsException.class;

			Project projectToSave = createProject(null, identifierToSave, "description");
			Project projectFromRepository = createProject(1, identifierExisted, "description2");

			Mockito.when(repository.findByIdentifier(identifierExisted)).thenReturn(Optional.of(projectFromRepository));

			//when
			Throwable actual = Assertions.catchThrowable(() -> service.save(projectToSave));

			//then
			Mockito.verify(repository, Mockito.times(1)).findByIdentifier(ArgumentMatchers.anyString());
			Mockito.verify(repository, Mockito.times(0)).save(ArgumentMatchers.any(Project.class));
			Assertions.assertThat(actual)
					.isInstanceOf(expected);
		}
	}

	@Nested
	class Update {

		@ParameterizedTest
		@CsvSource({"TEST, TEST", "TEST, test", "NEW ENTITY 1, 'new entity 1'"})
		void givenExistingProjectIdentifierWithDifferentId_whenUpdate_thenThrowEntityAlreadyExistsException(String identifierExisted,
				String identifierToSave) {
			//given
			Class expected = EntityAlreadyExistsException.class;

			Project project = createProject(1, identifierToSave, "description");
			Mockito.when(repository.findById(project.getId())).thenReturn(Optional.of(project));

			Project otherProject = createProject(2, identifierExisted, "description");
			Mockito.when(repository.findByIdentifier(identifierExisted)).thenReturn(Optional.of(otherProject));

			//when
			Throwable actual = Assertions.catchThrowable(() -> service.update(project));

			//then
			Mockito.verify(repository, Mockito.times(1)).findByIdentifier(ArgumentMatchers.anyString());
			Mockito.verify(repository, Mockito.times(0)).save(ArgumentMatchers.any(Project.class));
			Assertions.assertThat(actual)
					.isInstanceOf(expected);
		}

		@ParameterizedTest
		@CsvSource({"TEST, TEST", "TEST, test", "NEW ENTITY 1, 'new entity 1'"})
		void givenExistingProjectIdentifierWithSameId_whenUpdate_thenReturnProject(String identifierExisted,
				String identifierToSave) {
			//given
			Project project = createProject(1, identifierToSave, "description");
			Mockito.when(repository.findById(project.getId())).thenReturn(Optional.of(project));

			Project otherProject = createProject(1, identifierExisted, "description");
			Mockito.when(repository.findByIdentifier(identifierExisted)).thenReturn(Optional.of(otherProject));
			Mockito.when(repository.save(project)).thenReturn(project);

			//when
			Project actual = service.update(project);

			//then
			Mockito.verify(repository, Mockito.times(1)).findByIdentifier(ArgumentMatchers.anyString());
			Mockito.verify(repository, Mockito.times(1)).save(ArgumentMatchers.any(Project.class));
			Assertions.assertThat(actual)
					.isNotNull()
					.isEqualTo(project);
		}

		@Test
		void givenExistingProjectIdAndIdentifier_whenUpdate_thenReturnProject() {
			//given
			Project projectToUpdate = createProject(1, "TEST", "description");
			Project expected = createProject(1, "TEST", "description2");

			Mockito.when(repository.findById(projectToUpdate.getId())).thenReturn(Optional.of(projectToUpdate));
			Mockito.when(repository.save(projectToUpdate)).thenReturn(expected);

			//when
			Project actual = service.update(projectToUpdate);

			//then
			Mockito.verify(repository, Mockito.times(1)).findById(ArgumentMatchers.anyInt());
			Mockito.verify(repository, Mockito.times(1)).save(ArgumentMatchers.any(Project.class));
			Assertions.assertThat(actual)
					.isNotNull()
					.isEqualTo(expected);
		}

		@Test
		void givenNonExistentProjectId_whenUpdate_thenThrowEntityNotFoundException() {
			//given
			Class expected = EntityNotFoundException.class;
			Project project = createProject(1, "TEST", "description");
			Mockito.when(repository.findById(ArgumentMatchers.anyInt())).thenReturn(Optional.empty());

			//when
			Throwable actual = Assertions.catchThrowable(() -> service.update(project));

			//then
			Mockito.verify(repository, Mockito.times(1)).findById(ArgumentMatchers.anyInt());
			Mockito.verify(repository, Mockito.times(0)).save(ArgumentMatchers.any(Project.class));
			Assertions.assertThat(actual).isInstanceOf(expected);
		}

		@Test
		void givenNonExistentProjectIdentifier_whenUpdate_thenThrowEntityNotFoundException() {
			//given
			Class expected = EntityNotFoundException.class;
			Project projectToUpdate = createProject(1, "TEST", "description");
			Project projectFromRepository = createProject(1, "TEST1", "description2");

			Mockito.when(repository.findById(projectToUpdate.getId())).thenReturn(Optional.of(projectFromRepository));

			//when
			Throwable actual = Assertions.catchThrowable(() -> service.update(projectToUpdate));

			//then
			Mockito.verify(repository, Mockito.times(1)).findById(ArgumentMatchers.anyInt());
			Mockito.verify(repository, Mockito.times(0)).save(ArgumentMatchers.any(Project.class));
			Assertions.assertThat(actual).isInstanceOf(expected);
		}
	}

	@Nested
	class DeleteById {
		@Test
		void givenExistingProjectId_whenDeleteById_thenVoid() {
			//given
			Mockito.doNothing().when(repository).deleteById(ArgumentMatchers.anyInt());

			//when
			service.deleteById(ArgumentMatchers.anyInt());

			//then
			Mockito.verify(userSettingService, Mockito.times(1)).resetProject(ArgumentMatchers.anyInt());
			Mockito.verify(repository, Mockito.times(1)).deleteById(ArgumentMatchers.anyInt());
		}

		@Test
		void givenNonExistentProjectId_whenDeleteById_thenThrowEntityNotFoundException() {
			//given
			Class expected = EntityNotFoundException.class;
			Mockito.doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(ArgumentMatchers.anyInt());

			//when
			Throwable thrown = Assertions.catchThrowable(() -> service.deleteById(ArgumentMatchers.anyInt()));

			//then
			Mockito.verify(userSettingService, Mockito.times(1)).resetProject(ArgumentMatchers.anyInt());
			Mockito.verify(repository, Mockito.times(1)).deleteById(ArgumentMatchers.anyInt());
			Assertions.assertThat(thrown)
					.isInstanceOf(expected);
		}
	}

	@Nested
	class FindById {
		@Test
		void givenExistingProjectId_whenFindById_thenReturnProject() {
			//given
			int id = 1;
			Optional<Project> expected = Optional.of(createProject(id, "test", "description"));
			Mockito.when(repository.findById(id)).thenReturn(expected);

			//when
			Optional<Project> actual = service.findById(id);

			//then
			Mockito.verify(repository, Mockito.times(1)).findById(id);
			Assertions.assertThat(actual)
					.isNotNull()
					.isEqualTo(expected);
		}

		@Test
		void givenNonExistentProjectId_whenFindById_thenReturnEmptyProject() {
			//given

			Mockito.when(repository.findById(ArgumentMatchers.anyInt())).thenReturn(Optional.empty());

			//when
			Optional<Project> actual = service.findById(ArgumentMatchers.anyInt());

			//then
			Mockito.verify(repository, Mockito.times(1)).findById(ArgumentMatchers.anyInt());
			Assertions.assertThat(actual)
					.isNotNull()
					.isEmpty();
		}
	}

	@Nested
	class FindAll {
		@Test
		void givenExistingProjectsInDatabase_whenFindAll_thenReturnListOfProjects() {
			//given
			List<Project> expected = createProjectList();
			Mockito.when(repository.findAll()).thenReturn(expected);

			//when
			List<Project> actual = service.findAll();

			//then
			Mockito.verify(repository, Mockito.times(1)).findAll();
			Assertions.assertThat(actual)
					.isNotNull()
					.isEqualTo(expected);
		}

		@Test
		void givenNoProjectsInRepository_whenFindAll_thenReturnEmptyListOfProjects() {
			//given
			List<Project> expected = Collections.emptyList();
			Mockito.when(repository.findAll()).thenReturn(expected);

			//when
			List<Project> actual = service.findAll();

			//then
			Mockito.verify(repository, Mockito.times(1)).findAll();
			Assertions.assertThat(actual)
					.isNotNull()
					.isEqualTo(expected);
		}
	}

	private Project createProject(Integer id, String identifier, String description) {
		return Project.builder()
				.id(id)
				.identifier(identifier)
				.description(description)
				.projectSyncSettings(createProjectSyncSettingSet(id))
				.systems(createProjectSystemSet(id))
				.build();
	}

	private List<Project> createProjectList() {
		return List.of(createProject(1, "identi1", "Description 1"),
				createProject(2, "identi2", "Description 2"),
				createProject(3, "identi3", "Description 3"));
	}

	private Set<ProjectSyncSetting> createProjectSyncSettingSet(Integer projectId) {
		return Set.of(ProjectSyncSetting.builder()
						.id(new ProjectSyncSettingId(Project.builder().id(projectId).build(), TypeProjectElement.SOURCE_CODE))
						.isSyncEnabled(false)
						.location(Location.builder()
								.id(1)
								.identifier("location1")
								.build())
						.build(),
				ProjectSyncSetting.builder()
						.id(new ProjectSyncSettingId(Project.builder().id(projectId).build(), TypeProjectElement.MAPS))
						.isSyncEnabled(false)
						.location(Location.builder()
								.id(2)
								.identifier("location2")
								.build())
						.build());
	}

	private Set<ProjectSystem> createProjectSystemSet(Integer projectId) {
		return Set.of(ProjectSystem.builder()
						.id(new ProjectSystemId(Project.builder().id(projectId).build(), System.builder().id(1).identifier("test1").build()))
						.isSynchronizationEnabled(false)
						.location(Location.builder()
								.id(1)
								.identifier("location1")
								.build())
						.build(),
				ProjectSystem.builder()
						.id(new ProjectSystemId(Project.builder().id(projectId).build(), System.builder().id(2).identifier("test2").build()))
						.isSynchronizationEnabled(false)
						.location(Location.builder()
								.id(2)
								.identifier("location2")
								.build())
						.build());
	}

}
