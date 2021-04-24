package com.equisoft.dca.backend.language.model;

import com.equisoft.dca.backend.common.model.BaseEnum;

import lombok.Getter;

@Getter
public enum Language implements BaseEnum<Integer> {
	ENGLISH(1, "English", "en"),
	SPANISH(2, "Español", "es");

	private final int id;

	private final String name;

	private final String code;

	Language(int id, String name, String code) {
		this.id = id;
		this.name = name;
		this.code = code;
	}

	@Override
	public Integer getId() {
		return id;
	}
}
