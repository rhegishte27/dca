package com.equisoft.dca.backend.project.model;

import com.equisoft.dca.backend.common.model.BaseEnum;

import lombok.Getter;

@Getter
public enum Compiler implements BaseEnum<Integer> {
	GENERIC(1, "Generic"),
	MICROFOCUS(2, "MicroFocus"),
	FUJITSU(3, "Fujitsu"),
	VISUAL_AGE(4, "Visual Age"),
	COBOL_II(5, "COBOL II"),
	DOUBLE_BYTE(6, "Double Byte");

	private final int id;

	private final String name;

	Compiler(int id, String name) {
		this.id = id;
		this.name = name;
	}

	@Override
	public Integer getId() {
		return id;
	}
}
