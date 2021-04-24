package com.equisoft.dca.api.directory;

import java.io.UncheckedIOException;
import java.nio.file.InvalidPathException;
import java.nio.file.NoSuchFileException;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.equisoft.dca.api.auth.AuthenticationExceptionHandler;
import com.equisoft.dca.api.common.exception.BaseExceptionHandler;
import com.equisoft.dca.backend.configuration.service.ConfigurationService;
import com.equisoft.dca.infra.security.context.AuthenticationFacade;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class DirectoryExceptionHandler extends BaseExceptionHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationExceptionHandler.class);

	@Inject
	protected DirectoryExceptionHandler(MessageSource messageSource, ConfigurationService configurationService,
			AuthenticationFacade authenticationFacade) {
		super(messageSource, configurationService, authenticationFacade);
	}

	@ExceptionHandler(UncheckedIOException.class)
	public ResponseEntity<Object> handleAccessDenied(UncheckedIOException ex) {
		logError(ex);
		return createResponseEntity(HttpStatus.FORBIDDEN, createErrorInformation("io.accessdenied", ex));
	}

	@ExceptionHandler(NoSuchFileException.class)
	public ResponseEntity<Object> handleNoSuchFileException(NoSuchFileException ex) {
		logError(ex);
		return createResponseEntity(HttpStatus.NOT_FOUND, createErrorInformation("io.nosuchfileexception", ex));
	}

	@ExceptionHandler(InvalidPathException.class)
	public ResponseEntity<Object> handleInvalidPathException(InvalidPathException ex) {
		logError(ex);
		return createResponseEntity(HttpStatus.BAD_REQUEST, createErrorInformation("io.invalidpathexception", ex));
	}

	private void logError(Exception ex) {
		LOGGER.error("Error handled by " + getClass().getCanonicalName(), ex);
	}
}
