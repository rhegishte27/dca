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

import com.equisoft.dca.api.location.dto.PlatformTypeDto;
import com.equisoft.dca.api.location.mapper.PlatformTypeMapper;
import com.equisoft.dca.backend.location.model.PlatformType;

@ExtendWith(MockitoExtension.class)
class PlatformTypeResourceTest {

	private PlatformTypeResource platformTypeResource;

	@Mock
	private PlatformTypeMapper platformTypeMapper;

	@BeforeEach
	void setUp() {
		platformTypeResource = new PlatformTypeResource(platformTypeMapper);
	}

	@Nested
	class FindAll {

		@Test
		void givenPlatformTypes_whenFindAll_thenReturnStatusCodeOkAndBodyWithPlatformTypeList() {
			//given
			List<PlatformType> platformTypes = Arrays.asList(PlatformType.values());
			List<PlatformTypeDto> platformTypeDtos = createPlatformTypeDtoList();
			Mockito.when(platformTypeMapper.toDtoList(platformTypes)).thenReturn(platformTypeDtos);
			ResponseEntity expected = ResponseEntity.ok().body(createPlatformTypeDtoList());

			//when
			ResponseEntity<List<PlatformTypeDto>> actual = platformTypeResource.findAll();

			//then
			Assertions.assertThat(actual)
					.isNotNull()
					.extracting(ResponseEntity::getStatusCode, ResponseEntity::getBody)
					.containsExactly(expected.getStatusCode(), expected.getBody());
		}

		private List<PlatformTypeDto> createPlatformTypeDtoList() {
			return Arrays.asList(PlatformType.values()).stream()
					.map(e -> PlatformTypeDto.builder().id(e.getId()).name(e.getName()).build())
					.collect(Collectors.toList());
		}
	}
}
