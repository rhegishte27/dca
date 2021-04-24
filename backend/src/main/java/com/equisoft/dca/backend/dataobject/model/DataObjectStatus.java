package com.equisoft.dca.backend.dataobject.model;

import com.equisoft.dca.backend.common.model.BaseEnum;

import lombok.Getter;

@Getter
public enum DataObjectStatus implements BaseEnum<Integer> {

	SUCCESS(1, "Success"),
	WARNING(2, "Warning"),
	ERROR(3, "Error");

	private final int id;

	private final String name;

	DataObjectStatus(int id, String name) {
		this.id = id;
		this.name = name;
	}

	@Override
	public Integer getId() {
		return id;
	}
}
