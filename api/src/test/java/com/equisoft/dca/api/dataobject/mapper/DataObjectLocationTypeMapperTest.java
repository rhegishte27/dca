package com.equisoft.dca.api.dataobject.mapper;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.equisoft.dca.api.dataobject.dto.DataObjectLocationTypeDto;
import com.equisoft.dca.backend.common.model.BaseEnum;
import com.equisoft.dca.backend.dataobject.model.DataObjectLocationType;

class DataObjectLocationTypeMapperTest {
	private DataObjectLocationTypeMapper mapper;

	@BeforeEach
	void setUp() {
		mapper = new DataObjectLocationTypeMapperImpl();
	}

	@Nested
	class ToEntity {
		private DataObjectLocationTypeDto dto;

		@Test
		void givenDtoNull_whenToEntity_returnNull() {
			// given
			dto = null;

			// when
			DataObjectLocationType actual = mapper.toEntity(dto);

			// then
			Assertions.assertThat(actual).isNull();
		}

		@Test
		void givenIdDtoNull_whenToEntity_returnNull() {
			// given
			dto = DataObjectLocationTypeDto.builder().build();

			// when
			DataObjectLocationType actual = mapper.toEntity(dto);

			// then
			Assertions.assertThat(actual).isNull();
		}

		@ParameterizedTest
		@ValueSource(ints = {0, 20, -2, 9999})
		void givenInvalidDtoId_whenToEntity_throwIllegalArgumentException(Integer id) {
			// given
			dto = DataObjectLocationTypeDto.builder().id(id).build();

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
			dto = DataObjectLocationTypeDto.builder().id(id).build();
			DataObjectLocationType expected = BaseEnum.valueOf(DataObjectLocationType.class, id);

			// when
			DataObjectLocationType actual = mapper.toEntity(dto);

			// then
			Assertions.assertThat(actual).isEqualTo(expected);
		}
	}
}
