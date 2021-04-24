package com.equisoft.dca.api.dataobject;

import javax.inject.Inject;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.equisoft.dca.api.JsonRequestMapping;
import com.equisoft.dca.api.dataobject.dto.DataObjectFileDto;
import com.equisoft.dca.api.dataobject.mapper.DataObjectFileMapper;
import com.equisoft.dca.backend.dataobject.model.DataObject;
import com.equisoft.dca.backend.dataobject.service.DataObjectFileService;
import com.equisoft.dca.backend.exception.EntityNotFoundException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(value = DataObjectFileResource.PATH)
@Tag(name = "Data object")
public class DataObjectFileResource {
	static final String PATH = "/dataobjectfiles";

	private final DataObjectFileService service;

	private final DataObjectFileMapper mapper;

	@Inject
	public DataObjectFileResource(DataObjectFileService service, DataObjectFileMapper mapper) {
		this.service = service;
		this.mapper = mapper;
	}

	@JsonRequestMapping(value = "/{dataObjectId}", method = RequestMethod.GET)
	@Operation(summary = "Get a latest data object result by data object id")
	public ResponseEntity<DataObjectFileDto> findLatestResultByDataObjectId(@PathVariable Integer dataObjectId) {
		return service.findLatestResultByDataObjectId(dataObjectId)
				.map(o -> ResponseEntity.ok(mapper.toDto(o)))
				.orElseThrow(() -> new EntityNotFoundException(DataObject.class, "id", dataObjectId.toString()));
	}
}
