package com.equisoft.dca.backend.configuration.service;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.equisoft.dca.backend.common.model.BaseEnum;
import com.equisoft.dca.backend.configuration.model.Configuration;
import com.equisoft.dca.backend.language.model.Language;
import com.equisoft.dca.backend.setting.model.Setting;
import com.equisoft.dca.backend.setting.service.SettingService;
import com.equisoft.dca.backend.user.model.User;
import com.equisoft.dca.backend.user.model.UserSetting;
import com.equisoft.dca.backend.user.service.UserService;
import com.equisoft.dca.backend.user.service.UserSettingService;

@ExtendWith(MockitoExtension.class)
class ConfigurationServiceImplTest {

	@Mock
	private UserService userService;
	@Mock
	private SettingService settingService;
	@Mock
	private UserSettingService userSettingService;

	private ConfigurationService configurationService;

	@BeforeEach
	void setUp() {
		configurationService = new ConfigurationServiceImpl(userService, settingService, userSettingService);
	}

	@Nested
	class Get {

		private User user;

		@Nested
		class GivenUserEmpty {

			@BeforeEach
			void setUp() {
				Mockito.doReturn(Optional.empty()).when(userService).findByIdentifier(ArgumentMatchers.anyString());
			}

			@Test
			void givenSettingEmpty_whenGetUserLanguage_thenReturnConfigurationWithDefaultLanguageAndUserAndUserSettingNull() {
				// given
				Configuration expected = createConfiguration(null, ConfigurationService.DEFAULT_LANGUAGE, null);
				Mockito.doReturn(Optional.empty()).when(settingService).get();

				// when
				Configuration actual = configurationService.get(ArgumentMatchers.anyString());

				// then
				Assertions.assertThat(actual).isEqualTo(expected);
			}

			@ParameterizedTest
			@ValueSource(ints = {1, 2})
			void givenSettingNotEmpty_whenGetUserLanguage_thenReturnConfigurationWithSettingLanguageAndUserAndUserSettingNull(Integer idLanguage) {
				// given

				Language language = BaseEnum.valueOf(Language.class, idLanguage);
				Setting setting = Setting.builder().language(language).build();
				Mockito.doReturn(Optional.of(setting)).when(settingService).get();

				Configuration expected = createConfiguration(null, language, null);

				// when
				Configuration actual = configurationService.get(ArgumentMatchers.anyString());

				// then
				Assertions.assertThat(actual).isEqualTo(expected);
			}
		}

		@Nested
		class GivenUserNotEmpty {

			@BeforeEach
			void setUp() {
				user = createUser(1);
				Mockito.doReturn(Optional.of(user)).when(userService).findByIdentifier(ArgumentMatchers.anyString());
			}

			@Nested
			class GivenUserLanguageEmpty {
				@BeforeEach
				void setUp() {
					user.setLanguage(null);
				}

				@Test
				void givenSettingEmpty_whenGetUserLanguage_thenReturnUserWithDefaultLanguage() {
					// given
					Configuration expected = createConfiguration(user, ConfigurationService.DEFAULT_LANGUAGE, null);
					Mockito.doReturn(Optional.empty()).when(settingService).get();

					// when
					Configuration actual = configurationService.get(ArgumentMatchers.anyString());

					// then
					Assertions.assertThat(actual).isEqualTo(expected);
				}

				@ParameterizedTest
				@ValueSource(ints = {1, 2})
				void givenSettingNotEmpty_whenGetUserLanguage_thenReturnUserWithSettingLanguage(Integer idLanguage) {
					// given
					Language language = BaseEnum.valueOf(Language.class, idLanguage);

					Setting setting = Setting.builder().language(language).build();
					Mockito.doReturn(Optional.of(setting)).when(settingService).get();

					Configuration expected = createConfiguration(user, language, null);

					// when
					Configuration actual = configurationService.get(ArgumentMatchers.anyString());

					// then
					Assertions.assertThat(actual).isEqualTo(expected);
				}
			}

			@Nested
			class GivenUserLanguageNotEmpty {
				@ParameterizedTest
				@ValueSource(ints = {1, 2})
				void whenGetUserLanguage_thenReturnUserWithUserLanguage(Integer idLanguage) {
					// given
					Language language = BaseEnum.valueOf(Language.class, idLanguage);
					user.setLanguage(language);

					Configuration expected = createConfiguration(user, language, null);

					// when
					Configuration actual = configurationService.get(ArgumentMatchers.anyString());

					// then
					Assertions.assertThat(actual).isEqualTo(expected);
				}
			}

			@Nested
			class GivenUserSettingIsNull {

				@Test
				void givenUserHasNoUserSetting_whenGet_thenReturnConfigurationWithUserSettingNull() {
					// given
					Configuration expected = createConfiguration(user, ConfigurationService.DEFAULT_LANGUAGE, null);
					Mockito.doReturn(Optional.empty()).when(settingService).get();
					Mockito.doReturn(Optional.empty()).when(userSettingService).findById(ArgumentMatchers.anyInt());

					// when
					Configuration actual = configurationService.get(ArgumentMatchers.anyString());

					// then
					Assertions.assertThat(actual).isEqualTo(expected);
				}
			}

			@Nested
			class GivenUserSettingIsNotNull {

				@Test
				void givenUserHasUserSetting_whenGet_thenReturnConfigurationWithUserSetting() {
					// given
					UserSetting userSetting = createUserSetting(user);
					Configuration expected = createConfiguration(user, ConfigurationService.DEFAULT_LANGUAGE, userSetting);
					Mockito.doReturn(Optional.empty()).when(settingService).get();
					Mockito.doReturn(Optional.of(userSetting)).when(userSettingService).findById(ArgumentMatchers.anyInt());

					// when
					Configuration actual = configurationService.get(ArgumentMatchers.anyString());

					// then
					Assertions.assertThat(actual).isEqualTo(expected);
				}
			}
		}
	}

	private User createUser(Integer id) {
		return User.builder().id(id).build();
	}

	private UserSetting createUserSetting(User user) {
		return UserSetting.builder().id(user.getId()).user(user).build();
	}

	private Configuration createConfiguration(User user, Language language, UserSetting userSetting) {
		return Configuration.builder().user(user).language(language).userSetting(userSetting).build();
	}
}
