package com.equisoft.dca.api.dataobject.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.equisoft.dca.api.common.mapper.BaseMapper;
import com.equisoft.dca.api.dataobject.dto.DataObjDto;
import com.equisoft.dca.backend.dataobject.model.DataObject;

@Mapper(componentModel = BaseMapper.SPRING_COMPONENT_MODEL)
public interface DataObjectResponseSummaryMapper {

	@Mapping(source = "system.id", target = "system")
	@Mapping(source = "status.id", target = "status")
	DataObjDto.Response.Summary toDto(DataObject dataObject);

	List<DataObjDto.Response.Summary> toDto(List<DataObject> dataObjects);
}
