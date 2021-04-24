package com.equisoft.dca.api.usersetting.mapper;

import org.mapstruct.Mapper;

import com.equisoft.dca.api.common.mapper.BaseMapper;
import com.equisoft.dca.api.dataobject.mapper.DataObjectMapper;
import com.equisoft.dca.api.location.mapper.LocationMapper;
import com.equisoft.dca.api.organization.mapper.OrganizationMapper;
import com.equisoft.dca.api.project.mapper.ProjectMapper;
import com.equisoft.dca.api.system.mapper.SystemMapper;
import com.equisoft.dca.api.user.mapper.UserMapper;
import com.equisoft.dca.api.usersetting.dto.UserSettingDto;
import com.equisoft.dca.backend.user.model.UserSetting;

@Mapper(componentModel = BaseMapper.SPRING_COMPONENT_MODEL,
		uses = {UserMapper.class,
				OrganizationMapper.class,
				SystemMapper.class,
				ProjectMapper.class,
				LocationMapper.class,
				DataObjectMapper.class})
public interface UserSettingMapper extends BaseMapper<UserSetting, UserSettingDto> {

}
