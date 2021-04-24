package com.equisoft.dca.api.common.exception;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.equisoft.dca.backend.configuration.model.Configuration;
import com.equisoft.dca.backend.configuration.service.ConfigurationService;
import com.equisoft.dca.backend.language.model.Language;
import com.equisoft.dca.infra.ErrorInformation;
import com.equisoft.dca.infra.security.context.AuthenticationFacade;
import com.fasterxml.jackson.core.JsonProcessingException;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

	private static final String exceptionMessage = "This is the original exception message";
	private static final String exceptionCauseMessage = "This is the exception cause message";
	private static final String translatedMessage = "This is the translated message";
	private Language language = Language.ENGLISH;

	private GlobalExceptionHandler handler;

	@Mock
	private MessageSource messageSource;

	@Mock
	private ConfigurationService configurationService;

	@Mock
	private Throwable cause;

	@Mock
	private AuthenticationFacade authenticationFacade;

	@BeforeEach
	void setUp() {
		this.handler = new GlobalExceptionHandler(messageSource, configurationService, authenticationFacade);
	}

	@Nested
	class ConstraintViolationTest {

		@Mock
		private ConstraintViolationException exception;

		@Mock
		private ConstraintViolation constraintViolation;

		@BeforeEach
		void setUp() {
			Mockito.when(constraintViolation.getMessage()).thenReturn("ConstraintViolation message");
			Mockito.when(exception.getConstraintViolations()).thenReturn(Set.of(constraintViolation));
			Mockito.when(authenticationFacade.getUserIdentifier()).thenReturn(Optional.empty());
			Mockito.when(configurationService.get(null)).thenReturn(createConfiguration(language));
		}

		@Test
		void givenConstraintViolationException_whenHandleException_thenReturnBadRequestWithListOfErrors() {
			//given
			List<ErrorInformation> errors = List.of(ErrorInformation.builder().message(translatedMessage).build());
			ResponseEntity<Object> expected = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);

			Mockito.when(messageSource.getMessage(ArgumentMatchers.anyString(), ArgumentMatchers.any(Object[].class), ArgumentMatchers.any(Locale.class)))
					.thenReturn(translatedMessage);

			//when
			ResponseEntity<Object> actual = handler.handleConstraintViolation(exception);

			//then
			Assertions.assertThat(actual)
					.isNotNull()
					.extracting(ResponseEntity::getStatusCode, ResponseEntity::getBody)
					.containsExactly(expected.getStatusCode(), expected.getBody());
		}
	}

	@Nested
	class MethodArgumentNotValid {

		@Mock
		private MethodArgumentNotValidException exception;

		@Mock
		private BindingResult bindingResult;

		@Mock
		private ObjectError objectError;

		@BeforeEach
		void setUp() {
			Mockito.when(exception.getBindingResult()).thenReturn(bindingResult);
			Mockito.when(bindingResult.getAllErrors()).thenReturn(List.of(objectError));
			Mockito.when(configurationService.get(null)).thenReturn(createConfiguration(language));
		}

		@Test
		void givenMethodArgumentNotValidException_whenHandleException_thenReturnBadRequestWithListOfErrors() {
			//given
			List<ErrorInformation> errors = List.of(ErrorInformation.builder().message(translatedMessage).build());
			ResponseEntity<Object> expected = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);

			Mockito.when(messageSource.getMessage(ArgumentMatchers.any(ObjectError.class), ArgumentMatchers.any(Locale.class))).thenReturn(translatedMessage);

			//when
			ResponseEntity<Object> actual = handler.handleMethodArgumentNotValid(exception, null, null, null);

			//then
			Assertions.assertThat(actual)
					.isNotNull()
					.extracting(ResponseEntity::getStatusCode, ResponseEntity::getBody)
					.containsExactly(expected.getStatusCode(), expected.getBody());
		}
	}

	@Nested
	class JsonProcessing {

		@Mock
		private JsonProcessingException exception;

		@BeforeEach
		void setUp() {
			Mockito.when(exception.getMessage()).thenReturn(exceptionMessage);
			Mockito.when(configurationService.get(null)).thenReturn(createConfiguration(language));
		}

		@Test
		void givenJsonProcessingExceptionWithoutCauseAndMessageCodeTranslated_whenHandleException_thenReturnErrorWithMessageTranslated() {
			//given
			ErrorInformation error = ErrorInformation.builder().message(translatedMessage).extraInformation(exceptionMessage).build();
			ResponseEntity<Object> expected = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);

			Mockito.when(messageSource.getMessage(ArgumentMatchers.anyString(), ArgumentMatchers.any(Object[].class), ArgumentMatchers.any(Locale.class)))
					.thenReturn(translatedMessage);

			//when
			ResponseEntity<Object> actual = handler.handleJsonProcessing(exception);

			//then
			Assertions.assertThat(actual)
					.isNotNull()
					.extracting(ResponseEntity::getStatusCode, ResponseEntity::getBody)
					.containsExactly(expected.getStatusCode(), expected.getBody());
		}

		@Test
		void givenJsonProcessingExceptionWithoutCauseAndMessageCodeNotTranslated_whenHandleException_thenReturnErrorWithMessageCode() {
			//given
			String messageCode = "api.errors.invalidJson";
			ErrorInformation error = ErrorInformation.builder().message(messageCode).extraInformation(exceptionMessage).build();
			ResponseEntity<Object> expected = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);

			Mockito.when(messageSource.getMessage(ArgumentMatchers.anyString(), ArgumentMatchers.any(Object[].class), ArgumentMatchers.any(Locale.class)))
					.thenThrow(NoSuchMessageException.class);

			//when
			ResponseEntity<Object> actual = handler.handleJsonProcessing(exception);

			//then
			Assertions.assertThat(actual)
					.isNotNull()
					.extracting(ResponseEntity::getStatusCode, ResponseEntity::getBody)
					.containsExactly(expected.getStatusCode(), expected.getBody());
		}

		@Test
		void givenJsonProcessingExceptionWithCause_whenHandleException_thenReturnErrorWithExtraInformationCause() {
			//given
			ErrorInformation error = ErrorInformation.builder().message(translatedMessage).extraInformation(exceptionCauseMessage).build();
			ResponseEntity<Object> expected = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);

			Mockito.when(exception.getCause()).thenReturn(cause);
			Mockito.when(exception.getCause().getMessage()).thenReturn(exceptionCauseMessage);
			Mockito.when(messageSource.getMessage(ArgumentMatchers.anyString(), ArgumentMatchers.any(Object[].class), ArgumentMatchers.any(Locale.class)))
					.thenReturn(translatedMessage);

			//when
			ResponseEntity<Object> actual = handler.handleJsonProcessing(exception);

			//then
			Assertions.assertThat(actual)
					.isNotNull()
					.extracting(ResponseEntity::getStatusCode, ResponseEntity::getBody)
					.containsExactly(expected.getStatusCode(), expected.getBody());
		}
	}

	@Nested
	class HttpMessageNotReadable {

		@Mock
		private HttpMessageNotReadableException exception;

		@Mock
		private JsonProcessingException jsonCause;

		@BeforeEach
		void setUp() {
			Mockito.when(configurationService.get(null)).thenReturn(createConfiguration(language));
		}

		@Test
		void givenHttpMessageNotReadableExceptionWithoutCauseAndMessageCodeTranslated_whenHandleException_thenReturnErrorWithMessageTranslated() {
			//given
			ErrorInformation error = ErrorInformation.builder().message(translatedMessage).extraInformation(exceptionMessage).build();
			ResponseEntity<Object> expected = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);

			Mockito.when(exception.getMessage()).thenReturn(exceptionMessage);
			Mockito.when(messageSource.getMessage(ArgumentMatchers.anyString(), ArgumentMatchers.any(Object[].class), ArgumentMatchers.any(Locale.class)))
					.thenReturn(translatedMessage);

			//when
			ResponseEntity<Object> actual = handler.handleHttpMessageNotReadable(exception, null, null, null);

			//then
			Assertions.assertThat(actual)
					.isNotNull()
					.extracting(ResponseEntity::getStatusCode, ResponseEntity::getBody)
					.containsExactly(expected.getStatusCode(), expected.getBody());
		}

		@Test
		void givenHttpMessageNotReadableExceptionWithoutCauseAndMessageCodeNotTranslated_whenHandleException_thenReturnErrorWithMessageCode() {
			//given
			String messageCode = "api.errors.unableToReadMessage";
			ErrorInformation error = ErrorInformation.builder().message(messageCode).extraInformation(exceptionMessage).build();
			ResponseEntity<Object> expected = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);

			Mockito.when(exception.getMessage()).thenReturn(exceptionMessage);
			Mockito.when(messageSource.getMessage(ArgumentMatchers.anyString(), ArgumentMatchers.any(Object[].class), ArgumentMatchers.any(Locale.class)))
					.thenThrow(NoSuchMessageException.class);

			//when
			ResponseEntity<Object> actual = handler.handleHttpMessageNotReadable(exception, null, null, null);

			//then
			Assertions.assertThat(actual)
					.isNotNull()
					.extracting(ResponseEntity::getStatusCode, ResponseEntity::getBody)
					.containsExactly(expected.getStatusCode(), expected.getBody());
		}

		@Test
		void givenHttpMessageNotReadableExceptionWithJsonProcessingCauseAndMessageCodeTranslated_whenHandleException_thenReturnErrorWithMessageTranslated() {
			//given
			ErrorInformation error = ErrorInformation.builder().message(translatedMessage).extraInformation(exceptionCauseMessage).build();

			ResponseEntity<Object> expected = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);

			Mockito.when(exception.getCause()).thenReturn(jsonCause);
			Mockito.when(exception.getCause().getMessage()).thenReturn(exceptionCauseMessage);
			Mockito.when(messageSource.getMessage(ArgumentMatchers.anyString(), ArgumentMatchers.any(Object[].class), ArgumentMatchers.any(Locale.class)))
					.thenReturn(translatedMessage);

			//when
			ResponseEntity<Object> actual = handler.handleHttpMessageNotReadable(exception, null, null, null);

			//then
			Assertions.assertThat(actual)
					.isNotNull()
					.extracting(ResponseEntity::getStatusCode, ResponseEntity::getBody)
					.containsExactly(expected.getStatusCode(), expected.getBody());
		}
	}

	private Configuration createConfiguration(Language language) {
		return Configuration.builder().language(language).build();
	}
}
