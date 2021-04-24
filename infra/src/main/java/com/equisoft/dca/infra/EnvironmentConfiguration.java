package com.equisoft.dca.infra;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EnvironmentConfiguration {

	@Value("#{systemProperties['com.equisoft.dca.rest.jwtPublicKey']}")
	private String jwtPublicKeyFile;

	@Value("#{systemProperties['com.equisoft.dca.rest.jwtPrivateKey']}")
	private String jwtPrivateKeyFile;

	@Value("#{systemProperties['com.equisoft.dca.rest.reactDevModeUrl']}")
	private String reactDevModeUrl;

	public boolean hasReactDevModeUrl() {
		return StringUtils.isNotBlank(reactDevModeUrl);
	}

	public String getReactDevModeUrl() {
		return reactDevModeUrl;
	}

	public String getJwtPublicKeyFile() {
		return jwtPublicKeyFile;
	}

	public String getJwtPrivateKeyFile() {
		return jwtPrivateKeyFile;
	}

	public void setReactDevModeUrl(String reactDevModeUrl) {
		this.reactDevModeUrl = reactDevModeUrl;
	}
}
