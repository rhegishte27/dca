package com.equisoft.dca.api.feature.mapper;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.equisoft.dca.api.feature.dto.FeatureDto;
import com.equisoft.dca.backend.common.model.BaseEnum;
import com.equisoft.dca.backend.user.model.Feature;

class FeatureMapperTest {
	private FeatureMapper mapper;

	@BeforeEach
	void setUp() {
		mapper = new FeatureMapperImpl();
	}

	@Nested
	class ToEntity {
		private FeatureDto dto;

		@Test
		void givenDtoNull_whenToEntity_returnNull() {
			// given
			dto = null;

			// when
			Feature actual = mapper.toEntity(dto);

			// then
			Assertions.assertThat(actual).isNull();
		}

		@Test
		void givenIdDtoNull_whenToEntity_returnNull() {
			// given
			dto = FeatureDto.builder().build();

			// when
			Feature actual = mapper.toEntity(dto);

			// then
			Assertions.assertThat(actual).isNull();
		}

		@ParameterizedTest
		@ValueSource(ints = {0, 20, -2, 9999})
		void givenInvalidDtoId_whenToEntity_throwIllegalArgumentException(Integer id) {
			// given
			dto = FeatureDto.builder().id(id).build();

			Class expected = IllegalArgumentException.class;

			// when
			Throwable actual = Assertions.catchThrowable(() -> mapper.toEntity(dto));

			// then
			Assertions.assertThat(actual).isInstanceOf(expected);
		}

		@ParameterizedTest
		@ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8, 9})
		void givenDtoWithRightId_whenMap_returnProjectWithRightData(Integer id) {
			// given
			dto = FeatureDto.builder().id(id).build();
			Feature expected = BaseEnum.valueOf(Feature.class, id);

			// when
			Feature actual = mapper.toEntity(dto);

			// then
			Assertions.assertThat(actual).isEqualTo(expected);
		}
	}
}
