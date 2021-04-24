package com.equisoft.dca.api.project.mapper;

import org.mapstruct.Mapper;

import com.equisoft.dca.api.common.mapper.BaseMapper;
import com.equisoft.dca.api.project.dto.BackupIntervalDto;
import com.equisoft.dca.backend.common.model.BaseEnum;
import com.equisoft.dca.backend.project.model.BackupInterval;

@Mapper(componentModel = BaseMapper.SPRING_COMPONENT_MODEL)
public interface BackupIntervalMapper extends BaseMapper<BackupInterval, BackupIntervalDto> {

	default BackupInterval toEntity(BackupIntervalDto dto) {
		return dto == null || dto.getId() == null
				? null
				: BaseEnum.valueOf(BackupInterval.class, dto.getId());
	}
}
