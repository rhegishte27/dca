package com.equisoft.dca.api.location;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.equisoft.dca.api.JsonRequestMapping;
import com.equisoft.dca.api.location.dto.LocationTypeDto;
import com.equisoft.dca.api.location.mapper.LocationTypeMapper;
import com.equisoft.dca.backend.location.model.LocationType;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@JsonRequestMapping(value = LocationTypeResource.PATH)
@Tag(name = "Location type")
public class LocationTypeResource {

	static final String PATH = "/locationtypes";

	private final LocationTypeMapper locationTypeMapper;

	@Inject
	public LocationTypeResource(LocationTypeMapper locationTypeMapper) {
		this.locationTypeMapper = locationTypeMapper;
	}

	@JsonRequestMapping(method = RequestMethod.GET)
	@Operation(summary = "Get all location types")
	public ResponseEntity<List<LocationTypeDto>> findAll() {
		return ResponseEntity.ok(locationTypeMapper.toDtoList(Arrays.asList(LocationType.values())));
	}
}
