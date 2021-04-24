package com.equisoft.dca.backend.user.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.equisoft.dca.backend.common.model.BaseEnum;
import com.equisoft.dca.backend.exception.EntityAlreadyExistsException;
import com.equisoft.dca.backend.exception.EntityNotFoundException;
import com.equisoft.dca.backend.user.dao.UserRepository;
import com.equisoft.dca.backend.user.exception.UserRoleLevelException;
import com.equisoft.dca.backend.user.model.Feature;
import com.equisoft.dca.backend.user.model.Organization;
import com.equisoft.dca.backend.user.model.Role;
import com.equisoft.dca.backend.user.model.User;

import static org.mockito.Mockito.only;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
	@Mock
	private UserRepository userRepository;
	@Mock
	private PasswordEncoder passwordEncoder;
	@Mock
	private UserSettingService userSettingService;

	private UserService userService;

	@BeforeEach
	void setUp() {
		userService = new UserServiceImpl(userRepository, passwordEncoder, userSettingService);
	}

	@Nested
	class FindById {
		@Test
		void givenRepositoryFindByIdReturnUser_whenFindById_shouldCallTheRepositoryFindById_shouldReturnWhatTheRepositoryReturn() {
			// given
			int id = 3;
			Optional<User> expected = Optional.of(createUser(id));
			Mockito.doReturn(expected).when(userRepository).findById(id);

			// when
			Optional<User> actual = userService.findById(id);

			// then
			Mockito.verify(userRepository, only()).findById(id);
			Assertions.assertThat(actual).isEqualTo(expected);
		}

		@Test
		void givenRepositoryFindByIdReturnEmpty_whenFindById_shouldCallTheRepositoryFindById_shouldReturnWhatTheRepositoryReturn() {
			// given
			int id = 3;
			Optional<User> expected = Optional.empty();
			Mockito.doReturn(Optional.empty()).when(userRepository).findById(id);

			// when
			Optional<User> actual = userService.findById(id);

			// then
			Mockito.verify(userRepository, only()).findById(id);
			Assertions.assertThat(actual).isEqualTo(expected);
		}
	}

	@Nested
	class FindByOrganizationId {
		@Test
		void givenRepositoryFindByOrganizationReturnUser_whenFindByOrganizationId_shouldCallTheRepositoryFindByOrganization_shouldReturnTheRepositoryReturn() {
			// given
			int id = 3;
			List<User> expected = createListUsers();
			Mockito.doReturn(expected).when(userRepository).findByOrganizationId(id);

			// when
			List<User> actual = userService.findByOrganizationId(id);

			// then
			Mockito.verify(userRepository, only()).findByOrganizationId(id);
			Assertions.assertThat(actual).isEqualTo(expected);
		}

		@Test
		void givenRepositoryFindByOrganizationReturnEmpty_FindByOrganizationId_shouldCallTheRepositoryFindByOrganizationId_shouldReturnTheRepositoryReturn() {
			// given
			int id = 3;
			List<User> expected = Collections.emptyList();
			Mockito.doReturn(expected).when(userRepository).findByOrganizationId(id);

			// when
			List<User> actual = userService.findByOrganizationId(id);

			// then
			Mockito.verify(userRepository, only()).findByOrganizationId(id);
			Assertions.assertThat(actual).isEqualTo(expected);
		}
	}

	@Nested
	class Save {
		@Nested
		class GivenCurrentUserIdentifierNull {
			@Test
			void givenCurrentUserIdentifierNull_whenSave_thenThrowIllegalArgumentExeption() {
				//given
				Class expected = NullPointerException.class;
				User user = createUser(null);

				//when
				Throwable actual = Assertions.catchThrowable(() -> userService.save(null, user));

				//then
				Assertions.assertThat(actual).isInstanceOf(expected);
			}
		}

		@Nested
		class GivenCurrentUserIdentifierNotNull {
			private String currentUserIdentifier = "identifier";

			@Nested
			class GivenCurrentUserHasLowerRole {
				@BeforeEach
				void setUp() {
					Mockito.when(userRepository.findByIdentifier(currentUserIdentifier))
							.thenReturn(Optional.of(createUser(1, currentUserIdentifier, 5)));
				}

				@Test
				void givenNonExistentUserIdentifier_whenSave_thenThrowUserRoleLevelException() {
					//given
					Class expected = UserRoleLevelException.class;
					User user = createUser(null);

					//when
					Throwable actual = Assertions.catchThrowable(() -> userService.save(currentUserIdentifier, user));

					//then
					Assertions.assertThat(actual).isInstanceOf(expected);
				}
			}

			@Nested
			class GivenCurrentUserHasHigherRole {
				@BeforeEach
				void setUp() {
					Mockito.when(userRepository.findByIdentifier(currentUserIdentifier))
							.thenReturn(Optional.of(createUser(1, currentUserIdentifier, 1)));
				}

				@Test
				void givenNonExistentUserIdentifier_whenSave_thenReturnNewUser() {
					//given
					User user = createUser(null);
					String identifier = user.getIdentifier().toUpperCase();
					User userFromRepository = createUser(1);
					userFromRepository.setIdentifier(identifier);

					Mockito.when(userRepository.save(user)).thenReturn(userFromRepository);
					Mockito.when(userRepository.findByIdentifier(userFromRepository.getIdentifier())).thenReturn(Optional.empty());

					//when
					User actual = userService.save(currentUserIdentifier, user);

					//then
					Mockito.verify(userRepository, Mockito.times(2)).findByIdentifier(ArgumentMatchers.anyString());
					Mockito.verify(userRepository, Mockito.times(1)).save(ArgumentMatchers.any(User.class));
					Assertions.assertThat(actual)
							.isNotNull()
							.isEqualTo(userFromRepository);
				}

				@ParameterizedTest
				@CsvSource({"TEST, TEST", "TEST, test", "NEW ENTITY 1, 'new entity 1'"})
				void givenExistingUserIdentifier_whenSave_thenThrowEntityAlreadyExistsException(String identifierExisted, String identifierToSave) {
					//given
					User user = createUser(null, identifierToSave, 2);
					User otherUser = createUser(1, identifierExisted, 2);
					Mockito.when(userRepository.findByIdentifier(identifierExisted)).thenReturn(Optional.of(otherUser));

					//when
					Throwable thrown = Assertions.catchThrowable(() -> userService.save(currentUserIdentifier, user));

					//then
					Mockito.verify(userRepository, Mockito.times(2)).findByIdentifier(ArgumentMatchers.anyString());
					Mockito.verify(userRepository, Mockito.times(0)).save(ArgumentMatchers.any(User.class));
					Assertions.assertThat(thrown)
							.isInstanceOf(EntityAlreadyExistsException.class);
				}

				@ParameterizedTest
				@CsvSource({"test, TEST", "iDENtiFIeR123@#, IDENTIFIER123@#"})
				void givenUserIdentifier_whenSave_shouldChangeIdentifierToUpperCase(String actual, String expected) {
					// given
					User user = createUser(null);
					user.setIdentifier(actual);

					// when
					userService.save(currentUserIdentifier, user);

					// then
					Assertions.assertThat(user.getIdentifier()).isEqualTo(expected);
				}

				@ParameterizedTest
				@CsvSource({"test test, Test Test", "TEST TEST, Test Test", "donald TRUMP, donald TRUMP", "   test test     , Test Test",
						"  test                test, Test Test", "test\t\t\t\ttest, Test Test"})
				void givenUserUserName_whenSave_shouldTrimAndChangeUserNameToTitleCaseIfAllLowerCaseAllUpperCase(String actual, String expected) {
					// given
					User user = createUser(null);
					user.setName(actual);

					// when
					userService.save(currentUserIdentifier, user);

					// then
					Assertions.assertThat(user.getName()).isEqualTo(expected);
				}

				@Test
				void givenUserPasswordNotEmpty_whenSave_shouldEncryptPassword() {
					// given
					User user = createUser(null);
					user.setPassword("password");

					String passwordHash = "passwordHash";
					Mockito.doReturn(passwordHash).when(passwordEncoder).encode(user.getPassword());

					user.setPasswordHash("");

					// when
					userService.save(currentUserIdentifier, user);

					// then
					Assertions.assertThat(user.getPasswordHash()).isEqualTo(passwordHash);
				}

				@Test
				void givenUserPasswordEmpty_whenSave_shouldEncryptPassword() {
					// given
					User user = createUser(null);
					user.setPassword("");

					String passwordHash = "passwordHash";
					Mockito.doReturn(passwordHash).when(passwordEncoder).encode(user.getPassword());

					user.setPasswordHash("");

					// when
					userService.save(currentUserIdentifier, user);

					// then
					Assertions.assertThat(user.getPasswordHash()).isEqualTo(passwordHash);
				}
			}
		}
	}

	@Nested
	class Update {

		private User currentUser = createUser(3, "username", 1);

		@ParameterizedTest
		@CsvSource({"TEST, TEST", "TEST, test", "NEW ENTITY 1, 'new entity 1'"})
		void givenExistingUserIdentifierWithDifferentId_whenUpdate_thenThrowEntityAlreadyExistsException(String identifierExisted, String identifierToSave) {
			//given
			User user = createUser(1, identifierToSave, 2);
			User otherUser = createUser(2, identifierExisted, 2);
			Class expected = EntityAlreadyExistsException.class;

			Mockito.when(userRepository.findById(1)).thenReturn(Optional.of(user));
			Mockito.when(userRepository.findByIdentifier(identifierExisted)).thenReturn(Optional.of(otherUser));
			Mockito.when(userRepository.findByIdentifier(currentUser.getIdentifier())).thenReturn(Optional.of(currentUser));

			//when
			Throwable actual = Assertions.catchThrowable(() -> userService.update(currentUser.getIdentifier(), user));

			//then
			Mockito.verify(userRepository, Mockito.times(1)).findById(ArgumentMatchers.anyInt());
			Mockito.verify(userRepository, Mockito.times(2)).findByIdentifier(ArgumentMatchers.anyString());
			Mockito.verify(userRepository, Mockito.times(0)).save(ArgumentMatchers.any(User.class));
			Assertions.assertThat(actual).isInstanceOf(expected);
		}

		@ParameterizedTest
		@CsvSource({"TEST, TEST", "TEST, test", "NEW ENTITY 1, 'new entity 1'"})
		void givenExistingUserIdentifierWithSameId_whenUpdate_thenReturnUser(String identifierExisted, String identifierToSave) {
			//given
			User user = createUser(1, identifierToSave, 2);
			User otherUser = createUser(1, identifierExisted, 2);

			Mockito.when(userRepository.findById(1)).thenReturn(Optional.of(user));
			Mockito.when(userRepository.findByIdentifier(identifierExisted)).thenReturn(Optional.of(otherUser));
			Mockito.when(userRepository.findByIdentifier(currentUser.getIdentifier())).thenReturn(Optional.of(currentUser));
			Mockito.when(userRepository.save(user)).thenReturn(user);

			//when
			User actual = userService.update(currentUser.getIdentifier(), user);

			//then
			Mockito.verify(userRepository, Mockito.times(1)).findById(ArgumentMatchers.anyInt());
			Mockito.verify(userRepository, Mockito.times(2)).findByIdentifier(ArgumentMatchers.anyString());
			Mockito.verify(userRepository, Mockito.times(1)).save(ArgumentMatchers.any(User.class));
			Assertions.assertThat(actual)
					.isNotNull()
					.isEqualTo(user);
		}

		@Test
		void givenExistingUserIdAndIdentifier_whenUpdate_thenReturnUser() {
			//given
			User user = createUser(1);
			String identifier = user.getIdentifier().toUpperCase();
			User userFromRepository = createUser(1);
			userFromRepository.setIdentifier(identifier);

			Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
			Mockito.when(userRepository.findByIdentifier(currentUser.getIdentifier())).thenReturn(Optional.of(currentUser));
			Mockito.when(userRepository.save(user)).thenReturn(userFromRepository);

			//when
			User actual = userService.update(currentUser.getIdentifier(), user);

			//then
			Mockito.verify(userRepository, Mockito.times(1)).findById(ArgumentMatchers.anyInt());
			Mockito.verify(userRepository, Mockito.times(2)).findByIdentifier(ArgumentMatchers.anyString());
			Mockito.verify(userRepository, Mockito.times(1)).save(ArgumentMatchers.any(User.class));
			Assertions.assertThat(actual)
					.isNotNull()
					.isEqualTo(userFromRepository);
		}

		@Test
		void givenNonExistentUserId_whenUpdate_thenThrowEntityNotFoundException() {
			//given
			User user = createUser(1);

			Mockito.when(userRepository.findById(ArgumentMatchers.anyInt())).thenReturn(Optional.empty());

			//when
			Throwable thrown = Assertions.catchThrowable(() -> userService.update(currentUser.getIdentifier(), user));

			//then
			Mockito.verify(userRepository, Mockito.times(1)).findById(ArgumentMatchers.anyInt());
			Mockito.verify(userRepository, Mockito.times(0)).findByIdentifier(ArgumentMatchers.anyString());
			Mockito.verify(userRepository, Mockito.times(0)).save(ArgumentMatchers.any(User.class));
			Assertions.assertThat(thrown).isInstanceOf(EntityNotFoundException.class);
		}

		@Test
		void givenNonExistentUserIdentifier_whenUpdate_thenThrowEntityNotFoundException() {
			//given
			User user = createUser(1);
			User userUpdate = createUser(1);
			userUpdate.setIdentifier(user.getIdentifier() + "IdentifierDifferentFromUser");

			Mockito.when(userRepository.findById(ArgumentMatchers.anyInt())).thenReturn(Optional.of(user));

			//when
			Throwable thrown = Assertions.catchThrowable(() -> userService.update(currentUser.getIdentifier(), userUpdate));

			//then
			Mockito.verify(userRepository, Mockito.times(1)).findById(ArgumentMatchers.anyInt());
			Mockito.verify(userRepository, Mockito.times(0)).findByIdentifier(ArgumentMatchers.anyString());
			Mockito.verify(userRepository, Mockito.times(0)).save(ArgumentMatchers.any(User.class));
			Assertions.assertThat(thrown).isInstanceOf(EntityNotFoundException.class);
		}

		@Test
		void givenNonExistentCurrentUserIdentifier_whenUpdate_thenThrowEntityNotFoundException() {
			//given
			Class expected = EntityNotFoundException.class;
			User user = createUser(1);
			User userUpdate = createUser(1);

			Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
			Mockito.when(userRepository.findByIdentifier(currentUser.getIdentifier())).thenReturn(Optional.empty());

			//when
			Throwable actual = Assertions.catchThrowable(() -> userService.update(currentUser.getIdentifier(), userUpdate));

			//then
			Mockito.verify(userRepository, Mockito.times(1)).findById(ArgumentMatchers.anyInt());
			Mockito.verify(userRepository, Mockito.times(1)).findByIdentifier(ArgumentMatchers.anyString());
			Mockito.verify(userRepository, Mockito.times(0)).save(ArgumentMatchers.any(User.class));

			Assertions.assertThat(actual).isInstanceOf(expected);
		}

		@ParameterizedTest
		@CsvSource({"test, TEST", "iDENtiFIeR123@#, IDENTIFIER123@#"})
		void givenUserIdentifier_whenUpdate_shouldChangeIdentifierToUpperCase(String actual, String expected) {
			// given
			int id = 1;
			User user = createUser(id);
			user.setIdentifier(actual);

			Mockito.when(userRepository.findById(id)).thenReturn(Optional.of(user));
			Mockito.when(userRepository.findByIdentifier(currentUser.getIdentifier())).thenReturn(Optional.of(currentUser));

			// when
			userService.update(currentUser.getIdentifier(), user);

			// then
			Assertions.assertThat(user.getIdentifier()).isEqualTo(expected);
		}

		@ParameterizedTest
		@CsvSource({"test test, Test Test", "TEST TEST, Test Test", "donald TRUMP, donald TRUMP", "   test test     , Test Test",
				"  test                test, Test Test", "test\t\t\t\ttest, Test Test"})
		void givenUserUserName_whenUpdate_shouldTrimAndChangeUserNameToTitleCaseIfAllLowerCaseUpperCase(String actual, String expected) {
			// given
			int id = 1;
			User user = createUser(id);
			user.setName(actual);

			Mockito.when(userRepository.findById(id)).thenReturn(Optional.of(user));
			Mockito.when(userRepository.findByIdentifier(currentUser.getIdentifier())).thenReturn(Optional.of(currentUser));

			// when
			userService.update(currentUser.getIdentifier(), user);

			// then
			Assertions.assertThat(user.getName()).isEqualTo(expected);
		}

		@Test
		void givenUserPasswordNotEmpty_whenUpdate_shouldEncryptPassword() {
			// given
			int id = 1;
			String passwordHash = "passwordHash";
			User user = createUser(id);
			user.setPassword("password");
			user.setPasswordHash("");

			Mockito.when(userRepository.findById(id)).thenReturn(Optional.of(user));
			Mockito.when(passwordEncoder.encode(user.getPassword())).thenReturn(passwordHash);
			Mockito.when(userRepository.findByIdentifier(currentUser.getIdentifier())).thenReturn(Optional.of(currentUser));

			// when
			userService.update(currentUser.getIdentifier(), user);

			// then
			Assertions.assertThat(user.getPasswordHash()).isEqualTo(passwordHash);
		}

		@Test
		void givenUserPasswordEmpty_whenUpdate_shouldTakeTheOldPasswordStored() {
			// given
			int id = 1;
			User userReturnByRepo = createUser(id);
			User user = createUser(id);
			user.setPassword("");
			user.setPasswordHash("");

			Mockito.when(userRepository.findById(id)).thenReturn(Optional.of(userReturnByRepo));
			Mockito.when(userRepository.findByIdentifier(currentUser.getIdentifier())).thenReturn(Optional.of(currentUser));

			// when
			userService.update(currentUser.getIdentifier(), user);

			// then
			Assertions.assertThat(user.getPasswordHash()).isEqualTo(userReturnByRepo.getPasswordHash());
		}

		@Test
		void givenCurrentUserSameUserToBeUpdated_whenUpdate_thenExecuteUpdate() {
			// given
			Integer id = 1;
			User user = createUser(id, "username", 2);
			User currentUser = createUser(id, "username", 2);

			Mockito.when(userRepository.findById(id)).thenReturn(Optional.of(user));

			// when
			userService.update(currentUser.getIdentifier(), user);

			// then
			Mockito.verify(userRepository, Mockito.times(1)).findById(ArgumentMatchers.anyInt());
			Mockito.verify(userRepository, Mockito.times(1)).findByIdentifier(ArgumentMatchers.anyString());
			Mockito.verify(userRepository, Mockito.times(1)).save(ArgumentMatchers.any(User.class));
		}
	}

	@Nested
	class FindByIdentifier {
		@Test
		void givenRepositoryReturnUser_whenFindByIdentifier_shouldCallTheRepositoryFindByIdentifier_shouldReturnWhatTheRepositoryReturn() {
			// given the userRepository return an user
			String identifier = "identifier";
			Optional<User> expected = Optional.of(createUser(1));
			Mockito.doReturn(expected).when(userRepository).findByIdentifier(identifier);

			// when
			Optional<User> actual = userService.findByIdentifier(identifier);

			// then
			Mockito.verify(userRepository, only()).findByIdentifier(identifier);
			Assertions.assertThat(actual).isEqualTo(expected);
		}

		@Test
		void givenRepositoryReturnEmpty_whenFindByIdentifier_shouldCallTheRepositoryFindByIdentifier_shouldReturnWhatTheRepositoryReturn() {
			// given the userRepository return an user
			String identifier = "identifier";
			Optional<User> expected = Optional.empty();
			Mockito.doReturn(expected).when(userRepository).findByIdentifier(identifier);

			// when
			Optional<User> actual = userService.findByIdentifier(identifier);

			// then
			Mockito.verify(userRepository, only()).findByIdentifier(identifier);
			Assertions.assertThat(actual).isEqualTo(expected);
		}
	}

	@Nested
	class Delete {

		private User currentUser = createUser(11, "username", 1);

		@Test
		void given_whenDelete_shouldCallTheRepositoryDelete() {
			// given
			int id = 1;
			User user = createUser(id);

			Mockito.when(userRepository.findById(id)).thenReturn(Optional.of(user));
			Mockito.when(userRepository.findByIdentifier(currentUser.getIdentifier())).thenReturn(Optional.of(currentUser));

			// when
			userService.deleteById(currentUser.getIdentifier(), id);

			// then
			Mockito.verify(userRepository, Mockito.times(1)).findById(ArgumentMatchers.anyInt());
			Mockito.verify(userRepository, Mockito.times(1)).findByIdentifier(ArgumentMatchers.anyString());
			Mockito.verify(userSettingService, Mockito.times(1)).resetUser(ArgumentMatchers.anyInt());
			Mockito.verify(userSettingService, Mockito.times(1)).deleteById(ArgumentMatchers.anyInt());
			Mockito.verify(userRepository, Mockito.times(1)).deleteById(ArgumentMatchers.anyInt());
		}

		@Test
		void givenNonExistentUserId_whenDeleteById_thenThrowEntityNotFoundException() {
			//given
			int id = 1;
			User user = createUser(id);
			Class expected = EntityNotFoundException.class;

			Mockito.when(userRepository.findById(id)).thenReturn(Optional.of(user));
			Mockito.when(userRepository.findByIdentifier(currentUser.getIdentifier())).thenReturn(Optional.of(currentUser));
			Mockito.doThrow(EmptyResultDataAccessException.class).when(userRepository).deleteById(ArgumentMatchers.anyInt());

			//when
			Throwable thrown = Assertions.catchThrowable(() -> userService.deleteById(currentUser.getIdentifier(), id));

			//then
			Mockito.verify(userRepository, Mockito.times(1)).findById(ArgumentMatchers.anyInt());
			Mockito.verify(userRepository, Mockito.times(1)).findByIdentifier(ArgumentMatchers.anyString());
			Mockito.verify(userSettingService, Mockito.times(1)).resetUser(ArgumentMatchers.anyInt());
			Mockito.verify(userSettingService, Mockito.times(1)).resetUser(ArgumentMatchers.anyInt());
			Mockito.verify(userRepository, Mockito.times(1)).deleteById(ArgumentMatchers.anyInt());

			Assertions.assertThat(thrown).isInstanceOf(expected);
		}

		@Test
		void givenNonExistentId_whenDeleteById_thenThrowEntityNotFoundException() {
			//given
			Integer id = 1;
			Class expected = EntityNotFoundException.class;

			Mockito.when(userRepository.findById(id)).thenReturn(Optional.empty());

			//when
			Throwable thrown = Assertions.catchThrowable(() -> userService.deleteById(currentUser.getIdentifier(), id));

			//then
			Mockito.verify(userRepository, Mockito.times(1)).findById(ArgumentMatchers.anyInt());
			Mockito.verify(userRepository, Mockito.times(0)).findByIdentifier(ArgumentMatchers.anyString());
			Mockito.verify(userSettingService, Mockito.times(0)).resetUser(ArgumentMatchers.anyInt());
			Mockito.verify(userSettingService, Mockito.times(0)).resetUser(ArgumentMatchers.anyInt());
			Mockito.verify(userRepository, Mockito.times(0)).deleteById(ArgumentMatchers.anyInt());

			Assertions.assertThat(thrown).isInstanceOf(expected);
		}

		@Test
		void givenNullCurrentUserName_whenDeleteUserById_thenThrowUserNullPointerException() {
			// given
			Integer id = 1;
			Class expected = NullPointerException.class;

			// when
			Throwable actual = Assertions.catchThrowable(() -> userService.deleteById(null, id));

			// then
			Mockito.verify(userRepository, Mockito.times(0)).findById(ArgumentMatchers.anyInt());
			Mockito.verify(userRepository, Mockito.times(0)).findByIdentifier(ArgumentMatchers.anyString());
			Mockito.verify(userSettingService, Mockito.times(0)).resetUser(ArgumentMatchers.anyInt());
			Mockito.verify(userSettingService, Mockito.times(0)).resetUser(ArgumentMatchers.anyInt());
			Mockito.verify(userRepository, Mockito.times(0)).deleteById(ArgumentMatchers.anyInt());
			Assertions.assertThat(actual).isInstanceOf(expected);
		}

		@ParameterizedTest
		@ValueSource(ints = {3, 4, 5})
		void givenUserLesserRoleLevel_whenDeleteUserById_thenThrowUserRoleLevelException(Integer roleLevel) {
			// given
			Class expected = UserRoleLevelException.class;
			Integer id = 1;
			User user = createUser(id, "username", 2);
			User currentUser = createUser(2, "username2", roleLevel);

			Mockito.when(userRepository.findById(id)).thenReturn(Optional.of(user));
			Mockito.when(userRepository.findByIdentifier(currentUser.getIdentifier())).thenReturn(Optional.of(currentUser));

			// when
			Throwable actual = Assertions.catchThrowable(() -> userService.deleteById(currentUser.getIdentifier(), id));

			// then
			Mockito.verify(userRepository, Mockito.times(1)).findById(ArgumentMatchers.anyInt());
			Mockito.verify(userRepository, Mockito.times(1)).findByIdentifier(ArgumentMatchers.anyString());
			Mockito.verify(userSettingService, Mockito.times(0)).resetUser(ArgumentMatchers.anyInt());
			Mockito.verify(userSettingService, Mockito.times(0)).resetUser(ArgumentMatchers.anyInt());
			Mockito.verify(userRepository, Mockito.times(0)).deleteById(ArgumentMatchers.anyInt());
			Assertions.assertThat(actual).isInstanceOf(expected);
		}

		@Test
		void givenCurrentUserSameUserToBeDeleted_whenDeleteUserById_thenExecuteDeletion() {
			// given
			Integer id = 1;
			User user = createUser(id, "username", 2);
			User currentUser = createUser(id, "username", 2);

			Mockito.when(userRepository.findById(id)).thenReturn(Optional.of(user));

			// when
			userService.deleteById(currentUser.getIdentifier(), id);

			// then
			Mockito.verify(userRepository, Mockito.times(1)).findById(ArgumentMatchers.anyInt());
			Mockito.verify(userRepository, Mockito.times(0)).findByIdentifier(ArgumentMatchers.anyString());
			Mockito.verify(userSettingService, Mockito.times(1)).resetUser(ArgumentMatchers.anyInt());
			Mockito.verify(userSettingService, Mockito.times(1)).resetUser(ArgumentMatchers.anyInt());
			Mockito.verify(userRepository, Mockito.times(1)).deleteById(ArgumentMatchers.anyInt());
		}
	}

	@Nested
	class FindAll {
		@Test
		void givenRepositoryReturnListOfUser_whenFindAll_shouldCallTheRepositoryFindAll_shouldReturnWhatTheRepositoryReturn() {
			// given
			List<User> lstUserReturnedByRepository = createListUsers();
			Mockito.doReturn(lstUserReturnedByRepository).when(userRepository).findAll();

			// when
			List<User> lstUserReturnedByService = userService.findAll();

			// then
			Mockito.verify(userRepository, only()).findAll();
			Assertions.assertThat(lstUserReturnedByService).isEqualTo(lstUserReturnedByRepository);
		}

		@Test
		void givenRepositoryReturnEmptyList_whenFindAll_shouldCallTheRepositoryFindAll_shouldReturnWhatTheRepositoryReturn() {
			// given
			List<User> expected = Collections.emptyList();
			Mockito.doReturn(expected).when(userRepository).findAll();

			// when
			List<User> actual = userService.findAll();

			// then
			Mockito.verify(userRepository, only()).findAll();
			Assertions.assertThat(actual).isEqualTo(expected);
		}
	}

	@Nested
	class FindAllWithAdminUser {

		@Test
		void givenRepositoryContainsUsersAndAdminUser_whenFindAll_thenReturnAllUsersExceptAdminUser() {
			// given
			List<User> listUserReturnedByRepository = createListUsers();
			listUserReturnedByRepository.add(createAdminUser());
			List<User> expected = createListUsers();

			Mockito.doReturn(listUserReturnedByRepository).when(userRepository).findAll();

			// when
			List<User> actual = userService.findAll();

			// then
			Mockito.verify(userRepository, only()).findAll();
			Assertions.assertThat(actual).isEqualTo(expected);
		}

		@Test
		void givenRepositoryReturnOnlyAdminUser_whenFindAll_thenReturnEmptyList() {
			// given
			List<User> listUserReturnedByRepository = List.of(createAdminUser());
			List<User> expected = Collections.emptyList();

			Mockito.doReturn(listUserReturnedByRepository).when(userRepository).findAll();

			// when
			List<User> actual = userService.findAll();

			// then
			Mockito.verify(userRepository, only()).findAll();
			Assertions.assertThat(actual).isEqualTo(expected);
		}

		private User createAdminUser() {
			return User.builder().identifier(User.ADMIN_IDENTIFIER).build();
		}
	}

	private List<User> createListUsers() {
		List<User> lstUser = new ArrayList<>();

		lstUser.add(createUser(1));
		lstUser.add(createUser(2));
		lstUser.add(createUser(3));

		return lstUser;
	}

	private User createUser(Integer id) {
		return createUser(id, "identifier" + id, 2);
	}

	private User createUser(Integer id, String identifier, int roleLevel) {
		Role role = Arrays.stream(Role.values()).filter(s -> roleLevel == s.getLevel()).findFirst().orElse(null);
		String userName = "username" + id;
		String password = "password" + id;
		String passwordHash = "edEF123";
		String email = "test@test.ca" + id;

		return User.builder()
				.id(id)
				.organization(createOrganization())
				.role(role)
				.identifier(identifier)
				.name(userName)
				.password(password)
				.passwordHash(passwordHash)
				.emailAddress(email)
				.features(createFeatureSet())
				.build();
	}

	private Organization createOrganization() {
		return Organization.builder()
				.id(1)
				.name("name")
				.description("description")
				.build();
	}

	private Set<Feature> createFeatureSet() {
		return Set.of(createFeature(1),
				createFeature(2));
	}

	private Feature createFeature(int id) {
		return BaseEnum.valueOf(Feature.class, id);
	}
}
