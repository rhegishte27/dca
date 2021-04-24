package com.equisoft.dca.api.dataobject.mapper;

import org.mapstruct.Mapper;

import com.equisoft.dca.api.common.mapper.BaseMapper;
import com.equisoft.dca.api.dataobject.dto.DataObjectTypeDto;
import com.equisoft.dca.backend.common.model.BaseEnum;
import com.equisoft.dca.backend.dataobject.model.DataObjectType;

@Mapper(componentModel = BaseMapper.SPRING_COMPONENT_MODEL)
public interface DataObjectTypeMapper extends BaseMapper<DataObjectType, DataObjectTypeDto> {

	default DataObjectType toEntity(DataObjectTypeDto dto) {
		return dto == null || dto.getId() == null
				? null
				: BaseEnum.valueOf(DataObjectType.class, dto.getId());
	}
}

