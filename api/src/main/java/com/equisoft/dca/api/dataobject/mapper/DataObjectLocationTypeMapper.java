package com.equisoft.dca.api.dataobject.mapper;

import org.mapstruct.Mapper;

import com.equisoft.dca.api.common.mapper.BaseMapper;
import com.equisoft.dca.api.dataobject.dto.DataObjectLocationTypeDto;
import com.equisoft.dca.backend.common.model.BaseEnum;
import com.equisoft.dca.backend.dataobject.model.DataObjectLocationType;

@Mapper(componentModel = BaseMapper.SPRING_COMPONENT_MODEL)
public interface DataObjectLocationTypeMapper extends BaseMapper<DataObjectLocationType, DataObjectLocationTypeDto> {

	default DataObjectLocationType toEntity(DataObjectLocationTypeDto dto) {
		return dto == null || dto.getId() == null
				? null
				: BaseEnum.valueOf(DataObjectLocationType.class, dto.getId());
	}
}
