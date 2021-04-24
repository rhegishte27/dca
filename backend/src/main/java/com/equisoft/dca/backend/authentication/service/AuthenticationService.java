package com.equisoft.dca.backend.authentication.service;

import org.springframework.security.core.Authentication;

public interface AuthenticationService {

	Authentication authenticate(String identifier, String rawPassword);
}
