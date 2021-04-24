package com.equisoft.dca.backend.authentication.service;

import java.util.Collections;

import javax.inject.Inject;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.equisoft.dca.backend.user.model.User;
import com.equisoft.dca.backend.user.service.UserService;

@Service
class AuthenticationServiceImpl implements AuthenticationService {
	private static final String ERROR_LOGIN_INVALID = "The user identifier or password entered was not recognized";

	private final UserService userService;

	private final PasswordEncoder passwordEncoder;

	@Inject
	AuthenticationServiceImpl(UserService userService, PasswordEncoder passwordEncoder) {
		this.userService = userService;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public Authentication authenticate(String identifier, String rawPassword) {
		User user = userService.findByIdentifier(identifier)
				.orElseThrow(() -> new BadCredentialsException(ERROR_LOGIN_INVALID));
		String passwordHash = user.getPasswordHash().trim();

		if (!passwordEncoder.matches(rawPassword, passwordHash)) {
			throw new BadCredentialsException(ERROR_LOGIN_INVALID);
		}
		return new UsernamePasswordAuthenticationToken(user.getIdentifier(), passwordHash, Collections.emptyList());
	}
}
