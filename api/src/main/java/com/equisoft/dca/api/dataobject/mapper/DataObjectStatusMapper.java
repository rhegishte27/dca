package com.equisoft.dca.api.dataobject.mapper;

import org.mapstruct.Mapper;

import com.equisoft.dca.api.common.mapper.BaseMapper;
import com.equisoft.dca.api.dataobject.dto.DataObjStatusDto;
import com.equisoft.dca.backend.common.model.BaseEnum;
import com.equisoft.dca.backend.dataobject.model.DataObjectStatus;

@Mapper(componentModel = BaseMapper.SPRING_COMPONENT_MODEL)
public interface DataObjectStatusMapper extends BaseMapper<DataObjectStatus, DataObjStatusDto> {

	default DataObjectStatus toEntity(DataObjStatusDto dto) {
		return dto == null || dto.getId() == null
				? null
				: BaseEnum.valueOf(DataObjectStatus.class, dto.getId());
	}
}
