package com.equisoft.dca.api.project.mapper;

import org.mapstruct.Mapper;

import com.equisoft.dca.api.common.mapper.BaseMapper;
import com.equisoft.dca.api.project.dto.CompilerDto;
import com.equisoft.dca.backend.common.model.BaseEnum;
import com.equisoft.dca.backend.project.model.Compiler;

@Mapper(componentModel = BaseMapper.SPRING_COMPONENT_MODEL)
public interface CompilerMapper extends BaseMapper<Compiler, CompilerDto> {

	default Compiler toEntity(CompilerDto dto) {
		return dto == null || dto.getId() == null
				? null
				: BaseEnum.valueOf(Compiler.class, dto.getId());
	}
}
