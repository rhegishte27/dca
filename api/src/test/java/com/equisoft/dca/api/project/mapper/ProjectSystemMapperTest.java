package com.equisoft.dca.api.project.mapper;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import com.equisoft.dca.api.project.dto.ProjectSystemDto;
import com.equisoft.dca.api.system.dto.SystemDto;
import com.equisoft.dca.backend.project.model.Project;
import com.equisoft.dca.backend.project.model.ProjectSystem.ProjectSystemId;
import com.equisoft.dca.backend.system.model.System;

class ProjectSystemMapperTest {
	private ProjectSystemMapper mapper;

	@BeforeEach
	void setup() {
		mapper = new ProjectSystemMapperImpl();
	}

	@Nested
	class GetId {
		@Nested
		class GivenDtoNull {
			@Test
			void givenDtoNull_whenGetId_returnNull() {
				// given

				// when
				ProjectSystemId actual = mapper.getId(null);

				// then
				Assertions.assertThat(actual).isNull();
			}
		}

		@Nested
		class GivenDtoNotNull {
			private ProjectSystemDto.ProjectSystemDtoBuilder dtoBuilder;

			@BeforeEach
			void setUp() {
				dtoBuilder = ProjectSystemDto.builder();
			}

			@ParameterizedTest
			@ValueSource(ints = {1, 2, 3, 0, -10, 9999})
			void givenSystemNull_whenGetId_throwIllegalArgumentException(Integer projectId) {
				// given
				dtoBuilder = dtoBuilder.projectId(projectId).system(null);
				Class expected = IllegalArgumentException.class;

				// when
				Throwable actual = Assertions.catchThrowable(() -> mapper.getId(dtoBuilder.build()));

				// then
				Assertions.assertThat(actual).isInstanceOf(expected);
			}

			@ParameterizedTest
			@CsvSource(value = {"1, 1", "1, 2", "2, 3", ", 3"})
			void givenSystemValid_whenGetId_returnRightId(Integer projectId, Integer systemId) {
				// given
				SystemDto systemDto = SystemDto.builder()
						.id(systemId)
						.identifier("test")
						.description("test")
						.build();

				dtoBuilder = dtoBuilder
						.projectId(projectId)
						.system(systemDto);
				ProjectSystemId expected = new ProjectSystemId(Project.builder().id(projectId).build(), System.builder().id(systemDto.getId()).build());

				// when
				ProjectSystemId actual = mapper.getId(dtoBuilder.build());

				// then
				Assertions.assertThat(actual).isEqualTo(expected);
			}
		}
	}
}
