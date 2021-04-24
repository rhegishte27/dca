package com.equisoft.dca.api.feature.mapper;

import org.mapstruct.Mapper;

import com.equisoft.dca.api.common.mapper.BaseMapper;
import com.equisoft.dca.api.feature.dto.FeatureDto;
import com.equisoft.dca.backend.common.model.BaseEnum;
import com.equisoft.dca.backend.user.model.Feature;

@Mapper(componentModel = BaseMapper.SPRING_COMPONENT_MODEL)
public interface FeatureMapper extends BaseMapper<Feature, FeatureDto> {

	@Override
	default Feature toEntity(FeatureDto dto) {
		return dto == null || dto.getId() == null
				? null
				: BaseEnum.valueOf(Feature.class, dto.getId());
	}
}
