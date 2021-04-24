package com.equisoft.dca.api.dataobject;

import java.util.List;

import javax.inject.Inject;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.equisoft.dca.api.JsonRequestMapping;
import com.equisoft.dca.api.dataobject.dto.DataObjectContainerDto;
import com.equisoft.dca.api.dataobject.dto.DataObjectDto;
import com.equisoft.dca.api.dataobject.dto.DataObjectFileDto;
import com.equisoft.dca.api.dataobject.mapper.DataObjectFileMapper;
import com.equisoft.dca.api.dataobject.mapper.DataObjectMapper;
import com.equisoft.dca.api.dataobject.mapper.DataObjectTypeMapper;
import com.equisoft.dca.api.location.mapper.LocationMapper;
import com.equisoft.dca.api.system.mapper.SystemMapper;
import com.equisoft.dca.backend.dataobject.model.DataObject;
import com.equisoft.dca.backend.dataobject.service.DataObjectService;
import com.equisoft.dca.backend.exception.EntityNotFoundException;
import com.equisoft.dca.infra.security.context.AuthenticationFacade;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(value = DataObjectResource.PATH)
@Tag(name = "Data object")
public class DataObjectResource {

	static final String PATH = "/dataobjects";

	private final DataObjectService service;

	private final DataObjectMapper mapper;

	private final LocationMapper locationMapper;

	private final SystemMapper systemMapper;

	private final DataObjectFileMapper dataObjectFileMapper;

	private final DataObjectTypeMapper dataObjectTypeMapper;

	private final AuthenticationFacade authenticationFacade;

	@Inject
	public DataObjectResource(DataObjectService service, DataObjectMapper mapper, LocationMapper locationMapper, SystemMapper systemMapper,
			DataObjectFileMapper dataObjectFileMapper, DataObjectTypeMapper dataObjectTypeMapper,
			AuthenticationFacade authenticationFacade) {
		this.service = service;
		this.mapper = mapper;
		this.locationMapper = locationMapper;
		this.dataObjectFileMapper = dataObjectFileMapper;
		this.systemMapper = systemMapper;
		this.dataObjectTypeMapper = dataObjectTypeMapper;
		this.authenticationFacade = authenticationFacade;
	}

	@JsonRequestMapping(value = "/dataobjectstocreate", method = RequestMethod.POST)
	@Operation(summary = "Create multiple data objects")
	public ResponseEntity<List<DataObjectDto>> create(@RequestBody DataObjectContainerDto containerDto) {
		return ResponseEntity.ok().body(mapper.toDtoList(service.create(systemMapper.toEntity(containerDto.getSystem()),
				dataObjectTypeMapper.toEntity(containerDto.getType()),
				dataObjectFileMapper.toEntityList(containerDto.getDataObjectFileListChosenInCreateForm()))));
	}

	@JsonRequestMapping(value = "/dataobjectstoimport", method = RequestMethod.POST)
	@Operation(summary = "Import multiple data objects")
	public ResponseEntity<List<DataObjectDto>> importDataObjects(@RequestBody List<DataObjectDto> dataObjectDtos) {
		return ResponseEntity.ok().body(mapper.toDtoList(service.save(getUserIdentifier(), mapper.toEntityList(dataObjectDtos))));
	}

	@JsonRequestMapping(value = "/validatedataobjects", method = RequestMethod.POST)
	@Operation(summary = "Validate data objects")
	public ResponseEntity<List<DataObjectFileDto>> validate(@RequestBody DataObjectContainerDto containerDto) {
		return ResponseEntity.ok().body(dataObjectFileMapper.toDtoList(
				service.validateDataObjects(dataObjectTypeMapper.toEntity(containerDto.getType()), locationMapper.toEntity(containerDto.getLocation()),
						dataObjectFileMapper.toEntityList(containerDto.getDataObjectFileList()))));
	}

	/*
	@JsonRequestMapping(value = "/{id}", method = RequestMethod.PUT)
	@Operation(summary = "Update a data object")
	public ResponseEntity<Void> update(@PathVariable Integer id, @RequestBody DataObjectDto dataObjectDto) {
		dataObjectDto.setId(id);
		dataObjectDto.setUserIdentifier(getUserIdentifier());
		service.update(mapper.toEntity(dataObjectDto));

		return ResponseEntity.ok().build();
	}
	*/

	@JsonRequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@Operation(summary = "Delete a data object")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		service.deleteById(id);

		return ResponseEntity.noContent().build();
	}

	@JsonRequestMapping(value = "/{id}", method = RequestMethod.GET)
	@Operation(summary = "Get a data object by id")
	public ResponseEntity<DataObjectDto> findById(@PathVariable Integer id) {
		return service.findById(id)
				.map(o -> ResponseEntity.ok(mapper.toDto(o)))
				.orElseThrow(() -> new EntityNotFoundException(DataObject.class, "id", id.toString()));
	}

	@JsonRequestMapping(method = RequestMethod.GET)
	@Operation(summary = "Get all data objects")
	public ResponseEntity<List<DataObjectDto>> findAll() {
		return ResponseEntity.ok().body(mapper.toDtoList(service.findAll()));
	}

	private String getUserIdentifier() {
		return authenticationFacade.getUserIdentifier()
				.orElseThrow(() -> new AccessDeniedException("User must be authenticated to use this feature"));
	}
}
