package com.equisoft.dca.api.organization;

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

import com.equisoft.dca.api.organization.dto.OrganizationDto;
import com.equisoft.dca.api.organization.mapper.OrganizationMapper;
import com.equisoft.dca.backend.exception.EntityAlreadyExistsException;
import com.equisoft.dca.backend.exception.EntityNotFoundException;
import com.equisoft.dca.backend.user.model.Organization;
import com.equisoft.dca.backend.user.service.OrganizationService;

@ExtendWith(MockitoExtension.class)
class OrganizationResourceTest {

	@Mock
	private OrganizationService organizationService;

	@Mock
	private OrganizationMapper organizationMapper;

	private OrganizationResource organizationResource;
	private Organization organization;
	private OrganizationDto organizationDto;

	@BeforeEach
	void setUp() {
		this.organizationResource = new OrganizationResource(organizationService, organizationMapper);
		this.organization = createOrganization(1, "Org1");
		this.organizationDto = createOrganizationDto(organization);
	}

	@Nested
	class Add {
		@Test
		void givenOrganizationWithNameDuplicated_whenAddOrganization_thenThrowException() {
			//given
			Class expected = EntityAlreadyExistsException.class;

			Mockito.when(organizationService.save(organization)).thenThrow(EntityAlreadyExistsException.class);
			Mockito.when(organizationMapper.toEntity(organizationDto)).thenReturn(organization);

			//when
			Throwable actual = Assertions.catchThrowable(() -> organizationResource.add(organizationDto));

			//then
			Assertions.assertThat(actual).isInstanceOf(expected);
		}

		@Test
		void givenValidOrganization_whenAddOrganization_thenReturnStatusCodeCreatedAndOrganization() {
			//given
			ResponseEntity expected = ResponseEntity.created(URI.create("/organizations/" + organization.getId())).body(organizationDto);

			Mockito.when(organizationService.save(ArgumentMatchers.any())).thenReturn(organization);
			Mockito.when(organizationMapper.toEntity(organizationDto)).thenReturn(organization);
			Mockito.when(organizationMapper.toDto(organization)).thenReturn(organizationDto);

			//when
			ResponseEntity<OrganizationDto> actual = organizationResource.add(organizationDto);

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
		void givenOrganization_whenUpdateOrganization_thenReturnStatusCodeOkAndNoResponseBody() {
			//given
			ResponseEntity expected = ResponseEntity.ok().build();

			Mockito.when(organizationService.update(ArgumentMatchers.any())).thenReturn(organization);

			//when
			ResponseEntity<Void> actual = organizationResource.update(organizationDto.getId(), organizationDto);

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
		void givenOrganizationId_whenDeleteOrganization_thenReturnStatusCodeNoContentAndNoResponseBody() {
			//given
			ResponseEntity expected = ResponseEntity.noContent().build();

			//when
			ResponseEntity<Void> actual = organizationResource.delete(ArgumentMatchers.anyInt());

			//then
			Assertions.assertThat(actual)
					.isNotNull()
					.extracting(ResponseEntity::getStatusCode, ResponseEntity::getBody)
					.containsExactly(expected.getStatusCode(), expected.getBody());
		}

		@Test
		void givenInvalidOrganizationId_whenDeleteOrganization_thenThrowEntityNotFoundException() {
			//given
			Class expected = EntityNotFoundException.class;

			Mockito.doThrow(EntityNotFoundException.class).when(organizationService).deleteById(ArgumentMatchers.anyInt());

			//when
			Throwable actual = Assertions.catchThrowable(() -> organizationResource.delete(ArgumentMatchers.anyInt()));

			//then
			Assertions.assertThat(actual).isInstanceOf(expected);
		}
	}

	@Nested
	class FindById {

		@Test
		void givenOrganizationId_whenFindOrganizationById_thenReturnStatusCodeOkAndOrganization() {
			//given
			ResponseEntity expected = ResponseEntity.ok().body(organizationDto);

			Mockito.when(organizationService.findById(ArgumentMatchers.anyInt())).thenReturn(Optional.of(organization));
			Mockito.when(organizationMapper.toDto(organization)).thenReturn(organizationDto);

			//when
			ResponseEntity<OrganizationDto> actual = organizationResource.findById(ArgumentMatchers.anyInt());

			//then
			Assertions.assertThat(actual)
					.isNotNull()
					.extracting(ResponseEntity::getStatusCode, ResponseEntity::getBody)
					.containsExactly(expected.getStatusCode(), expected.getBody());
		}

		@Test
		void givenInvalidOrganizationId_whenFindOrganizationById_thenThrowEntityNotFoundException() {
			//given
			Class expected = EntityNotFoundException.class;

			Mockito.when(organizationService.findById(ArgumentMatchers.anyInt())).thenReturn(Optional.empty());

			//when
			Throwable actual = Assertions.catchThrowable(() -> organizationResource.findById(ArgumentMatchers.anyInt()));

			//then
			Assertions.assertThat(actual).isInstanceOf(expected);
		}
	}

	@Nested
	class FindAll {

		@Test
		void givenServiceReturnLstOrganization_whenFindAllOrganizations_thenReturnStatusCodeOkAndOrganizationsList() {
			//given
			List<Organization> organizations = createOrganizationList();
			List<OrganizationDto> organizationDtos = createOrganizationDtoList(organizations);

			ResponseEntity expected = ResponseEntity.ok().body(organizationDtos);

			Mockito.when(organizationService.findAll()).thenReturn(organizations);
			Mockito.when(organizationMapper.toDtoList(organizations)).thenReturn(organizationDtos);

			//when
			ResponseEntity<List<OrganizationDto>> actual = organizationResource.findAll();

			//then
			Assertions.assertThat(actual)
					.isNotNull()
					.extracting(ResponseEntity::getStatusCode, ResponseEntity::getBody)
					.containsExactly(expected.getStatusCode(), expected.getBody());
		}

		@Test
		void givenServiceReturnEmptyList_whenFindAllOrganizations_thenReturnStatusCodeOkAndNoResponseBody() {
			//given
			ResponseEntity expected = ResponseEntity.ok().body(Collections.emptyList());

			Mockito.when(organizationService.findAll()).thenReturn(Collections.emptyList());

			//when
			ResponseEntity<List<OrganizationDto>> actual = organizationResource.findAll();

			//then
			Assertions.assertThat(actual)
					.isNotNull()
					.extracting(ResponseEntity::getStatusCode, ResponseEntity::getBody)
					.containsExactly(expected.getStatusCode(), expected.getBody());
		}
	}

	private List<Organization> createOrganizationList() {
		return List.of(createOrganization(1, "Org1"),
				createOrganization(2, "Org2"),
				createOrganization(3, "Org3"));
	}

	private List<OrganizationDto> createOrganizationDtoList(List<Organization> organizations) {
		return organizations
				.stream()
				.map(this::createOrganizationDto)
				.collect(Collectors.toList());
	}

	private Organization createOrganization(Integer id, String name) {
		return Organization.builder()
				.id(id)
				.name(name)
				.description("description")
				.build();
	}

	private OrganizationDto createOrganizationDto(Organization organization) {
		return OrganizationDto.builder()
				.id(organization.getId())
				.name(organization.getName())
				.description(organization.getDescription())
				.build();
	}
}
