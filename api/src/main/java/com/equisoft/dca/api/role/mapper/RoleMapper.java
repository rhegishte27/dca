package com.equisoft.dca.api.role.mapper;

import org.mapstruct.Mapper;

import com.equisoft.dca.api.common.mapper.BaseMapper;
import com.equisoft.dca.api.feature.mapper.FeatureMapper;
import com.equisoft.dca.api.role.dto.RoleDto;
import com.equisoft.dca.backend.common.model.BaseEnum;
import com.equisoft.dca.backend.user.model.Role;

@Mapper(componentModel = BaseMapper.SPRING_COMPONENT_MODEL,
		uses = {FeatureMapper.class})
public interface RoleMapper extends BaseMapper<Role, RoleDto> {

	@Override
	default Role toEntity(RoleDto dto) {
		return dto == null || dto.getId() == null
				? null
				: BaseEnum.valueOf(Role.class, dto.getId());
	}
}
