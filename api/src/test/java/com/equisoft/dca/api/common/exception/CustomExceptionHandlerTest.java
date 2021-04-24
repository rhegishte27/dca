package com.equisoft.dca.api.common.exception;

import java.util.Locale;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.equisoft.dca.backend.configuration.model.Configuration;
import com.equisoft.dca.backend.configuration.service.ConfigurationService;
import com.equisoft.dca.backend.exception.EntityAlreadyExistsException;
import com.equisoft.dca.backend.exception.EntityNotFoundException;
import com.equisoft.dca.backend.exception.EntityReferenceConflictedException;
import com.equisoft.dca.backend.language.model.Language;
import com.equisoft.dca.backend.user.exception.UserRoleLevelException;
import com.equisoft.dca.infra.ErrorInformation;
import com.equisoft.dca.infra.security.context.AuthenticationFacade;

@ExtendWith(MockitoExtension.class)
class CustomExceptionHandlerTest {

	private static final String translatedMessage = "This is the translated message";
	private Language language = Language.ENGLISH;

	private CustomExceptionHandler handler;

	@Mock
	private MessageSource messageSource;

	@Mock
	private ConfigurationService configurationService;

	@Mock
	private AuthenticationFacade authenticationFacade;

	@BeforeEach
	void setUp() {
		this.handler = new CustomExceptionHandler(messageSource, configurationService, authenticationFacade);
	}

	@Nested
	class EntityNotFound {

		private final String[] messageArguments = new String[]{"oneclass", "id", "1"};
		private final String[] translatedArguments = new String[]{"Oneclass", "Id", "1"};
		private final Locale locale = new Locale(language.getCode());

		@Mock
		private EntityNotFoundException exception;

		@BeforeEach
		void setUp() {
			Optional<String> username = Optional.of("username");
			Mockito.when(exception.getMessageArguments()).thenReturn(messageArguments);
			Mockito.when(authenticationFacade.getUserIdentifier()).thenReturn(username);
			Mockito.when(configurationService.get(username.get())).thenReturn(createConfiguration(language));
		}

		@Test
		void givenEntityNotFoundExceptionAndMessageCodeTranslated_whenHandleException_thenReturnErrorWithMessageTranslated() {
			//given
			ErrorInformation error = ErrorInformation.builder().message(translatedMessage).build();
			ResponseEntity<Object> expected = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);

			Mockito.when(exception.getMessageCode()).thenReturn("message.code");
			Mockito.when(messageSource.getMessage(messageArguments[0], null, locale)).thenReturn(translatedArguments[0]);
			Mockito.when(messageSource.getMessage(messageArguments[1], null, locale)).thenReturn(translatedArguments[1]);
			Mockito.when(messageSource.getMessage(messageArguments[2], null, locale)).thenReturn(translatedArguments[2]);
			Mockito.when(messageSource.getMessage(exception.getMessageCode(), translatedArguments, locale)).thenReturn(translatedMessage);

			//when
			ResponseEntity<Object> actual = handler.handleBaseException(exception);

			//then
			Assertions.assertThat(actual)
					.isNotNull()
					.extracting(ResponseEntity::getStatusCode, ResponseEntity::getBody)
					.containsExactly(expected.getStatusCode(), expected.getBody());
		}
	}

	@Nested
	class EntityAlreadyExists {

		private final String[] messageArguments = new String[]{"oneclass", "id", "1"};
		private final String[] translatedArguments = new String[]{"Oneclass", "Id", "1"};
		private final Locale locale = new Locale(language.getCode());

		@Mock
		private EntityAlreadyExistsException exception;

		@BeforeEach
		void setUp() {
			Mockito.when(exception.getMessageArguments()).thenReturn(messageArguments);
			Mockito.when(configurationService.get(null)).thenReturn(createConfiguration(language));
		}

		@Test
		void givenEntityAlreadyExistsExceptionAndMessageCodeTranslated_whenHandleException_thenReturnErrorWithMessageTranslated() {
			//given
			ErrorInformation error = ErrorInformation.builder().message(translatedMessage).build();
			ResponseEntity<Object> expected = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
			String[] usedArguments = new String[]{"oneclass", "Id", "1"};

			Mockito.when(exception.getMessageCode()).thenReturn("message.code");
			Mockito.when(messageSource.getMessage(messageArguments[0], null, locale)).thenThrow(NoSuchMessageException.class);
			Mockito.when(messageSource.getMessage(messageArguments[1], null, locale)).thenReturn(translatedArguments[1]);
			Mockito.when(messageSource.getMessage(messageArguments[2], null, locale)).thenThrow(NoSuchMessageException.class);
			Mockito.when(messageSource.getMessage(exception.getMessageCode(), usedArguments, locale)).thenReturn(translatedMessage);

			//when
			ResponseEntity<Object> actual = handler.handleBaseException(exception);

			//then
			Assertions.assertThat(actual)
					.isNotNull()
					.extracting(ResponseEntity::getStatusCode, ResponseEntity::getBody)
					.containsExactly(expected.getStatusCode(), expected.getBody());
		}
	}

	@Nested
	class EntityReferenceConflicted {

		private final String[] messageArguments = new String[]{"oneclass", "id", "1", "otherclass"};
		private final String[] translatedArguments = new String[]{"Oneclass", "Id", "1", "Otherclass"};
		private final Locale locale = new Locale(language.getCode());

		@Mock
		private EntityReferenceConflictedException exception;

		@BeforeEach
		void setUp() {
			Mockito.when(exception.getMessageArguments()).thenReturn(messageArguments);
			Mockito.when(configurationService.get(null)).thenReturn(createConfiguration(language));
		}

		@Test
		void givenEntityReferenceConflictedExceptionAndMessageCodeTranslated_whenHandleException_thenReturnErrorWithMessageTranslated() {
			//given
			ErrorInformation error = ErrorInformation.builder().message(translatedMessage).build();
			ResponseEntity<Object> expected = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
			Object[] finalArguments = new Object[]{"oneclass", "Id", "1", "Otherclass"};

			Mockito.when(exception.getMessageCode()).thenReturn("message.code");
			Mockito.when(messageSource.getMessage("oneclass", null, locale)).thenThrow(NoSuchMessageException.class);
			Mockito.when(messageSource.getMessage("id", null, locale)).thenReturn(translatedArguments[1]);
			Mockito.when(messageSource.getMessage("1", null, locale)).thenThrow(NoSuchMessageException.class);
			Mockito.when(messageSource.getMessage("otherclass", null, locale)).thenReturn(translatedArguments[3]);
			Mockito.when(messageSource.getMessage(exception.getMessageCode(), finalArguments, locale)).thenReturn(translatedMessage);

			//when
			ResponseEntity<Object> actual = handler.handleBaseException(exception);

			//then
			Assertions.assertThat(actual)
					.isNotNull()
					.extracting(ResponseEntity::getStatusCode, ResponseEntity::getBody)
					.containsExactly(expected.getStatusCode(), expected.getBody());
		}
	}

	@Nested
	class NullMessageCode {

		@Mock
		private EntityNotFoundException exception;

		@Test
		void givenGenericExceptionWithNullMessage_whenHandleException_thenReturnNullErrorMessage() {
			//given
			ErrorInformation error = ErrorInformation.builder().build();
			ResponseEntity<Object> expected = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);

			Mockito.when(exception.getMessageCode()).thenReturn(null);
			Mockito.when(exception.getMessageArguments()).thenReturn(new String[] { });

			//when
			ResponseEntity<Object> actual = handler.handleBaseException(exception);

			//then
			Assertions.assertThat(actual)
					.isNotNull()
					.extracting(ResponseEntity::getStatusCode, ResponseEntity::getBody)
					.containsExactly(expected.getStatusCode(), expected.getBody());
		}
	}

	@Nested
	class UserRoleLevel {

		private final String[] messageArguments = new String[]{"identifier", "ident", "id", "1"};
		private final String[] translatedArguments = new String[]{"Identifier", "ident", "Id", "1"};
		private final Locale locale = new Locale(language.getCode());

		@Mock
		private UserRoleLevelException exception;

		@BeforeEach
		void setUp() {
			Mockito.when(exception.getMessageArguments()).thenReturn(messageArguments);
			Mockito.when(configurationService.get(null)).thenReturn(createConfiguration(language));
		}

		@Test
		void givenUserRoleLevelExceptionAndMessageCodeTranslated_whenHandleException_thenReturnErrorWithMessageTranslated() {
			//given
			ErrorInformation error = ErrorInformation.builder().message(translatedMessage).build();
			ResponseEntity<Object> expected = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);

			Mockito.when(exception.getMessageCode()).thenReturn("message.code");
			Mockito.when(messageSource.getMessage(messageArguments[0], null, locale)).thenReturn(translatedArguments[0]);
			Mockito.when(messageSource.getMessage(messageArguments[1], null, locale)).thenThrow(NoSuchMessageException.class);
			Mockito.when(messageSource.getMessage(messageArguments[2], null, locale)).thenReturn(translatedArguments[2]);
			Mockito.when(messageSource.getMessage(messageArguments[3], null, locale)).thenThrow(NoSuchMessageException.class);
			Mockito.when(messageSource.getMessage(exception.getMessageCode(), translatedArguments, locale)).thenReturn(translatedMessage);

			//when
			ResponseEntity<Object> actual = handler.handleBaseException(exception);

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
