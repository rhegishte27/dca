package com.equisoft.dca.api.project.mapper;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import com.equisoft.dca.api.project.dto.ProjectSyncSettingDto;
import com.equisoft.dca.api.project.dto.TypeProjectElementDto;
import com.equisoft.dca.backend.common.model.BaseEnum;
import com.equisoft.dca.backend.project.model.Project;
import com.equisoft.dca.backend.project.model.ProjectSyncSetting.ProjectSyncSettingId;
import com.equisoft.dca.backend.project.model.TypeProjectElement;

class ProjectSyncSettingMapperTest {
	private ProjectSyncSettingMapper mapper;

	@BeforeEach
	void setup() {
		mapper = new ProjectSyncSettingMapperImpl();
	}

	@Nested
	class GetId {
		@Nested
		class GivenDtoNull {
			@Test
			void givenDtoNull_whenGetId_returnNull() {
				// given

				// when
				ProjectSyncSettingId actual = mapper.getId(null);

				// then
				Assertions.assertThat(actual).isNull();
			}
		}

		@Nested
		class GivenDtoNotNull {
			private ProjectSyncSettingDto.ProjectSyncSettingDtoBuilder dtoBuilder;

			@BeforeEach
			void setUp() {
				dtoBuilder = ProjectSyncSettingDto.builder();
			}

			@ParameterizedTest
			@ValueSource(ints = {1, 2, 3, 0, -10, 9999})
			void givenTypeProjectElementNull_whenGetId_throwIllegalArgumentException(Integer projectId) {
				// given
				dtoBuilder = dtoBuilder.projectId(projectId).typeProjectElement(null);
				Class expected = IllegalArgumentException.class;

				// when
				Throwable actual = Assertions.catchThrowable(() -> mapper.getId(dtoBuilder.build()));

				// then
				Assertions.assertThat(actual).isInstanceOf(expected);
			}

			@ParameterizedTest
			@CsvSource(value = {"1, -10", "1, 0", "2, 999"})
			void givenTypeProjectElementInvalid_whenGetId_throwIllegalArgumentException(Integer projectId, Integer typeProjectElementId) {
				// given
				dtoBuilder = dtoBuilder
						.projectId(projectId)
						.typeProjectElement(TypeProjectElementDto.builder()
								.id(typeProjectElementId)
								.build());
				Class expected = IllegalArgumentException.class;

				// when
				Throwable actual = Assertions.catchThrowable(() -> mapper.getId(dtoBuilder.build()));

				// then
				Assertions.assertThat(actual).isInstanceOf(expected);
			}

			@ParameterizedTest
			@CsvSource(value = {"1, 1", "1, 2", "2, 3", ", 3"})
			void givenTypeProjectElementValid_whenGetId_returnRightId(Integer projectId, Integer typeProjectElementId) {
				// given
				dtoBuilder = dtoBuilder
						.projectId(projectId)
						.typeProjectElement(TypeProjectElementDto.builder()
								.id(typeProjectElementId)
								.build());
				ProjectSyncSettingId expected = new ProjectSyncSettingId(Project.builder().id(projectId).build(),
						BaseEnum.valueOf(TypeProjectElement.class, typeProjectElementId));

				// when
				ProjectSyncSettingId actual = mapper.getId(dtoBuilder.build());

				// then
				Assertions.assertThat(actual)
						.extracting(ProjectSyncSettingId::getProject, ProjectSyncSettingId::getTypeProjectElement)
						.containsExactly(expected.getProject(), expected.getTypeProjectElement());
			}
		}
	}
}
