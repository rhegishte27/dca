package com.equisoft.dca.api.auth;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.equisoft.dca.api.JsonRequestMapping;
import com.equisoft.dca.api.auth.dto.LoginDto;
import com.equisoft.dca.api.configuration.dto.ConfigurationDto;
import com.equisoft.dca.api.configuration.mapper.ConfigurationMapper;
import com.equisoft.dca.backend.authentication.service.AuthenticationService;
import com.equisoft.dca.backend.configuration.service.ConfigurationService;
import com.equisoft.dca.infra.authentication.LoginHandler;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Login")
public class AuthResource {
	private final AuthenticationService authenticationService;

	private final ConfigurationService configurationService;

	private final LoginHandler loginHandler;

	private final LogoutHandler logoutHandler;

	private final ConfigurationMapper configurationMapper;

	@Inject
	public AuthResource(AuthenticationService authenticationService, ConfigurationService configurationService, LoginHandler loginHandler,
			LogoutHandler logoutHandler, ConfigurationMapper configurationMapper) {
		this.authenticationService = authenticationService;
		this.configurationService = configurationService;
		this.loginHandler = loginHandler;
		this.logoutHandler = logoutHandler;
		this.configurationMapper = configurationMapper;
	}

	@JsonRequestMapping(method = RequestMethod.POST, value = "/login")
	@Operation(summary = "Login using a username / password")
	public ResponseEntity<ConfigurationDto> login(HttpServletRequest request, @RequestBody @Validated LoginDto loginDto, HttpServletResponse response) {
		String identifier = loginDto.getIdentifier();
		Authentication authentication =
				authenticationService.authenticate(identifier, loginDto.getPassword());

		loginHandler.onAuthenticationSuccess(request, response, authentication);

		return ResponseEntity.ok(configurationMapper.toDto(configurationService.get(loginDto.getIdentifier())));
	}

	@JsonRequestMapping(method = RequestMethod.POST, value = "/logout")
	@Operation(summary = "Logout current user")
	public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
		logoutHandler.logout(request, response, SecurityContextHolder.getContext().getAuthentication());
		return ResponseEntity.ok().build();
	}
}
