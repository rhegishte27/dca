package com.equisoft.dca.api.project;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.equisoft.dca.api.JsonRequestMapping;
import com.equisoft.dca.api.project.dto.SystemTypeDto;
import com.equisoft.dca.api.project.mapper.SystemTypeMapper;
import com.equisoft.dca.backend.project.model.SystemType;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@JsonRequestMapping(value = SystemTypeResource.PATH)
@Tag(name = "System type")
public class SystemTypeResource {

	static final String PATH = "/systemtypes";

	private final SystemTypeMapper systemTypeMapper;

	@Inject
	public SystemTypeResource(SystemTypeMapper systemTypeMapper) {
		this.systemTypeMapper = systemTypeMapper;
	}

	@JsonRequestMapping(method = RequestMethod.GET)
	@Operation(summary = "Get all system types")
	public ResponseEntity<List<SystemTypeDto>> findAll() {
		return ResponseEntity.ok(systemTypeMapper.toDtoList(Arrays.asList(SystemType.values())));
	}
}
