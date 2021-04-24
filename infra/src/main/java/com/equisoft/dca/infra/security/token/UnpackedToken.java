package com.equisoft.dca.infra.security.token;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UnpackedToken {
	private final String identifier;
	private final List<String> roles;
}
