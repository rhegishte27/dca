package com.equisoft.dca.api.project;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.equisoft.dca.api.JsonRequestMapping;
import com.equisoft.dca.api.project.dto.GeneratedCodeLanguageDto;
import com.equisoft.dca.api.project.mapper.GeneratedCodeLanguageMapper;
import com.equisoft.dca.backend.project.model.GeneratedCodeLanguage;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@JsonRequestMapping(value = "/generatedcodelanguages")
@Tag(name = "Generated code language")
public class GeneratedCodeLanguageResource {

	private final GeneratedCodeLanguageMapper mapper;

	@Inject
	public GeneratedCodeLanguageResource(GeneratedCodeLanguageMapper mapper) {
		this.mapper = mapper;
	}

	@JsonRequestMapping(method = RequestMethod.GET)
	@Operation(summary = "Get all generated code languages")
	public ResponseEntity<List<GeneratedCodeLanguageDto>> findAll() {
		return ResponseEntity.ok(mapper.toDtoList(Arrays.asList(GeneratedCodeLanguage.values())));
	}
}
