package com.equisoft.dca.api.project.mapper;

import org.mapstruct.Mapper;

import com.equisoft.dca.api.common.mapper.BaseMapper;
import com.equisoft.dca.api.project.dto.BackupKeepIntervalDto;
import com.equisoft.dca.backend.common.model.BaseEnum;
import com.equisoft.dca.backend.project.model.BackupKeepInterval;

@Mapper(componentModel = BaseMapper.SPRING_COMPONENT_MODEL)
public interface BackupKeepIntervalMapper extends BaseMapper<BackupKeepInterval, BackupKeepIntervalDto> {

	default BackupKeepInterval toEntity(BackupKeepIntervalDto dto) {
		return dto == null || dto.getId() == null
				? null
				: BaseEnum.valueOf(BackupKeepInterval.class, dto.getId());
	}
}
