package com.equisoft.dca.backend.project.model;

import com.equisoft.dca.backend.common.model.BaseEnum;

import lombok.Getter;

@Getter
public enum BackupInterval implements BaseEnum<Integer> {
	DAYS(1, "Days"),
	WEEKS(2, "Weeks"),
	MONTHS(3, "Months");

	private final int id;

	private final String name;

	BackupInterval(int id, String name) {
		this.id = id;
		this.name = name;
	}

	@Override
	public Integer getId() {
		return id;
	}
}
