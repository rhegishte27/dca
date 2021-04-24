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

import com.equisoft.dca.api.dataobject.dto.DataObjectLocationTypeDto;
import com.equisoft.dca.api.dataobject.mapper.DataObjectLocationTypeMapper;
import com.equisoft.dca.backend.dataobject.model.DataObjectLocationType;

@ExtendWith(MockitoExtension.class)
class DataObjectLocationTypeResourceTest {

	private DataObjectLocationTypeResource resource;

	@Mock
	private DataObjectLocationTypeMapper mapper;

	@BeforeEach
	void setUp() {
		resource = new DataObjectLocationTypeResource(mapper);
	}

	@Nested
	class FindAll {

		@Test
		void givenDataObjectLocationTypes_whenFindAll_thenReturnStatusCodeOkAndBodyWithDataObjectLocationTypeList() {
			//given
			List<DataObjectLocationType> backupIntervals = Arrays.asList(DataObjectLocationType.values());
			Mockito.when(mapper.toDtoList(backupIntervals)).thenReturn(getLstDto());

			ResponseEntity expected = ResponseEntity.ok().body(getLstDto());

			//when
			ResponseEntity<List<DataObjectLocationTypeDto>> actual = resource.findAll();

			//then
			Assertions.assertThat(actual)
					.isNotNull()
					.extracting(ResponseEntity::getStatusCode, ResponseEntity::getBody)
					.containsExactly(expected.getStatusCode(), expected.getBody());
		}

		private List<DataObjectLocationTypeDto> getLstDto() {
			return List.of(
					DataObjectLocationTypeDto.builder().id(1).name("test").build(),
					DataObjectLocationTypeDto.builder().id(2).name("test2").build()
			);
		}
	}
}
