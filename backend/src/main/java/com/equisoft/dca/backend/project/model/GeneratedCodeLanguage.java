package com.equisoft.dca.backend.project.model;

import com.equisoft.dca.backend.common.model.BaseEnum;

import lombok.Getter;

@Getter
public enum GeneratedCodeLanguage implements BaseEnum<Integer> {
	JAVA(1, "Java"),
	COBOL(2, "COBOL");

	private final int id;

	private final String name;

	GeneratedCodeLanguage(int id, String name) {
		this.id = id;
		this.name = name;
	}

	@Override
	public Integer getId() {
		return id;
	}
}
