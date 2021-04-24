package com.equisoft.dca.api.dataobject;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.equisoft.dca.api.JsonRequestMapping;
import com.equisoft.dca.api.dataobject.dto.DataObjectStatusDto;
import com.equisoft.dca.api.dataobject.mapper.DataObjectStatusResponseDetailedMapper;
import com.equisoft.dca.backend.dataobject.model.DataObjectStatus;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@JsonRequestMapping(value = "/dataobjectstatuses")
@Tag(name = "Data object statuses")
public class DataObjectStatusResource {

	private final DataObjectStatusResponseDetailedMapper mapper;

	@Inject
	public DataObjectStatusResource(DataObjectStatusResponseDetailedMapper mapper) {
		this.mapper = mapper;
	}

	@JsonRequestMapping(method = RequestMethod.GET)
	@Operation(summary = "Get all data object statuses")
	public ResponseEntity<List<DataObjectStatusDto.Response.Detailed>> findAll() {
		return ResponseEntity.ok(mapper.toDto(Arrays.asList(DataObjectStatus.values())));
	}
}
