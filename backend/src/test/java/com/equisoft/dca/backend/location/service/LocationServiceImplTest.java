package com.equisoft.dca.backend.location.service;

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
import com.equisoft.dca.backend.filesystem.service.FileSystemService;
import com.equisoft.dca.backend.ftp.service.FtpService;
import com.equisoft.dca.backend.location.dao.LocationRepository;
import com.equisoft.dca.backend.location.model.Location;
import com.equisoft.dca.backend.location.model.LocationType;
import com.equisoft.dca.backend.project.service.ProjectService;
import com.equisoft.dca.backend.user.service.UserSettingService;

@ExtendWith(MockitoExtension.class)
class LocationServiceImplTest {

	@Mock
	private LocationRepository locationRepository;
	@Mock
	private UserSettingService userSettingService;

	@Mock
	private FtpService ftpService;

	private LocationServiceImpl locationService;

	@Mock
	private ProjectService projectService;

	@Mock
	private FileSystemService fileSystemService;

	@BeforeEach
	void setUp() {
		locationService = new LocationServiceImpl(locationRepository, userSettingService, ftpService, projectService, fileSystemService);
	}

	@Nested
	class DeleteById {

		@Test
		void givenExistingLocationId_whenDeleteById_thenVoid() {
			//given
			Mockito.doNothing().when(locationRepository).deleteById(ArgumentMatchers.anyInt());
			Mockito.doNothing().when(userSettingService).resetLocation(ArgumentMatchers.anyInt());

			//when
			locationService.deleteById(ArgumentMatchers.anyInt());

			//then
			Mockito.verify(locationRepository, Mockito.times(1)).deleteById(ArgumentMatchers.anyInt());
			Mockito.verify(userSettingService, Mockito.times(1)).resetLocation(ArgumentMatchers.anyInt());
		}

		@Test
		void givenNonExistentLocationId_whenDeleteById_thenThrowEntityNotFoundException() {
			//given
			Class expected = EntityNotFoundException.class;
			Mockito.doThrow(EmptyResultDataAccessException.class).when(locationRepository).deleteById(ArgumentMatchers.anyInt());
			Mockito.doNothing().when(userSettingService).resetLocation(ArgumentMatchers.anyInt());

			//when
			Throwable thrown = Assertions.catchThrowable(() -> locationService.deleteById(ArgumentMatchers.anyInt()));

			//then
			Mockito.verify(locationRepository, Mockito.times(1)).deleteById(ArgumentMatchers.anyInt());
			Mockito.verify(userSettingService, Mockito.times(1)).resetLocation(ArgumentMatchers.anyInt());
			Assertions.assertThat(thrown).isInstanceOf(expected);
		}
	}

	@Nested
	class FindById {

		@Test
		void givenExistingLocationId_whenFindById_thenReturnLocation() {
			//given
			int id = 1;
			Optional<Location> expected = Optional.of(createLocation(1, "Identifier1"));
			Mockito.when(locationRepository.findById(id)).thenReturn(expected);

			//when
			Optional<Location> actual = locationService.findById(id);

			//then
			Mockito.verify(locationRepository, Mockito.times(1)).findById(id);
			Assertions.assertThat(actual)
					.isNotNull()
					.isEqualTo(expected);
		}

		@Test
		void givenNonExistentLocationId_whenFindById_thenReturnEmptyLocation() {
			//given

			Mockito.when(locationRepository.findById(ArgumentMatchers.anyInt())).thenReturn(Optional.empty());

			//when
			Optional<Location> actual = locationService.findById(ArgumentMatchers.anyInt());

			//then
			Mockito.verify(locationRepository, Mockito.times(1)).findById(ArgumentMatchers.anyInt());
			Assertions.assertThat(actual)
					.isNotNull()
					.isEmpty();
		}
	}

	@Nested
	class FindAll {

		@Test
		void givenExistingLocationsInDatabase_whenFindAll_thenReturnListOfLocations() {
			//given
			List<Location> expected = createLocationList();
			Mockito.when(locationRepository.findAll()).thenReturn(expected);

			//when
			List<Location> actual = locationService.findAll();

			//then
			Mockito.verify(locationRepository, Mockito.times(1)).findAll();
			Assertions.assertThat(actual)
					.isNotNull()
					.isEqualTo(expected);
		}

		@Test
		void givenNoLocationsInRepository_whenFindAll_thenReturnEmptyListOfLocations() {
			//given
			List<Location> expected = Collections.emptyList();
			Mockito.when(locationRepository.findAll()).thenReturn(expected);

			//when
			List<Location> actual = locationService.findAll();

			//then
			Mockito.verify(locationRepository, Mockito.times(1)).findAll();
			Assertions.assertThat(actual)
					.isNotNull()
					.isEqualTo(expected);
		}
	}

	@Nested
	class Save {

		@Test
		void givenNonExistentLocationIdentifier_whenSave_thenReturnNewLocation() {
			//given
			Location locationToSave = createLocation(null, "identifier1");
			Location locationFromRepository = createLocation(1, "Identifier1");
			Location expected = createLocation(1, "Identifier1");

			Mockito.when(locationRepository.findByIdentifier(expected.getIdentifier())).thenReturn(Optional.empty());
			Mockito.when(locationRepository.save(locationToSave)).thenReturn(locationFromRepository);
			Mockito.when(fileSystemService.isValidDirectory(ArgumentMatchers.anyString())).thenReturn(true);

			//when
			Location actual = locationService.save(locationToSave);

			//then
			Mockito.verify(locationRepository, Mockito.times(1)).findByIdentifier(ArgumentMatchers.anyString());
			Mockito.verify(locationRepository, Mockito.times(1)).save(ArgumentMatchers.any(Location.class));
			Assertions.assertThat(actual)
					.isNotNull()
					.isEqualTo(expected);
		}

		@Test
		void givenExistingLocationIdentifier_whenSave_thenThrowEntityAlreadyExistsException() {
			//given
			Class expected = EntityAlreadyExistsException.class;

			Location locationToSave = createLocation(null, "identifier1");
			Location locationFromRepository = createLocation(1, "Identifier1");

			Mockito.when(locationRepository.findByIdentifier(ArgumentMatchers.anyString())).thenReturn(Optional.of(locationFromRepository));
			Mockito.when(fileSystemService.isValidDirectory(ArgumentMatchers.anyString())).thenReturn(true);

			//when
			Throwable actual = Assertions.catchThrowable(() -> locationService.save(locationToSave));

			//then
			Mockito.verify(locationRepository, Mockito.times(1)).findByIdentifier(ArgumentMatchers.anyString());
			Mockito.verify(locationRepository, Mockito.times(0)).save(ArgumentMatchers.any(Location.class));
			Assertions.assertThat(actual).isInstanceOf(expected);
		}

		@ParameterizedTest
		@CsvSource({"   test test     , Test Test", "  test                test, Test Test", "test\t\t\t\ttest, Test Test"})
		void givenIdentifier_whenSave_shouldNormalizeSpaceAndTitleCaseIdentifier(String actual, String expected) {
			// given
			Location location = createLocation(null, actual);
			Mockito.when(fileSystemService.isValidDirectory(ArgumentMatchers.anyString())).thenReturn(true);

			// when
			locationService.save(location);

			// then
			Assertions.assertThat(location.getIdentifier()).isEqualTo(expected);
		}
	}

	@Nested
	class Update {

		@Test
		void givenExistingLocationIdAndIdentifier_whenUpdate_thenReturnLocation() {
			//given
			Location locationToUpdate = createLocation(1, "Identifier");
			Location locationFromRepository = createLocation(1, "Identifier");
			Location expected = createLocation(1, "Identifier");

			Mockito.when(locationRepository.findById(locationToUpdate.getId())).thenReturn(Optional.of(locationToUpdate));
			Mockito.when(locationRepository.findByIdentifier(locationToUpdate.getIdentifier())).thenReturn(Optional.of(locationFromRepository));
			Mockito.when(locationRepository.save(locationToUpdate)).thenReturn(locationFromRepository);
			Mockito.when(fileSystemService.isValidDirectory(ArgumentMatchers.anyString())).thenReturn(true);

			//when
			Location actual = locationService.update(locationToUpdate);

			//then
			Mockito.verify(locationRepository, Mockito.times(1)).findById(ArgumentMatchers.anyInt());
			Mockito.verify(locationRepository, Mockito.times(1)).findByIdentifier(ArgumentMatchers.anyString());
			Mockito.verify(locationRepository, Mockito.times(1)).save(ArgumentMatchers.any(Location.class));
			Assertions.assertThat(actual)
					.isNotNull()
					.isEqualTo(expected);
		}

		@Test
		void givenNonExistentLocationId_whenUpdate_thenThrowEntityNotFoundException() {
			//given
			Class expected = EntityNotFoundException.class;
			Location locationToUpdate = createLocation(1, "Identifier");

			Mockito.when(locationRepository.findById(ArgumentMatchers.anyInt())).thenReturn(Optional.empty());

			//when
			Throwable actual = Assertions.catchThrowable(() -> locationService.update(locationToUpdate));

			//then
			Mockito.verify(locationRepository, Mockito.times(1)).findById(ArgumentMatchers.anyInt());
			Mockito.verify(locationRepository, Mockito.times(0)).findByIdentifier(ArgumentMatchers.anyString());
			Mockito.verify(locationRepository, Mockito.times(0)).save(ArgumentMatchers.any(Location.class));
			Assertions.assertThat(actual).isInstanceOf(expected);
		}

		@Test
		void givenNonExistentLocationIdentifier_whenUpdate_thenThrowEntityNotFoundException() {
			//given
			Class expected = EntityNotFoundException.class;
			Location locationToUpdate = createLocation(1, "Identifier");
			Location locationFromRepository = createLocation(1, "IdentifierXX");

			Mockito.when(locationRepository.findById(locationToUpdate.getId())).thenReturn(Optional.of(locationFromRepository));

			//when
			Throwable actual = Assertions.catchThrowable(() -> locationService.update(locationToUpdate));

			//then
			Mockito.verify(locationRepository, Mockito.times(1)).findById(ArgumentMatchers.anyInt());
			Mockito.verify(locationRepository, Mockito.times(0)).findByIdentifier(ArgumentMatchers.anyString());
			Mockito.verify(locationRepository, Mockito.times(0)).save(ArgumentMatchers.any(Location.class));
			Assertions.assertThat(actual).isInstanceOf(expected);
		}

		@Test
		void givenExistingLocationIdentifierWithDifferentId_whenUpdate_thenThrowEntityAlreadyExistsException() {
			//given
			Class expected = EntityAlreadyExistsException.class;

			Location locationToUpdate = createLocation(1, "IdentifierToUpdate");
			Location locationFound = createLocation(2, "IdentifierFromRepository");

			Mockito.when(locationRepository.findById(locationToUpdate.getId())).thenReturn(Optional.of(locationToUpdate));
			Mockito.when(locationRepository.findByIdentifier(locationToUpdate.getIdentifier())).thenReturn(Optional.of(locationFound));
			Mockito.when(fileSystemService.isValidDirectory(ArgumentMatchers.anyString())).thenReturn(true);

			//when
			Throwable actual = Assertions.catchThrowable(() -> locationService.update(locationToUpdate));

			//then
			Mockito.verify(locationRepository, Mockito.times(1)).findById(ArgumentMatchers.anyInt());
			Mockito.verify(locationRepository, Mockito.times(1)).findByIdentifier(ArgumentMatchers.anyString());
			Mockito.verify(locationRepository, Mockito.times(0)).save(ArgumentMatchers.any(Location.class));
			Assertions.assertThat(actual)
					.isInstanceOf(expected);
		}
	}

	private Location createLocation(Integer id, String identifier) {
		return Location.builder()
				.id(id)
				.identifier(identifier)
				.locationType(LocationType.NETWORK)
				.path("/test/test")
				.build();
	}

	private List<Location> createLocationList() {
		return List.of(createLocation(1, "Identifier1"),
				createLocation(2, "Identifier2"),
				createLocation(3, "Identifier3"));
	}
}
