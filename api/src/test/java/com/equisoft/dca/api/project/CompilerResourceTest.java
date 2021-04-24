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

import com.equisoft.dca.api.project.dto.CompilerDto;
import com.equisoft.dca.api.project.mapper.CompilerMapper;
import com.equisoft.dca.backend.project.model.Compiler;

@ExtendWith(MockitoExtension.class)
class CompilerResourceTest {

	private CompilerResource compilerResource;

	@Mock
	private CompilerMapper mapper;

	@BeforeEach
	void setUp() {
		this.compilerResource = new CompilerResource(mapper);
	}

	@Nested
	class FindAll {

		@Test
		void givenCompilers_whenFindAll_thenReturnStatusCodeOkAndBodyWithCompilerList() {
			//given
			List<Compiler> compilers = Arrays.asList(Compiler.values());
			Mockito.when(mapper.toDtoList(compilers)).thenReturn(getLstDto());

			ResponseEntity expected = ResponseEntity.ok().body(getLstDto());

			//when
			ResponseEntity<List<CompilerDto>> actual = compilerResource.findAll();

			//then
			Assertions.assertThat(actual)
					.isNotNull()
					.extracting(ResponseEntity::getStatusCode, ResponseEntity::getBody)
					.containsExactly(expected.getStatusCode(), expected.getBody());
		}

		private List<CompilerDto> getLstDto() {
			return List.of(
					CompilerDto.builder().id(1).name("test").build(),
					CompilerDto.builder().id(2).name("test2").build()
			);
		}
	}
}
