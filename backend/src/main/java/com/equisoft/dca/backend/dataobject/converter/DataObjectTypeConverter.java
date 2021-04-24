package com.equisoft.dca.backend.dataobject.converter;

import javax.persistence.Converter;

import com.equisoft.dca.backend.common.converter.BaseEnumConverter;
import com.equisoft.dca.backend.dataobject.model.DataObjectType;

@Converter(autoApply = true)
public class DataObjectTypeConverter extends BaseEnumConverter<DataObjectType, Integer> {

	DataObjectTypeConverter() {
		super(DataObjectType.class);
	}
}
