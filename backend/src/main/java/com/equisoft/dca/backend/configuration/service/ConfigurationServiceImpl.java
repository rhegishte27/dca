package com.equisoft.dca.backend.configuration.service;

import java.util.Optional;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.equisoft.dca.backend.configuration.model.Configuration;
import com.equisoft.dca.backend.language.model.Language;
import com.equisoft.dca.backend.setting.model.Setting;
import com.equisoft.dca.backend.setting.service.SettingService;
import com.equisoft.dca.backend.user.model.User;
import com.equisoft.dca.backend.user.model.UserSetting;
import com.equisoft.dca.backend.user.service.UserService;
import com.equisoft.dca.backend.user.service.UserSettingService;

@Service
class ConfigurationServiceImpl implements ConfigurationService {

	private final UserService userService;

	private final SettingService settingService;

	private final UserSettingService userSettingService;

	@Inject
	ConfigurationServiceImpl(UserService userService, SettingService settingService,
			UserSettingService userSettingService) {
		this.userService = userService;
		this.settingService = settingService;
		this.userSettingService = userSettingService;
	}

	@Override
	public Configuration get(String userIdentifier) {
		User user = userService.findByIdentifier(userIdentifier).orElse(null);
		return Configuration.builder()
				.user(user)
				.language(getLanguage(user))
				.userSetting(getUserSetting(user))
				.build();
	}

	private UserSetting getUserSetting(User user) {
		return Optional.ofNullable(user)
				.map(u -> userSettingService.findById(u.getId()).orElse(null))
				.orElse(null);
	}

	private Language getLanguage(User user) {
		return Optional.ofNullable(user)
				.map(User::getLanguage)
				.orElse(getSettingLanguage());
	}

	private Language getSettingLanguage() {
		return settingService.get()
				.map(Setting::getLanguage)
				.orElse(DEFAULT_LANGUAGE);
	}
}
