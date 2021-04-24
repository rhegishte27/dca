package com.equisoft.dca.backend.project.converter;

import javax.persistence.Converter;

import com.equisoft.dca.backend.common.converter.BaseEnumConverter;
import com.equisoft.dca.backend.project.model.Compiler;

@Converter(autoApply = true)
public class CompilerConverter extends BaseEnumConverter<Compiler, Integer> {

	CompilerConverter() {
		super(Compiler.class);
	}
}
