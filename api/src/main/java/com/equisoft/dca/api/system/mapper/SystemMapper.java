package com.equisoft.dca.api.system.mapper;

import org.mapstruct.Mapper;

import com.equisoft.dca.api.common.mapper.BaseMapper;
import com.equisoft.dca.api.dataobject.mapper.DataObjectMapper;
import com.equisoft.dca.api.system.dto.SystemDto;
import com.equisoft.dca.backend.system.model.System;

@Mapper(componentModel = BaseMapper.SPRING_COMPONENT_MODEL,
		uses = {DataObjectMapper.class})
public interface SystemMapper extends BaseMapper<System, SystemDto> {

}
