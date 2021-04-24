package com.equisoft.dca.api.location;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.equisoft.dca.api.JsonRequestMapping;
import com.equisoft.dca.api.location.dto.PlatformTypeDto;
import com.equisoft.dca.api.location.mapper.PlatformTypeMapper;
import com.equisoft.dca.backend.location.model.PlatformType;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@JsonRequestMapping(value = PlatformTypeResource.PATH)
@Tag(name = "Platform type")
public class PlatformTypeResource {

	static final String PATH = "/platformtypes";

	private final PlatformTypeMapper platformTypeMapper;

	@Inject
	public PlatformTypeResource(PlatformTypeMapper platformTypeMapper) {
		this.platformTypeMapper = platformTypeMapper;
	}

	@JsonRequestMapping(method = RequestMethod.GET)
	@Operation(summary = "Get all platform types")
	public ResponseEntity<List<PlatformTypeDto>> findAll() {
		return ResponseEntity.ok(platformTypeMapper.toDtoList(Arrays.asList(PlatformType.values())));
	}
}
