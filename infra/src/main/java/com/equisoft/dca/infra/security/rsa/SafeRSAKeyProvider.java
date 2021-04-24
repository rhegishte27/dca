package com.equisoft.dca.infra.security.rsa;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;

public class SafeRSAKeyProvider implements DcaRsaKeyProvider {
	private final RSAPublicKey publicKey;
	private final RSAPrivateKey privateKey;

	public SafeRSAKeyProvider(RSAPublicKey publicKey, RSAPrivateKey privateKey) {
		this.publicKey = publicKey;
		this.privateKey = privateKey;
	}

	@Override
	public RSAPublicKey getPublicKeyById(String s) {
		return publicKey;
	}

	@Override
	public RSAPrivateKey getPrivateKey() {
		return privateKey;
	}

	@Override
	public String getPrivateKeyId() {
		return null;
	}

	@Override
	public String getPublicKeyBase64() {
		return Base64.getEncoder().encodeToString(publicKey.getEncoded());
	}
}
