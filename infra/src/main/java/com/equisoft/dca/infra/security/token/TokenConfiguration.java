package com.equisoft.dca.infra.security.token;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.equisoft.dca.infra.EnvironmentConfiguration;
import com.equisoft.dca.infra.security.rsa.DcaRsaKeyProvider;
import com.equisoft.dca.infra.security.rsa.SafeRSAKeyProvider;
import com.equisoft.dca.infra.security.rsa.UnsafeRSAKeyProvider;
import com.equisoft.dca.infra.web.filter.TokenAuthenticationFilter;

@Configuration
public class TokenConfiguration {
	private static final Logger LOGGER = Logger.getLogger(TokenConfiguration.class.toString());

	private static final String ALGORITHM = "RSA";
	private static final String ASCII_KEY_HEADER_MARKER = "-----";

	@Bean
	@Named("jwtPublicKey")
	public OptionalRsaKey<RSAPublicKey> jwtPublicKey(EnvironmentConfiguration config) {
		if (StringUtils.isBlank(config.getJwtPublicKeyFile())) {
			return OptionalRsaKey.empty();
		}

		try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(config.getJwtPublicKeyFile()), StandardCharsets.UTF_8))) {
			byte[] keyBytes = readPEMKey(reader);
			X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
			KeyFactory kf = KeyFactory.getInstance(ALGORITHM);

			return OptionalRsaKey.of((RSAPublicKey) kf.generatePublic(spec));
		} catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e) {
			throw new RuntimeException(e);
		}
	}

	@Bean
	@Named("jwtPrivateKey")
	public OptionalRsaKey<RSAPrivateKey> jwtPrivateKey(EnvironmentConfiguration config) {
		if (StringUtils.isBlank(config.getJwtPrivateKeyFile())) {
			return OptionalRsaKey.empty();
		}

		try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(config.getJwtPrivateKeyFile()), StandardCharsets.UTF_8))) {
			byte[] keyBytes = readPEMKey(reader);
			PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
			KeyFactory kf = KeyFactory.getInstance(ALGORITHM);

			return OptionalRsaKey.of((RSAPrivateKey) kf.generatePrivate(spec));
		} catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e) {
			throw new RuntimeException(e);
		}
	}

	private byte[] readPEMKey(BufferedReader reader) throws IOException {
		String line;
		StringBuilder asciiKey = new StringBuilder();
		while ((line = reader.readLine()) != null) {
			if (!line.startsWith(ASCII_KEY_HEADER_MARKER)) {
				asciiKey.append(line);
			}
		}

		return Base64.getDecoder().decode(asciiKey.toString());
	}

	@Bean
	public DcaRsaKeyProvider keyProvider(@Named("jwtPublicKey") OptionalRsaKey<RSAPublicKey> publicKey,
			@Named("jwtPrivateKey") OptionalRsaKey<RSAPrivateKey> privateKey) {

		if (publicKey.isPresent() && privateKey.isPresent()) {
			return new SafeRSAKeyProvider(publicKey.get(), privateKey.get());
		} else {
			LOGGER.log(Level.SEVERE, "\n\n--------------------\n"
					+ "!!!! BE CAREFUL !!!!\n"
					+ "Since you did not configure DCA to use a specific rsa key pair,\n"
					+ "one will be auto-generated for you in order to sign rest API tokens.\n"
					+ "This is fine in development mode,\n"
					+ "but should be avoided in production!\n"
					+ "--------------------\n\n");
			return new UnsafeRSAKeyProvider(ALGORITHM);
		}
	}

	@Bean
	public FilterRegistrationBean tokenRegistration(TokenAuthenticationFilter filter) {
		FilterRegistrationBean registration = new FilterRegistrationBean<>(filter);
		registration.setEnabled(false);
		return registration;
	}

	// Cannot use java's Optional, because it tells the CDI this is an optional bean, so it does not even try to look it up.
	private static class OptionalRsaKey<T> {
		private final T key;

		public static <T> OptionalRsaKey<T> empty() {
			return new OptionalRsaKey<>(null);
		}

		public static <T> OptionalRsaKey<T> of(T key) {
			return new OptionalRsaKey<>(key);
		}

		private OptionalRsaKey(T key) {
			this.key = key;
		}

		public boolean isPresent() {
			return key != null;
		}

		public T get() {
			return key;
		}
	}
}

