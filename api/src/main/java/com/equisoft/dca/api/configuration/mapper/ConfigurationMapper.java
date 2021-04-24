package com.equisoft.dca.api.configuration.mapper;

import org.mapstruct.Mapper;

import com.equisoft.dca.api.common.mapper.BaseMapper;
import com.equisoft.dca.api.configuration.dto.ConfigurationDto;
import com.equisoft.dca.api.language.mapper.LanguageMapper;
import com.equisoft.dca.api.user.mapper.UserMapper;
import com.equisoft.dca.api.usersetting.mapper.UserSettingMapper;
import com.equisoft.dca.backend.configuration.model.Configuration;

@Mapper(componentModel = BaseMapper.SPRING_COMPONENT_MODEL,
		uses = {UserMapper.class,
				LanguageMapper.class,
				UserSettingMapper.class})
public interface ConfigurationMapper extends BaseMapper<Configuration, ConfigurationDto> {

}
