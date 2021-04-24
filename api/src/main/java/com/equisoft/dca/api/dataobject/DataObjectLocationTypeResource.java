package com.equisoft.dca.api.dataobject;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.equisoft.dca.api.JsonRequestMapping;
import com.equisoft.dca.api.dataobject.dto.DataObjectLocationTypeDto;
import com.equisoft.dca.api.dataobject.mapper.DataObjectLocationTypeMapper;
import com.equisoft.dca.backend.dataobject.model.DataObjectLocationType;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@JsonRequestMapping(value = "/dataobjectlocationtypes")
@Tag(name = "Data object types")
public class DataObjectLocationTypeResource {

	private final DataObjectLocationTypeMapper mapper;

	@Inject
	public DataObjectLocationTypeResource(DataObjectLocationTypeMapper mapper) {
		this.mapper = mapper;
	}

	@JsonRequestMapping(method = RequestMethod.GET)
	@Operation(summary = "Get all data object location types")
	public ResponseEntity<List<DataObjectLocationTypeDto>> findAll() {
		return ResponseEntity.ok(mapper.toDtoList(Arrays.asList(DataObjectLocationType.values())));
	}
}
