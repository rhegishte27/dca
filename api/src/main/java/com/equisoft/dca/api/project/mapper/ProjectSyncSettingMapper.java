package com.equisoft.dca.api.project.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.equisoft.dca.api.common.mapper.BaseMapper;
import com.equisoft.dca.api.location.mapper.LocationMapper;
import com.equisoft.dca.api.project.dto.ProjectSyncSettingDto;
import com.equisoft.dca.backend.common.model.BaseEnum;
import com.equisoft.dca.backend.project.model.Project;
import com.equisoft.dca.backend.project.model.ProjectSyncSetting;
import com.equisoft.dca.backend.project.model.ProjectSyncSetting.ProjectSyncSettingId;
import com.equisoft.dca.backend.project.model.TypeProjectElement;

@Mapper(componentModel = BaseMapper.SPRING_COMPONENT_MODEL,
		uses = {TypeProjectElementMapper.class,
				LocationMapper.class})
public interface ProjectSyncSettingMapper extends BaseMapper<ProjectSyncSetting, ProjectSyncSettingDto> {

	@Mapping(target = "id", expression = "java(getId(dto))")
	ProjectSyncSetting toEntity(ProjectSyncSettingDto dto);

	default ProjectSyncSettingId getId(ProjectSyncSettingDto dto) {
		if (dto == null) {
			return null;
		}

		if (dto.getTypeProjectElement() == null) {
			throw new IllegalArgumentException("TypeProjectElement is required");
		}

		ProjectSyncSettingId projectSyncSettingId = new ProjectSyncSettingId();
		projectSyncSettingId.setProject(Project.builder().id(dto.getProjectId()).build());
		projectSyncSettingId
				.setTypeProjectElement(BaseEnum.valueOf(TypeProjectElement.class, dto.getTypeProjectElement().getId()));
		return projectSyncSettingId;
	}

	@Mapping(target = "projectId", source = "id.project.id")
	@Mapping(target = "typeProjectElement", source = "id.typeProjectElement")
	ProjectSyncSettingDto toDto(ProjectSyncSetting entity);
}
