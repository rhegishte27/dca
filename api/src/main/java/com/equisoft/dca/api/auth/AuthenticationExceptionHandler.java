package com.equisoft.dca.api.auth;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.equisoft.dca.api.common.exception.BaseExceptionHandler;
import com.equisoft.dca.backend.configuration.service.ConfigurationService;
import com.equisoft.dca.infra.security.context.AuthenticationFacade;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AuthenticationExceptionHandler extends BaseExceptionHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationExceptionHandler.class);

	@Inject
	protected AuthenticationExceptionHandler(MessageSource messageSource, ConfigurationService configurationService,
			AuthenticationFacade authenticationFacade) {
		super(messageSource, configurationService, authenticationFacade);
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<Object> handleAccessDenied(AccessDeniedException ex) {
		logError(ex);
		return createResponseEntity(HttpStatus.FORBIDDEN, createErrorInformation("api.errors.forbidden", ex));
	}

	@ExceptionHandler(AuthenticationServiceException.class)
	public ResponseEntity<Object> handleAuthenticationService(AuthenticationServiceException ex) {
		logError(ex);
		return createResponseEntity(HttpStatus.BAD_REQUEST, createErrorInformation("api.errors.authenticationError", ex));
	}

	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<Object> handleBadCredentials(BadCredentialsException ex) {
		logError(ex);
		return createResponseEntity(HttpStatus.BAD_REQUEST, createErrorInformation("api.errors.authenticationError", ex));
	}

	private void logError(Exception ex) {
		LOGGER.error("Error handled by " + getClass().getCanonicalName(), ex);
	}
}
