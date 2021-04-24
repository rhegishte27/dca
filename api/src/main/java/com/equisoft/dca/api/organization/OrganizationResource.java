package com.equisoft.dca.api.organization;

import java.net.URI;
import java.util.List;

import javax.inject.Inject;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.equisoft.dca.api.JsonRequestMapping;
import com.equisoft.dca.api.organization.dto.OrganizationDto;
import com.equisoft.dca.api.organization.mapper.OrganizationMapper;
import com.equisoft.dca.backend.exception.EntityNotFoundException;
import com.equisoft.dca.backend.user.model.Organization;
import com.equisoft.dca.backend.user.service.OrganizationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@JsonRequestMapping(value = OrganizationResource.PATH)
@Tag(name = "Organization")
public class OrganizationResource {

	static final String PATH = "/organizations";

	private final OrganizationService organizationService;

	private final OrganizationMapper organizationMapper;

	@Inject
	public OrganizationResource(OrganizationService organizationService, OrganizationMapper organizationMapper) {
		this.organizationService = organizationService;
		this.organizationMapper = organizationMapper;
	}

	@JsonRequestMapping(method = RequestMethod.POST)
	@Operation(summary = "Create an organization")
	public ResponseEntity<OrganizationDto> add(@RequestBody OrganizationDto organizationDto) {
		Organization organization = organizationService.save(organizationMapper.toEntity(organizationDto));
		URI location = URI.create(String.format(PATH + "/%d", organization.getId()));

		return ResponseEntity.created(location).body(organizationMapper.toDto(organization));
	}

	@JsonRequestMapping(value = "/{id}", method = RequestMethod.PUT)
	@Operation(summary = "Update an organization")
	public ResponseEntity<Void> update(@PathVariable Integer id, @RequestBody OrganizationDto organizationDto) {
		organizationDto.setId(id);
		organizationService.update(organizationMapper.toEntity(organizationDto));

		return ResponseEntity.ok().build();
	}

	@JsonRequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@Operation(summary = "Delete an organization")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		organizationService.deleteById(id);

		return ResponseEntity.noContent().build();
	}

	@JsonRequestMapping(method = RequestMethod.GET)
	@Operation(summary = "Get all organizations")
	public ResponseEntity<List<OrganizationDto>> findAll() {
		return ResponseEntity.ok().body(organizationMapper.toDtoList(organizationService.findAll()));
	}

	@JsonRequestMapping(value = "/{id}", method = RequestMethod.GET)
	@Operation(summary = "Get an organization by identifier")
	public ResponseEntity<OrganizationDto> findById(@PathVariable Integer id) {
		return organizationService.findById(id)
				.map(o -> ResponseEntity.ok(organizationMapper.toDto(o)))
				.orElseThrow(() -> new EntityNotFoundException(Organization.class, "id", id.toString()));
	}
}
