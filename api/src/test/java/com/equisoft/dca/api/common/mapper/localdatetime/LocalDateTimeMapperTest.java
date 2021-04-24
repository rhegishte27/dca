package com.equisoft.dca.api.common.mapper.localdatetime;

import java.time.LocalDateTime;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import com.equisoft.dca.api.common.mapper.localdatatime.LocalDateTimeMapper;
import com.equisoft.dca.api.common.mapper.localdatatime.LocalDateTimeMapperImpl;

class LocalDateTimeMapperTest {

	private LocalDateTimeMapper mapper;

	@BeforeEach
	void setUp() {
		mapper = new LocalDateTimeMapperImpl();
	}

	@Nested
	class ToEntity {
		private String dto;

		@ParameterizedTest
		@NullSource
		@EmptySource
		@ValueSource(strings = {"test", "aaaa", "1234", "01-01-2020"})
		void givenInvalidDto_whenToEntity_returnNull(String input) {
			// given
			dto = input;

			// when
			LocalDateTime actual = mapper.toEntity(dto);

			// then
			Assertions.assertThat(actual).isNull();
		}

		@ParameterizedTest
		@ValueSource(strings = {"2011-12-03T10:15:30", "2013-12-03T23:10:30"})
		void givenValidDto_whenToEntity_returnEntity(String input) {
			// given
			dto = input;
			LocalDateTime expected = LocalDateTime.parse(dto, LocalDateTimeMapper.formatter);

			// when
			LocalDateTime actual = mapper.toEntity(dto);

			// then
			Assertions.assertThat(actual).isEqualTo(expected);
		}
	}

	@Nested
	class ToDto {
		private LocalDateTime entity;

		@Test
		void givenNullEntity_whenToDto_returnNull() {
			// given
			entity = null;

			// when
			String actual = mapper.toDto(entity);

			// then
			Assertions.assertThat(actual).isNull();
		}

		@ParameterizedTest
		@CsvSource(value = {
				"2020, 3, 12, 5, 10, 3",
				"2021, 4, 5, 23, 10 , 2",
				"1990, 7, 5, 23, 10 , 2",})
		void givenEntity_whenToDto_returnStringFormat(int year, int month, int dayOfMonth, int hour, int minute, int second) {
			// given
			entity = LocalDateTime.of(year, month, dayOfMonth, hour, minute, second);
			String expected = LocalDateTimeMapper.formatter.format(entity);

			// when
			String actual = mapper.toDto(entity);

			// then
			Assertions.assertThat(actual).isEqualTo(expected);
		}
	}
}
