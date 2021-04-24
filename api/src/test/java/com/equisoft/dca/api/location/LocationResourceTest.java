package com.equisoft.dca.api.location;

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

import com.equisoft.dca.api.directory.mapper.FileDataMapper;
import com.equisoft.dca.api.location.dto.LocationDto;
import com.equisoft.dca.api.location.dto.LocationTypeDto;
import com.equisoft.dca.api.location.mapper.LocationMapper;
import com.equisoft.dca.backend.exception.EntityAlreadyExistsException;
import com.equisoft.dca.backend.exception.EntityNotFoundException;
import com.equisoft.dca.backend.location.model.Location;
import com.equisoft.dca.backend.location.model.LocationType;
import com.equisoft.dca.backend.location.service.LocationService;

@ExtendWith(MockitoExtension.class)
class LocationResourceTest {

	@Mock
	private LocationService locationService;

	@Mock
	private LocationMapper locationMapper;

	@Mock
	private FileDataMapper fileDataMapper;

	private LocationResource locationResource;

	private Location location;

	private LocationDto locationDto;

	@BeforeEach
	void setUp() {
		locationResource = new LocationResource(locationService, locationMapper, fileDataMapper);
		location = createLocation(1, "Location");
		locationDto = createLocationDto(location);
	}

	@Nested
	class Add {

		@Test
		void givenLocationWithIdentifierDuplicated_whenAdd_thenThrowEntityAlreadyExistsException() {
			//given
			Mockito.when(locationMapper.toEntity(locationDto)).thenReturn(location);
			Class expected = EntityAlreadyExistsException.class;

			Mockito.when(locationService.save(ArgumentMatchers.any(Location.class))).thenThrow(EntityAlreadyExistsException.class);

			//when
			Throwable actual = Assertions.catchThrowable(() -> locationResource.add(locationDto));

			//then
			Assertions.assertThat(actual).isInstanceOf(expected);
		}

		@Test
		void givenValidLocation_whenAdd_thenReturnStatusCodeCreatedAndLocation() {
			//given
			Mockito.when(locationMapper.toEntity(locationDto)).thenReturn(location);
			Mockito.when(locationMapper.toDto(location)).thenReturn(locationDto);
			ResponseEntity expected = ResponseEntity.created(URI.create(LocationResource.PATH + "/" + location.getId())).body(locationDto);

			Mockito.when(locationService.save(ArgumentMatchers.any(Location.class))).thenReturn(location);

			//when
			ResponseEntity<LocationDto> actual = locationResource.add(locationDto);

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
		void givenLocation_whenUpdate_thenReturnStatusCodeOkAndNoResponseBody() {
			//given
			ResponseEntity expected = ResponseEntity.ok().build();

			Mockito.when(locationService.update(ArgumentMatchers.any())).thenReturn(location);

			//when
			ResponseEntity<Void> actual = locationResource.update(locationDto.getId(), locationDto);

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
		void givenValidLocationId_whenDelete_thenReturnStatusCodeNoContentAndNoResponseBody() {
			//given
			ResponseEntity expected = ResponseEntity.noContent().build();

			//when
			ResponseEntity<Void> actual = locationResource.delete(ArgumentMatchers.anyInt());

			//then
			Assertions.assertThat(actual)
					.isNotNull()
					.extracting(ResponseEntity::getStatusCode, ResponseEntity::getBody)
					.containsExactly(expected.getStatusCode(), expected.getBody());
		}

		@Test
		void givenInvalidLocationId_whenDelete_thenThrowEntityNotFoundException() {
			//given
			Class expected = EntityNotFoundException.class;

			Mockito.doThrow(EntityNotFoundException.class).when(locationService).deleteById(ArgumentMatchers.anyInt());

			//when
			Throwable actual = Assertions.catchThrowable(() -> locationResource.delete(ArgumentMatchers.anyInt()));

			//then
			Assertions.assertThat(actual).isInstanceOf(expected);
		}
	}

	@Nested
	class FindById {

		@Test
		void givenValidLocationId_whenFindById_thenReturnStatusCodeOkAndLocation() {
			//given
			ResponseEntity expected = ResponseEntity.ok().body(locationDto);

			Mockito.when(locationMapper.toDto(location)).thenReturn(locationDto);
			Mockito.when(locationService.findById(ArgumentMatchers.anyInt())).thenReturn(Optional.of(location));

			//when
			ResponseEntity<LocationDto> actual = locationResource.findById(ArgumentMatchers.anyInt());

			//then
			Assertions.assertThat(actual)
					.isNotNull()
					.extracting(ResponseEntity::getStatusCode, ResponseEntity::getBody)
					.containsExactly(expected.getStatusCode(), expected.getBody());
		}

		@Test
		void givenInvalidLocationId_whenFindById_thenThrowEntityNotFoundException() {
			//given
			Class expected = EntityNotFoundException.class;

			Mockito.when(locationService.findById(ArgumentMatchers.anyInt())).thenReturn(Optional.empty());

			//when
			Throwable actual = Assertions.catchThrowable(() -> locationResource.findById(ArgumentMatchers.anyInt()));

			//then
			Assertions.assertThat(actual).isInstanceOf(expected);
		}
	}

	@Nested
	class FindAll {

		@Test
		void givenServiceReturnLocationsList_whenFindAll_thenReturnStatusCodeOkAndLocationsList() {
			//given
			List<Location> locations = createLocationList();
			List<LocationDto> locationDtos = createLocationDtoList(locations);

			ResponseEntity expected = ResponseEntity.ok().body(locationDtos);

			Mockito.when(locationMapper.toDtoList(locations)).thenReturn(locationDtos);
			Mockito.when(locationService.findAll()).thenReturn(locations);

			//when
			ResponseEntity<List<LocationDto>> actual = locationResource.findAll();

			//then
			Assertions.assertThat(actual)
					.isNotNull()
					.extracting(ResponseEntity::getStatusCode, ResponseEntity::getBody)
					.containsExactly(expected.getStatusCode(), expected.getBody());
		}

		@Test
		void givenServiceReturnEmptyList_whenFindAll_thenReturnStatusCodeOkAndNoResponseBody() {
			//given
			ResponseEntity expected = ResponseEntity.ok().body(Collections.emptyList());

			Mockito.when(locationService.findAll()).thenReturn(Collections.emptyList());

			//when
			ResponseEntity<List<LocationDto>> actual = locationResource.findAll();

			//then
			Assertions.assertThat(actual)
					.isNotNull()
					.extracting(ResponseEntity::getStatusCode, ResponseEntity::getBody)
					.containsExactly(expected.getStatusCode(), expected.getBody());
		}
	}

	private List<Location> createLocationList() {
		return List.of(createLocation(1, "Location1"),
				createLocation(2, "Location2"),
				createLocation(3, "Location3"));
	}

	private List<LocationDto> createLocationDtoList(List<Location> locations) {
		return locations
				.stream()
				.map(this::createLocationDto)
				.collect(Collectors.toList());
	}

	private Location createLocation(Integer id, String identifier) {
		return Location.builder()
				.id(id)
				.identifier(identifier)
				.locationType(LocationType.NETWORK)
				.path("/test/test")
				.build();
	}

	private LocationDto createLocationDto(Location location) {
		return LocationDto.builder()
				.id(location.getId())
				.identifier(location.getIdentifier())
				.locationType(LocationTypeDto.builder()
						.id(location.getLocationType().getId())
						.name(location.getLocationType().getName())
						.build())
				.path(location.getPath())
				.build();
	}
}
