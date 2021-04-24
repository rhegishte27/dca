package com.equisoft.dca.api.user.mapper;

import org.mapstruct.Mapper;

import com.equisoft.dca.api.common.mapper.BaseMapper;
import com.equisoft.dca.api.feature.mapper.FeatureMapper;
import com.equisoft.dca.api.language.mapper.LanguageMapper;
import com.equisoft.dca.api.organization.mapper.OrganizationMapper;
import com.equisoft.dca.api.role.mapper.RoleMapper;
import com.equisoft.dca.api.user.dto.UserDto;
import com.equisoft.dca.backend.user.model.User;

@Mapper(componentModel = BaseMapper.SPRING_COMPONENT_MODEL,
		uses = {OrganizationMapper.class,
				FeatureMapper.class,
				RoleMapper.class,
				LanguageMapper.class})
public interface UserMapper extends BaseMapper<User, UserDto> {
}
