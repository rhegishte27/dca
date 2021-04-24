package com.equisoft.dca.api.organization.mapper;

import org.mapstruct.Mapper;

import com.equisoft.dca.api.common.mapper.BaseMapper;
import com.equisoft.dca.api.organization.dto.OrganizationDto;
import com.equisoft.dca.backend.user.model.Organization;

@Mapper(componentModel = BaseMapper.SPRING_COMPONENT_MODEL)
public interface OrganizationMapper extends BaseMapper<Organization, OrganizationDto> {

}
