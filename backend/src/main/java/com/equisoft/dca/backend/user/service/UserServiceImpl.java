package com.equisoft.dca.backend.user.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.equisoft.dca.backend.exception.EntityAlreadyExistsException;
import com.equisoft.dca.backend.exception.EntityNotFoundException;
import com.equisoft.dca.backend.user.dao.UserRepository;
import com.equisoft.dca.backend.user.exception.UserRoleLevelException;
import com.equisoft.dca.backend.user.model.User;
import com.equisoft.dca.backend.util.StringUtil;

@Service
@Validated
class UserServiceImpl implements UserService {
	private final UserRepository userRepository;

	private final PasswordEncoder passwordEncoder;

	private final UserSettingService userSettingService;

	@Inject
	UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, UserSettingService userSettingService) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.userSettingService = userSettingService;
	}

	@Override
	public Optional<User> findById(int id) {
		return userRepository.findById(id);
	}

	@Override
	public User save(String currentUserIdentifier, @Valid User user) {
		assertUserIdentifierNotNull(currentUserIdentifier);
		validateUserRoleLevel(currentUserIdentifier, user);
		prepareUserForSave(user, false);
		checkIfIdentifierDuplicated(user);
		return userRepository.save(user);
	}

	@Override
	public User update(String currentUserIdentifier, @Valid User user) {
		assertUserIdentifierNotNull(currentUserIdentifier);
		User userFound = validateBeforeUpdate(currentUserIdentifier, user);
		prepareUserForSave(user, true);
		checkIfIdentifierDuplicated(user);
		if (StringUtils.isBlank(user.getPassword())) {
			// If the password is empty at this point, we wont update it
			user.setPasswordHash(userFound.getPasswordHash());
		}
		return userRepository.save(user);
	}

	private User validateBeforeUpdate(String currentUserIdentifier, User user) {
		User userFound = findUserByIdOrElseThrowException(user.getId());
		if (!userFound.getIdentifier().equals(user.getIdentifier())) {
			throw new EntityNotFoundException(User.class, "identifier", user.getIdentifier());
		}
		validateUserRoleLevel(currentUserIdentifier, userFound);
		return userFound;
	}

	private void checkIfIdentifierDuplicated(User user) {
		userRepository.findByIdentifier(user.getIdentifier()).ifPresent(u -> {
			if (user.getId() == null || u.getId().intValue() != user.getId().intValue()) {
				throw new EntityAlreadyExistsException(User.class, "identifier", user.getIdentifier());
			}
		});
	}

	@Override
	public Optional<User> findByIdentifier(String identifier) {
		return userRepository.findByIdentifier(identifier);
	}

	@Override
	public void deleteById(String currentUserIdentifier, Integer id) {
		assertUserIdentifierNotNull(currentUserIdentifier);
		validateBeforeDelete(currentUserIdentifier, id);
		try {
			userSettingService.deleteById(id);
			userSettingService.resetUser(id);
			userRepository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new EntityNotFoundException(User.class, "id", String.valueOf(id));
		}
	}

	private void assertUserIdentifierNotNull(String identifier) {
		Objects.requireNonNull(identifier, "User identifier cannot be null");
	}

	private void validateBeforeDelete(String currentUserIdentifier, Integer id) {
		User userFound = findUserByIdOrElseThrowException(id);
		validateUserRoleLevel(currentUserIdentifier, userFound);
	}

	private User findUserByIdOrElseThrowException(Integer id) {
		return userRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException(User.class, "id", id.toString()));
	}

	private void validateUserRoleLevel(String currentUserIdentifier, User user) {
		if (!currentUserIdentifier.equals(user.getIdentifier())) {
			User currentUser = userRepository.findByIdentifier(currentUserIdentifier)
					.orElseThrow(() -> new EntityNotFoundException(User.class, "identifier", currentUserIdentifier));
			if (!currentUser.getRole().hasHigherOrEqualsLevel(user.getRole())) {
				throw new UserRoleLevelException("identifier", currentUser.getIdentifier(), "identifier", user.getIdentifier());
			}
		}
	}

	@Override
	public List<User> findAll() {
		return userRepository.findAll().stream()
				.filter(u -> !User.ADMIN_IDENTIFIER.equals(u.getIdentifier()))
				.collect(Collectors.toList());
	}

	@Override
	public List<User> findByOrganizationId(int organizationId) {
		return userRepository.findByOrganizationId(organizationId);
	}

	private void prepareUserForSave(User user, boolean isUpdate) {
		user.setIdentifier(user.getIdentifier().toUpperCase());
		user.setName(StringUtil.toTitleCaseIfNotMixedCase(StringUtils.normalizeSpace(user.getName())));
		if (!isUpdate || StringUtils.isNotBlank(user.getPassword())) {
			user.setPasswordHash(passwordEncoder.encode(user.getPassword()));
		}
		user.setEmailAddress(user.getEmailAddress().toLowerCase());
	}
}
