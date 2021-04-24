package com.equisoft.dca.api.organization.dto;

import java.io.Serializable;
import java.util.Objects;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OrganizationDto implements Serializable {

	private static final long serialVersionUID = -2117308638157650793L;

	@Setter
	@Schema(required = true)
	private Integer id;

	@Schema(required = true)
	private String name;

	@Schema(required = true)
	private String description;

	@Override
	public int hashCode() {
		return Objects.hash(id, name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		OrganizationDto organizationDto = (OrganizationDto) obj;
		return Objects.equals(id, organizationDto.getId()) &&
				Objects.equals(name, organizationDto.getName());
	}
}
