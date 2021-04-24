package com.equisoft.dca.api.location.mapper;

import org.mapstruct.Mapper;

import com.equisoft.dca.api.common.mapper.BaseMapper;
import com.equisoft.dca.api.location.dto.PlatformTypeDto;
import com.equisoft.dca.backend.common.model.BaseEnum;
import com.equisoft.dca.backend.location.model.PlatformType;

@Mapper(componentModel = BaseMapper.SPRING_COMPONENT_MODEL)
public interface PlatformTypeMapper extends BaseMapper<PlatformType, PlatformTypeDto> {

	default PlatformType toEntity(PlatformTypeDto dto) {
		return dto == null || dto.getId() == null
				? null
				: BaseEnum.valueOf(PlatformType.class, dto.getId());
	}

}
