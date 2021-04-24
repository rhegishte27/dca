package com.equisoft.dca.api.common.exception;

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

import com.equisoft.dca.backend.configuration.service.ConfigurationService;
import com.equisoft.dca.backend.exception.BaseException;
import com.equisoft.dca.infra.security.context.AuthenticationFacade;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CustomExceptionHandler extends BaseExceptionHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomExceptionHandler.class);

	@Inject
	public CustomExceptionHandler(MessageSource messageSource, ConfigurationService configurationService, AuthenticationFacade authenticationFacade) {
		super(messageSource, configurationService, authenticationFacade);
	}

	@ExceptionHandler(BaseException.class)
	public ResponseEntity<Object> handleBaseException(BaseException ex) {
		logError(ex);
		return createResponseEntity(HttpStatus.BAD_REQUEST, createErrorInformation(ex.getMessageCode(), ex.getMessageArguments()));
	}

	private void logError(Exception ex) {
		LOGGER.error("Error handled by " + getClass().getCanonicalName(), ex);
	}
}
