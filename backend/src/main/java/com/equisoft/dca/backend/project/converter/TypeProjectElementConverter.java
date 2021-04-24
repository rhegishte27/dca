package com.equisoft.dca.backend.project.converter;

import javax.persistence.Converter;

import com.equisoft.dca.backend.common.converter.BaseEnumConverter;
import com.equisoft.dca.backend.project.model.TypeProjectElement;

@Converter(autoApply = true)
public class TypeProjectElementConverter extends BaseEnumConverter<TypeProjectElement, Integer> {

	TypeProjectElementConverter() {
		super(TypeProjectElement.class);
	}
}
