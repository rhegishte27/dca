package com.equisoft.dca.api.project;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.equisoft.dca.api.JsonRequestMapping;
import com.equisoft.dca.api.project.dto.TypeProjectElementDto;
import com.equisoft.dca.api.project.mapper.TypeProjectElementMapper;
import com.equisoft.dca.backend.project.model.TypeProjectElement;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@JsonRequestMapping(value = TypeProjectElementResource.PATH)
@Tag(name = "TypeProjectElement")
public class TypeProjectElementResource {

	static final String PATH = "/typeprojectelements";

	private final TypeProjectElementMapper mapper;

	@Inject
	public TypeProjectElementResource(TypeProjectElementMapper mapper) {
		this.mapper = mapper;
	}

	@JsonRequestMapping(method = RequestMethod.GET)
	@Operation(summary = "Get all typeProjectElements")
	public ResponseEntity<List<TypeProjectElementDto>> findAll() {
		return ResponseEntity.ok(mapper.toDtoList(Arrays.asList(TypeProjectElement.values())));
	}
}
