package com.equisoft.dca.api.language.mapper;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.equisoft.dca.api.language.dto.LanguageDto;
import com.equisoft.dca.backend.common.model.BaseEnum;
import com.equisoft.dca.backend.language.model.Language;

class LanguageMapperTest {
	private LanguageMapper mapper;

	@BeforeEach
	void setUp() {
		mapper = new LanguageMapperImpl();
	}

	@Nested
	class ToEntity {
		private LanguageDto dto;

		@Test
		void givenDtoNull_whenToEntity_returnNull() {
			// given
			dto = null;

			// when
			Language actual = mapper.toEntity(dto);

			// then
			Assertions.assertThat(actual).isNull();
		}

		@Test
		void givenIdDtoNull_whenToEntity_returnNull() {
			// given
			dto = LanguageDto.builder().id(null).build();

			// when
			Language actual = mapper.toEntity(dto);

			// then
			Assertions.assertThat(actual).isNull();
		}

		@ParameterizedTest
		@ValueSource(ints = {0, 20, -2, 9999})
		void givenInvalidDtoId_whenToEntity_throwIllegalArgumentException(Integer id) {
			// given
			dto = LanguageDto.builder().id(id).build();

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
			dto = LanguageDto.builder().id(id).build();
			Language expected = BaseEnum.valueOf(Language.class, id);

			// when
			Language actual = mapper.toEntity(dto);

			// then
			Assertions.assertThat(actual).isEqualTo(expected);
		}
	}
}
