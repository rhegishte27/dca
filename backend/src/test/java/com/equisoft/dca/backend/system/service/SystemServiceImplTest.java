package com.equisoft.dca.backend.system.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
import com.equisoft.dca.backend.system.dao.SystemRepository;
import com.equisoft.dca.backend.system.model.System;
import com.equisoft.dca.backend.user.service.UserSettingService;

@ExtendWith(MockitoExtension.class)
class SystemServiceImplTest {

	@Mock
	private SystemRepository repository;
	@Mock
	private UserSettingService userSettingService;

	private SystemService service;

	@BeforeEach
	void init() {
		service = new SystemServiceImpl(repository, userSettingService);
	}

	@Nested
	class Save {
		@Test
		void givenNonExistentSystemIdentifier_whenSave_thenReturnNewSystem() {
			//given
			System systemToSave = createSystem(null, "test", "description");
			System expected = createSystem(1, "TEST", "description");

			Mockito.when(repository.findByIdentifier(expected.getIdentifier())).thenReturn(Optional.empty());
			Mockito.when(repository.save(systemToSave)).thenReturn(expected);

			//when
			System actual = service.save(systemToSave);

			//then
			Mockito.verify(repository, Mockito.times(1)).findByIdentifier(ArgumentMatchers.anyString());
			Mockito.verify(repository, Mockito.times(1)).save(ArgumentMatchers.any(System.class));
			Assertions.assertThat(actual)
					.isNotNull()
					.isEqualTo(expected);
		}

		@ParameterizedTest
		@CsvSource({"TEST, TEST", "TEST, test", "NEW ENTITY 1, 'new entity 1'"})
		void givenExistingSystemIdentifier_whenSave_thenThrowEntityAlreadyExistsException(String identifierExisted, String identifierToSave) {
			//given
			System system = createSystem(null, identifierToSave, "description");
			System otherSystem = createSystem(1, identifierExisted, "description");
			Mockito.when(repository.findByIdentifier(identifierExisted)).thenReturn(Optional.of(otherSystem));

			//when
			Throwable thrown = Assertions.catchThrowable(() -> service.save(system));

			//then
			Mockito.verify(repository, Mockito.times(1)).findByIdentifier(ArgumentMatchers.anyString());
			Mockito.verify(repository, Mockito.times(0)).save(ArgumentMatchers.any(System.class));
			Assertions.assertThat(thrown)
					.isInstanceOf(EntityAlreadyExistsException.class);
		}

		@ParameterizedTest
		@CsvSource({"   test test     , test test", "  test                test, test test", "test\t\t\t\ttest, test test"})
		void givenDescription_whenSave_shouldTrimDescription(String actual, String expected) {
			// given
			System system = createSystem(null, "test", actual);

			// when
			service.save(system);

			// then
			Assertions.assertThat(system.getDescription()).isEqualTo(expected);
		}
	}

	@Nested
	class Update {
		@ParameterizedTest
		@CsvSource({"TEST, TEST", "TEST, test", "NEW ENTITY 1, 'new entity 1'"})
		void givenExistingSystemIdentifierWithDifferentId_whenUpdate_thenThrowEntityAlreadyExistsException(String identifierExisted, String identifierToSave) {
			//given
			System system = createSystem(1, identifierToSave, "description");
			Mockito.when(repository.findById(system.getId())).thenReturn(Optional.of(system));

			System otherSystem = createSystem(2, identifierExisted, "description");
			Mockito.when(repository.findByIdentifier(identifierExisted)).thenReturn(Optional.of(otherSystem));

			//when
			Throwable thrown = Assertions.catchThrowable(() -> service.update(system));

			//then
			Mockito.verify(repository, Mockito.times(1)).findByIdentifier(ArgumentMatchers.anyString());
			Mockito.verify(repository, Mockito.times(0)).save(ArgumentMatchers.any(System.class));
			Assertions.assertThat(thrown)
					.isInstanceOf(EntityAlreadyExistsException.class);
		}

		@ParameterizedTest
		@CsvSource({"TEST, TEST", "TEST, test", "NEW ENTITY 1, 'new entity 1'"})
		void givenExistingSystemIdentifierWithSameId_whenUpdate_thenReturnSystem(String identifierExisted, String identifierToSave) {
			//given
			System system = createSystem(1, identifierToSave, "description");
			Mockito.when(repository.findById(system.getId())).thenReturn(Optional.of(system));

			System otherSystem = createSystem(1, identifierExisted, "description");
			Mockito.when(repository.findByIdentifier(identifierExisted)).thenReturn(Optional.of(otherSystem));
			Mockito.when(repository.save(system)).thenReturn(system);

			//when
			System actual = service.update(system);

			//then
			Mockito.verify(repository, Mockito.times(1)).findByIdentifier(ArgumentMatchers.anyString());
			Mockito.verify(repository, Mockito.times(1)).save(ArgumentMatchers.any(System.class));
			Assertions.assertThat(actual)
					.isNotNull()
					.isEqualTo(system);
		}

		@Test
		void givenExistingSystemIdAndIdentifier_whenUpdate_thenReturnSystem() {
			//given
			System system = createSystem(1, "TEST", "description");
			System systemInRepository = createSystem(1, "TEST", "description");

			Mockito.when(repository.findById(system.getId())).thenReturn(Optional.of(systemInRepository));
			Mockito.when(repository.save(system)).thenReturn(systemInRepository);

			//when
			System actual = service.update(system);

			//then
			Mockito.verify(repository, Mockito.times(1)).findById(ArgumentMatchers.anyInt());
			Mockito.verify(repository, Mockito.times(1)).save(ArgumentMatchers.any(System.class));
			Assertions.assertThat(actual)
					.isNotNull()
					.isEqualTo(systemInRepository);
		}

		@Test
		void givenNonExistentSystemId_whenUpdate_thenThrowEntityNotFoundException() {
			//given
			System system = createSystem(1, "test", "description");
			Mockito.when(repository.findById(ArgumentMatchers.anyInt())).thenReturn(Optional.empty());

			//when
			Throwable thrown = Assertions.catchThrowable(() -> service.update(system));

			//then
			Mockito.verify(repository, Mockito.times(1)).findById(ArgumentMatchers.anyInt());
			Mockito.verify(repository, Mockito.times(0)).save(ArgumentMatchers.any(System.class));
			Assertions.assertThat(thrown)
					.isInstanceOf(EntityNotFoundException.class);
		}

		@Test
		void givenNonExistentSystemIdentifier_whenUpdate_thenThrowEntityNotFoundException() {
			//given
			System system = createSystem(1, "test", "description");
			System systemInRepository = createSystem(1, "testdif", "description");
			Mockito.when(repository.findById(ArgumentMatchers.anyInt())).thenReturn(Optional.of(systemInRepository));

			//when
			Throwable thrown = Assertions.catchThrowable(() -> service.update(system));

			//then
			Mockito.verify(repository, Mockito.times(1)).findById(ArgumentMatchers.anyInt());
			Mockito.verify(repository, Mockito.times(0)).save(ArgumentMatchers.any(System.class));
			Assertions.assertThat(thrown)
					.isInstanceOf(EntityNotFoundException.class);
		}

		@ParameterizedTest
		@CsvSource({"   test test     , test test", "  test                test, test test", "test\t\t\t\ttest, test test"})
		void givenDescription_whenSave_shouldTrimDescription(String actual, String expected) {
			// given
			System system = createSystem(1, "test", actual);
			Mockito.when(repository.findById(ArgumentMatchers.anyInt())).thenReturn(Optional.of(system));

			// when
			service.update(system);

			// then
			Assertions.assertThat(system.getDescription()).isEqualTo(expected);
		}
	}

	@Nested
	class DeleteById {
		@Test
		void givenExistingSystemId_whenDeleteById_thenVoid() {
			//given
			Mockito.doNothing().when(repository).deleteById(ArgumentMatchers.anyInt());

			//when
			service.deleteById(ArgumentMatchers.anyInt());

			//then
			Mockito.verify(userSettingService, Mockito.times(1)).resetSystem(ArgumentMatchers.anyInt());
			Mockito.verify(repository, Mockito.times(1)).deleteById(ArgumentMatchers.anyInt());
		}

		@Test
		void givenNonExistentSystemId_whenDeleteById_thenThrowEntityNotFoundException() {
			//given
			Mockito.doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(ArgumentMatchers.anyInt());

			//when
			Throwable thrown = Assertions.catchThrowable(() -> service.deleteById(ArgumentMatchers.anyInt()));

			//then
			Mockito.verify(userSettingService, Mockito.times(1)).resetSystem(ArgumentMatchers.anyInt());
			Mockito.verify(repository, Mockito.times(1)).deleteById(ArgumentMatchers.anyInt());
			Assertions.assertThat(thrown)
					.isInstanceOf(EntityNotFoundException.class);
		}
	}

	@Nested
	class FindById {
		@Test
		void givenExistingSystemId_whenFindById_thenReturnSystem() {
			//given
			System system = createSystem(1, "test", "description");
			Mockito.when(repository.findById(ArgumentMatchers.anyInt())).thenReturn(Optional.of(system));

			//when
			Optional<System> actual = service.findById(system.getId());

			//then
			Mockito.verify(repository, Mockito.times(1)).findById(system.getId());
			Assertions.assertThat(actual)
					.isNotNull()
					.isNotEmpty()
					.containsSame(system);
		}

		@Test
		void givenNonExistentSystemId_whenFindById_thenReturnEmpty() {
			//given
			Mockito.when(repository.findById(ArgumentMatchers.anyInt())).thenReturn(Optional.empty());

			//when
			Optional<System> actual = service.findById(ArgumentMatchers.anyInt());

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
		void givenExistingSystems_whenFindAll_thenReturnListOfSystems() {
			//given
			List<System> systems = createSystemList();
			Mockito.when(repository.findAll()).thenReturn(systems);

			//when
			List<System> actual = service.findAll();

			//then
			Mockito.verify(repository, Mockito.times(1)).findAll();
			Assertions.assertThat(actual)
					.isNotNull()
					.hasSameSizeAs(systems)
					.hasSameElementsAs(systems);
		}

		@Test
		void givenNoSystems_whenFindAll_thenReturnEmptyListOfSystems() {
			//given
			Mockito.when(repository.findAll()).thenReturn(Collections.emptyList());

			//when
			List<System> actual = service.findAll();

			//then
			Mockito.verify(repository, Mockito.times(1)).findAll();
			Assertions.assertThat(actual)
					.isNotNull()
					.isEmpty();
		}
	}

	private System createSystem(Integer id, String identifier, String description) {
		return System.builder().id(id).identifier(identifier).description(description).build();
	}

	private List<System> createSystemList() {
		return List.of(createSystem(1, "test1", "description1"),
				createSystem(2, "test2", "description2"),
				createSystem(3, "test3", "description3"));
	}
}
