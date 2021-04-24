package com.equisoft.dca.backend.project.model;

import com.equisoft.dca.backend.common.model.BaseEnum;

import lombok.Getter;

@Getter
public enum SystemType implements BaseEnum<Integer> {

	SOURCE(1, "Source"),
	TARGET(2, "Target"),
	UTILITY_SYSTEM(3, "Utility system");

	private final int id;

	private final String name;

	SystemType(int id, String name) {
		this.id = id;
		this.name = name;
	}

	@Override
	public Integer getId() {
		return id;
	}
}
