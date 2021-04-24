package com.equisoft.dca.api.common.exception;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.validation.ConstraintViolationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.equisoft.dca.backend.configuration.service.ConfigurationService;
import com.equisoft.dca.infra.ErrorInformation;
import com.equisoft.dca.infra.security.context.AuthenticationFacade;
import com.fasterxml.jackson.core.JsonProcessingException;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler extends BaseExceptionHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@Inject
	public GlobalExceptionHandler(MessageSource messageSource, ConfigurationService configurationService, AuthenticationFacade authenticationFacade) {
		super(messageSource, configurationService, authenticationFacade);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status,
			WebRequest request) {
		logError(ex);
		List<ErrorInformation> errors = ex.getBindingResult()
				.getAllErrors()
				.stream()
				.map(this::createErrorInformation)
				.collect(Collectors.toList());
		return createResponseEntity(HttpStatus.BAD_REQUEST, errors);
	}

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status,
			WebRequest request) {
		if (ex.getCause() instanceof JsonProcessingException) {
			return handleJsonProcessing((JsonProcessingException) ex.getCause());
		}

		logError(ex);
		return createResponseEntity(HttpStatus.BAD_REQUEST, createErrorInformation("api.errors.unableToReadMessage", ex));
	}

	@ExceptionHandler(JsonProcessingException.class)
	public ResponseEntity<Object> handleJsonProcessing(JsonProcessingException ex) {
		logError(ex);
		return createResponseEntity(HttpStatus.BAD_REQUEST, createErrorInformation("api.errors.invalidJson", ex));
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex) {
		logError(ex);
		List<ErrorInformation> errors = ex.getConstraintViolations()
				.stream()
				.map(this::createErrorInformation)
				.collect(Collectors.toList());
		return createResponseEntity(HttpStatus.BAD_REQUEST, errors);
	}

	private void logError(Exception ex) {
		LOGGER.error("Error handled by " + getClass().getCanonicalName(), ex);
	}
}
