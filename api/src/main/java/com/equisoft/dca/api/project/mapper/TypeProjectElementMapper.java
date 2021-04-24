package com.equisoft.dca.api.project.mapper;

import org.mapstruct.Mapper;

import com.equisoft.dca.api.common.mapper.BaseMapper;
import com.equisoft.dca.api.project.dto.TypeProjectElementDto;
import com.equisoft.dca.backend.common.model.BaseEnum;
import com.equisoft.dca.backend.project.model.TypeProjectElement;

@Mapper(componentModel = BaseMapper.SPRING_COMPONENT_MODEL)
public interface TypeProjectElementMapper extends BaseMapper<TypeProjectElement, TypeProjectElementDto> {

	@Override
	default TypeProjectElement toEntity(TypeProjectElementDto dto) {
		return dto == null || dto.getId() == null
				? null
				: BaseEnum.valueOf(TypeProjectElement.class, dto.getId());
	}
}
