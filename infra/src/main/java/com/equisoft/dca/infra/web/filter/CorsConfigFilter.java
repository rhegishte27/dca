package com.equisoft.dca.infra.web.filter;

import javax.inject.Inject;

import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.equisoft.dca.infra.EnvironmentConfiguration;

@Component
public class CorsConfigFilter extends CorsFilter {
	private static CorsConfigurationSource prepareConfig(EnvironmentConfiguration environmentConfiguration) {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		if (environmentConfiguration.hasReactDevModeUrl()) {
			CorsConfiguration config = new CorsConfiguration();
			config.setAllowCredentials(true);
			config.addAllowedOrigin(environmentConfiguration.getReactDevModeUrl());
			config.addAllowedMethod("*");
			config.addAllowedHeader("*");
			source.registerCorsConfiguration("/**", config);
		}
		return source;
	}

	@Inject
	public CorsConfigFilter(EnvironmentConfiguration environmentConfiguration) {
		super(prepareConfig(environmentConfiguration));
	}
}
