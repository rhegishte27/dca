package com.equisoft.dca.api.project;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.equisoft.dca.api.JsonRequestMapping;
import com.equisoft.dca.api.project.dto.CompilerDto;
import com.equisoft.dca.api.project.mapper.CompilerMapper;
import com.equisoft.dca.backend.project.model.Compiler;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@JsonRequestMapping(value = "/compilers")
@Tag(name = "Compiler")
public class CompilerResource {

	private final CompilerMapper mapper;

	@Inject
	public CompilerResource(CompilerMapper mapper) {
		this.mapper = mapper;
	}

	@JsonRequestMapping(method = RequestMethod.GET)
	@Operation(summary = "Get all compilers")
	public ResponseEntity<List<CompilerDto>> findAll() {
		return ResponseEntity.ok(mapper.toDtoList(Arrays.asList(Compiler.values())));
	}
}
