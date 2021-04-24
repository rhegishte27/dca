package com.equisoft.dca.backend.util;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;

public final class StringUtil {
	private static final String SPACE = " ";

	private StringUtil() {
	}

	public static boolean isAllUpperCase(String s) {
		if (StringUtils.isBlank(s)) {
			return false;
		}
		return s.equals(s.toUpperCase());
	}

	public static boolean isAllLowerCase(String s) {
		if (StringUtils.isBlank(s)) {
			return false;
		}
		return s.equals(s.toLowerCase());
	}

	public static String toTitleCaseIfNotMixedCase(String s) {
		if (isAllLowerCase(s) || isAllUpperCase(s)) {
			return toTitleCaseSpaceDelimiter(s);
		}
		return s;
	}

	public static String toTitleCaseSpaceDelimiter(String s) {
		return toTitleCase(s, SPACE);
	}

	private static String toTitleCase(String s, String delimiter) {
		if (StringUtils.isBlank(s)) {
			return s;
		}

		if (s.length() == 1) {
			return s.toUpperCase();
		}

		StringBuilder resultPlaceHolder = new StringBuilder(s.length());

		Arrays.stream(s.split(delimiter)).forEach(stringPart -> {
			char[] charArray = stringPart.toLowerCase().toCharArray();
			charArray[0] = Character.toUpperCase(charArray[0]);
			resultPlaceHolder.append(new String(charArray)).append(" ");
		});

		return resultPlaceHolder.toString().trim();
	}
}
