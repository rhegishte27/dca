package com.equisoft.dca.api.auth;

import java.util.Locale;
import java.util.Optional;

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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;

import com.equisoft.dca.backend.configuration.model.Configuration;
import com.equisoft.dca.backend.configuration.service.ConfigurationService;
import com.equisoft.dca.backend.language.model.Language;
import com.equisoft.dca.infra.ErrorInformation;
import com.equisoft.dca.infra.security.context.AuthenticationFacade;

@ExtendWith(MockitoExtension.class)
class AuthenticationExceptionHandlerTest {

	private static final String exceptionMessage = "This is the original exception message";
	private static final String exceptionCauseMessage = "This is the exception cause message";
	private static final String translatedMessage = "This is the translated message";
	private Language language = Language.ENGLISH;

	private AuthenticationExceptionHandler handler;

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
		this.handler = new AuthenticationExceptionHandler(messageSource, configurationService, authenticationFacade);
		Mockito.when(authenticationFacade.getUserIdentifier()).thenReturn(Optional.empty());
		Mockito.when(configurationService.get(null)).thenReturn(Configuration.builder().language(language).build());
	}

	@Nested
	class AccessDenied {

		@Mock
		private AccessDeniedException exception;

		@BeforeEach
		void setUp() {
			Mockito.when(exception.getMessage()).thenReturn(exceptionMessage);
		}

		@Test
		void givenAccessDeniedExceptionAndMessageCodeTranslated_whenHandleException_thenReturnErrorWithMessageTranslated() {
			//given
			ErrorInformation error = ErrorInformation.builder().message(translatedMessage).extraInformation(exceptionMessage).build();
			ResponseEntity<Object> expected = ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);

			Mockito.when(messageSource.getMessage(ArgumentMatchers.anyString(), ArgumentMatchers.any(Object[].class), ArgumentMatchers.any(Locale.class)))
					.thenReturn(translatedMessage);

			//when
			ResponseEntity<Object> actual = handler.handleAccessDenied(exception);

			//then
			Assertions.assertThat(actual)
					.isNotNull()
					.extracting(ResponseEntity::getStatusCode, ResponseEntity::getBody)
					.containsExactly(expected.getStatusCode(), expected.getBody());
		}

		@Test
		void givenAccessDeniedExceptionAndMessageCodeNotTranslated_whenHandleException_thenReturnErrorWithMessageCode() {
			//given
			String messageCode = "api.errors.forbidden";
			ErrorInformation error = ErrorInformation.builder().message(messageCode).extraInformation(exceptionMessage).build();
			ResponseEntity<Object> expected = ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);

			Mockito.when(messageSource.getMessage(ArgumentMatchers.anyString(), ArgumentMatchers.any(Object[].class), ArgumentMatchers.any(Locale.class)))
					.thenThrow(NoSuchMessageException.class);

			//when
			ResponseEntity<Object> actual = handler.handleAccessDenied(exception);

			//then
			Assertions.assertThat(actual)
					.isNotNull()
					.extracting(ResponseEntity::getStatusCode, ResponseEntity::getBody)
					.containsExactly(expected.getStatusCode(), expected.getBody());
		}

		@Test
		void givenAccessDeniedExceptionWithCause_whenHandleException_thenReturnErrorWithExtraInformationCause() {
			//given
			ErrorInformation error = ErrorInformation.builder().message(translatedMessage).extraInformation(exceptionCauseMessage).build();
			ResponseEntity<Object> expected = ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);

			Mockito.when(exception.getCause()).thenReturn(cause);
			Mockito.when(exception.getCause().getMessage()).thenReturn(exceptionCauseMessage);
			Mockito.when(messageSource.getMessage(ArgumentMatchers.anyString(), ArgumentMatchers.any(Object[].class), ArgumentMatchers.any(Locale.class)))
					.thenReturn(translatedMessage);

			//when
			ResponseEntity<Object> actual = handler.handleAccessDenied(exception);

			//then
			Assertions.assertThat(actual)
					.isNotNull()
					.extracting(ResponseEntity::getStatusCode, ResponseEntity::getBody)
					.containsExactly(expected.getStatusCode(), expected.getBody());
		}
	}

	@Nested
	class AuthenticationService {

		@Mock
		private AuthenticationServiceException exception;

		@BeforeEach
		void setUp() {
			Mockito.when(exception.getMessage()).thenReturn(exceptionMessage);
		}

		@Test
		void givenAuthenticationServiceExceptionAndMessageCodeTranslated_whenHandleException_thenReturnErrorWithMessageTranslated() {
			//given
			ErrorInformation error = ErrorInformation.builder().message(translatedMessage).extraInformation(exceptionMessage).build();
			ResponseEntity<Object> expected = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);

			Mockito.when(messageSource.getMessage(ArgumentMatchers.anyString(), ArgumentMatchers.any(Object[].class), ArgumentMatchers.any(Locale.class)))
					.thenReturn(translatedMessage);

			//when
			ResponseEntity<Object> actual = handler.handleAuthenticationService(exception);

			//then
			Assertions.assertThat(actual)
					.isNotNull()
					.extracting(ResponseEntity::getStatusCode, ResponseEntity::getBody)
					.containsExactly(expected.getStatusCode(), expected.getBody());
		}
	}

	@Nested
	class BadCredentials {

		@Mock
		private BadCredentialsException exception;

		@BeforeEach
		void setUp() {
			Mockito.when(exception.getMessage()).thenReturn(exceptionMessage);
		}

		@Test
		void givenBadCredentialsExceptionWithCause_whenHandleException_thenReturnErrorWithExtraInformationCause() {
			//given
			ErrorInformation error = ErrorInformation.builder().message(translatedMessage).extraInformation(exceptionCauseMessage).build();
			ResponseEntity<Object> expected = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);

			Mockito.when(exception.getCause()).thenReturn(cause);
			Mockito.when(exception.getCause().getMessage()).thenReturn(exceptionCauseMessage);
			Mockito.when(messageSource.getMessage(ArgumentMatchers.anyString(), ArgumentMatchers.any(Object[].class), ArgumentMatchers.any(Locale.class)))
					.thenReturn(translatedMessage);

			//when
			ResponseEntity<Object> actual = handler.handleBadCredentials(exception);

			//then
			Assertions.assertThat(actual)
					.isNotNull()
					.extracting(ResponseEntity::getStatusCode, ResponseEntity::getBody)
					.containsExactly(expected.getStatusCode(), expected.getBody());
		}
	}
}
