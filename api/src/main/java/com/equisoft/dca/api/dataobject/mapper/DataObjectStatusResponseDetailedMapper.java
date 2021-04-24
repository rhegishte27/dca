package com.equisoft.dca.api.dataobject.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.equisoft.dca.api.common.mapper.BaseMapper;
import com.equisoft.dca.api.dataobject.dto.DataObjectStatusDto;
import com.equisoft.dca.backend.dataobject.model.DataObjectStatus;

@Mapper(componentModel = BaseMapper.SPRING_COMPONENT_MODEL)
public interface DataObjectStatusResponseDetailedMapper {

	DataObjectStatusDto.Response.Detailed toDto(DataObjectStatus dataObjectStatus);

	List<DataObjectStatusDto.Response.Detailed> toDto(List<DataObjectStatus> dataObjectStatus);
}
