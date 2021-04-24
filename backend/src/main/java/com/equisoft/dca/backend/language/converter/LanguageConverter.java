package com.equisoft.dca.backend.language.converter;

import javax.persistence.Converter;

import com.equisoft.dca.backend.common.converter.BaseEnumConverter;
import com.equisoft.dca.backend.language.model.Language;

@Converter(autoApply = true)
public class LanguageConverter extends BaseEnumConverter<Language, Integer> {

	LanguageConverter() {
		super(Language.class);
	}
}
