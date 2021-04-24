package com.equisoft.dca.api.language;

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

import com.equisoft.dca.api.language.dto.LanguageDto;
import com.equisoft.dca.api.language.mapper.LanguageMapper;
import com.equisoft.dca.backend.language.model.Language;

@ExtendWith(MockitoExtension.class)
class LanguageResourceTest {

	private LanguageResource languageResource;

	@Mock
	private LanguageMapper languageMapper;

	@BeforeEach
	void setUp() {
		this.languageResource = new LanguageResource(languageMapper);
	}

	@Nested
	class FindAll {

		@Test
		void givenGenerateCodeLanguages_whenFindAll_thenReturnStatusCodeOkAndBodyWithLanguageList() {
			//given
			List<Language> languages = Arrays.asList(Language.values());
			Mockito.when(languageMapper.toDtoList(languages)).thenReturn(getLstDto());

			ResponseEntity expected = ResponseEntity.ok().body(getLstDto());

			//when
			ResponseEntity<List<LanguageDto>> actual = languageResource.findAll();

			//then
			Assertions.assertThat(actual)
					.isNotNull()
					.extracting(ResponseEntity::getStatusCode, ResponseEntity::getBody)
					.containsExactly(expected.getStatusCode(), expected.getBody());
		}

		private List<LanguageDto> getLstDto() {
			return List.of(LanguageDto.builder().id(1).name("name1").code("code").build(),
					LanguageDto.builder().id(2).name("name2").code("code").build());
		}
	}
}
