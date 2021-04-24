package com.equisoft.dca.api.project.mapper;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.equisoft.dca.api.project.dto.GeneratedCodeLanguageDto;
import com.equisoft.dca.backend.common.model.BaseEnum;
import com.equisoft.dca.backend.project.model.GeneratedCodeLanguage;

class GeneratedCodeLanguageMapperTest {
	private GeneratedCodeLanguageMapper mapper;

	@BeforeEach
	void setUp() {
		mapper = new GeneratedCodeLanguageMapperImpl();
	}

	@Nested
	class ToEntity {
		private GeneratedCodeLanguageDto dto;

		@Test
		void givenDtoNull_whenToEntity_returnNull() {
			// given
			dto = null;

			// when
			GeneratedCodeLanguage actual = mapper.toEntity(dto);

			// then
			Assertions.assertThat(actual).isNull();
		}

		@Test
		void givenIdDtoNull_whenToEntity_returnNull() {
			// given
			dto = GeneratedCodeLanguageDto.builder().build();

			// when
			GeneratedCodeLanguage actual = mapper.toEntity(dto);

			// then
			Assertions.assertThat(actual).isNull();
		}

		@ParameterizedTest
		@ValueSource(ints = {0, 20, -2, 9999})
		void givenInvalidDtoId_whenToEntity_throwIllegalArgumentException(Integer id) {
			// given
			dto = GeneratedCodeLanguageDto.builder().id(id).build();

			Class expected = IllegalArgumentException.class;

			// when
			Throwable actual = Assertions.catchThrowable(() -> mapper.toEntity(dto));

			// then
			Assertions.assertThat(actual).isInstanceOf(expected);
		}

		@ParameterizedTest
		@ValueSource(ints = {1, 2})
		void givenDtoWithRightId_whenMap_returnProjectWithRightData(Integer id) {
			// given
			dto = GeneratedCodeLanguageDto.builder().id(id).build();
			GeneratedCodeLanguage expected = BaseEnum.valueOf(GeneratedCodeLanguage.class, id);

			// when
			GeneratedCodeLanguage actual = mapper.toEntity(dto);

			// then
			Assertions.assertThat(actual).isEqualTo(expected);
		}
	}
}
