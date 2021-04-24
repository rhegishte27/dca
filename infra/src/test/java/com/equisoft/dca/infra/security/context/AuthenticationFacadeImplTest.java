package com.equisoft.dca.infra.security.context;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

class AuthenticationFacadeImplTest {

	private AuthenticationFacadeImpl authenticationFacade;

	private List<SimpleGrantedAuthority> authorities;

	private UsernamePasswordAuthenticationToken authenticationToken;

	private static final String principal = "username";

	private static final String credentials = "password";

	@BeforeEach
	void setUp() {
		authenticationFacade = new AuthenticationFacadeImpl();
		authorities = List.of(new SimpleGrantedAuthority("a1"), new SimpleGrantedAuthority("a2"));
		authenticationToken = new UsernamePasswordAuthenticationToken(principal, credentials, authorities);
		SecurityContextHolder.clearContext();
	}

	@AfterEach
	void tearDown() {
		SecurityContextHolder.clearContext();
	}

	@Nested
	class GetAuthentication {

		@Test
		void givenValidAuthentication_whenGetAuthentication_thenReturnAuthenticationWithContent() {
			//given
			SecurityContextHolder.getContext().setAuthentication(authenticationToken);

			//when
			Optional<Authentication> actual = authenticationFacade.getAuthentication();

			//then
			Assertions.assertThat(actual)
					.isPresent()
					.get()
					.extracting(Authentication::getAuthorities, Authentication::getPrincipal, Authentication::getCredentials)
					.containsExactly(authorities, principal, credentials);
		}

		@Test
		void givenNoAuthentication_whenGetAuthentication_thenReturnEmptyAuthentication() {
			//given

			//when
			Optional<Authentication> actual = authenticationFacade.getAuthentication();

			//then
			Assertions.assertThat(actual)
					.isEmpty();
		}
	}

	@Nested
	class SetAuthentication {

		@Test
		void givenValidAuthentication_whenGetAuthentication_thenReturnAuthenticationWithContent() {
			//given

			//when
			authenticationFacade.setAuthentication(authenticationToken);

			//then
			Assertions.assertThat(SecurityContextHolder.getContext().getAuthentication())
					.extracting(Authentication::getAuthorities, Authentication::getPrincipal, Authentication::getCredentials)
					.containsExactly(authorities, principal, credentials);
		}
	}

	@Nested
	class GetUserIdentifier {

		@Test
		void givenValidAuthentication_whenGetUserIdentifier_thenReturnUserIdentifier() {
			//given
			authenticationFacade.setAuthentication(authenticationToken);

			//when
			Optional<String> actual = authenticationFacade.getUserIdentifier();

			//then
			Assertions.assertThat(actual)
					.isPresent()
					.get()
					.isEqualTo(principal);
		}

		@Test
		void givenNoAuthentication_whenGetAuthentication_thenReturnEmptyAuthentication() {
			//given

			//when
			Optional<String> actual = authenticationFacade.getUserIdentifier();

			//then
			Assertions.assertThat(actual).isEmpty();
		}
	}

	@Nested
	class ClearContext {

		@Test
		void givenValidAuthentication_whenClearContext_thenAuthenticationShouldBeEmpty() {
			//given
			authenticationFacade.setAuthentication(authenticationToken);

			//when
			authenticationFacade.clearContext();
			Optional<Authentication> actual = authenticationFacade.getAuthentication();

			//then
			Assertions.assertThat(actual).isEmpty();
		}
	}

}
