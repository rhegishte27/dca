package com.equisoft.dca.api.common.exception;

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

import com.equisoft.dca.backend.configuration.model.Configuration;
import com.equisoft.dca.backend.configuration.service.ConfigurationService;
import com.equisoft.dca.backend.language.model.Language;
import com.equisoft.dca.infra.ErrorInformation;
import com.equisoft.dca.infra.security.context.AuthenticationFacade;

@ExtendWith(MockitoExtension.class)
class GenericExceptionHandlerTest {

	private static final String exceptionMessage = "This is the original exception message";
	private static final String translatedMessage = "This is the translated message";
	private Language language = Language.ENGLISH;

	private GenericExceptionHandler handler;

	@Mock
	private MessageSource messageSource;

	@Mock
	private ConfigurationService configurationService;

	@Mock
	private IllegalArgumentException exception;

	@Mock
	private AuthenticationFacade authenticationFacade;

	@BeforeEach
	void setUp() {
		this.handler = new GenericExceptionHandler(messageSource, configurationService, authenticationFacade);
		Mockito.when(exception.getStackTrace()).thenReturn(createStackTraceElementArray());
		Mockito.when(authenticationFacade.getUserIdentifier()).thenReturn(Optional.empty());
		Mockito.when(configurationService.get(null)).thenReturn(Configuration.builder().language(language).build());
	}

	@Nested
	class WithMessage {

		@BeforeEach
		void setUp() {
			Mockito.when(exception.getMessage()).thenReturn(exceptionMessage);
		}

		@Test
		void givenGenericExceptionAndMessageCodeTranslated_whenHandleException_thenReturnErrorWithMessageTranslated() {
			//given
			ErrorInformation error = ErrorInformation.builder().message(translatedMessage).extraInformation(exceptionMessage).build();
			ResponseEntity<Object> expected = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);

			Mockito.when(messageSource.getMessage(ArgumentMatchers.anyString(), ArgumentMatchers.any(Object[].class), ArgumentMatchers.any(Locale.class)))
					.thenReturn(translatedMessage);

			//when
			ResponseEntity<Object> actual = handler.handleGeneric(exception);

			//then
			Assertions.assertThat(actual)
					.isNotNull()
					.extracting(ResponseEntity::getStatusCode, ResponseEntity::getBody)
					.containsExactly(expected.getStatusCode(), expected.getBody());
		}

		@Test
		void givenGenericExceptionAndMessageNotTranslated_whenHandleException_thenReturnErrorWithOriginalMessage() {
			//given
			String messageCode = "api.errors.genericError";
			ErrorInformation error = ErrorInformation.builder().message(messageCode).extraInformation(exceptionMessage).build();
			ResponseEntity<Object> expected = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);

			Mockito.when(messageSource.getMessage(ArgumentMatchers.anyString(), ArgumentMatchers.any(Object[].class), ArgumentMatchers.any(Locale.class)))
					.thenThrow(NoSuchMessageException.class);

			//when
			ResponseEntity<Object> actual = handler.handleGeneric(exception);

			//then
			Assertions.assertThat(actual)
					.isNotNull()
					.extracting(ResponseEntity::getStatusCode, ResponseEntity::getBody)
					.containsExactly(expected.getStatusCode(), expected.getBody());
		}
	}

	@Nested
	class WithCause {

		@Mock
		private Throwable cause;

		private static final String exceptionCauseMessage = "This is the exception cause message";

		@Test
		void givenGenericExceptionWithCause_whenHandleException_thenReturnErrorWithExtraInformationCause() {
			//given
			ErrorInformation error = ErrorInformation.builder().message(translatedMessage).extraInformation(exceptionCauseMessage).build();
			ResponseEntity<Object> expected = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);

			Mockito.when(exception.getCause()).thenReturn(cause);
			Mockito.when(exception.getCause().getMessage()).thenReturn(exceptionCauseMessage);
			Mockito.when(messageSource.getMessage(ArgumentMatchers.anyString(), ArgumentMatchers.any(Object[].class), ArgumentMatchers.any(Locale.class)))
					.thenReturn(translatedMessage);

			//when
			ResponseEntity<Object> actual = handler.handleGeneric(exception);

			//then
			Assertions.assertThat(actual)
					.isNotNull()
					.extracting(ResponseEntity::getStatusCode, ResponseEntity::getBody)
					.containsExactly(expected.getStatusCode(), expected.getBody());
		}
	}

	private StackTraceElement[] createStackTraceElementArray() {
		return new StackTraceElement[] {
				new StackTraceElement("lotro.Bilbo", "smokes", "Bilbo.java", 5),
				new StackTraceElement("com.example.Foo", "b", null, 1),
				new StackTraceElement("java.lang.Frodo", "lives", "Frodo.java", 3)
		};
	}
}
