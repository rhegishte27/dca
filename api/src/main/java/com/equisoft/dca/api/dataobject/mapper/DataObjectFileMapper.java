package com.equisoft.dca.api.dataobject.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.equisoft.dca.api.common.mapper.BaseMapper;
import com.equisoft.dca.api.dataobject.dto.DataObjectFileDto;
import com.equisoft.dca.backend.dataobject.model.DataObjectFile;
import com.equisoft.dca.backend.user.model.User;

@Mapper(componentModel = BaseMapper.SPRING_COMPONENT_MODEL,
		uses = {DataObjectStatusMapper.class})
public interface DataObjectFileMapper extends BaseMapper<DataObjectFile, DataObjectFileDto> {

	@Mapping(target = "user", expression = "java(getUser(dto))")
	DataObjectFile toEntity(DataObjectFileDto dto);

	default User getUser(DataObjectFileDto dto) {
		if (dto == null) {
			return null;
		}

		return User.builder().identifier(dto.getUserIdentifier()).build();
	}

}
