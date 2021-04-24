package com.equisoft.dca.api.project.mapper;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.equisoft.dca.api.project.dto.SystemTypeDto;
import com.equisoft.dca.backend.common.model.BaseEnum;
import com.equisoft.dca.backend.project.model.SystemType;

public class SystemTypeMapperTest {

	private SystemTypeMapper mapper;

	@BeforeEach
	void setUp() {
		mapper = new SystemTypeMapperImpl();
	}

	@Nested
	class ToEntity {

		private SystemTypeDto dto;

		@Test
		void givenDtoNull_whenToEntity_returnNull() {
			// given

			// when
			SystemType actual = mapper.toEntity(null);

			// then
			Assertions.assertThat(actual).isNull();
		}

		@Test
		void givenIdDtoNull_whenToEntity_returnNull() {
			// given
			dto = SystemTypeDto.builder().build();

			// when
			SystemType actual = mapper.toEntity(dto);

			// then
			Assertions.assertThat(actual).isNull();
		}

		@ParameterizedTest
		@ValueSource(ints = {0, 20, -2, 9999})
		void givenInvalidDtoId_whenToEntity_throwIllegalArgumentException(Integer id) {
			// given
			dto = SystemTypeDto.builder().id(id).build();

			Class expected = IllegalArgumentException.class;

			// when
			Throwable actual = Assertions.catchThrowable(() -> mapper.toEntity(dto));

			// then
			Assertions.assertThat(actual).isInstanceOf(expected);
		}

		@ParameterizedTest
		@ValueSource(ints = {1, 2, 3})
		void givenDtoWithRightId_whenMap_returnProjectWithRightData(Integer id) {
			// given
			dto = SystemTypeDto.builder().id(id).build();
			SystemType expected = BaseEnum.valueOf(SystemType.class, id);

			// when
			SystemType actual = mapper.toEntity(dto);

			// then
			Assertions.assertThat(actual).isEqualTo(expected);
		}
	}
}
