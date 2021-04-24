package com.equisoft.dca.api.dataobject.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.equisoft.dca.api.common.mapper.BaseMapper;
import com.equisoft.dca.api.dataobject.dto.DataObjectDto;
import com.equisoft.dca.api.system.mapper.SystemMapper;
import com.equisoft.dca.backend.dataobject.model.DataObject;
import com.equisoft.dca.backend.system.model.System;

@Mapper(componentModel = BaseMapper.SPRING_COMPONENT_MODEL,
		uses = {SystemMapper.class,
				DataObjectTypeMapper.class,
				DataObjectStatusMapper.class,
				DataObjectFileMapper.class})
public interface DataObjectMapper extends BaseMapper<DataObject, DataObjectDto> {

	@Mapping(target = "systemId", source = "system.id")
	@Mapping(target = "systemIdentifier", source = "system.identifier")
	@Override
	DataObjectDto toDto(DataObject entity);

	@Mapping(target = "system", expression = "java(getSystem(dto))")
	DataObject toEntity(DataObjectDto dto);

	default System getSystem(DataObjectDto dto) {
		if (dto == null) {
			return null;
		}

		if (dto.getSystemId() == null) {
			throw new IllegalArgumentException("SystemId is required");
		}

		return System.builder().id(dto.getSystemId()).build();
	}
}
