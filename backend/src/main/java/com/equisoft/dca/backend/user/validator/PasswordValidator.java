package com.equisoft.dca.backend.user.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PasswordValidator extends AbstractPasswordValidator implements ConstraintValidator<ConstraintPassword, String> {
	@Override
	public void initialize(ConstraintPassword annotation) {
		minSize = annotation.minSize();
		maxSize = annotation.maxSize();
		regex = annotation.regex();
	}

	@Override
	public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
		return isValid(s, false);
	}
}
