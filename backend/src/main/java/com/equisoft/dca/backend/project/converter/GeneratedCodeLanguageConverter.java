package com.equisoft.dca.backend.project.converter;

import javax.persistence.Converter;

import com.equisoft.dca.backend.common.converter.BaseEnumConverter;
import com.equisoft.dca.backend.project.model.GeneratedCodeLanguage;

@Converter(autoApply = true)
public class GeneratedCodeLanguageConverter extends BaseEnumConverter<GeneratedCodeLanguage, Integer> {

	GeneratedCodeLanguageConverter() {
		super(GeneratedCodeLanguage.class);
	}
}
