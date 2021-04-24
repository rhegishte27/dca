package com.equisoft.dca.api.user;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;

import com.equisoft.dca.api.feature.dto.FeatureDto;
import com.equisoft.dca.api.language.dto.LanguageDto;
import com.equisoft.dca.api.organization.dto.OrganizationDto;
import com.equisoft.dca.api.role.dto.RoleDto;
import com.equisoft.dca.api.user.dto.UserDto;
import com.equisoft.dca.api.user.mapper.UserMapper;
import com.equisoft.dca.backend.exception.EntityAlreadyExistsException;
import com.equisoft.dca.backend.exception.EntityNotFoundException;
import com.equisoft.dca.backend.language.model.Language;
import com.equisoft.dca.backend.user.model.Feature;
import com.equisoft.dca.backend.user.model.Organization;
import com.equisoft.dca.backend.user.model.Role;
import com.equisoft.dca.backend.user.model.User;
import com.equisoft.dca.backend.user.service.UserService;
import com.equisoft.dca.infra.security.context.AuthenticationFacade;

@ExtendWith(MockitoExtension.class)
class UserResourceTest {

	@Mock
	private UserService userService;

	@Mock
	private UserMapper mapper;

	@Mock
	private AuthenticationFacade authenticationFacade;

	private UserResource userResource;
	private User user;
	private UserDto userDto;

	@BeforeEach
	void setUp() {
		this.userResource = new UserResource(userService, mapper, authenticationFacade);
		this.user = createUser(1);
		this.userDto = createUserDto(user);
	}

	@Nested
	class Add {

		@Test
		void givenNoCurrentUser_whenUpdate_thenThrowAccessDeniedException() {
			//given
			Class expected = AccessDeniedException.class;

			Mockito.when(authenticationFacade.getUserIdentifier()).thenReturn(Optional.empty());

			//when
			Throwable actual = Assertions.catchThrowable(() -> userResource.add(userDto));

			//then
			Assertions.assertThat(actual).isInstanceOf(expected);
		}

		@Test
		void givenUserWithNameDuplicated_whenAddUser_thenThrowException() {
			//given
			Class expected = EntityAlreadyExistsException.class;
			String currentUserIdentifier = "identifier";

			Mockito.when(authenticationFacade.getUserIdentifier()).thenReturn(Optional.of(currentUserIdentifier));
			Mockito.when(userService.save(currentUserIdentifier, user)).thenThrow(EntityAlreadyExistsException.class);
			Mockito.when(mapper.toEntity(userDto)).thenReturn(user);

			//when
			Throwable actual = Assertions.catchThrowable(() -> userResource.add(userDto));

			//then
			Assertions.assertThat(actual).isInstanceOf(expected);
		}

		@Test
		void givenValidUser_whenAddUser_thenReturnStatusCodeCreatedAndUser() {
			//given
			ResponseEntity expected = ResponseEntity.created(URI.create("/users/" + user.getId())).body(userDto);
			String currentUserIdentifier = "identifier";

			Mockito.when(authenticationFacade.getUserIdentifier()).thenReturn(Optional.of(currentUserIdentifier));
			Mockito.when(mapper.toEntity(userDto)).thenReturn(user);
			Mockito.when(userService.save(ArgumentMatchers.anyString(), ArgumentMatchers.any(User.class))).thenReturn(user);
			Mockito.when(mapper.toDto(user)).thenReturn(userDto);

			//when
			ResponseEntity<UserDto> actual = userResource.add(userDto);

			//then
			Assertions.assertThat(actual)
					.isNotNull()
					.extracting(ResponseEntity::getStatusCode, ResponseEntity::getHeaders, ResponseEntity::getBody)
					.containsExactly(expected.getStatusCode(), expected.getHeaders(), expected.getBody());
		}
	}

	@Nested
	class Update {

		@Test
		void givenNoCurrentUser_whenUpdate_thenThrowAccessDeniedException() {
			//given
			Integer id = 1;
			Class expected = AccessDeniedException.class;

			Mockito.when(authenticationFacade.getUserIdentifier()).thenReturn(Optional.empty());

			//when
			Throwable actual = Assertions.catchThrowable(() -> userResource.update(id, userDto));

			//then
			Assertions.assertThat(actual).isInstanceOf(expected);
		}

		@Test
		void givenExistingUser_whenUpdateUser_thenReturnStatusCodeOkAndNoResponseBody() {
			//given
			String username = "username";
			ResponseEntity expected = ResponseEntity.ok().build();

			Mockito.when(authenticationFacade.getUserIdentifier()).thenReturn(Optional.of(username));
			Mockito.when(userService.update(ArgumentMatchers.anyString(), ArgumentMatchers.any())).thenReturn(user);

			//when
			ResponseEntity actual = userResource.update(userDto.getId(), userDto);

			//then
			Assertions.assertThat(actual)
					.isNotNull()
					.extracting(ResponseEntity::getStatusCode, ResponseEntity::getBody)
					.containsExactly(expected.getStatusCode(), expected.getBody());
		}
	}

	@Nested
	class Delete {

		@Test
		void givenUserId_whenDeleteUser_thenReturnStatusCodeNoContentAndNoResponseBody() {
			//given
			int id = 1;
			String username = "username";
			ResponseEntity expected = ResponseEntity.noContent().build();

			Mockito.when(authenticationFacade.getUserIdentifier()).thenReturn(Optional.of(username));

			//when
			ResponseEntity actual = userResource.delete(id);

			//then
			Assertions.assertThat(actual)
					.isNotNull()
					.extracting(ResponseEntity::getStatusCode, ResponseEntity::getBody)
					.containsExactly(expected.getStatusCode(), expected.getBody());
		}

		@Test
		void givenNoUserId_whenDeleteUser_thenThrowAccessDeniedException() {
			//given
			int id = 1;
			Class expected = AccessDeniedException.class;

			Mockito.when(authenticationFacade.getUserIdentifier()).thenReturn(Optional.empty());

			//when
			Throwable actual = Assertions.catchThrowable(() -> userResource.delete(id));

			//then
			Assertions.assertThat(actual).isInstanceOf(expected);
		}
	}

	@Nested
	class FindById {

		@Test
		void givenUserId_whenFindUserById_thenReturnStatusCodeOkAndUser() {
			//given
			ResponseEntity expected = ResponseEntity.ok().body(userDto);

			Mockito.when(userService.findById(ArgumentMatchers.anyInt())).thenReturn(Optional.of(user));
			Mockito.when(mapper.toDto(user)).thenReturn(userDto);

			//when
			ResponseEntity<UserDto> actual = userResource.findById(ArgumentMatchers.anyInt());

			//then
			Assertions.assertThat(actual)
					.isNotNull()
					.extracting(ResponseEntity::getStatusCode, ResponseEntity::getBody)
					.containsExactly(expected.getStatusCode(), expected.getBody());
		}

		@Test
		void givenInvalidUserId_whenFindUserById_thenThrowEntityNotFoundException() {
			//given
			Class expected = EntityNotFoundException.class;

			Mockito.when(userService.findById(ArgumentMatchers.anyInt())).thenReturn(Optional.empty());

			//when
			Throwable actual = Assertions.catchThrowable(() -> userResource.findById(ArgumentMatchers.anyInt()));

			//then
			Assertions.assertThat(actual).isInstanceOf(expected);
		}
	}

	@Nested
	class FindAll {

		@Test
		void given_whenFindAllUsers_thenReturnStatusCodeOkAndUsersList() {
			//given
			List<User> users = createUserList();
			List<UserDto> userDtos = createUserDtoList(users);

			ResponseEntity expected = ResponseEntity.ok().body(userDtos);

			Mockito.when(userService.findAll()).thenReturn(users);
			Mockito.when(mapper.toDtoList(users)).thenReturn(userDtos);

			//when
			ResponseEntity<List<UserDto>> actual = userResource.findAll();

			//then
			Assertions.assertThat(actual)
					.isNotNull()
					.extracting(ResponseEntity::getStatusCode, ResponseEntity::getBody)
					.containsExactly(expected.getStatusCode(), expected.getBody());
		}

		@Test
		void given_whenFindAllUsers_thenReturnStatusCodeOkAndNoResponseBody() {
			//given
			ResponseEntity expected = ResponseEntity.ok().body(Collections.emptyList());

			Mockito.when(userService.findAll()).thenReturn(Collections.emptyList());

			//when
			ResponseEntity<List<UserDto>> actual = userResource.findAll();

			//then
			Assertions.assertThat(actual)
					.isNotNull()
					.extracting(ResponseEntity::getStatusCode, ResponseEntity::getBody)
					.containsExactly(expected.getStatusCode(), expected.getBody());
		}
	}

	private List<User> createUserList() {
		return List.of(createUser(1),
				createUser(2),
				createUser(3));
	}

	private List<UserDto> createUserDtoList(List<User> users) {
		return users
				.stream()
				.map(this::createUserDto)
				.collect(Collectors.toList());
	}

	private User createUser(Integer id) {
		Role role = Role.PROJECT_MANAGER;
		String identifier = "USER" + id;
		String userName = "username" + id;
		String password = "password" + id;
		String passwordHash = "100abcEf";
		String email = "test@test.ca" + id;

		return User.builder()
				.id(id)
				.organization(createOrganization(1, "name"))
				.role(role)
				.identifier(identifier)
				.name(userName)
				.password(password)
				.passwordHash(passwordHash)
				.emailAddress(email)
				.language(Language.ENGLISH)
				.features(createFeatureSet())
				.build();
	}

	private Organization createOrganization(Integer id, String name) {
		return Organization.builder()
				.id(id)
				.name(name)
				.description("description")
				.build();
	}

	private UserDto createUserDto(User user) {
		return UserDto.builder()
				.id(user.getId())
				.identifier(user.getIdentifier())
				.name(user.getName())
				.password(user.getPassword())
				.emailAddress(user.getEmailAddress())
				.language(createLanguageDto(user.getLanguage()))
				.organization(createOrganizationDto(user.getOrganization()))
				.role(createRoleDto(user.getRole()))
				.features(createFeatureDtoSet(user.getFeatures()))
				.build();
	}

	private LanguageDto createLanguageDto(Language language) {
		return LanguageDto.builder()
				.id(language.getId())
				.name(language.getName())
				.code(language.getCode())
				.build();
	}

	private OrganizationDto createOrganizationDto(Organization organization) {
		return OrganizationDto.builder()
				.id(organization.getId())
				.name(organization.getName())
				.description(organization.getDescription())
				.build();
	}

	private RoleDto createRoleDto(Role role) {
		return RoleDto.builder()
				.id(role.getId())
				.name(role.getName())
				.level(role.getLevel())
				.nonEditableFeatures(createFeatureDtoSet(role.getNonEditableFeatures()))
				.defaultFeatures(createFeatureDtoSet(role.getDefaultFeatures()))
				.build();
	}

	private FeatureDto createFeatureDto(Feature feature) {
		return FeatureDto.builder()
				.id(feature.getId())
				.name(feature.getName())
				.build();
	}

	private Set<Feature> createFeatureSet() {
		return Set.of(Feature.PROJECT_MANAGEMENT, Feature.PROJECT_DATA_MAPS);
	}

	private Set<FeatureDto> createFeatureDtoSet(Set<Feature> features) {
		return features.stream()
				.map(this::createFeatureDto)
				.collect(Collectors.toSet());
	}
}
