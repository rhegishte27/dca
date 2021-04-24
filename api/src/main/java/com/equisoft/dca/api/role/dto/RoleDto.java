package com.equisoft.dca.api.role.dto;

import java.io.Serializable;
import java.util.Set;

import com.equisoft.dca.api.feature.dto.FeatureDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class RoleDto implements Serializable {

	private static final long serialVersionUID = -2472038197894182381L;

	@Schema(required = true)
	private Integer id;

	@Schema(required = true)
	private String name;

	@Schema(required = true)
	private int level;

	@Schema(required = true)
	private Set<FeatureDto> defaultFeatures;

	@Schema(required = true)
	private Set<FeatureDto> nonEditableFeatures;
}
