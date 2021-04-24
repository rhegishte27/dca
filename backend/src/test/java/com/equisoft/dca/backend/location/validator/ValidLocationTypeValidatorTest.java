package com.equisoft.dca.backend.location.validator;

import org.apache.commons.lang3.RandomStringUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;

import com.equisoft.dca.backend.location.model.Location;
import com.equisoft.dca.backend.location.model.LocationType;
import com.equisoft.dca.backend.location.model.PlatformType;

class ValidLocationTypeValidatorTest {

	private static ValidLocationTypeValidator validator;

	@BeforeAll
	static void setUpOnce() {
		validator = new ValidLocationTypeValidator();
	}

	@Nested
	class LocationIsNull {

		@Test
		void givenNullLocation_whenValidate_thenReturnFalse() {
			//given

			//when
			boolean actual = validator.isValid(null, null);

			//then
			Assertions.assertThat(actual).isFalse();
		}
	}

	@Nested
	class LocationTypeIsNull {

		private Location.LocationBuilder locationBuilder;

		@BeforeEach
		void setUp() {
			locationBuilder = Location.builder()
					.id(1)
					.identifier("Identifier")
					.path("/test/path");
		}

		@ParameterizedTest
		@NullSource
		void givenNullLocation_whenValidate_thenReturnFalse(LocationType locationType) {
			//given
			Location location = locationBuilder.locationType(locationType).build();

			//when
			boolean actual = validator.isValid(location, null);

			//then
			Assertions.assertThat(actual).isFalse();
		}
	}

	@Nested
	class LocationTypeIsNetwork {

		private Location.LocationBuilder locationBuilder;

		@BeforeEach
		void setUp() {
			locationBuilder = Location.builder()
					.id(1)
					.identifier("Identifier")
					.locationType(LocationType.NETWORK)
					.path("/test/path");
		}

		@Test
		void givenNetworkLocationType_whenValidateObject_thenReturnTrue() {
			// given
			Location location = locationBuilder.build();

			// when
			boolean actual = validator.isValid(location, null);

			//then
			Assertions.assertThat(actual).isTrue();
		}

		@Test
		void givenNetworkLocationTypeWithServerName_whenValidateObject_thenReturnFalse() {
			// given
			Location location = locationBuilder
					.serverName("servername")
					.build();

			// when
			boolean actual = validator.isValid(location, null);

			//then
			Assertions.assertThat(actual).isFalse();
		}

		@Test
		void givenNetworkLocationTypeWithUserName_whenValidateObject_thenReturnFalse() {
			// given
			Location location = locationBuilder
					.userName("username")
					.build();

			// when
			boolean actual = validator.isValid(location, null);

			//then
			Assertions.assertThat(actual).isFalse();
		}

		@Test
		void givenNetworkLocationTypeWithPassword_whenValidateObject_thenReturnFalse() {
			// given
			Location location = locationBuilder
					.password("password")
					.build();

			// when
			boolean actual = validator.isValid(location, null);

			//then
			Assertions.assertThat(actual).isFalse();
		}

		@Test
		void givenNetworkLocationTypeWithPlatformType_whenValidateObject_thenReturnFalse() {
			// given
			Location location = locationBuilder
					.platformType(PlatformType.AS400_2)
					.build();

			// when
			boolean actual = validator.isValid(location, null);

			//then
			Assertions.assertThat(actual).isFalse();
		}
	}

	@Nested
	class LocationTypeIsFtp {

		private Location.LocationBuilder locationBuilder;

		@BeforeEach
		void setUp() {
			locationBuilder = Location.builder()
					.id(1)
					.identifier("Identifier")
					.locationType(LocationType.FTP)
					.path("/test/path");
		}

		@Test
		void givenFtpLocationType_whenValidateObject_thenReturnTrue() {
			// given
			Location location = locationBuilder
					.serverName("servername")
					.userName("username")
					.password("password")
					.platformType(PlatformType.VSE_BIM)
					.build();

			// when
			boolean actual = validator.isValid(location, null);

			// then
			Assertions.assertThat(actual).isTrue();
		}

		@Nested
		class LocationTypeIsFtpAndServerNameIsInvalid {

			@BeforeEach
			void setUp() {
				locationBuilder = Location.builder()
						.id(1)
						.identifier("Identifier")
						.locationType(LocationType.FTP)
						.path("/test/path")
						.userName("username")
						.password("password")
						.platformType(PlatformType.VSE_BIM);
			}

			@ParameterizedTest
			@NullSource
			@EmptySource
			void givenFtpLocationTypeWithServerNameBlank_whenValidateObject_thenReturnFalse(String serverName) {
				// given
				Location location = locationBuilder
						.serverName(serverName)
						.build();

				// when
				boolean actual = validator.isValid(location, null);

				// then
				Assertions.assertThat(actual).isFalse();
			}

			@Test
			void givenFtpLocationTypeWithServerNameAboveMaxLimit_whenValidateObject_thenReturnFalse() {
				// given
				Location location = locationBuilder
						.serverName(RandomStringUtils.randomAlphabetic(Location.SERVERNAME_MAX_SIZE + 1))
						.build();

				// when
				boolean actual = validator.isValid(location, null);

				// then
				Assertions.assertThat(actual).isFalse();
			}

			@Test
			void givenFtpLocationTypeWithServerNameBelowMinLimit_whenValidateObject_thenReturnFalse() {
				// given
				Location location = locationBuilder
						.serverName(RandomStringUtils.randomAlphabetic(Location.SERVERNAME_MIN_SIZE - 1))
						.build();

				// when
				boolean actual = validator.isValid(location, null);

				// then
				Assertions.assertThat(actual).isFalse();
			}
		}

		@Nested
		class LocationTypeIsFtpAndUserNameIsInvalid {

			@BeforeEach
			void setUp() {
				locationBuilder = Location.builder()
						.id(1)
						.identifier("Identifier")
						.locationType(LocationType.FTP)
						.path("/test/path")
						.serverName("servername")
						.password("password")
						.platformType(PlatformType.VSE_BIM);
			}

			@ParameterizedTest
			@NullSource
			@EmptySource
			void givenFtpLocationTypeWithUserNameNullOrEmpty_whenValidateObject_thenReturnFalse(String userName) {
				// given
				Location location = locationBuilder
						.userName(userName)
						.build();

				// when
				boolean actual = validator.isValid(location, null);

				// then
				Assertions.assertThat(actual).isFalse();
			}

			@Test
			void givenFtpLocationTypeWithUserNameAboveMaxLimit_whenValidateObject_thenReturnFalse() {
				// given
				Location location = locationBuilder
						.userName(RandomStringUtils.randomAlphabetic(Location.USERNAME_MAX_SIZE + 1))
						.build();

				// when
				boolean actual = validator.isValid(location, null);

				// then
				Assertions.assertThat(actual).isFalse();
			}

			@Test
			void givenFtpLocationTypeWithUserNameBelowMinLimit_whenValidateObject_thenReturnFalse() {
				// given
				Location location = locationBuilder
						.userName(RandomStringUtils.randomAlphabetic(Location.USERNAME_MIN_SIZE - 1))
						.build();

				// when
				boolean actual = validator.isValid(location, null);

				// then
				Assertions.assertThat(actual).isFalse();
			}
		}

		@Nested
		class LocationTypeIsFtpAndPasswordIsInvalid {

			@BeforeEach
			void setUp() {
				locationBuilder = Location.builder()
						.id(1)
						.identifier("Identifier")
						.locationType(LocationType.FTP)
						.path("/test/path")
						.serverName("servername")
						.userName("username")
						.platformType(PlatformType.VSE_BIM);
			}

			@ParameterizedTest
			@NullSource
			@EmptySource
			void givenFtpLocationTypeWithPasswordNullOrEmpty_whenValidateObject_thenReturnFalse(String password) {
				// given
				Location location = locationBuilder
						.password(password)
						.build();

				// when
				boolean actual = validator.isValid(location, null);

				// then
				Assertions.assertThat(actual).isFalse();
			}

			@Test
			void givenFtpLocationTypeWithPasswordAboveMaxLimit_whenValidateObject_thenReturnFalse() {
				// given
				Location location = locationBuilder
						.password(RandomStringUtils.randomAlphabetic(Location.PASSWORD_MAX_SIZE + 1))
						.build();

				// when
				boolean actual = validator.isValid(location, null);

				// then
				Assertions.assertThat(actual).isFalse();
			}

			@Test
			void givenFtpLocationTypeWithPasswordBelowMinLimit_whenValidateObject_thenReturnFalse() {
				// given
				Location location = locationBuilder
						.password(RandomStringUtils.randomAlphabetic(Location.PASSWORD_MIN_SIZE - 1))
						.build();

				// when
				boolean actual = validator.isValid(location, null);

				// then
				Assertions.assertThat(actual).isFalse();
			}
		}

		@Nested
		class LocationTypeIsFtpAndPlatformTypeIsInvalid {

			@BeforeEach
			void setUp() {
				locationBuilder = Location.builder()
						.id(1)
						.identifier("Identifier")
						.locationType(LocationType.FTP)
						.path("/test/path")
						.serverName("servername")
						.userName("username")
						.password("password");
			}

			@ParameterizedTest
			@NullSource
			void givenFtpLocationTypeWithPasswordAboveMaxLimit_whenValidateObject_thenReturnFalse(PlatformType platformType) {
				// given
				Location location = locationBuilder
						.platformType(platformType)
						.build();

				// when
				boolean actual = validator.isValid(location, null);

				// then
				Assertions.assertThat(actual).isFalse();
			}
		}
	}
}
