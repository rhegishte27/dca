package com.equisoft.dca.api.common.exception;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.equisoft.dca.backend.configuration.service.ConfigurationService;
import com.equisoft.dca.infra.security.context.AuthenticationFacade;

@ControllerAdvice
public class GenericExceptionHandler extends BaseExceptionHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(GenericExceptionHandler.class);

	@Inject
	public GenericExceptionHandler(MessageSource messageSource, ConfigurationService configurationService, AuthenticationFacade authenticationFacade) {
		super(messageSource, configurationService, authenticationFacade);
	}

	@ExceptionHandler({Exception.class})
	public ResponseEntity<Object> handleGeneric(Exception ex) {
		LOGGER.error(String.format("Uncaught error. Stack trace is: %s", ex + getFullStackTraceLog(ex)));
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createErrorInformation("api.errors.genericError", ex));
	}

	private String getFullStackTraceLog(Exception ex) {
		return Arrays.stream(ex.getStackTrace())
				.map(Objects::toString)
				.collect(Collectors.joining("\n"));
	}
}
