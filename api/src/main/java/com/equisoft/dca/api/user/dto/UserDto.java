package com.equisoft.dca.api.user.dto;

import java.io.Serializable;
import java.util.Set;

import com.equisoft.dca.api.feature.dto.FeatureDto;
import com.equisoft.dca.api.language.dto.LanguageDto;
import com.equisoft.dca.api.organization.dto.OrganizationDto;
import com.equisoft.dca.api.role.dto.RoleDto;

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
public class UserDto implements Serializable {

	private static final long serialVersionUID = -1848956427895032933L;

	@Setter
	@Schema(required = true)
	private Integer id;

	@Schema(required = true)
	private OrganizationDto organization;

	@Schema(required = true)
	private RoleDto role;

	@Schema(required = true)
	private String identifier;

	@Schema(required = true)
	private String name;

	@Schema(required = true)
	private String password;

	@Schema(required = true)
	private String emailAddress;

	@Schema(required = true)
	private LanguageDto language;

	@Schema(required = true)
	private Set<FeatureDto> features;
}
