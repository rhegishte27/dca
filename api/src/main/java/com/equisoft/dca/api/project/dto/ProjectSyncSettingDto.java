package com.equisoft.dca.api.project.dto;

import java.io.Serializable;
import java.util.Objects;

import com.equisoft.dca.api.location.dto.LocationDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProjectSyncSettingDto implements Serializable {
	private static final long serialVersionUID = -7621000121768297021L;

	@Schema(required = true)
	private Integer projectId;

	@Schema(required = true)
	private TypeProjectElementDto typeProjectElement;

	@Schema(required = true)
	private Boolean isSyncEnabled;

	@Schema(required = true)
	private LocationDto location;

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		ProjectSyncSettingDto that = (ProjectSyncSettingDto) o;
		return Objects.equals(getProjectId(), that.getProjectId()) &&
				Objects.equals(getTypeProjectElement(), that.getTypeProjectElement());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getProjectId(), getTypeProjectElement());
	}
}
