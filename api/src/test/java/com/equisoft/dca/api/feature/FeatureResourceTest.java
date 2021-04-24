package com.equisoft.dca.api.feature;

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

import com.equisoft.dca.api.feature.dto.FeatureDto;
import com.equisoft.dca.api.feature.mapper.FeatureMapper;
import com.equisoft.dca.backend.user.model.Feature;

@ExtendWith(MockitoExtension.class)
class FeatureResourceTest {

	@Mock
	private FeatureMapper mapper;

	private FeatureResource featureResource;

	@BeforeEach
	void setUp() {
		this.featureResource = new FeatureResource(mapper);
	}

	@Nested
	class FindAll {

		@Test
		void givenExistingFeatures_whenFindAll_thenReturnStatusCodeOkAndBodyWithFeatureList() {
			//given
			List<Feature> features = Arrays.asList(Feature.values());
			List<FeatureDto> featureDtos = createFeatureDtoList(features);

			Mockito.when(mapper.toDtoList(features)).thenReturn(featureDtos);
			ResponseEntity expected = ResponseEntity.ok().body(featureDtos);

			//when
			ResponseEntity<List<FeatureDto>> actual = featureResource.findAll();

			//then
			Assertions.assertThat(actual)
					.isNotNull()
					.extracting(ResponseEntity::getStatusCode, ResponseEntity::getBody)
					.containsExactly(expected.getStatusCode(), expected.getBody());
		}
	}

	private FeatureDto createFeatureDto(Feature feature) {
		return FeatureDto.builder()
				.id(feature.getId())
				.name(feature.getName())
				.build();
	}

	private List<FeatureDto> createFeatureDtoList(List<Feature> features) {
		return features.stream()
				.map(this::createFeatureDto)
				.collect(Collectors.toList());
	}
}
