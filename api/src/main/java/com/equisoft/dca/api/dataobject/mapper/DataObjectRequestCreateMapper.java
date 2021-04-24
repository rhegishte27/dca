package com.equisoft.dca.api.dataobject.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.equisoft.dca.api.common.mapper.BaseMapper;
import com.equisoft.dca.api.dataobject.dto.DataObjDto;
import com.equisoft.dca.backend.common.model.BaseEnum;
import com.equisoft.dca.backend.dataobject.model.DataObject;
import com.equisoft.dca.backend.dataobject.model.DataObjectType;

@Mapper(componentModel = BaseMapper.SPRING_COMPONENT_MODEL)
public interface DataObjectRequestCreateMapper {

	@Mapping(source = "system", target = "system.id")
	@Mapping(target = "type", expression = "java(toDataObjectType(dto))")
	DataObject toEntity(DataObjDto.Request.Create dto);

	List<DataObject> toEntity(List<DataObjDto.Request.Create> dtos);

	default DataObjectType toDataObjectType(DataObjDto.Request.Create dto) {
		return dto == null || dto.getType() == null
				? null
				: BaseEnum.valueOf(DataObjectType.class, dto.getType());
	}
}
