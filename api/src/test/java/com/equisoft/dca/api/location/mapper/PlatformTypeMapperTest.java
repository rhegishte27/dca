package com.equisoft.dca.api.location.mapper;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.equisoft.dca.api.location.dto.PlatformTypeDto;
import com.equisoft.dca.backend.common.model.BaseEnum;
import com.equisoft.dca.backend.location.model.PlatformType;

class PlatformTypeMapperTest {
	private PlatformTypeMapper mapper;

	@BeforeEach
	void setUp() {
		mapper = new PlatformTypeMapperImpl();
	}

	@Nested
	class ToEntity {
		private PlatformTypeDto dto;

		@Test
		void givenDtoNull_whenToEntity_returnNull() {
			// given
			dto = null;

			// when
			PlatformType actual = mapper.toEntity(dto);

			// then
			Assertions.assertThat(actual).isNull();
		}

		@Test
		void givenIdDtoNull_whenToEntity_returnNull() {
			// given
			dto = PlatformTypeDto.builder().id(null).build();

			// when
			PlatformType actual = mapper.toEntity(dto);

			// then
			Assertions.assertThat(actual).isNull();
		}

		@ParameterizedTest
		@ValueSource(ints = {0, 20, -2, 9999})
		void givenInvalidDtoId_whenToEntity_throwIllegalArgumentException(Integer id) {
			// given
			dto = PlatformTypeDto.builder().id(id).build();

			Class expected = IllegalArgumentException.class;

			// when
			Throwable actual = Assertions.catchThrowable(() -> mapper.toEntity(dto));

			// then
			Assertions.assertThat(actual).isInstanceOf(expected);
		}

		@ParameterizedTest
		@ValueSource(ints = {1, 2, 3, 4, 5})
		void givenDtoWithRightId_whenMap_returnProjectWithRightData(Integer id) {
			// given
			dto = PlatformTypeDto.builder().id(id).build();
			PlatformType expected = BaseEnum.valueOf(PlatformType.class, id);

			// when
			PlatformType actual = mapper.toEntity(dto);

			// then
			Assertions.assertThat(actual).isEqualTo(expected);
		}
	}
}
