package com.equisoft.dca.api.project;

import java.util.Arrays;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import com.equisoft.dca.api.project.dto.GeneratedCodeLanguageDto;
import com.equisoft.dca.api.project.mapper.GeneratedCodeLanguageMapper;
import com.equisoft.dca.backend.project.model.GeneratedCodeLanguage;

@ExtendWith(MockitoExtension.class)
class GeneratedCodeLanguageResourceTest {

	private GeneratedCodeLanguageResource generatedCodeLanguageResource;

	@Mock
	private GeneratedCodeLanguageMapper mapper;

	@BeforeEach
	void setUp() {
		this.generatedCodeLanguageResource = new GeneratedCodeLanguageResource(mapper);
	}

	@Nested
	class FindAll {

		@Test
		void givenGenerateCodeLanguages_whenFindAll_thenReturnStatusCodeOkAndBodyWithGeneratedCodeLanguageList() {
			//given
			List<GeneratedCodeLanguage> generatedCodeLanguages = Arrays.asList(GeneratedCodeLanguage.values());
			Mockito.when(mapper.toDtoList(generatedCodeLanguages)).thenReturn(getLstDto());

			ResponseEntity expected = ResponseEntity.ok().body(getLstDto());

			//when
			ResponseEntity<List<GeneratedCodeLanguageDto>> actual = generatedCodeLanguageResource.findAll();

			//then
			Assertions.assertThat(actual)
					.isNotNull()
					.extracting(ResponseEntity::getStatusCode, ResponseEntity::getBody)
					.containsExactly(expected.getStatusCode(), expected.getBody());
		}

		private List<GeneratedCodeLanguageDto> getLstDto() {
			return List.of(
					GeneratedCodeLanguageDto.builder().id(1).name("test").build(),
					GeneratedCodeLanguageDto.builder().id(2).name("test2").build()
			);
		}
	}
}
