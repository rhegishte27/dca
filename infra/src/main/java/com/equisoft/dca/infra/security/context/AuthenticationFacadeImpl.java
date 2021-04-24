package com.equisoft.dca.infra.security.context;

import java.security.Principal;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
class AuthenticationFacadeImpl implements AuthenticationFacade {

	@Override
	public Optional<Authentication> getAuthentication() {
		return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication());
	}

	@Override
	public void setAuthentication(Authentication authentication) {
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

	@Override
	public void clearContext() {
		SecurityContextHolder.clearContext();
	}

	@Override
	public Optional<String> getUserIdentifier() {
		return getAuthentication().map(Principal::getName);
	}
}
