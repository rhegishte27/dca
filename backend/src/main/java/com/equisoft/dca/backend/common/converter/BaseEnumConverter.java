package com.equisoft.dca.backend.common.converter;

import javax.persistence.AttributeConverter;

import com.equisoft.dca.backend.common.model.BaseEnum;

public abstract class BaseEnumConverter<E extends BaseEnum<ID>, ID> implements AttributeConverter<E, ID> {

	private final Class<E> clazz;

	protected BaseEnumConverter(Class<E> clazz) {
		this.clazz = clazz;
	}

	@Override
	public ID convertToDatabaseColumn(E attribute) {
		return attribute == null
				? null
				: attribute.getId();
	}

	@Override
	public E convertToEntityAttribute(ID id) {
		return id == null
				? null
				: BaseEnum.valueOf(clazz, id);
	}
}
