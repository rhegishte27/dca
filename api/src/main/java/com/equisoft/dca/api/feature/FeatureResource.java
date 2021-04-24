package com.equisoft.dca.api.feature;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.equisoft.dca.api.JsonRequestMapping;
import com.equisoft.dca.api.feature.dto.FeatureDto;
import com.equisoft.dca.api.feature.mapper.FeatureMapper;
import com.equisoft.dca.backend.user.model.Feature;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@JsonRequestMapping(value = FeatureResource.PATH)
@Tag(name = "Feature")
public class FeatureResource {

	static final String PATH = "/features";

	private final FeatureMapper mapper;

	@Inject
	public FeatureResource(FeatureMapper mapper) {
		this.mapper = mapper;
	}

	@JsonRequestMapping(method = RequestMethod.GET)
	@Operation(summary = "Get all features")
	public ResponseEntity<List<FeatureDto>> findAll() {
		return ResponseEntity.ok().body(mapper.toDtoList(Arrays.asList(Feature.values())));
	}
}
