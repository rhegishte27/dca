package com.equisoft.dca.api.project.mapper;

import org.mapstruct.Mapper;

import com.equisoft.dca.api.common.mapper.BaseMapper;
import com.equisoft.dca.api.project.dto.SystemTypeDto;
import com.equisoft.dca.backend.common.model.BaseEnum;
import com.equisoft.dca.backend.project.model.SystemType;

@Mapper(componentModel = BaseMapper.SPRING_COMPONENT_MODEL)
public interface SystemTypeMapper extends BaseMapper<SystemType, SystemTypeDto> {

	default SystemType toEntity(SystemTypeDto dto) {
		return dto == null || dto.getId() == null
				? null
				: BaseEnum.valueOf(SystemType.class, dto.getId());
	}
}
