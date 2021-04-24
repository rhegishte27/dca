package com.equisoft.dca.backend.dataobject.converter;

import javax.persistence.Converter;

import com.equisoft.dca.backend.common.converter.BaseEnumConverter;
import com.equisoft.dca.backend.dataobject.model.FieldDdlType;

@Converter(autoApply = true)
public class FieldDdlTypeConverter  extends BaseEnumConverter<FieldDdlType, Integer> {

	FieldDdlTypeConverter() {
		super(FieldDdlType.class);
	}
}
