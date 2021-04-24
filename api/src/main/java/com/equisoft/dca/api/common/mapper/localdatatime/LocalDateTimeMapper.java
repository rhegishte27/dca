package com.equisoft.dca.api.common.mapper.localdatatime;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.mapstruct.Mapper;

import com.equisoft.dca.api.common.mapper.BaseMapper;

/**
 * The mapper to convert LocalDateTime to String having {@link java.time.format.DateTimeFormatter#ISO_LOCAL_DATE_TIME} format and vice-versa.
 */
@Mapper(componentModel = BaseMapper.SPRING_COMPONENT_MODEL)
public interface LocalDateTimeMapper extends BaseMapper<LocalDateTime, String> {
	DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

	default LocalDateTime toEntity(String dto) {
		if (dto == null) {
			return null;
		}

		try {
			return LocalDateTime.parse(dto, formatter);
		} catch (DateTimeParseException e) {
			return null;
		}
	}

	default String toDto(LocalDateTime entity) {
		if (entity == null) {
			return null;
		}
		return formatter.format(entity);
	}
}
