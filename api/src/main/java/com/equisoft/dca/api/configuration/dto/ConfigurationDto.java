package com.equisoft.dca.api.configuration.dto;

import java.io.Serializable;

import com.equisoft.dca.api.language.dto.LanguageDto;
import com.equisoft.dca.api.user.dto.UserDto;
import com.equisoft.dca.api.usersetting.dto.UserSettingDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ConfigurationDto implements Serializable {

	private static final long serialVersionUID = 7862783332545887122L;

	@Schema(required = true)
	private UserDto user;

	@Schema(required = true)
	private LanguageDto language;

	@Schema(required = true)
	private UserSettingDto userSetting;
}
