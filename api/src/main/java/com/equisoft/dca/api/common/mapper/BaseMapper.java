package com.equisoft.dca.api.common.mapper;

import java.util.List;

public interface BaseMapper<T, DTO> {

	String SPRING_COMPONENT_MODEL = "spring";

	T toEntity(DTO dto);

	DTO toDto(T entity);

	List<DTO> toDtoList(List<T> entities);

	List<T> toEntityList(List<DTO> dtos);
}
