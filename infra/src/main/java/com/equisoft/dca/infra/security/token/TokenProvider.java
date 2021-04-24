package com.equisoft.dca.infra.security.token;

import java.util.Set;

public interface TokenProvider {
	Token generateToken(String identifier, Set<String> roles);

	UnpackedToken validateToken(String token);
}
