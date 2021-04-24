package com.equisoft.dca.backend.user.model;

import com.equisoft.dca.backend.common.model.BaseEnum;

import lombok.Getter;

@Getter
public enum Feature implements BaseEnum<Integer> {

	SYSTEM_SETTINGS(1, "System settings"),
	ORGANIZATIONS_AND_USERS(2, "Organizations and users"),
	PROJECT_MANAGEMENT(3, "Project management"),
	PROJECT_DATA_MAPS(4, "Project datamaps"),
	PROJECT_TABLES(5, "Project tables"),
	SYSTEM_MANAGEMENT(6, "System management"),
	SYSTEM_CODE_ANALYSIS(7, "System code analysis"),
	SYSTEM_DATA_OBJECTS(8, "System data objects"),
	SYSTEM_TRANSACTIONS(9, "System transactions");

	private final int id;

	private final String name;

	Feature(int id, String name) {
		this.id = id;
		this.name = name;
	}

	@Override
	public Integer getId() {
		return id;
	}
}
