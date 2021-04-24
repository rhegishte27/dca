package com.equisoft.dca.api.location.mapper;

import org.mapstruct.Mapper;

import com.equisoft.dca.api.common.mapper.BaseMapper;
import com.equisoft.dca.api.location.dto.LocationDto;
import com.equisoft.dca.backend.location.model.Location;

@Mapper(componentModel = BaseMapper.SPRING_COMPONENT_MODEL,
		uses = {LocationTypeMapper.class,
				PlatformTypeMapper.class})
public interface LocationMapper extends BaseMapper<Location, LocationDto> {

}
