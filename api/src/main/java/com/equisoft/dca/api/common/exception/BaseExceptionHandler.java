package com.equisoft.dca.api.common.exception;

import java.util.Arrays;
import java.util.Locale;

import javax.validation.ConstraintViolation;

import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.equisoft.dca.backend.configuration.service.ConfigurationService;
import com.equisoft.dca.infra.ErrorInformation;
import com.equisoft.dca.infra.security.context.AuthenticationFacade;

public abstract class BaseExceptionHandler extends ResponseEntityExceptionHandler {

	private final MessageSource messageSource;

	private final ConfigurationService configurationService;

	private final AuthenticationFacade authenticationFacade;

	public BaseExceptionHandler(MessageSource messageSource, ConfigurationService configurationService,
			AuthenticationFacade authenticationFacade) {
		super();
		this.messageSource = messageSource;
		this.configurationService = configurationService;
		this.authenticationFacade = authenticationFacade;
	}

	ErrorInformation createErrorInformation(ObjectError objectError) {
		return ErrorInformation.builder()
				.message(getTranslatedMessage(objectError))
				.build();
	}

	ErrorInformation createErrorInformation(ConstraintViolation constraintViolation) {
		return ErrorInformation.builder()
				.message(getTranslatedMessage(constraintViolation.getMessage()))
				.build();
	}

	ErrorInformation createErrorInformation(String message, Object... args) {
		return ErrorInformation.builder()
				.message(getTranslatedMessage(message, args))
				.build();
	}

	protected ErrorInformation createErrorInformation(String message, Exception ex) {
		return ErrorInformation.builder()
				.message(getTranslatedMessage(message))
				.extraInformation(ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage())
				.build();
	}

	protected ResponseEntity<Object> createResponseEntity(HttpStatus status, Object body) {
		return ResponseEntity.status(status)
				.contentType(MediaType.APPLICATION_JSON)
				.body(body);
	}

	private String getTranslatedMessage(ObjectError objectError) {
		Locale locale = getLocaleUser();
		return  messageSource.getMessage(objectError, locale);
	}

	private String getTranslatedMessage(String message, Object... args) {
		if (message == null) {
			return null;
		}
		Locale locale = getLocaleUser();
		String[] strArgs = null;
		if (args != null) {
			strArgs = Arrays.stream(args)
					.map(arg -> getTranslatedMessage(arg.toString(), null))
					.toArray(String[]::new);
		}
		try {
			return messageSource.getMessage(message, strArgs, locale);
		} catch (NoSuchMessageException e) {
			return message;
		}
	}

	private Locale getLocaleUser() {
		String identifier = authenticationFacade.getUserIdentifier().orElse(null);
		return new Locale(configurationService.get(identifier).getLanguage().getCode());
	}
}
