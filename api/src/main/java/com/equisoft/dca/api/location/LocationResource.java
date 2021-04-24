package com.equisoft.dca.api.location;

import java.net.URI;
import java.util.List;

import javax.inject.Inject;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.equisoft.dca.api.JsonRequestMapping;
import com.equisoft.dca.api.directory.dto.FileDataDto;
import com.equisoft.dca.api.directory.mapper.FileDataMapper;
import com.equisoft.dca.api.location.dto.LocationDto;
import com.equisoft.dca.api.location.mapper.LocationMapper;
import com.equisoft.dca.backend.exception.EntityNotFoundException;
import com.equisoft.dca.backend.location.model.Location;
import com.equisoft.dca.backend.location.service.LocationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@JsonRequestMapping(value = LocationResource.PATH)
@Tag(name = "Location")
public class LocationResource {

	static final String PATH = "/locations";

	private final LocationService locationService;

	private final LocationMapper locationMapper;

	private final FileDataMapper fileDataMapper;

	@Inject
	public LocationResource(LocationService locationService, LocationMapper locationMapper, FileDataMapper fileDataMapper) {
		this.locationService = locationService;
		this.locationMapper = locationMapper;
		this.fileDataMapper = fileDataMapper;
	}

	@JsonRequestMapping(method = RequestMethod.POST)
	@Operation(summary = "Create a location")
	public ResponseEntity<LocationDto> add(@RequestBody LocationDto locationDto) {
		Location location = locationService.save(locationMapper.toEntity(locationDto));
		URI uri = URI.create(String.format(PATH + "/%d", location.getId()));

		return ResponseEntity.created(uri).body(locationMapper.toDto(location));
	}

	@JsonRequestMapping(value = "/{id}", method = RequestMethod.PUT)
	@Operation(summary = "Update a location")
	public ResponseEntity<Void> update(@PathVariable Integer id, @RequestBody LocationDto locationDto) {
		locationDto.setId(id);
		locationService.update(locationMapper.toEntity(locationDto));

		return ResponseEntity.ok().build();
	}

	@JsonRequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@Operation(summary = "Delete a location")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		locationService.deleteById(id);

		return ResponseEntity.noContent().build();
	}

	@JsonRequestMapping(method = RequestMethod.GET)
	@Operation(summary = "Get all locations")
	public ResponseEntity<List<LocationDto>> findAll() {
		return ResponseEntity.ok().body(locationMapper.toDtoList(locationService.findAll()));
	}

	@JsonRequestMapping(value = "/{id}", method = RequestMethod.GET)
	@Operation(summary = "Get a location by identifier")
	public ResponseEntity<LocationDto> findById(@PathVariable Integer id) {
		return locationService.findById(id)
				.map(l -> ResponseEntity.ok(locationMapper.toDto(l)))
				.orElseThrow(() -> new EntityNotFoundException(Location.class, "id", id.toString()));
	}

	@JsonRequestMapping(value = "/{id}/files", method = RequestMethod.GET)
	@Operation(summary = "Get all files from the location")
	public ResponseEntity<List<FileDataDto>> getFiles(@PathVariable Integer id) {
		return ResponseEntity.ok().body(fileDataMapper.toDtoList(locationService.getFiles(id)));
	}
}
