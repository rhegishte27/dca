package com.equisoft.dca.api.project.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.equisoft.dca.api.common.mapper.BaseMapper;
import com.equisoft.dca.api.location.mapper.LocationMapper;
import com.equisoft.dca.api.project.dto.ProjectSystemDto;
import com.equisoft.dca.api.system.mapper.SystemMapper;
import com.equisoft.dca.backend.project.model.Project;
import com.equisoft.dca.backend.project.model.ProjectSystem;
import com.equisoft.dca.backend.project.model.ProjectSystem.ProjectSystemId;
import com.equisoft.dca.backend.system.model.System;

@Mapper(componentModel = BaseMapper.SPRING_COMPONENT_MODEL,
		uses = {SystemMapper.class,
				SystemTypeMapper.class,
				LocationMapper.class})
interface ProjectSystemMapper extends BaseMapper<ProjectSystem, ProjectSystemDto> {
	@Mapping(target = "id", expression = "java(getId(dto))")
	ProjectSystem toEntity(ProjectSystemDto dto);

	default ProjectSystemId getId(ProjectSystemDto dto) {
		if (dto == null) {
			return null;
		}

		if (dto.getSystem() == null) {
			throw new IllegalArgumentException("System is required");
		}

		ProjectSystemId projectSystemId = new ProjectSystemId();
		projectSystemId.setProject(Project.builder().id(dto.getProjectId()).build());
		projectSystemId.setSystem(System.builder().id(dto.getSystem().getId()).build());
		return projectSystemId;
	}

	@Mapping(target = "projectId", source = "id.project.id")
	@Mapping(target = "system", source = "id.system")
	ProjectSystemDto toDto(ProjectSystem entity);
}
