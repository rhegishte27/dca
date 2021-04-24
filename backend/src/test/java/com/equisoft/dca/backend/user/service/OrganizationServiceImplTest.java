package com.equisoft.dca.backend.user.service;

import java.util.Arrays;
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
import com.equisoft.dca.backend.exception.EntityReferenceConflictedException;
import com.equisoft.dca.backend.user.dao.OrganizationRepository;
import com.equisoft.dca.backend.user.model.Organization;
import com.equisoft.dca.backend.user.model.User;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.only;

@ExtendWith(MockitoExtension.class)
class OrganizationServiceImplTest {
	@Mock
	private OrganizationRepository organizationRepository;
	@Mock
	private UserService userService;
	@Mock
	private UserSettingService userSettingService;

	private OrganizationService organizationService;

	@BeforeEach
	void setUp() {
		organizationService = new OrganizationServiceImpl(organizationRepository, userService, userSettingService);
	}

	@Nested
	class Save {
		@Test
		void givenNonExistentOrganizationName_whenSave_thenReturnNewOrganization() {
			//given
			Organization organization = createOrganization(null, "Org");
			Organization organizationFromRepository = createOrganization(1, "OrgTest");

			Mockito.when(organizationRepository.findByName(organization.getName())).thenReturn(Optional.empty());
			Mockito.when(organizationRepository.save(ArgumentMatchers.any(Organization.class))).thenReturn(organizationFromRepository);

			//when
			Organization actual = organizationService.save(organization);

			//then
			Mockito.verify(organizationRepository, Mockito.times(1)).findByName(ArgumentMatchers.anyString());
			Mockito.verify(organizationRepository, Mockito.times(1)).save(ArgumentMatchers.any(Organization.class));
			Assertions.assertThat(actual)
					.isNotNull()
					.isEqualTo(organizationFromRepository);
		}

		@ParameterizedTest
		@CsvSource({"Test, Test", "Test, test", "Test, '         test            '", "New Entity 1, '    new        entity        1     '"})
		void givenExistingOrganizationName_whenSave_thenThrowEntityAlreadyExistsException(String identifierExisted, String identifierToSave) {
			//given
			Organization organization = createOrganization(null, identifierToSave);
			Organization otherOrganization = createOrganization(1, identifierExisted);
			Mockito.when(organizationRepository.findByName(identifierExisted)).thenReturn(Optional.of(otherOrganization));

			//when
			Throwable thrown = Assertions.catchThrowable(() -> organizationService.save(organization));

			//then
			Mockito.verify(organizationRepository, Mockito.times(1)).findByName(ArgumentMatchers.anyString());
			Mockito.verify(organizationRepository, Mockito.times(0)).save(ArgumentMatchers.any(Organization.class));
			Assertions.assertThat(thrown)
					.isInstanceOf(EntityAlreadyExistsException.class);
		}

		@ParameterizedTest
		@CsvSource({"test test, Test Test", "TEST TEST, Test Test", "donald TRUMP, donald TRUMP", "   test test     , Test Test",
				"  test                test, Test Test", "test\t\t\t\ttest, Test Test"})
		void givenName_whenSave_shouldTrimAndChangeNameToTitleCaseIfNotMixedCase(String actual, String expected) {
			// given
			Organization org = createOrganization();
			org.setName(actual);

			// when
			organizationService.save(org);

			// then
			Assertions.assertThat(org.getName()).isEqualTo(expected);
		}

		@ParameterizedTest
		@CsvSource({"   test test     , test test", "  test                test, test test", "test\t\t\t\ttest, test test"})
		void givenDescription_whenSave_shouldTrimDescription(String actual, String expected) {
			// given
			Organization org = createOrganization();
			org.setDescription(actual);

			// when
			organizationService.save(org);

			// then
			Assertions.assertThat(org.getDescription()).isEqualTo(expected);
		}
	}

	@Nested
	class Update {

		@ParameterizedTest
		@CsvSource({"Test, Test", "Test, test", "Test, '         test            '", "New Entity 1, '    new        entity        1     '"})
		void givenExistingOrganizationNameWithDifferentId_whenUpdate_thenThrowEntityAlreadyExistsException(String identifierExisted, String identifierToSave) {
			//given
			Organization organization = createOrganization(1, identifierToSave);
			Mockito.when(organizationRepository.findById(1)).thenReturn(Optional.of(organization));

			Organization otherOrganization = createOrganization(2, identifierExisted);
			Mockito.when(organizationRepository.findByName(identifierExisted)).thenReturn(Optional.of(otherOrganization));

			//when
			Throwable thrown = Assertions.catchThrowable(() -> organizationService.update(organization));

			//then
			Mockito.verify(organizationRepository, Mockito.times(1)).findByName(ArgumentMatchers.anyString());
			Mockito.verify(organizationRepository, Mockito.times(0)).save(ArgumentMatchers.any(Organization.class));
			Assertions.assertThat(thrown)
					.isInstanceOf(EntityAlreadyExistsException.class);
		}

		@ParameterizedTest
		@CsvSource({"Test, Test", "Test, test", "Test, '         test            '", "New Entity 1, '    new        entity        1     '"})
		void givenExistingOrganizationNameWithSameId_whenUpdate_thenReturnOrganization(String identifierExisted, String identifierToSave) {
			//given
			Organization organization = createOrganization(1, identifierToSave);
			Mockito.when(organizationRepository.findById(1)).thenReturn(Optional.of(organization));

			Organization otherOrganization = createOrganization(1, identifierExisted);
			Mockito.when(organizationRepository.findByName(identifierExisted)).thenReturn(Optional.of(otherOrganization));
			Mockito.when(organizationRepository.save(organization)).thenReturn(organization);

			//when
			Organization actual = organizationService.update(organization);

			//then
			Mockito.verify(organizationRepository, Mockito.times(1)).findByName(ArgumentMatchers.anyString());
			Mockito.verify(organizationRepository, Mockito.times(1)).save(ArgumentMatchers.any(Organization.class));
			Assertions.assertThat(actual)
					.isNotNull()
					.isEqualTo(organization);
		}

		@Test
		void givenExistingOrganizationId_whenUpdate_thenReturnOrganization() {
			//given
			Organization organization = createOrganization();
			Organization organizationFromRepository = createOrganization();

			Mockito.when(organizationRepository.findById(organization.getId())).thenReturn(Optional.of(organization));
			Mockito.when(organizationRepository.save(organization)).thenReturn(organizationFromRepository);

			//when
			Organization actual = organizationService.update(organization);

			//then
			Mockito.verify(organizationRepository, Mockito.times(1)).findById(ArgumentMatchers.anyInt());
			Mockito.verify(organizationRepository, Mockito.times(1)).save(ArgumentMatchers.any(Organization.class));
			Assertions.assertThat(actual)
					.isNotNull()
					.isEqualTo(organizationFromRepository);
		}

		@Test
		void givenNonExistentOrganizationId_whenUpdate_thenThrowEntityNotFoundException() {
			//given
			Organization organization = createOrganization();
			Mockito.when(organizationRepository.findById(ArgumentMatchers.anyInt())).thenReturn(Optional.empty());

			//when
			Throwable thrown = Assertions.catchThrowable(() -> organizationService.update(organization));

			//then
			Mockito.verify(organizationRepository, Mockito.times(1)).findById(ArgumentMatchers.anyInt());
			Mockito.verify(organizationRepository, Mockito.times(0)).save(ArgumentMatchers.any(Organization.class));
			Assertions.assertThat(thrown)
					.isInstanceOf(EntityNotFoundException.class);
		}

		@ParameterizedTest
		@CsvSource({"test test, Test Test", "TEST TEST, Test Test", "donald TRUMP, donald TRUMP", "   test test     , Test Test",
				"  test                test, Test Test", "test\t\t\t\ttest, Test Test"})
		void givenName_whenUpdate_shouldTrimAndChangeUserNameToTitleCaseIfNotMixedCase(String actual, String expected) {
			// given
			Organization org = createOrganization();
			Mockito.when(organizationRepository.findById(ArgumentMatchers.anyInt())).thenReturn(Optional.of(org));
			org.setName(actual);

			// when
			organizationService.update(org);

			// then
			Assertions.assertThat(org.getName()).isEqualTo(expected);
		}

		@ParameterizedTest
		@CsvSource({"   test test     , test test", "  test                test, test test", "test\t\t\t\ttest, test test"})
		void givenDescription_whenUpdate_shouldTrimDescription(String actual, String expected) {
			// given
			Organization org = createOrganization();
			Mockito.when(organizationRepository.findById(ArgumentMatchers.anyInt())).thenReturn(Optional.of(org));
			org.setDescription(actual);

			// when
			organizationService.update(org);

			// then
			Assertions.assertThat(org.getDescription()).isEqualTo(expected);
		}
	}

	@Nested
	class FindById {
		@Test
		void givenRepositoryReturnOrganization_whenFindById_shouldCallTheRepositoryFindById_shouldReturnWhatTheRepositoryReturn() {
			// given
			int idOrganization = 3;
			Optional<Organization> expected = Optional.of(createOrganization());
			Mockito.doReturn(expected).when(organizationRepository).findById(idOrganization);

			// when
			Optional<Organization> actual = organizationService.findById(idOrganization);

			// then
			Mockito.verify(organizationRepository, only()).findById(idOrganization);
			Assertions.assertThat(actual).isEqualTo(expected);
		}

		@Test
		void givenRepositoryReturnEmpty_whenFindById_shouldCallTheRepositoryFindById_shouldReturnWhatTheRepositoryReturn() {
			// given
			int idOrganization = 3;
			Optional<Organization> expected = Optional.empty();
			Mockito.doReturn(expected).when(organizationRepository).findById(idOrganization);

			// when
			Optional<Organization> actual = organizationService.findById(idOrganization);

			// then
			Mockito.verify(organizationRepository, only()).findById(idOrganization);
			Assertions.assertThat(actual).isEqualTo(expected);
		}
	}

	@Nested
	class FindByName {
		@Test
		void givenRepositoryReturnOrganization_whenFindByName_shouldCallTheRepositoryFindByName_shouldReturnWhatTheRepositoryReturn() {
			// given
			String name = "name";
			Optional<Organization> expected = Optional.of(createOrganization());
			Mockito.doReturn(expected).when(organizationRepository).findByName(name);

			// when
			Optional<Organization> actual = organizationService.findByName(name);

			// then
			Mockito.verify(organizationRepository, only()).findByName(name);
			Assertions.assertThat(actual).isEqualTo(expected);
		}

		@Test
		void givenRepositoryReturnEmpty_whenFindByName_shouldCallTheRepositoryFindByName_shouldReturnWhatTheRepositoryReturn() {
			// given
			String name = "name";
			Optional<Organization> expected = Optional.empty();
			Mockito.doReturn(expected).when(organizationRepository).findByName(name);

			// when
			Optional<Organization> actual = organizationService.findByName(name);

			// then
			Mockito.verify(organizationRepository, only()).findByName(name);
			Assertions.assertThat(actual).isEqualTo(expected);
		}
	}

	@Nested
	class Delete {
		@Test
		void givenUserServiceFindByOrgIdReturnEmptyLst_whenDelete_shouldCallRepoDeleteById() {
			// given
			int id = 1;
			Mockito.doReturn(Collections.emptyList()).when(userService).findByOrganizationId(id);

			// when
			organizationService.deleteById(id);

			// then
			Mockito.verify(userSettingService, only()).resetOrganization(id);
			Mockito.verify(organizationRepository, only()).deleteById(id);
		}

		@Test
		void givenUserServiceFindByOrgIdReturnLstAndRepoFindByIdReturnOrg_whenDelete_shouldThrowEntityReferenceConflictedException() {
			// given
			int id = 1;
			Mockito.doReturn(Arrays.asList(createUser(), createUser())).when(userService).findByOrganizationId(id);
			Mockito.doReturn(Optional.of(createOrganization())).when(organizationRepository).findById(id);

			// when
			Throwable thrown = Assertions.catchThrowable(
					() -> organizationService.deleteById(id));

			// then
			Assertions.assertThat(thrown)
					.isInstanceOf(EntityReferenceConflictedException.class);
			Mockito.verify(userSettingService, never()).resetOrganization(id);
			Mockito.verify(organizationRepository, never()).deleteById(id);
		}

		@Test
		void givenUserServiceFindByOrgIdReturnLstAndRepoFindByIdReturnOrg_whenDelete_shouldThrowEntityNotFoundException() {
			// given
			int id = 1;
			Mockito.doReturn(Arrays.asList(createUser(), createUser())).when(userService).findByOrganizationId(id);
			Mockito.doReturn(Optional.empty()).when(organizationRepository).findById(id);

			// when
			Throwable thrown = Assertions.catchThrowable(
					() -> organizationService.deleteById(id));

			// then
			Assertions.assertThat(thrown)
					.isInstanceOf(EntityNotFoundException.class);
			Mockito.verify(userSettingService, never()).resetOrganization(id);
			Mockito.verify(organizationRepository, never()).deleteById(id);
		}

		@Test
		void givenNonExistentOrganization_whenDeleteById_thenThrowEntityNotFoundException() {
			//given
			Class expected = EntityNotFoundException.class;

			Mockito.doReturn(Collections.emptyList()).when(userService).findByOrganizationId(ArgumentMatchers.anyInt());
			Mockito.doThrow(EmptyResultDataAccessException.class).when(organizationRepository).deleteById(ArgumentMatchers.anyInt());

			//when
			Throwable thrown = Assertions.catchThrowable(() -> organizationService.deleteById(ArgumentMatchers.anyInt()));

			//then
			Mockito.verify(userService, Mockito.times(1)).findByOrganizationId(ArgumentMatchers.anyInt());
			Mockito.verify(userSettingService, Mockito.times(1)).resetOrganization(ArgumentMatchers.anyInt());
			Mockito.verify(organizationRepository, Mockito.times(1)).deleteById(ArgumentMatchers.anyInt());

			Assertions.assertThat(thrown).isInstanceOf(expected);
		}
	}

	@Nested
	class FindAll {
		@Test
		void givenRepositoryReturnLstOrganization_whenFindAll_shouldCallTheRepositoryFindAll_shouldReturnWhatTheRepositoryReturn() {
			// given
			List<Organization> expected = createListOrganizations();
			Mockito.doReturn(expected).when(organizationRepository).findAll();

			// when
			List<Organization> actual = organizationService.findAll();

			// then
			Mockito.verify(organizationRepository, only()).findAll();
			Assertions.assertThat(actual).isEqualTo(expected);
		}

		@Test
		void givenRepositoryReturnEmptyLst_whenFindAll_shouldCallTheRepositoryFindAll_shouldReturnWhatTheRepositoryReturn() {
			// given
			List<Organization> expected = Collections.emptyList();
			Mockito.doReturn(expected).when(organizationRepository).findAll();

			// when
			List<Organization> actual = organizationService.findAll();

			// then
			Mockito.verify(organizationRepository, only()).findAll();
			Assertions.assertThat(actual).isEqualTo(expected);
		}
	}

	private List<Organization> createListOrganizations() {
		return List.of(createOrganization(1, "Org1"),
				createOrganization(2, "Org2"),
				createOrganization(3, "Org3"));
	}

	private Organization createOrganization(Integer id, String name) {
		return Organization.builder()
				.id(id)
				.name(name)
				.description("description")
				.build();
	}

	private Organization createOrganization() {
		return createOrganization(1, "org");
	}

	private User createUser() {
		return User.builder().build();
	}
}
