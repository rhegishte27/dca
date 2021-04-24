package com.equisoft.dca.api.project;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.equisoft.dca.api.JsonRequestMapping;
import com.equisoft.dca.api.project.dto.BackupKeepIntervalDto;
import com.equisoft.dca.api.project.mapper.BackupKeepIntervalMapper;
import com.equisoft.dca.backend.project.model.BackupKeepInterval;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@JsonRequestMapping(value = "/backupkeepintervals")
@Tag(name = "Backup keep interval")
public class BackupKeepIntervalResource {

	private final BackupKeepIntervalMapper mapper;

	@Inject
	public BackupKeepIntervalResource(BackupKeepIntervalMapper mapper) {
		this.mapper = mapper;
	}

	@JsonRequestMapping(method = RequestMethod.GET)
	@Operation(summary = "Get all backup keep intervals")
	public ResponseEntity<List<BackupKeepIntervalDto>> findAll() {
		return ResponseEntity.ok(mapper.toDtoList(Arrays.asList(BackupKeepInterval.values())));
	}
}
