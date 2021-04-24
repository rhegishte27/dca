package com.equisoft.dca.infra.security.token;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Token {

	private final String value;

	private final String publicKeyBase64;

	private final int duration;
}
