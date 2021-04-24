package com.equisoft.dca.infra.security.token;

import java.util.Arrays;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.equisoft.dca.backend.setting.model.Setting;
import com.equisoft.dca.backend.setting.service.SettingService;
import com.equisoft.dca.infra.security.rsa.DcaRsaKeyProvider;

import static com.auth0.jwt.algorithms.Algorithm.RSA512;

@Component
class JwtTokenProvider implements TokenProvider {

	private static final int DEFAULT_TOKEN_DURATION = 90;

	private static final String TOKEN_ISSUER = "DCA";

	private static final String ROLES = "r";

	private DcaRsaKeyProvider rsaKeyProvider;

	private final SettingService settingService;

	JwtTokenProvider(DcaRsaKeyProvider rsaKeyProvider, SettingService settingService) {
		this.rsaKeyProvider = rsaKeyProvider;
		this.settingService = settingService;
	}

	@Override
	public Token generateToken(String identifier, Set<String> roles) {
		int tokenDuration = getTokenDuration();
		Date expirationDate = new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(tokenDuration));
		String token = JWT.create()
				.withIssuer(TOKEN_ISSUER)
				.withSubject(identifier)
				.withArrayClaim(ROLES, roles.toArray(new String[]{}))
				.withExpiresAt(expirationDate)
				.sign(RSA512(rsaKeyProvider));

		return Token.builder()
				.value(token)
				.publicKeyBase64(rsaKeyProvider.getPublicKeyBase64())
				.duration(tokenDuration).build();
	}

	@Override
	public UnpackedToken validateToken(String token) {
		JWTVerifier verifier = JWT.require(RSA512(rsaKeyProvider))
				.withIssuer(TOKEN_ISSUER)
				.build();
		DecodedJWT jwt = verifier.verify(token);

		String identifier = jwt.getSubject();
		String[] roles = jwt.getClaim(ROLES).asArray(String.class);
		return new UnpackedToken(identifier, Arrays.asList(roles));
	}

	private int getTokenDuration() {
		return settingService.get()
				.map(Setting::getTokenDuration)
				.orElse(DEFAULT_TOKEN_DURATION);
	}
}
