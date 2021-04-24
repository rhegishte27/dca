package com.equisoft.dca.api.location.mapper;

import org.mapstruct.Mapper;

import com.equisoft.dca.api.common.mapper.BaseMapper;
import com.equisoft.dca.api.location.dto.LocationTypeDto;
import com.equisoft.dca.backend.common.model.BaseEnum;
import com.equisoft.dca.backend.location.model.LocationType;

@Mapper(componentModel = BaseMapper.SPRING_COMPONENT_MODEL)
public interface LocationTypeMapper extends BaseMapper<LocationType, LocationTypeDto> {

	default LocationType toEntity(LocationTypeDto dto) {
		return dto == null || dto.getId() == null
				? null
				: BaseEnum.valueOf(LocationType.class, dto.getId());
	}
}
