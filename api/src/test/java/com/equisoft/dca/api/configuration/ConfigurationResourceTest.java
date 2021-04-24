package com.equisoft.dca.api.configuration;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import com.equisoft.dca.api.configuration.dto.ConfigurationDto;
import com.equisoft.dca.api.configuration.mapper.ConfigurationMapper;
import com.equisoft.dca.api.language.dto.LanguageDto;
import com.equisoft.dca.api.user.dto.UserDto;
import com.equisoft.dca.api.usersetting.dto.UserSettingDto;
import com.equisoft.dca.backend.configuration.model.Configuration;
import com.equisoft.dca.backend.configuration.service.ConfigurationService;
import com.equisoft.dca.backend.language.model.Language;
import com.equisoft.dca.backend.user.model.User;
import com.equisoft.dca.backend.user.model.UserSetting;
import com.equisoft.dca.infra.security.context.AuthenticationFacade;

@ExtendWith(MockitoExtension.class)
class ConfigurationResourceTest {
	@Mock
	private ConfigurationService configurationService;
	@Mock
	private AuthenticationFacade authenticationFacade;
	@Mock
	private ConfigurationMapper configurationMapper;

	private ConfigurationResource configurationResource;

	@BeforeEach
	void setUp() {
		this.configurationResource = new ConfigurationResource(configurationService, authenticationFacade, configurationMapper);
	}

	@Nested
	class GetLanguage {

		@Nested
		class GivenAuthenticationUserIdentifierNotEmpty {

			@Test
			void givenConfiguration_whenGetLanguage_returnOkAndConfigurationDto() {
				// given
				String identifier = "identifier";
				User user = createUser(1, identifier);
				UserSetting userSetting = createUserSetting(user);
				Configuration configuration = createConfiguration(user, Language.SPANISH, userSetting);

				ConfigurationDto configurationDto = createConfigurationDto(configuration);
				ResponseEntity expected = ResponseEntity.ok().body(configurationDto);

				Mockito.doReturn(Optional.of(identifier)).when(authenticationFacade).getUserIdentifier();
				Mockito.doReturn(configuration).when(configurationService).get(identifier);
				Mockito.doReturn(configurationDto).when(configurationMapper).toDto(configuration);

				// when
				ResponseEntity<ConfigurationDto> actual = configurationResource.get();

				// then
				Assertions.assertThat(actual)
						.isNotNull()
						.extracting(ResponseEntity::getStatusCode, ResponseEntity::getBody)
						.containsExactly(expected.getStatusCode(), expected.getBody());
			}
		}

		@Nested
		class GivenAuthenticationUserIdentifierEmpty {

			@Test
			void givenConfiguration_whenGetLanguage_returnOkAndConfigurationDto() {
				// given
				Configuration configuration = createConfiguration(null, Language.SPANISH, null);
				ConfigurationDto configurationDto = createConfigurationDto(configuration);
				ResponseEntity expected = ResponseEntity.ok().body(configurationDto);

				Mockito.doReturn(Optional.empty()).when(authenticationFacade).getUserIdentifier();
				Mockito.doReturn(configuration).when(configurationService).get(null);
				Mockito.doReturn(configurationDto).when(configurationMapper).toDto(configuration);

				// when
				ResponseEntity<ConfigurationDto> actual = configurationResource.get();

				// then
				Assertions.assertThat(actual)
						.isNotNull()
						.extracting(ResponseEntity::getStatusCode, ResponseEntity::getBody)
						.containsExactly(expected.getStatusCode(), expected.getBody());
			}
		}
	}

	private User createUser(Integer id, String identifier) {
		return User.builder()
				.id(id)
				.identifier(identifier)
				.build();
	}

	private UserSetting createUserSetting(User user) {
		return UserSetting.builder().id(user.getId()).user(user).build();
	}

	private Configuration createConfiguration(User user, Language language, UserSetting userSetting) {
		return Configuration.builder().user(user).language(language).userSetting(userSetting).build();
	}

	private ConfigurationDto createConfigurationDto(Configuration configuration) {
		LanguageDto languageDto = LanguageDto.builder()
				.id(configuration.getLanguage().getId())
				.name(configuration.getLanguage().getName())
				.code(configuration.getLanguage().getCode())
				.build();
		return ConfigurationDto.builder()
				.user(createUserDto(configuration.getUser()))
				.language(languageDto)
				.userSetting(createUserSettingDto(configuration.getUserSetting()))
				.build();
	}

	private UserDto createUserDto(User user) {
		if (user == null) {
			return null;
		}
		return UserDto.builder().id(user.getId()).identifier(user.getIdentifier()).build();
	}

	private UserSettingDto createUserSettingDto(UserSetting userSetting) {
		if (userSetting == null) {
			return null;
		}
		return UserSettingDto.builder()
				.id(userSetting.getId())
				.user(createUserDto(userSetting.getUser()))
				.build();
	}
}
