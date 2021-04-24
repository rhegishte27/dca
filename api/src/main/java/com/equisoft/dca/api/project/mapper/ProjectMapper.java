package com.equisoft.dca.api.project.mapper;

import org.mapstruct.Mapper;

import com.equisoft.dca.api.common.mapper.BaseMapper;
import com.equisoft.dca.api.common.mapper.localdatatime.LocalDateTimeMapper;
import com.equisoft.dca.api.project.dto.ProjectDto;
import com.equisoft.dca.backend.project.model.Project;

@Mapper(componentModel = BaseMapper.SPRING_COMPONENT_MODEL,
		uses = {BackupIntervalMapper.class,
				BackupKeepIntervalMapper.class,
				CompilerMapper.class,
				GeneratedCodeLanguageMapper.class,
				ProjectSyncSettingMapper.class,
				ProjectSystemMapper.class,
				LocalDateTimeMapper.class})
public interface ProjectMapper extends BaseMapper<Project, ProjectDto> {

}
