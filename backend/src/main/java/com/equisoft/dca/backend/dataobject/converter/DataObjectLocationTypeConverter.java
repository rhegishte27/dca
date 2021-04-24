package com.equisoft.dca.backend.dataobject.converter;

import javax.persistence.Converter;

import com.equisoft.dca.backend.common.converter.BaseEnumConverter;
import com.equisoft.dca.backend.dataobject.model.DataObjectLocationType;

@Converter(autoApply = true)
public class DataObjectLocationTypeConverter extends BaseEnumConverter<DataObjectLocationType, Integer> {

	DataObjectLocationTypeConverter() {
		super(DataObjectLocationType.class);
	}
}
