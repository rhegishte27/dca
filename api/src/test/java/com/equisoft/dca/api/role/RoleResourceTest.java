package com.equisoft.dca.api.role;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
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
import com.equisoft.dca.api.role.dto.RoleDto;
import com.equisoft.dca.api.role.mapper.RoleMapper;
import com.equisoft.dca.backend.user.model.Feature;
import com.equisoft.dca.backend.user.model.Role;

@ExtendWith(MockitoExtension.class)
class RoleResourceTest {
	@Mock
	private RoleMapper mapper;

	private RoleResource roleResource;

	@BeforeEach
	void init() {
		this.roleResource = new RoleResource(mapper);
	}

	@Nested
	class FindAll {

		@Test
		void givenServiceReturnLstRole_whenFindAllRoles_thenReturnStatusCodeOkAndRolesList() {
			//given
			List<Role> roles = Arrays.asList(Role.values());
			List<RoleDto> roleDtos = createRoleDtoList(roles);

			ResponseEntity expected = ResponseEntity.ok().body(roleDtos);

			Mockito.when(mapper.toDtoList(roles)).thenReturn(roleDtos);

			//when
			ResponseEntity<List<RoleDto>> actual = roleResource.findAll();

			//then
			Assertions.assertThat(actual)
					.isNotNull()
					.extracting(ResponseEntity::getStatusCode, ResponseEntity::getBody)
					.containsExactly(expected.getStatusCode(), expected.getBody());
		}
	}

	private RoleDto createRoleDto(Role role) {
		return RoleDto.builder()
				.id(role.getId())
				.name(role.getName())
				.level(role.getLevel())
				.defaultFeatures(createFeatureDtoSet(role.getDefaultFeatures()))
				.nonEditableFeatures(createFeatureDtoSet(role.getNonEditableFeatures()))
				.build();
	}

	private List<RoleDto> createRoleDtoList(List<Role> roles) {
		return roles
				.stream()
				.map(this::createRoleDto)
				.collect(Collectors.toList());
	}

	private FeatureDto createFeatureDto(Feature feature) {
		return FeatureDto.builder()
				.id(feature.getId())
				.name(feature.getName())
				.build();
	}

	private Set<FeatureDto> createFeatureDtoSet(Set<Feature> features) {
		return features.stream()
				.map(this::createFeatureDto)
				.collect(Collectors.toSet());
	}
}
