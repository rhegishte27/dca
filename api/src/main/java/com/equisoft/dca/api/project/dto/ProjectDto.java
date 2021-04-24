package com.equisoft.dca.api.project.dto;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProjectDto implements Serializable {

	private static final long serialVersionUID = 833947259942942978L;

	@Schema(required = true)
	private Integer id;

	@Schema(required = true)
	private String identifier;

	@Schema(required = true)
	private String description;

	@Schema(required = true)
	private GeneratedCodeLanguageDto generatedCodeLanguage;

	@Schema(required = true)
	private CompilerDto compiler;

	@Schema(required = true)
	private Boolean isSynchronizationEnabled;

	@Schema(required = true)
	private Boolean isBackupEnabled;

	@Schema(required = true)
	private BackupIntervalDto backupInterval;

	@Schema(required = true)
	private String numberOfBackupIntervals;

	@Schema(required = true)
	private BackupKeepIntervalDto backupKeepInterval;

	@Schema(required = true)
	private String numberOfBackupKeepIntervals;

	@Schema(required = true)
	private Boolean isSynchronizeProjectElementsEnabled;

	@Schema(required = true)
	private Set<ProjectSyncSettingDto> projectSyncSettings;

	@Schema(required = true)
	private String lastBackup;

	@Schema(required = true)
	private Set<ProjectSystemDto> systems;

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		ProjectDto that = (ProjectDto) o;
		return Objects.equals(getId(), that.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getId());
	}
}
