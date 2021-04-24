package com.equisoft.dca.api.project.dto;

import java.io.Serializable;
import java.util.Objects;

import com.equisoft.dca.api.location.dto.LocationDto;
import com.equisoft.dca.api.system.dto.SystemDto;

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
public class ProjectSystemDto implements Serializable {

	private static final long serialVersionUID = 2017595824645374443L;

	@Schema(required = true)
	private Integer projectId;

	@Schema(required = true)
	private SystemDto system;

	@Schema(required = true)
	private SystemTypeDto systemType;

	@Schema(required = true)
	private Boolean isSynchronizationEnabled;

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
		ProjectSystemDto that = (ProjectSystemDto) o;
		return Objects.equals(getProjectId(), that.getProjectId()) &&
				Objects.equals(getSystem(), that.getSystem());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getProjectId(), getSystem());
	}
}
