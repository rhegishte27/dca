package com.equisoft.dca.api.role;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.equisoft.dca.api.JsonRequestMapping;
import com.equisoft.dca.api.role.dto.RoleDto;
import com.equisoft.dca.api.role.mapper.RoleMapper;
import com.equisoft.dca.backend.user.model.Role;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@JsonRequestMapping(value = RoleResource.PATH)
@Tag(name = "Roles")
public class RoleResource {

	static final String PATH = "/roles";

	private final RoleMapper mapper;

	@Inject
	public RoleResource(RoleMapper mapper) {
		this.mapper = mapper;
	}

	@JsonRequestMapping(method = RequestMethod.GET)
	@Operation(summary = "Get all roles")
	public ResponseEntity<List<RoleDto>> findAll() {
		return ResponseEntity.ok().body(mapper.toDtoList(Arrays.asList(Role.values())));
	}
}
