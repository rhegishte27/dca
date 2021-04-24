package com.equisoft.dca.backend.dataobject.validator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.IntStream;

import org.apache.commons.lang3.StringUtils;

class CobolDataObjectContentValidator implements DataObjectContentValidator {

	private static final int FIRST_COLUMN = 7;

	private static final int LAST_COLUMN = 72;

	@Override
	public boolean isValid(String dataObjectContent) {
		if (dataObjectContent == null) {
			return false;
		}

		return Arrays.stream(dataObjectContent.split(System.lineSeparator()))
				.filter(notBlank()
						.and(notBlankBetweenFirstColumnAndLastColumn())
						.and(noAsteriskInFirstColumn())
						.and(noDashInFirstColumn()))
				.findFirst()
				.filter(validContent())
				.isPresent();
	}

	// Remove blank line
	private Predicate<String> notBlank() {
		return StringUtils::isNotBlank;
	}

	// Remove line if:
	// - contains whitespaces between columns 7 and 72
	// - total length is less than 7
	private Predicate<String> notBlankBetweenFirstColumnAndLastColumn() {
		return s -> s.length() >= FIRST_COLUMN && StringUtils.isNotBlank(s.substring(FIRST_COLUMN - 1, Math.min(s.length(), LAST_COLUMN)));
	}

	// Remove line which contains asterisk in column 7
	private Predicate<String> noAsteriskInFirstColumn() {
		return s -> !s.substring(FIRST_COLUMN - 1, FIRST_COLUMN).equals("*");
	}

	// Remove line which contains dash in column 7
	private Predicate<String> noDashInFirstColumn() {
		return s -> !s.substring(FIRST_COLUMN - 1, FIRST_COLUMN).equals("-");
	}

	// Keep line if its first word is a valid word
	private Predicate<String> validContent() {
		return s -> s.trim().indexOf(' ') != -1
				&& validWords().stream().anyMatch(s.trim().substring(0, s.trim().indexOf(' '))::contains);
	}

	private List<String> validWords() {
		final int startInclusive = 1;
		final int endExclusive = 50;

		List<String> validWords = new ArrayList<>(Arrays.asList("66", "77", "88", "COPY"));
		IntStream.range(startInclusive, endExclusive).forEach(
				i -> validWords.add(String.format("%02d", i))
		);

		return validWords;
	}
}
