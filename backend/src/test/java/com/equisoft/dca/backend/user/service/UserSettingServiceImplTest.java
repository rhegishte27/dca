package com.equisoft.dca.backend.user.service;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;

import com.equisoft.dca.backend.exception.EntityNotFoundException;
import com.equisoft.dca.backend.user.dao.UserSettingRepository;
import com.equisoft.dca.backend.user.model.UserSetting;

@ExtendWith(MockitoExtension.class)
class UserSettingServiceImplTest {

	@Mock
	private UserSettingRepository repository;

	private UserSettingService service;

	@BeforeEach
	void setUp() {
		service = new UserSettingServiceImpl(repository);
	}

	@Nested
	class Reset {
		@Nested
		class ResetSystemToNull {
			@ParameterizedTest
			@NullSource
			@ValueSource(ints = {1, 2, 3})
			void givenId_whenSetDefaultSystemToNull_shouldCallRepoResetSystem(Integer id) {
				// given

				// when
				service.resetSystem(id);

				// then
				Mockito.verify(repository, Mockito.times(1)).resetSystem(id);
			}
		}

		@Nested
		class ResetProjectToNull {
			@ParameterizedTest
			@NullSource
			@ValueSource(ints = {1, 2, 3})
			void givenId_whenSetDefaultProjectToNull_shouldCallRepoResetProject(Integer id) {
				// given

				// when
				service.resetProject(id);

				// then
				Mockito.verify(repository, Mockito.times(1)).resetProject(id);
			}
		}

		@Nested
		class ResetOrganizationToNull {
			@ParameterizedTest
			@NullSource
			@ValueSource(ints = {1, 2, 3})
			void givenId_whenSetDefaultOrganizationToNull_shouldCallRepoResetOrganization(Integer id) {
				// given

				// when
				service.resetOrganization(id);

				// then
				Mockito.verify(repository, Mockito.times(1)).resetOrganization(id);
			}
		}

		@Nested
		class ResetUserToNull {
			@ParameterizedTest
			@NullSource
			@ValueSource(ints = {1, 2, 3})
			void givenId_whenSetDefaultUserToNull_shouldCallRepoResetUser(Integer id) {
				// given

				// when
				service.resetUser(id);

				// then
				Mockito.verify(repository, Mockito.times(1)).resetUser(id);
			}
		}

		@Nested
		class ResetLocationToNull {
			@ParameterizedTest
			@NullSource
			@ValueSource(ints = {1, 2, 3})
			void givenId_whenSetDefaultLocationToNull_shouldCallRepoResetLocation(Integer id) {
				// given

				// when
				service.resetLocation(id);

				// then
				Mockito.verify(repository, Mockito.times(1)).resetLocation(id);
			}
		}
	}

	@Nested
	class FindById {

		@Test
		void givenExistentUserSettingId_whenFindById_thenReturnUserSettingFromRepository() {
			// given
			int id = 3;
			Optional<UserSetting> expected = Optional.of(createUserSetting(id));
			Mockito.when(repository.findById(id)).thenReturn(expected);

			// when
			Optional<UserSetting> actual = service.findById(id);

			// then
			Mockito.verify(repository, Mockito.times(1)).findById(id);
			Assertions.assertThat(actual).isEqualTo(expected);
		}

		@Test
		void givenNonExistentUserSetting_whenFindById_thenReturnEmpty() {
			// given
			int id = 3;
			Optional<UserSetting> expected = Optional.empty();
			Mockito.when(repository.findById(ArgumentMatchers.anyInt())).thenReturn(expected);

			// when
			Optional<UserSetting> actual = service.findById(id);

			// then
			Mockito.verify(repository, Mockito.times(1)).findById(ArgumentMatchers.anyInt());
			Assertions.assertThat(actual).isEqualTo(expected);
		}
	}

	@Nested
	class Save {

		@Test
		void givenUserSetting_whenSave_thenReturnSavedUserSetting() {
			// given
			UserSetting expected = createUserSetting(1);
			Mockito.when(repository.save(expected)).thenReturn(expected);

			// when
			UserSetting actual = service.save(expected);

			// then
			Mockito.verify(repository, Mockito.times(1)).save(expected);
			Assertions.assertThat(actual).isEqualTo(expected);
		}
	}

	@Nested
	class Delete {

		@Test
		void givenNonExistentUserSettingId_whenDelete_thenThrowEntityNotFoundException() {
			//given
			Class expected = EntityNotFoundException.class;

			Mockito.doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(ArgumentMatchers.anyInt());

			//when
			Throwable actual = Assertions.catchThrowable(() -> service.deleteById(ArgumentMatchers.anyInt()));

			//then
			Mockito.verify(repository, Mockito.times(1)).deleteById(ArgumentMatchers.anyInt());

			Assertions.assertThat(actual).isInstanceOf(expected);
		}

		@Test
		void givenExistentUserSettingId_whenDelete_thenVoid() {
			//given
			Mockito.doNothing().when(repository).deleteById(ArgumentMatchers.anyInt());

			//when
			service.deleteById(ArgumentMatchers.anyInt());

			//then
			Mockito.verify(repository, Mockito.times(1)).deleteById(ArgumentMatchers.anyInt());
		}
	}

	private UserSetting createUserSetting(Integer id) {
		return UserSetting.builder().id(id).build();
	}
}
