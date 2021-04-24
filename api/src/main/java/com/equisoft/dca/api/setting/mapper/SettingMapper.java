package com.equisoft.dca.api.setting.mapper;

import org.mapstruct.Mapper;

import com.equisoft.dca.api.common.mapper.BaseMapper;
import com.equisoft.dca.api.language.mapper.LanguageMapper;
import com.equisoft.dca.api.setting.dto.SettingDto;
import com.equisoft.dca.backend.setting.model.Setting;

@Mapper(componentModel = BaseMapper.SPRING_COMPONENT_MODEL,
		uses = {LanguageMapper.class})
public interface SettingMapper extends BaseMapper<Setting, SettingDto> {
}
