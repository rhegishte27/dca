package com.equisoft.dca.backend.location.converter;

import javax.persistence.Converter;

import com.equisoft.dca.backend.common.converter.BaseEnumConverter;
import com.equisoft.dca.backend.location.model.LocationType;

@Converter(autoApply = true)
public class LocationTypeConverter extends BaseEnumConverter<LocationType, Integer> {

	LocationTypeConverter() {
		super(LocationType.class);
	}
}
