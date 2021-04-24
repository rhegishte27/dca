package com.equisoft.dca.api.directory.mapper;

import org.mapstruct.Mapper;

import com.equisoft.dca.api.common.mapper.BaseMapper;
import com.equisoft.dca.api.directory.dto.FileDataDto;
import com.equisoft.dca.backend.filesystem.model.FileData;

@Mapper(componentModel = BaseMapper.SPRING_COMPONENT_MODEL)
public interface FileDataMapper extends BaseMapper<FileData, FileDataDto> {
}
