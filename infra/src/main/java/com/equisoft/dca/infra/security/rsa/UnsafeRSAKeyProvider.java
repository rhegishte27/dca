package com.equisoft.dca.infra.security.rsa;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;

public class UnsafeRSAKeyProvider implements DcaRsaKeyProvider {
	private static final String WARNING = "This token is signed with the following key, but it was generated at runtime. Do not used in production. Key: ";

	private final KeyPair keyPair;

	public UnsafeRSAKeyProvider(String algo) {
		try {
			keyPair = KeyPairGenerator.getInstance(algo).generateKeyPair();
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public RSAPublicKey getPublicKeyById(String s) {
		return (RSAPublicKey) keyPair.getPublic();
	}

	@Override
	public RSAPrivateKey getPrivateKey() {
		return (RSAPrivateKey) keyPair.getPrivate();
	}

	@Override
	public String getPrivateKeyId() {
		return null;
	}

	@Override
	public String getPublicKeyBase64() {
		return WARNING + Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());
	}
}
