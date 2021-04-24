package com.equisoft.dca.infra.security.context;

import java.util.Optional;

import org.springframework.security.core.Authentication;

public interface AuthenticationFacade {

	Optional<Authentication> getAuthentication();

	void setAuthentication(Authentication authentication);

	void clearContext();

	Optional<String> getUserIdentifier();
}
