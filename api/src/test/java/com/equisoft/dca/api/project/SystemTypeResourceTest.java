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

import com.equisoft.dca.api.project.dto.SystemTypeDto;
import com.equisoft.dca.api.project.mapper.SystemTypeMapper;
import com.equisoft.dca.backend.project.model.SystemType;

@ExtendWith(MockitoExtension.class)
class SystemTypeResourceTest {

	private SystemTypeResource systemTypeResource;

	@Mock
	private SystemTypeMapper mapper;

	@BeforeEach
	void setUp() {
		this.systemTypeResource = new SystemTypeResource(mapper);
	}

	@Nested
	class FindAll {

		@Test
		void givenCompilers_whenFindAll_thenReturnStatusCodeOkAndBodyWithCompilerList() {
			//given
			List<SystemType> systemTypes = Arrays.asList(SystemType.values());
			Mockito.when(mapper.toDtoList(systemTypes)).thenReturn(getLstDto());

			ResponseEntity expected = ResponseEntity.ok().body(getLstDto());

			//when
			ResponseEntity<List<SystemTypeDto>> actual = systemTypeResource.findAll();

			//then
			Assertions.assertThat(actual)
					.isNotNull()
					.extracting(ResponseEntity::getStatusCode, ResponseEntity::getBody)
					.containsExactly(expected.getStatusCode(), expected.getBody());
		}

		private List<SystemTypeDto> getLstDto() {
			return List.of(
					SystemTypeDto.builder().id(1).name("test").build(),
					SystemTypeDto.builder().id(2).name("test2").build()
			);
		}
	}
}
