package com.equisoft.dca.backend.dataobject.converter;

import javax.persistence.Converter;

import com.equisoft.dca.backend.common.converter.BaseEnumConverter;
import com.equisoft.dca.backend.dataobject.model.FieldDataType;

@Converter(autoApply = true)
public class FieldDataTypeConverter extends BaseEnumConverter<FieldDataType, Integer> {

	FieldDataTypeConverter() {
		super(FieldDataType.class);
	}
}
