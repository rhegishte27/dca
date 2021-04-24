package com.equisoft.dca.api.configuration;

import javax.inject.Inject;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.equisoft.dca.api.JsonRequestMapping;
import com.equisoft.dca.api.configuration.dto.ConfigurationDto;
import com.equisoft.dca.api.configuration.mapper.ConfigurationMapper;
import com.equisoft.dca.backend.configuration.service.ConfigurationService;
import com.equisoft.dca.infra.security.context.AuthenticationFacade;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@JsonRequestMapping(value = "/configurations")
@Tag(name = "Configuration")
public class ConfigurationResource {

	private final ConfigurationService configurationService;

	private final AuthenticationFacade authenticationFacade;

	private final ConfigurationMapper configurationMapper;

	@Inject
	public ConfigurationResource(ConfigurationService configurationService, AuthenticationFacade authenticationFacade,
			ConfigurationMapper configurationMapper) {
		this.configurationService = configurationService;
		this.authenticationFacade = authenticationFacade;
		this.configurationMapper = configurationMapper;
	}

	@JsonRequestMapping(method = RequestMethod.GET)
	@Operation(summary = "Get configuration")
	public ResponseEntity<ConfigurationDto> get() {
		String userIdentifier = authenticationFacade.getUserIdentifier().orElse(null);
		return ResponseEntity.ok().body(configurationMapper.toDto(configurationService.get(userIdentifier)));
	}
}
