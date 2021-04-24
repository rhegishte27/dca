package com.equisoft.dca.backend.project.converter;

import javax.persistence.Converter;

import com.equisoft.dca.backend.common.converter.BaseEnumConverter;
import com.equisoft.dca.backend.project.model.SystemType;

@Converter(autoApply = true)
public class SystemTypeConverter extends BaseEnumConverter<SystemType, Integer> {

	SystemTypeConverter() {
		super(SystemType.class);
	}
}
