package com.equisoft.dca.api.usersetting;

import java.net.URI;
import java.util.Optional;

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

import com.equisoft.dca.api.usersetting.dto.UserSettingDto;
import com.equisoft.dca.api.usersetting.mapper.UserSettingMapper;
import com.equisoft.dca.backend.exception.EntityNotFoundException;
import com.equisoft.dca.backend.user.model.UserSetting;
import com.equisoft.dca.backend.user.service.UserSettingService;

@ExtendWith(MockitoExtension.class)
class UserSettingResourceTest {

	@Mock
	private UserSettingService service;

	@Mock
	private UserSettingMapper userSettingMapper;

	private UserSettingResource userSettingResource;

	private UserSetting userSetting;

	private UserSettingDto userSettingDto;

	@BeforeEach
	void setUp() {
		this.userSettingResource = new UserSettingResource(service, userSettingMapper);
		this.userSetting = createUserSetting(1);
		this.userSettingDto = createUserSettingDto(userSetting);
	}

	@Nested
	class Save {

		@Test
		void givenValidUserSetting_whenSave_thenReturnStatusCodeCreatedAndUserSetting() {
			//given
			ResponseEntity expected = ResponseEntity.created(URI.create(UserSettingResource.PATH + "/" + userSetting.getId())).body(userSettingDto);

			Mockito.when(userSettingMapper.toEntity(userSettingDto)).thenReturn(userSetting);
			Mockito.when(service.save(ArgumentMatchers.any(UserSetting.class))).thenReturn(userSetting);
			Mockito.when(userSettingMapper.toDto(userSetting)).thenReturn(userSettingDto);

			//when
			ResponseEntity<UserSettingDto> actual = userSettingResource.save(userSettingDto);

			//then
			Assertions.assertThat(actual)
					.isNotNull()
					.extracting(ResponseEntity::getStatusCode, ResponseEntity::getHeaders, ResponseEntity::getBody)
					.containsExactly(expected.getStatusCode(), expected.getHeaders(), expected.getBody());
		}
	}

	@Nested
	class Delete {

		@Test
		void givenExistingUserSettingId_whenDelete_thenReturnStatusCodeNoContentAndBodyNull() {
			//given
			ResponseEntity expected = ResponseEntity.noContent().build();

			//when
			ResponseEntity<Void> actual = userSettingResource.delete(ArgumentMatchers.anyInt());

			//then
			Assertions.assertThat(actual)
					.isNotNull()
					.extracting(ResponseEntity::getStatusCode, ResponseEntity::getBody)
					.containsExactly(expected.getStatusCode(), expected.getBody());
		}

		@Test
		void givenNonExistentUserSettingId_whenDelete_thenThrowEntityNotFoundException() {
			//given
			Class expected = EntityNotFoundException.class;

			Mockito.doThrow(EntityNotFoundException.class).when(service).deleteById(ArgumentMatchers.anyInt());

			//when
			Throwable actual = Assertions.catchThrowable(() -> userSettingResource.delete(ArgumentMatchers.anyInt()));

			//then
			Assertions.assertThat(actual).isInstanceOf(expected);
		}
	}

	@Nested
	class FindById {

		@Test
		void givenExistingUserSettingId_whenFindById_thenReturnStatusCodeOkAndBodyWithSystem() {
			//given
			ResponseEntity expected = ResponseEntity.ok().body(userSettingDto);

			Mockito.when(service.findById(ArgumentMatchers.anyInt())).thenReturn(Optional.of(userSetting));
			Mockito.when(userSettingMapper.toDto(userSetting)).thenReturn(userSettingDto);

			//when
			ResponseEntity<UserSettingDto> actual = userSettingResource.findById(ArgumentMatchers.anyInt());

			//then
			Assertions.assertThat(actual)
					.isNotNull()
					.extracting(ResponseEntity::getStatusCode, ResponseEntity::getBody)
					.containsExactly(expected.getStatusCode(), expected.getBody());
		}

		@Test
		void givenNonExistentUserSettingId_whenFindById_thenThrowEntityNotFoundException() {
			//given
			Class expected = EntityNotFoundException.class;

			Mockito.when(service.findById(ArgumentMatchers.anyInt())).thenReturn(Optional.empty());

			//when
			Throwable actual = Assertions.catchThrowable(() -> userSettingResource.findById(ArgumentMatchers.anyInt()));

			//then
			Assertions.assertThat(actual).isInstanceOf(expected);
		}
	}

	private UserSetting createUserSetting(Integer id) {
		return UserSetting.builder().id(id).build();
	}

	private UserSettingDto createUserSettingDto(UserSetting userSetting) {
		return UserSettingDto.builder().id(userSetting.getId()).build();
	}
}
