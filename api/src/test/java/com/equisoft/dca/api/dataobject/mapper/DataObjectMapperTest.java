package com.equisoft.dca.api.dataobject.mapper;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import com.equisoft.dca.api.dataobject.dto.DataObjectDto;
import com.equisoft.dca.backend.system.model.System;

class DataObjectMapperTest {
	private DataObjectMapper mapper;

	@BeforeEach
	void setup() {
		mapper = new DataObjectMapperImpl();
	}

	@Nested
	class GetSystem {
		@Nested
		class GivenDtoNull {
			@Test
			void givenDtoNull_whenGetId_returnNull() {
				// given

				// when
				System actual = mapper.getSystem(null);

				// then
				Assertions.assertThat(actual).isNull();
			}
		}

		@Nested
		class GivenDtoNotNull {
			private DataObjectDto.DataObjectDtoBuilder dtoBuilder;

			@BeforeEach
			void setUp() {
				dtoBuilder = DataObjectDto.builder();
			}

			@ParameterizedTest
			@NullSource
			void givenSystemIdNull_whenGetId_throwIllegalArgumentException(Integer systemId) {
				// given
				dtoBuilder = dtoBuilder.systemId(systemId);
				Class expected = IllegalArgumentException.class;

				// when
				Throwable actual = Assertions.catchThrowable(() -> mapper.getSystem(dtoBuilder.build()));

				// then
				Assertions.assertThat(actual).isInstanceOf(expected);
			}

			@ParameterizedTest
			@ValueSource(ints = {1, 2, 3, 0, 9999, -10000})
			void givenSystemIdValid_whenGetId_returnRightId(Integer systemId) {
				// given
				dtoBuilder = dtoBuilder.systemId(systemId);
				System expected = System.builder().id(systemId).build();

				// when
				System actual = mapper.getSystem(dtoBuilder.build());

				// then
				Assertions.assertThat(actual).isEqualTo(expected);
			}
		}
	}
}
