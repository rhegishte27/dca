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

import com.equisoft.dca.api.project.dto.TypeProjectElementDto;
import com.equisoft.dca.api.project.mapper.TypeProjectElementMapper;
import com.equisoft.dca.backend.project.model.TypeProjectElement;

@ExtendWith(MockitoExtension.class)
class TypeProjectElementResourceTest {

	private TypeProjectElementResource typeProjectElementResource;

	@Mock
	private TypeProjectElementMapper mapper;

	@BeforeEach
	void setUp() {
		this.typeProjectElementResource = new TypeProjectElementResource(mapper);
	}

	@Nested
	class FindAll {

		@Test
		void givenTypeProjectElements_whenFindAll_thenReturnStatusCodeOkAndBodyWithTypeProjectElementList() {
			//given
			List<TypeProjectElement> typeProjectElements = Arrays.asList(TypeProjectElement.values());
			Mockito.when(mapper.toDtoList(typeProjectElements)).thenReturn(getLstDto());

			ResponseEntity expected = ResponseEntity.ok().body(getLstDto());

			//when
			ResponseEntity<List<TypeProjectElementDto>> actual = typeProjectElementResource.findAll();

			//then
			Assertions.assertThat(actual)
					.isNotNull()
					.extracting(ResponseEntity::getStatusCode, ResponseEntity::getBody)
					.containsExactly(expected.getStatusCode(), expected.getBody());
		}

		private List<TypeProjectElementDto> getLstDto() {
			return List.of(
					TypeProjectElementDto.builder().id(1).name("test").build(),
					TypeProjectElementDto.builder().id(2).name("test2").build()
			);
		}
	}
}
