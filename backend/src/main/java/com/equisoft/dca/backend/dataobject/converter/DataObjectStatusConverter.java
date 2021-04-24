package com.equisoft.dca.backend.dataobject.converter;

import javax.persistence.Converter;

import com.equisoft.dca.backend.common.converter.BaseEnumConverter;
import com.equisoft.dca.backend.dataobject.model.DataObjectStatus;

@Converter(autoApply = true)
public class DataObjectStatusConverter extends BaseEnumConverter<DataObjectStatus, Integer> {

	DataObjectStatusConverter() {
		super(DataObjectStatus.class);
	}
}
