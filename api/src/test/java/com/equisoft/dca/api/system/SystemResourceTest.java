package com.equisoft.dca.api.system;

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

import com.equisoft.dca.api.system.dto.SystemDto;
import com.equisoft.dca.api.system.mapper.SystemMapper;
import com.equisoft.dca.backend.exception.EntityAlreadyExistsException;
import com.equisoft.dca.backend.exception.EntityNotFoundException;
import com.equisoft.dca.backend.system.model.System;
import com.equisoft.dca.backend.system.service.SystemService;

@ExtendWith(MockitoExtension.class)
class SystemResourceTest {

	@Mock
	private SystemService service;
	@Mock
	private SystemMapper systemMapper;

	private SystemResource systemResource;
	private System system;
	private SystemDto systemDto;
	private List<System> systems;
	private List<SystemDto> systemDtos;

	@BeforeEach
	void setUp() {
		this.systemResource = new SystemResource(service, systemMapper);
		this.system = createSystem(1, "identifier", "description");
		this.systemDto = createSystemDto(system);
		this.systems = createSystemList();
		this.systemDtos = createSystemDtoList(systems);
	}

	@Nested
	class Add {
		@Test
		void givenSystemWithDuplicateIdentifier_whenAddSystem_thenThrowEntityAlreadyExistsException() {
			//given
			Class expected = EntityAlreadyExistsException.class;

			Mockito.when(systemMapper.toEntity(systemDto)).thenReturn(system);
			Mockito.when(service.save(ArgumentMatchers.any(System.class))).thenThrow(EntityAlreadyExistsException.class);

			//when
			Throwable actual = Assertions.catchThrowable(() -> systemResource.add(systemDto));

			//then
			Assertions.assertThat(actual).isInstanceOf(expected);
		}

		@Test
		void givenValidSystem_whenAddSystem_thenReturnStatusCodeCreatedAndSystem() {
			//given
			ResponseEntity expected = ResponseEntity.created(URI.create("/systems/" + system.getId())).body(systemDto);

			Mockito.when(systemMapper.toEntity(systemDto)).thenReturn(system);
			Mockito.when(service.save(ArgumentMatchers.any(System.class))).thenReturn(system);
			Mockito.when(systemMapper.toDto(system)).thenReturn(systemDto);

			//when
			ResponseEntity<SystemDto> actual = systemResource.add(systemDto);

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
		void givenNonExistentSystem_whenUpdateSystem_thenThrowEntityNotFoundException() {
			//given
			Class expected = EntityNotFoundException.class;

			Mockito.when(systemMapper.toEntity(systemDto)).thenReturn(system);
			Mockito.when(service.update(ArgumentMatchers.any(System.class))).thenThrow(EntityNotFoundException.class);

			//when
			Throwable actual = Assertions.catchThrowable(() -> systemResource.update(systemDto.getId(), systemDto));

			//then
			Assertions.assertThat(actual).isInstanceOf(expected);
		}

		@Test
		void givenValidSystem_whenUpdateSystem_thenReturnStatusCodeOkAndBodyNull() {
			//given
			ResponseEntity expected = ResponseEntity.ok().build();

			Mockito.when(systemMapper.toEntity(systemDto)).thenReturn(system);
			Mockito.when(service.update(ArgumentMatchers.any(System.class))).thenReturn(system);

			//when
			ResponseEntity<Void> actual = systemResource.update(systemDto.getId(), systemDto);

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
		void givenExistingSystemId_whenDeleteSystem_thenReturnStatusCodeNoContentAndBodyNull() {
			//given
			ResponseEntity expected = ResponseEntity.noContent().build();

			//when
			ResponseEntity<Void> actual = systemResource.delete(ArgumentMatchers.anyInt());

			//then
			Assertions.assertThat(actual)
					.isNotNull()
					.extracting(ResponseEntity::getStatusCode, ResponseEntity::getBody)
					.containsExactly(expected.getStatusCode(), expected.getBody());
		}

		@Test
		void givenNonExistentSystemId_whenDeleteSystem_thenThrowEntityNotFoundException() {
			//given
			Class expected = EntityNotFoundException.class;

			Mockito.doThrow(EntityNotFoundException.class).when(service).deleteById(ArgumentMatchers.anyInt());

			//when
			Throwable actual = Assertions.catchThrowable(() -> systemResource.delete(ArgumentMatchers.anyInt()));

			//then
			Assertions.assertThat(actual).isInstanceOf(expected);
		}
	}

	@Nested
	class FindById {

		@Test
		void givenExistingSystemId_whenFindSystemById_thenReturnStatusCodeOkAndBodyWithSystem() {
			//given
			ResponseEntity expected = ResponseEntity.ok().body(systemDto);

			Mockito.when(service.findById(ArgumentMatchers.anyInt())).thenReturn(Optional.of(system));
			Mockito.when(systemMapper.toDto(system)).thenReturn(systemDto);

			//when
			ResponseEntity<SystemDto> actual = systemResource.findById(ArgumentMatchers.anyInt());

			//then
			Assertions.assertThat(actual)
					.isNotNull()
					.extracting(ResponseEntity::getStatusCode, ResponseEntity::getBody)
					.containsExactly(expected.getStatusCode(), expected.getBody());
		}

		@Test
		void givenNonExistentSystemId_whenFindSystemById_thenThrowEntityNotFoundException() {
			//given
			Class expected = EntityNotFoundException.class;

			Mockito.when(service.findById(ArgumentMatchers.anyInt())).thenReturn(Optional.empty());

			//when
			Throwable actual = Assertions.catchThrowable(() -> systemResource.findById(ArgumentMatchers.anyInt()));

			//then
			Assertions.assertThat(actual).isInstanceOf(expected);
		}
	}

	@Nested
	class FindAll {

		@Test
		void givenExistingSystems_whenFindAllSystems_thenReturnStatusCodeOkAndBodySystemList() {
			//given
			ResponseEntity expected = ResponseEntity.ok().body(systemDtos);

			Mockito.when(service.findAll()).thenReturn(systems);
			Mockito.when(systemMapper.toDtoList(systems)).thenReturn(systemDtos);

			//when
			ResponseEntity<List<SystemDto>> actual = systemResource.findAll();

			//then
			Assertions.assertThat(actual)
					.isNotNull()
					.extracting(ResponseEntity::getStatusCode, ResponseEntity::getBody)
					.containsExactly(expected.getStatusCode(), expected.getBody());
		}

		@Test
		void givenNoSystems_whenFindAllSystems_thenReturnStatusCodeOkAndBodyEmpty() {
			//given
			ResponseEntity expected = ResponseEntity.ok().body(Collections.emptyList());

			Mockito.when(service.findAll()).thenReturn(Collections.emptyList());

			//when
			ResponseEntity<List<SystemDto>> actual = systemResource.findAll();

			//then
			Assertions.assertThat(actual)
					.isNotNull()
					.extracting(ResponseEntity::getStatusCode, ResponseEntity::getBody)
					.containsExactly(expected.getStatusCode(), expected.getBody());
		}
	}

	private System createSystem(Integer id, String identifier, String description) {
		return System.builder().id(id).identifier(identifier).description(description).build();
	}

	private SystemDto createSystemDto(System system) {
		return SystemDto.builder()
				.id(system.getId())
				.identifier(system.getIdentifier())
				.description(system.getDescription())
				.build();
	}

	private List<System> createSystemList() {
		return List.of(createSystem(1, "identi1", "description 1"),
				createSystem(2, "identi2", "description 2"),
				createSystem(3, "identi3", "description 3"));
	}

	private List<SystemDto> createSystemDtoList(List<System> systems) {
		return systems
				.stream()
				.map(this::createSystemDto)
				.collect(Collectors.toList());
	}
}
