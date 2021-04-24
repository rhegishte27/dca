package com.equisoft.dca.backend.location.converter;

import com.equisoft.dca.backend.common.converter.BaseEnumConverter;
import com.equisoft.dca.backend.location.model.PlatformType;

public class PlatformTypeConverter extends BaseEnumConverter<PlatformType, Integer> {

	PlatformTypeConverter() {
		super(PlatformType.class);
	}
}
