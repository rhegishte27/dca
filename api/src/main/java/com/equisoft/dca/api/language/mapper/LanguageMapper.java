package com.equisoft.dca.api.language.mapper;

import org.mapstruct.Mapper;

import com.equisoft.dca.api.common.mapper.BaseMapper;
import com.equisoft.dca.api.language.dto.LanguageDto;
import com.equisoft.dca.backend.common.model.BaseEnum;
import com.equisoft.dca.backend.language.model.Language;

@Mapper(componentModel = BaseMapper.SPRING_COMPONENT_MODEL)
public interface LanguageMapper extends BaseMapper<Language, LanguageDto> {

	default Language toEntity(LanguageDto dto) {
		return dto == null || dto.getId() == null
				? null
				: BaseEnum.valueOf(Language.class, dto.getId());
	}
}
