package com.equisoft.dca.backend.project.model;

import com.equisoft.dca.backend.common.model.BaseEnum;

import lombok.Getter;

@Getter
public enum TypeProjectElement implements BaseEnum<Integer> {
	SOURCE_CODE(1, "Source code"),
	MAPS(2, "Maps"),
	TABLE_DATA_DICTIONARY(3, "Table data dictionary"),
	BACKUPS(4, "Backups");

	private final int id;

	private final String name;

	TypeProjectElement(int id, String name) {
		this.id = id;
		this.name = name;
	}

	@Override
	public Integer getId() {
		return id;
	}
}
