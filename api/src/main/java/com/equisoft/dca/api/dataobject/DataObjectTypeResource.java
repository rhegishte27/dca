package com.equisoft.dca.api.dataobject;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.equisoft.dca.api.JsonRequestMapping;
import com.equisoft.dca.api.dataobject.dto.DataObjectTypeDto;
import com.equisoft.dca.api.dataobject.mapper.DataObjectTypeMapper;
import com.equisoft.dca.backend.dataobject.model.DataObjectType;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@JsonRequestMapping(value = "/dataobjecttypes")
@Tag(name = "Data object types")
public class DataObjectTypeResource {

	private final DataObjectTypeMapper mapper;

	@Inject
	public DataObjectTypeResource(DataObjectTypeMapper mapper) {
		this.mapper = mapper;
	}

	@JsonRequestMapping(method = RequestMethod.GET)
	@Operation(summary = "Get all data object types")
	public ResponseEntity<List<DataObjectTypeDto>> findAll() {
		return ResponseEntity.ok(mapper.toDtoList(Arrays.asList(DataObjectType.values())));
	}
}
