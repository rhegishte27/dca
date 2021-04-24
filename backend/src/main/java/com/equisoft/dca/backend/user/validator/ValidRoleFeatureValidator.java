package com.equisoft.dca.backend.user.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.equisoft.dca.backend.user.model.User;

public class ValidRoleFeatureValidator implements ConstraintValidator<ValidRoleFeature, User> {

	@Override
	public boolean isValid(User user, ConstraintValidatorContext constraintValidatorContext) {
		if (user == null || user.getRole() == null || user.getFeatures() == null) {
			return false;
		}

		return user.getRole().getNonEditableFeatures().stream().allMatch(f -> {
			boolean isDefaultFeaturesContains = user.getRole().getDefaultFeatures().contains(f);
			boolean isFeaturesContains = user.getFeatures().contains(f);

			return isDefaultFeaturesContains == isFeaturesContains;
		});
	}
}
