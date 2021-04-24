package com.equisoft.dca.api.dataobject;

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

import com.equisoft.dca.api.dataobject.dto.DataObjectTypeDto;
import com.equisoft.dca.api.dataobject.mapper.DataObjectTypeMapper;
import com.equisoft.dca.backend.dataobject.model.DataObjectType;

@ExtendWith(MockitoExtension.class)
class DataObjectTypeResourceTest {

	private DataObjectTypeResource resource;

	@Mock
	private DataObjectTypeMapper mapper;

	@BeforeEach
	void setUp() {
		resource = new DataObjectTypeResource(mapper);
	}

	@Nested
	class FindAll {

		@Test
		void givenDataObjectTypes_whenFindAll_thenReturnStatusCodeOkAndBodyWithDataObjectTypeList() {
			//given
			List<DataObjectType> backupIntervals = Arrays.asList(DataObjectType.values());
			Mockito.when(mapper.toDtoList(backupIntervals)).thenReturn(getLstDto());

			ResponseEntity expected = ResponseEntity.ok().body(getLstDto());

			//when
			ResponseEntity<List<DataObjectTypeDto>> actual = resource.findAll();

			//then
			Assertions.assertThat(actual)
					.isNotNull()
					.extracting(ResponseEntity::getStatusCode, ResponseEntity::getBody)
					.containsExactly(expected.getStatusCode(), expected.getBody());
		}

		private List<DataObjectTypeDto> getLstDto() {
			return List.of(
					DataObjectTypeDto.builder().id(1).name("test").build(),
					DataObjectTypeDto.builder().id(2).name("test2").build()
			);
		}
	}
}
