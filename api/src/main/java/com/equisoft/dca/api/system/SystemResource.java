package com.equisoft.dca.api.system;

import java.net.URI;
import java.util.List;

import javax.inject.Inject;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.equisoft.dca.api.JsonRequestMapping;
import com.equisoft.dca.api.system.dto.SystemDto;
import com.equisoft.dca.api.system.mapper.SystemMapper;
import com.equisoft.dca.backend.exception.EntityNotFoundException;
import com.equisoft.dca.backend.system.model.System;
import com.equisoft.dca.backend.system.service.SystemService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@JsonRequestMapping(value = SystemResource.PATH)
@Tag(name = "System")
public class SystemResource {

	static final String PATH = "/systems";

	private final SystemService service;

	private final SystemMapper systemMapper;

	@Inject
	public SystemResource(SystemService service, SystemMapper systemMapper) {
		this.service = service;
		this.systemMapper = systemMapper;
	}

	@JsonRequestMapping(method = RequestMethod.POST)
	@Operation(summary = "Create a system")
	public ResponseEntity<SystemDto> add(@RequestBody SystemDto systemDto) {
		System system = service.save(systemMapper.toEntity(systemDto));

		URI location = URI.create(String.format(PATH + "/%d", system.getId()));
		return ResponseEntity.created(location).body(systemMapper.toDto(system));
	}

	@JsonRequestMapping(value = "/{id}", method = RequestMethod.PUT)
	@Operation(summary = "Update a system")
	public ResponseEntity<Void> update(@PathVariable Integer id, @RequestBody SystemDto systemDto) {
		systemDto.setId(id);
		service.update(systemMapper.toEntity(systemDto));

		return ResponseEntity.ok().build();
	}

	@JsonRequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@Operation(summary = "Delete a system")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		service.deleteById(id);

		return ResponseEntity.noContent().build();
	}

	@JsonRequestMapping(method = RequestMethod.GET)
	@Operation(summary = "Get all systems")
	public ResponseEntity<List<SystemDto>> findAll() {
		return ResponseEntity.ok().body(systemMapper.toDtoList(service.findAll()));
	}

	@JsonRequestMapping(value = "/{id}", method = RequestMethod.GET)
	@Operation(summary = "Get a system by identifier")
	public ResponseEntity<SystemDto> findById(@PathVariable Integer id) {
		return service.findById(id)
				.map(o -> ResponseEntity.ok(systemMapper.toDto(o)))
				.orElseThrow(() -> new EntityNotFoundException(System.class, "id", id.toString()));
	}

}
