package com.equisoft.dca.infra.web.configuration;

import javax.inject.Inject;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.equisoft.dca.infra.web.filter.CorsConfigFilter;
import com.equisoft.dca.infra.web.filter.TokenAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, proxyTargetClass = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	private final TokenAuthenticationFilter tokenAuthenticationFilter;

	private final CorsConfigFilter corsConfigFilter;

	private final LogoutHandler logoutHandler;

	@Inject
	WebSecurityConfig(TokenAuthenticationFilter tokenAuthenticationFilter, CorsConfigFilter corsConfigFilter, LogoutHandler logoutHandler) {
		this.tokenAuthenticationFilter = tokenAuthenticationFilter;
		this.corsConfigFilter = corsConfigFilter;
		this.logoutHandler = logoutHandler;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
		http.addFilterBefore(corsConfigFilter, TokenAuthenticationFilter.class);

		http
			.csrf().ignoringAntMatchers("/api/**")
			.and()
			.authorizeRequests()
			.antMatchers("/api/**").permitAll() // API
			.antMatchers("/", "/resources/**", "/images/**", "/logout").permitAll()
			.antMatchers("/*.js").permitAll()
			.antMatchers("/**").permitAll()
			.anyRequest().authenticated()
			.and()
			.logout()
			.logoutRequestMatcher(new AntPathRequestMatcher("/logout")).addLogoutHandler(logoutHandler)
			.permitAll();

		http.formLogin().loginPage("/login").permitAll();
	}
}
