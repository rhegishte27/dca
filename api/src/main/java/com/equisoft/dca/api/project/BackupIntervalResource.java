package com.equisoft.dca.api.project;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.equisoft.dca.api.JsonRequestMapping;
import com.equisoft.dca.api.project.dto.BackupIntervalDto;
import com.equisoft.dca.api.project.mapper.BackupIntervalMapper;
import com.equisoft.dca.backend.project.model.BackupInterval;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@JsonRequestMapping(value = "/backupintervals")
@Tag(name = "Backup interval")
public class BackupIntervalResource {

	private final BackupIntervalMapper mapper;

	@Inject
	public BackupIntervalResource(BackupIntervalMapper mapper) {
		this.mapper = mapper;
	}

	@JsonRequestMapping(method = RequestMethod.GET)
	@Operation(summary = "Get all backup intervals")
	public ResponseEntity<List<BackupIntervalDto>> findAll() {
		return ResponseEntity.ok(mapper.toDtoList(Arrays.asList(BackupInterval.values())));
	}
}
