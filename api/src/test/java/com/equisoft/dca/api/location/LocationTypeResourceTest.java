package com.equisoft.dca.api.location;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import com.equisoft.dca.api.location.dto.LocationTypeDto;
import com.equisoft.dca.api.location.mapper.LocationTypeMapper;
import com.equisoft.dca.backend.location.model.LocationType;

@ExtendWith(MockitoExtension.class)
class LocationTypeResourceTest {

	private LocationTypeResource locationTypeResource;

	@Mock
	private LocationTypeMapper locationTypeMapper;

	@BeforeEach
	void setUp() {
		locationTypeResource = new LocationTypeResource(locationTypeMapper);
	}

	@Nested
	class FindAll {

		@Test
		void givenLocationTypes_whenFindAll_thenReturnStatusCodeOkAndBodyWithLocationTypeList() {
			//given
			List<LocationType> locationTypes = Arrays.asList(LocationType.values());
			List<LocationTypeDto> locationTypeDtos = createLocationTypeDtoList();
			Mockito.when(locationTypeMapper.toDtoList(locationTypes)).thenReturn(locationTypeDtos);
			ResponseEntity expected = ResponseEntity.ok().body(locationTypeDtos);

			//when
			ResponseEntity<List<LocationTypeDto>> actual = locationTypeResource.findAll();

			//then
			Assertions.assertThat(actual)
					.isNotNull()
					.extracting(ResponseEntity::getStatusCode, ResponseEntity::getBody)
					.containsExactly(expected.getStatusCode(), expected.getBody());
		}

		private List<LocationTypeDto> createLocationTypeDtoList() {
			return Arrays.asList(LocationType.values()).stream()
					.map(e -> LocationTypeDto.builder().id(e.getId()).name(e.getName()).build())
					.collect(Collectors.toList());
		}
	}
}
