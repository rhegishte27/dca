package com.equisoft.dca.api.usersetting.dto;

import java.io.Serializable;

import com.equisoft.dca.api.dataobject.dto.DataObjectDto;
import com.equisoft.dca.api.location.dto.LocationDto;
import com.equisoft.dca.api.organization.dto.OrganizationDto;
import com.equisoft.dca.api.project.dto.ProjectDto;
import com.equisoft.dca.api.system.dto.SystemDto;
import com.equisoft.dca.api.user.dto.UserDto;

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
public class UserSettingDto implements Serializable {

	private static final long serialVersionUID = -3797459801439768489L;

	@Schema(required = true)
	private Integer id;

	@Schema(required = true)
	private UserDto user;

	@Schema(required = true)
	private SystemDto defaultSystem;

	@Schema(required = true)
	private ProjectDto defaultProject;

	@Schema(required = true)
	private OrganizationDto defaultOrganization;

	@Schema(required = true)
	private UserDto defaultUser;

	@Schema(required = true)
	private LocationDto defaultLocation;

	@Schema(required = true)
	private DataObjectDto defaultDataObject;
}
