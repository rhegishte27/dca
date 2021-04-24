package com.equisoft.dca.api.project.mapper;

import org.mapstruct.Mapper;

import com.equisoft.dca.api.common.mapper.BaseMapper;
import com.equisoft.dca.api.project.dto.GeneratedCodeLanguageDto;
import com.equisoft.dca.backend.common.model.BaseEnum;
import com.equisoft.dca.backend.project.model.GeneratedCodeLanguage;

@Mapper(componentModel = BaseMapper.SPRING_COMPONENT_MODEL)
public interface GeneratedCodeLanguageMapper extends BaseMapper<GeneratedCodeLanguage, GeneratedCodeLanguageDto> {

	default GeneratedCodeLanguage toEntity(GeneratedCodeLanguageDto dto) {
		return dto == null || dto.getId() == null
				? null
				: BaseEnum.valueOf(GeneratedCodeLanguage.class, dto.getId());
	}
}
