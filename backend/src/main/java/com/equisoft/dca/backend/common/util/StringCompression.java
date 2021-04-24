package com.equisoft.dca.backend.common.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.equisoft.dca.backend.util.StringUtil;

@Component
public class StringCompression {

	private final List<String> cobolReservedWords;

	StringCompression(@Value("#{'${dca.cobol.reservedwords}'.split(',')}") List<String> cobolReservedWords) {
		this.cobolReservedWords = cobolReservedWords;
	}

	public Optional<String> compress(String text, int maxLength, List<String> excludedWords, boolean checkCobolWords, boolean capitalize) {
		if (text == null) {
			return Optional.empty();
		}

		String result = capitalize ? text.toUpperCase() : text;
		if (isValid(result, maxLength, excludedWords, checkCobolWords)) {
			return Optional.of(result);
		}

		return process(result, maxLength, excludedWords, checkCobolWords);
	}

	private Optional<String> process(String text, int maxLength, List<String> excludedWords, boolean checkCobolWords) {
		List<Character> characters = text.chars().mapToObj(c -> (char) c).collect(Collectors.toList());
		List<Integer> ranks = rankCharacters(text, characters);
		for (int rank = 5; rank > 0; rank--) {
			for (int i = characters.size() - 1; i >= 0 ; i--) {
				if (ranks.get(i) == rank) {
					characters.remove(i);
					ranks.remove(i);
					String newText = characters.stream().map(String::valueOf).collect(Collectors.joining());
					if (isValid(newText, maxLength, excludedWords, checkCobolWords)) {
						return Optional.ofNullable(newText);
					}
				}
			}
		}
		return Optional.empty();
	}

	private List<Integer> rankCharacters(String text, List<Character> characters) {
		List<Integer> ranks = new ArrayList<>();
		for (int i = 0; i < characters.size(); i++) {
			if (isDashOrUnderscore(characters.get(i))) {
				ranks.add(5);
			} else if (i == 0 || (isMixedCase(text) && isUpperCase(characters.get(i))) || isDashOrUnderscore(characters.get(i - 1))) {
				ranks.add(1);
			} else if (isVowel(characters.get(i))) {
				ranks.add(4);
			} else if (Character.isDigit(characters.get(i))) {
				ranks.add(3);
			} else {
				ranks.add(2);
			}
		}
		return ranks;
	}

	private boolean isValid(String text, int maxLength, List<String> excludedWords, boolean checkCobolWords) {
		return text.length() <= maxLength && !excludedWords.contains(text) && (!checkCobolWords || !cobolReservedWords.contains(text));
	}

	private boolean isVowel(Character character) {
		return "AEIOUaeiou".indexOf(character) != -1;
	}

	private boolean isDashOrUnderscore(Character character) {
		return character == '-' || character == '_';
	}

	private boolean isMixedCase(String text) {
		return !StringUtil.isAllLowerCase(text) && !StringUtil.isAllUpperCase(text);
	}

	private boolean isUpperCase(Character character) {
		return Character.isUpperCase(character);
	}
}
