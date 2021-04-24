package com.equisoft.dca.infra.security.rsa;

import com.auth0.jwt.interfaces.RSAKeyProvider;

public interface DcaRsaKeyProvider extends RSAKeyProvider {
	String getPublicKeyBase64();
}
