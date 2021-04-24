package com.equisoft.dca.backend.location.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.StringUtils;

import com.equisoft.dca.backend.location.model.Location;
import com.equisoft.dca.backend.location.model.LocationType;
import com.equisoft.dca.backend.location.model.PlatformType;

public class ValidLocationTypeValidator implements ConstraintValidator<ValidLocationType, Location> {

	@Override
	public boolean isValid(Location location, ConstraintValidatorContext constraintValidatorContext) {
		if (location == null) {
			return false;
		}
		if (LocationType.NETWORK.equals(location.getLocationType())) {
			return StringUtils.isEmpty(location.getServerName()) &&
					StringUtils.isEmpty(location.getUserName()) &&
					StringUtils.isEmpty(location.getPassword()) &&
					location.getPlatformType() == null;
		}
		if (LocationType.FTP.equals(location.getLocationType())) {
			return isServerNameValid(location.getServerName()) &&
					isUserNameValid(location.getUserName()) &&
					isPasswordValid(location.getPassword()) &&
					isPlatformTypeValid(location.getPlatformType());
		}
		return false;
	}

	private boolean isServerNameValid(String serverName) {
		return StringUtils.isNotBlank(serverName) &&
				serverName.trim().length() >= Location.SERVERNAME_MIN_SIZE &&
				serverName.trim().length() <= Location.SERVERNAME_MAX_SIZE;
	}

	private boolean isUserNameValid(String userName) {
		return StringUtils.isNotBlank(userName) &&
				userName.trim().length() >= Location.USERNAME_MIN_SIZE &&
				userName.trim().length() <= Location.USERNAME_MAX_SIZE;
	}

	private boolean isPasswordValid(String password) {
		return StringUtils.isNotBlank(password) &&
				password.length() >= Location.PASSWORD_MIN_SIZE &&
				password.length() <= Location.PASSWORD_MAX_SIZE;
	}

	private boolean isPlatformTypeValid(PlatformType platformType) {
		return platformType != null;
	}
}
