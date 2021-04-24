package com.equisoft.dca.backend.user.converter;

import javax.persistence.Converter;

import com.equisoft.dca.backend.common.converter.BaseEnumConverter;
import com.equisoft.dca.backend.user.model.Feature;

@Converter(autoApply = true)
public class FeatureConverter extends BaseEnumConverter<Feature, Integer> {

	FeatureConverter() {
		super(Feature.class);
	}
}
