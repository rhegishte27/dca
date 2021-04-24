package com.equisoft.dca.backend.user.validator;

import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

abstract class AbstractPasswordValidator {
	int minSize;
	int maxSize;
	String regex;

	boolean isValid(String s, boolean isBlankAllowed) {
		if (StringUtils.isBlank(s)) {
			return isBlankAllowed;
		}

		if (s.length() < minSize || s.length() > maxSize) {
			return false;
		}

		Pattern pattern = Pattern.compile(regex);
		return pattern.matcher(s).matches();
	}
}
