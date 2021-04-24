package com.equisoft.dca.backend.user.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PasswordValidatorBlankAllowed extends AbstractPasswordValidator implements ConstraintValidator<ConstraintPasswordBlankAllowed, String> {
	@Override
	public void initialize(ConstraintPasswordBlankAllowed annotation) {
		minSize = annotation.minSize();
		maxSize = annotation.maxSize();
		regex = annotation.regex();
	}

	@Override
	public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
		return isValid(s, true);
	}
}
